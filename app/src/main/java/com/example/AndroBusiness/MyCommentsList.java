package com.example.AndroBusiness;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyCommentsList extends Fragment {

    private ListView list;
    private MyCommentsListAdapter adapter;
    private Context context;
    private ArrayList<Comment> myCommentsList ;
    private ArrayList<Product> productsList ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.list, container, false);

        list = (ListView) v.findViewById(R.id.list);
        myCommentsList = new ArrayList<Comment>();
        productsList = new ArrayList<Product>();
        context = v.getContext();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getReference();
                                Product product = document.toObject(Product.class);
                                productsList.add(product);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error loading documents"
                                    + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


        db = FirebaseFirestore.getInstance();
        db.collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getReference();
                                Comment comment = document.toObject(Comment.class);
                                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                // Check if the comment belongs to the connected user & insert an icon according to the product
                                if ( comment.getUserEmail().equals(userEmail) ) {
                                    myCommentsList.add(comment);
                                }
                            }

                        } else {
                            Toast.makeText(getActivity(), "Error loading documents"
                                    + task.getException(), Toast.LENGTH_LONG).show();
                        }

                        adapter = new MyCommentsListAdapter(context, R.layout.my_comments, myCommentsList,productsList);
                        list.setAdapter(adapter);
                    }
                });
    }

}



package com.example.AndroBusiness;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommentsList extends DialogFragment implements myInterface {

    private ListView list;
    private CommentsListAdapter adapter;
    private Context context;
    private ArrayList<Comment> commentsList;
    private boolean b = false;
    private static myInterface listener;


    public static myInterface getListener() {
        return listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.list, container, false);

        list = (ListView) v.findViewById(R.id.list);
        commentsList = new ArrayList<Comment>();
        context = v.getContext();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getReference();
                                Comment comment = document.toObject(Comment.class);
                                Product product= DataBaseManager.getInstance().getSelectedProduct();

                                // Displays comments that belong only to the selected product
                                if ( (comment.getSerialNum()).equals(product.getSerialNum()) ) {
                                    commentsList.add(comment);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error loading documents"
                                    + task.getException(), Toast.LENGTH_LONG).show();
                        }
                        adapter = new CommentsListAdapter(getActivity(), R.layout.comments_list, commentsList);
                        list.setAdapter(adapter);
                    }
                });
    }


    @Override
    public void refresh() {
        commentsList = new ArrayList<Comment>();
        onResume();
    }

}

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

public class MyLikedList extends Fragment {

    private ListView list;
    private MyLikedListAdapter adapter;
    private Context context;
    private ArrayList<Product> likedProductsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.list, container, false);

        list = (ListView) v.findViewById(R.id.list);
        likedProductsList = new ArrayList<Product>();
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
                                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                // Checking the products that the user liked
                                if ( product.getLikeArrayList().contains(userEmail) ) {
                                    likedProductsList.add(product);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error loading documents"
                                    + task.getException(), Toast.LENGTH_LONG).show();
                        }

                        adapter = new MyLikedListAdapter(context, R.layout.my_liked_list, likedProductsList);
                        list.setAdapter(adapter);
                    }
                });
        }

}



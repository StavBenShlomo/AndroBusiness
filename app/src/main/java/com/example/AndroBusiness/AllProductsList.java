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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllProductsList extends Fragment implements myInterface {

    private ListView list;
    private AllProductsListAdapter adapter;
    private Context context;
    private ArrayList<Product> productsList;
    private static myInterface listener;


    public static myInterface getListener() {
        return listener;
    }


    public static void setListener(myInterface listener) {
        AllProductsList.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.list, container, false);

        list = (ListView) v.findViewById(R.id.list);
        productsList = new ArrayList<Product>();
        context = v.getContext();
        AllProductsList.setListener(this);

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
                        adapter = new AllProductsListAdapter(context, R.layout.all_products_list, productsList);
                        list.setAdapter(adapter);
                    }
                });
    }


    @Override
    public void refresh() {
        productsList = new ArrayList<Product>();
        onResume();
    }


}

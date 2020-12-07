package com.example.AndroBusiness;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

public class Summary extends Fragment {

    private Context context;
    private TextView purchased_products, my_products, total_profit, total_spend, total_likes, total_unlikes;
    private int int_purchased_products = 0, int_my_products = 0, int_total_likes = 0, int_total_unlikes = 0;
    private double double_total_profit = 0 , double_total_spend = 0 ;
    private ArrayList<Product> productsList;
    private ArrayList<Product> arr_purchased_products;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.summary, container, false);

        purchased_products = (TextView) v.findViewById(R.id.purchased_products);
        my_products = (TextView) v.findViewById(R.id.my_products);
        total_profit = (TextView) v.findViewById(R.id.total_profit);
        total_spend = (TextView) v.findViewById(R.id.total_spend);
        total_likes = (TextView) v.findViewById(R.id.total_likes);
        total_unlikes = (TextView) v.findViewById(R.id.total_unlikes);
        productsList = new ArrayList<Product>();
        arr_purchased_products = new ArrayList<Product>();
        context = v.getContext();

        FirebasefirestoreReport();

        return v;
    }


    public void FirebasefirestoreReport() {
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
                                //Purchase purchase = document.toObject(Purchase.class);
                                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                if (product.getEmail().equals(userEmail)) {
                                    int_my_products++;
                                }

                                if (product.getLikeArrayList().contains(userEmail)) {
                                    int_total_likes++;
                                }

                                if (product.getUnlikeArrayList().contains(userEmail)) {
                                    int_total_unlikes++;
                                }

                                my_products.setText(String.valueOf(int_my_products));
                                total_likes.setText(String.valueOf(int_total_likes));
                                total_unlikes.setText(String.valueOf(int_total_unlikes));
                            }

                        } else {
                            Toast.makeText(getActivity(), "Error loading documents "
                                    + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        db.collection("purchases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getReference();
                                Purchase purchase = document.toObject(Purchase.class);
                                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                if (purchase.getUserEmailPurchase().equals(userEmail)) {
                                    int_purchased_products = int_purchased_products + purchase.getProductQuantity();
                                }

                                if (purchase.getUserEmailPurchase().equals(userEmail)) {
                                    double_total_spend = double_total_spend + ((double) purchase.getProductQuantity() * purchase.getProductPrice());
                                }

                                if (purchase.getProductUserEmail().equals(userEmail)) {
                                    double_total_profit = double_total_profit + ((double) purchase.getProductQuantity() * purchase.getProductPrice());
                                }

                                purchased_products.setText(String.valueOf(int_purchased_products));
                                total_spend.setText(String.valueOf(double_total_spend));
                                total_profit.setText(String.valueOf(double_total_profit));
                            }

                        } else {
                            Toast.makeText(getActivity(), "Error loading documents "
                                    + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

}

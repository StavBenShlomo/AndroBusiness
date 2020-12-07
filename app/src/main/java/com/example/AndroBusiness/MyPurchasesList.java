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

public class MyPurchasesList extends Fragment {

    private ListView list;
    private MyPurchasesListAdapter adapter;
    private Context context;
    private ArrayList<Purchase> purchaseProductsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.list, container, false);

        list = (ListView) v.findViewById(R.id.list);
        purchaseProductsList = new ArrayList<Purchase>();
        context = v.getContext();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("purchases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getReference();
                                Purchase purchase =document.toObject(Purchase.class);
                                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                // Checking the purchases that belongs to the connected user
                                if (purchase.getUserEmailPurchase().equals(userEmail) ) {
                                    purchaseProductsList.add(purchase);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error loading documents"
                                    + task.getException(), Toast.LENGTH_LONG).show();
                        }

                        adapter = new MyPurchasesListAdapter(context, R.layout.my_purchases_list, purchaseProductsList);
                        list.setAdapter(adapter);
                    }
                });
    }

}

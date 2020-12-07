package com.example.AndroBusiness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PurchaseProduct extends DialogFragment {

    private int position;
    private EditText quantity;
    private ImageView button_purchase;
    private int quantity_int;
    final Purchase purchase = new Purchase();
    private static myInterface listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.purchase_product, container, false);

        final Product selectedProduct = DataBaseManager.getInstance().getSelectedProduct();

        button_purchase = (ImageView) v.findViewById(R.id.button_purchase);
        quantity = (EditText) v.findViewById(R.id.quantity);

        button_purchase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String stringQuantity = String.valueOf(quantity.getText().toString());

                // Checking the data the user has entered
                if( stringQuantity.matches("[0-9]{1,}" ) &&!(stringQuantity.equals("0")) ){
                    quantity_int = Integer.parseInt(quantity.getText().toString());

                    // Check for sufficient quantity in stock
                    if (selectedProduct.getQuantity() >= quantity_int) {
                        Toast.makeText(getContext(), "Purchased", Toast.LENGTH_SHORT).show();
                        selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity_int);

                        purchase.setUserEmailPurchase(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        purchase.setProductSerialNum(selectedProduct.getSerialNum());
                        purchase.setProductName(selectedProduct.getName());
                        purchase.setProductImage(selectedProduct.getImage());
                        purchase.setProductDescription(selectedProduct.getDescription());
                        purchase.setProductPrice(selectedProduct.getPrice());
                        purchase.setProductQuantity(quantity_int);
                        purchase.setProductUserEmail(selectedProduct.getEmail());

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("purchases")
                                .document(purchase.getIDPurchase())
                                .set(purchase)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DataBaseManager.getInstance().addPurchase(purchase);
                                        // Toast.makeText(getContext(), "purchase", Toast.LENGTH_SHORT).show(); ///?????????
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println(e);
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "There isn't the desired quantity in stock", Toast.LENGTH_LONG).show();
                    }


                    DataBaseManager.getInstance().updateProduct(selectedProduct); //position
                    getActivity().startActivityForResult(getActivity().getIntent(), 10);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("products")
                            .document(selectedProduct.getSerialNum())
                            .set(selectedProduct)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DataBaseManager.getInstance().updateProduct(selectedProduct);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });
                    listener=AllProductsList.getListener();
                    if(listener!=null){
                        listener.refresh();
                    }
                    dismiss();
                }

                else {
                    quantity.setError("The quantity need to be a number"); }

            }});

        return  v;
    }
}

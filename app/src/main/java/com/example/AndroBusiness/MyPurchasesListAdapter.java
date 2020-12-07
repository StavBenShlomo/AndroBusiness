package com.example.AndroBusiness;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyPurchasesListAdapter extends ArrayAdapter<Purchase> {

    private ArrayList<Purchase> purchasesList;
    private Context context;
    private TextView productName, productDescription, productPrice, productQuantity;
    private static MyPurchasesListAdapter instance;


    public MyPurchasesListAdapter(Context context, int resource, ArrayList<Purchase> purchasesList) {
        super(context, resource, purchasesList);
        this.context = context;
        this.purchasesList = purchasesList;
    }


    @Override
    public int getCount() {
        return purchasesList.size();
    }


    @Override
    public Purchase getItem(int position) {
        return purchasesList.get(position);
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LinearLayout rowView = (LinearLayout) View.inflate(context, R.layout.my_purchases_list, null);

        final Purchase purchase = purchasesList.get(position);

        productName = (TextView) rowView.findViewById(R.id.productName);
        productDescription = (TextView) rowView.findViewById(R.id.productDescription);
        productPrice = (TextView) rowView.findViewById(R.id.productPrice);
        productQuantity = (TextView) rowView.findViewById(R.id.productQuantity);


        productName.setText(purchase.getProductName());
        productDescription.setText(purchase.getProductDescription());
        productPrice.setText(String.valueOf(purchase.getProductPrice()));
        productQuantity.setText(String.valueOf(purchase.getProductQuantity()));

        // Get the img from the Firestore
        final ImageView imageView = rowView.findViewById(R.id.productViewImg);
        String imageUrl = purchase.getProductImage();
        if(imageUrl!=null){
            FirebaseStorage storage= FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference storageReference = storageRef.child(imageUrl);
            storageReference.getDownloadUrl().addOnCompleteListener(
                    new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String downloadUrl = task.getResult().toString();
                                Glide.with(context)
                                        .load(downloadUrl)
                                        .into(imageView);
                            } else {
                                System.out.println( "Getting download url was not successful."+
                                        task.getException());
                            }
                        }
                    });
        }


        return rowView;

    }

}

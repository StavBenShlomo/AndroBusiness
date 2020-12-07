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

public class MyLikedListAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> productsList;
    private Context context;
    private int productQuantity_int;
    private TextView productName, productDescription, productPrice, productQuantity;
    private static MyLikedListAdapter instance;


    public MyLikedListAdapter(Context context, int resource, ArrayList<Product> productsList) {
        super(context, resource, productsList);
        this.context = context;
        this.productsList = productsList;
    }


    @Override
    public int getCount() {
        return productsList.size();
    }


    @Override
    public Product getItem(int position) {
        return productsList.get(position);
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LinearLayout rowView = (LinearLayout) View.inflate(context, R.layout.my_liked_list, null);

        final Product product = productsList.get(position);
        productQuantity_int = product.getQuantity();

        productName = (TextView) rowView.findViewById(R.id.productName);
        productDescription = (TextView) rowView.findViewById(R.id.productDescription);
        productPrice = (TextView) rowView.findViewById(R.id.productPrice);
        productQuantity = (TextView) rowView.findViewById(R.id.productQuantity);

        // Get the img from the Firestore
        final ImageView imageView = rowView.findViewById(R.id.productViewImg);
        String imageUrl = product.getImage();
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

        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText(String.valueOf(product.getPrice()));
        if (productQuantity_int > 0){
            productQuantity.setText(String.valueOf(product.getQuantity()));
        }
        else { productQuantity.setText("Sold Out");}


        return rowView;

    }

}

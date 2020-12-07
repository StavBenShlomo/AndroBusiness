package com.example.AndroBusiness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyProductsListAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> productsList;
    private Context context;
    private TextView productName, productDescription, productPrice, productQuantity;
    private String productNameText, productDescriptionText, productPriceText, productQuantityText;
    private ImageView delete, edit;
    private static MyProductsListAdapter instance;


    public MyProductsListAdapter(Context context, int resource, ArrayList<Product> productsList) {
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
        LinearLayout rowView = (LinearLayout) View.inflate(context, R.layout.my_products_list, null);

        final Product product = productsList.get(position);

        productName = (TextView) rowView.findViewById(R.id.productName);
        productDescription = (TextView) rowView.findViewById(R.id.productDescription);
        productPrice = (TextView) rowView.findViewById(R.id.productPrice);
        productQuantity = (TextView) rowView.findViewById(R.id.productQuantity);
        delete = (ImageView) rowView.findViewById(R.id.delete);
        edit = (ImageView) rowView.findViewById(R.id.edit);


        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText(String.valueOf(product.getPrice()));
        productQuantity.setText(String.valueOf(product.getQuantity()));

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



        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                    // Confirm with the user about the delete
                    String title = "Delete";
                    String message = "Are you sure you want to delete?";
                    String button1String = "Delete";
                    String button2String = "Cancel";
                    AlertDialog.Builder ad = new AlertDialog.Builder(context);
                    ad.setTitle(title);
                    ad.setMessage(message);
                    ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("products")
                                            .document(product.getSerialNum())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    DataBaseManager.getInstance().deleteProduct(product);
                                                    productsList.remove(position);
                                                    MyProductsListAdapter.this.notifyDataSetChanged();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                    );
                    ad.setNegativeButton(button2String, new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int arg1) {
                                    // do nothing
                                }
                            }
                    );
                    ad.show();
            }});


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                productNameText = product.getName();
                productDescriptionText = product.getDescription();
                productPriceText = String.valueOf(product.getPrice());
                productQuantityText = String.valueOf(product.getQuantity());

                DataBaseManager.getInstance().setSelectedProduct(product);
                EditProduct editDialog= new EditProduct();
                AppCompatActivity act = (AppCompatActivity) context;
                FragmentManager fm = act.getSupportFragmentManager();
                editDialog.show(fm,null);

            }
        });


        return rowView;

    }

}

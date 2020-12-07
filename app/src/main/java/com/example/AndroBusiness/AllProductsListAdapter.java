package com.example.AndroBusiness;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AllProductsListAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> productsList;
    private Context context;
    private static AllProductsListAdapter instance;
    private static myInterface listener;
    private TextView productName, productDescription, productPrice, productQuantity, CountUnlike, CountLike, myProduct;
    private ImageView addComment, allComments, unlike, like;
    private Button purchase;
    private String userEmail;
    private int productQuantity_int;


    public AllProductsListAdapter(Context context, int resource, ArrayList<Product> productsList) {
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
        final LinearLayout rowView = (LinearLayout) View.inflate(context, R.layout.all_products_list, null);

        final Product product = productsList.get(position);
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        productQuantity_int = product.getQuantity();

        myProduct = (TextView) rowView.findViewById(R.id.myProduct);
        productName = (TextView) rowView.findViewById(R.id.productName);
        productDescription = (TextView) rowView.findViewById(R.id.productDescription);
        productPrice = (TextView) rowView.findViewById(R.id.productPrice);
        productQuantity = (TextView) rowView.findViewById(R.id.productQuantity);
        CountUnlike = (TextView) rowView.findViewById(R.id.CountUnlike);
        CountLike = (TextView) rowView.findViewById(R.id.CountLike);
        unlike = (ImageView) rowView.findViewById(R.id.unlike);
        like = (ImageView) rowView.findViewById(R.id.like);
        addComment = (ImageView) rowView.findViewById(R.id.addComment);
        allComments = (ImageView) rowView.findViewById(R.id.allComments);
        purchase = (Button)rowView.findViewById(R.id.purchase);


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
        CountUnlike.setText(String.valueOf(product.getUnlikeArrayList().size()));
        CountLike.setText(String.valueOf(product.getLikeArrayList().size()));
        if (productQuantity_int > 0) {
            productQuantity.setText(String.valueOf(product.getQuantity()));
        }
        else { productQuantity.setText("Sold Out");}


        // Check if the product belongs to the connected user & insert an icon according to the product
        if (product.getEmail().equals(userEmail)) {
            myProduct.setVisibility(View.VISIBLE);
        }
        else {
            myProduct.setVisibility(View.GONE);
        }

        // Marking the likes / unlikes for the connected user
        if (product.getLikeArrayList().contains(userEmail)) {
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.likefull);
            like.setImageDrawable(myDrawable);
        }
        if (product.getUnlikeArrayList().contains(userEmail)) {
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.unlikefull);
            unlike.setImageDrawable(myDrawable);
        }



        // allComments button ---------------------------------------------------------------------------------------------------------
        allComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseManager.getInstance().setSelectedProduct(product);
                CommentsList CommentsListFragmentDialog = new CommentsList();
                AppCompatActivity act = (AppCompatActivity) context;
                FragmentManager fm = act.getSupportFragmentManager();
                CommentsListFragmentDialog.show(fm, null);
            }


        });



        // addComment button ---------------------------------------------------------------------------------------------------------
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( ! ((userEmail).equals(product.getEmail()) ) ) {
                    DataBaseManager.getInstance().setSelectedProduct(product);
                    AddComment addCommentDialog = new AddComment();
                    AppCompatActivity act = (AppCompatActivity) context;
                    FragmentManager fm = act.getSupportFragmentManager();
                    addCommentDialog.show(fm, null);
                }
                else {
                    Toast.makeText(getContext(), "You can't comment a product that belongs to you", Toast.LENGTH_LONG).show();
                }
            }
        });



        // purchase button ---------------------------------------------------------------------------------------------------------
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( ! ((userEmail).equals(product.getEmail()) )) {
                    if (product.getQuantity() > 0) {
                        DataBaseManager.getInstance().setSelectedProduct(product);
                        PurchaseProduct purchaseDialog = new PurchaseProduct();
                        AppCompatActivity act = (AppCompatActivity) context;
                        FragmentManager fm = act.getSupportFragmentManager();
                        purchaseDialog.show(fm, null);
                    }
                    else {
                        Toast.makeText(getContext(), "The product " + product.getName()+ " sold out", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(getContext(), "You can't purchase a product that belongs to you", Toast.LENGTH_LONG).show();
                }
            }
        });



        // unlike button ---------------------------------------------------------------------------------------------------------
        unlike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                if ( ! ((userEmail).equals(product.getEmail()) ) ) {

                    if (!(product.getLikeArrayList().contains(userEmail))) {

                        if (!(product.getUnlikeArrayList().contains(userEmail))) {
                            product.addToUnlikeArrayList(userEmail);
                            CountUnlike.setText(String.valueOf(product.getUnlikeArrayList().size()));
                            Drawable myDrawable = context.getResources().getDrawable(R.drawable.unlikefull);
                            unlike.setImageDrawable(myDrawable);
                        } else {
                            product.removeFromUnlikeArrayList(userEmail);
                            CountUnlike.setText(String.valueOf(product.getUnlikeArrayList().size()));
                            Drawable myDrawable = context.getResources().getDrawable(R.drawable.dislike);
                            unlike.setImageDrawable(myDrawable);
                        }

                    } else {
                        Toast.makeText(getContext(), "You've already clicked Like", Toast.LENGTH_LONG).show();
                    }

                }

                else{
                    Toast.makeText(getContext(), "You cannot unlike a product that belongs to you", Toast.LENGTH_LONG).show();
                }
                    //save in Firebase
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("products")
                            .document(product.getSerialNum())
                            .set(product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DataBaseManager.getInstance().updateProduct(product);
                                    listener=AllProductsList.getListener();
                                    if(listener!=null){
                                        listener.refresh();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });
            }});



        // like button ---------------------------------------------------------------------------------------------------------
        like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                if ( ! ((userEmail).equals(product.getEmail()) ) ) {

                    if (!(product.getUnlikeArrayList().contains(userEmail))) {

                        if (!(product.getLikeArrayList().contains(userEmail))) {
                            product.addToLikeArrayList(userEmail);
                            CountLike.setText(String.valueOf(product.getLikeArrayList().size()));
                            Drawable myDrawable = context.getResources().getDrawable(R.drawable.likefull);
                            like.setImageDrawable(myDrawable);
                        } else {
                            product.removeFromLikeArrayList(userEmail);
                            CountLike.setText(String.valueOf(product.getLikeArrayList().size()));
                            Drawable myDrawable = context.getResources().getDrawable(R.drawable.like);
                            like.setImageDrawable(myDrawable);
                        }

                    } else {
                        Toast.makeText(getContext(), "You've already clicked UnLike", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getContext(), "You cannot like a product that belongs to you", Toast.LENGTH_LONG).show();
                }
                //save in Firebase
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("products")
                        .document(product.getSerialNum())
                        .set(product)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DataBaseManager.getInstance().updateProduct(product);
                                listener=AllProductsList.getListener();
                                if(listener!=null){
                                    listener.refresh();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });
            }});



        return rowView;
    }


}

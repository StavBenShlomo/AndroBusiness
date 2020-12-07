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

public class MyCommentsListAdapter extends ArrayAdapter<Comment> {

    private ArrayList<Comment> commentsList;
    private ArrayList<Product> productsList;
    private Context context;
    private TextView contentComment, userEmailComment ,productName ,productDescription;

    private static MyLikedListAdapter instance; //??


    public MyCommentsListAdapter(Context context, int resource, ArrayList<Comment> commentsList ,
                                 ArrayList<Product> productsList) {
        super(context, resource, commentsList);
        this.context = context;
        this.commentsList = commentsList;
        this.productsList = productsList;
    }


    @Override
    public int getCount() {
        return commentsList.size();
    }


    @Override
    public Comment getItem(int position) {
        return commentsList.get(position);
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LinearLayout rowView = (LinearLayout) View.inflate(context, R.layout.my_comments, null);

        final Comment comment = commentsList.get(position);

        productDescription = (TextView) rowView.findViewById(R.id.productDescription);
        productName = (TextView) rowView.findViewById(R.id.productName);
        contentComment = (TextView) rowView.findViewById(R.id.contentComment);
        userEmailComment = (TextView) rowView.findViewById(R.id.userEmailComment);

        for (Product p : productsList) {

            // Matching comment to the right product
            if (comment.getSerialNum().equals(p.getSerialNum())) {
                Product product = p;
                productDescription.setText(product.getDescription());
                productName.setText(product.getName());

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

            }
        }

        contentComment.setText(comment.getContent());
        userEmailComment.setText(comment.getUserEmail());

        return rowView;
    }

}

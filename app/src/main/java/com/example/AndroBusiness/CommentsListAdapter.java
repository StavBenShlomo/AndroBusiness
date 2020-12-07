package com.example.AndroBusiness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentsListAdapter extends ArrayAdapter<Comment> {

    private Context context;
    private ArrayList<Comment> commentsList;
    private ImageView delete_comment_btn;
    private TextView userEmailComment, contentComment;
    private static myInterface listener;
    private static CommentsListAdapter instance;


    public CommentsListAdapter(Context context, int resource, ArrayList<Comment> commentsList) {
        super(context, resource, commentsList);
        this.context = context;
        this.commentsList = commentsList;
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
        LinearLayout rowView = (LinearLayout) View.inflate(context, R.layout.comments_list, null);

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final Comment comment = commentsList.get(position);

        delete_comment_btn = (ImageView) rowView.findViewById(R.id.delete_comment_btn);
        userEmailComment = (TextView) rowView.findViewById(R.id.userEmailComment);
        contentComment = (TextView) rowView.findViewById(R.id.contentComment);

        userEmailComment.setText(comment.getUserEmail());
        contentComment.setText(comment.getContent());


        // Check if the comment belongs to the connected user & insert an icon according to the product
        if (comment.getUserEmail().equals(userEmail)) {
            delete_comment_btn.setVisibility(View.VISIBLE);
        }
        else {
            delete_comment_btn.setVisibility(View.GONE);
        }


        delete_comment_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                // Check whether the user who wrote the comment wants to delete it
                if ( (FirebaseAuth.getInstance().getCurrentUser().getEmail()).equals(comment.getUserEmail()) ) {

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
                                    db.collection("comments")
                                            .document(comment.getID())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    DataBaseManager.getInstance().deleteComment(comment);
                                                    commentsList.remove(position);
                                                    CommentsListAdapter.this.notifyDataSetChanged();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });

                                    listener = CommentsList.getListener();
                                    if (listener!=null){
                                        listener.refresh();
                                    }
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
                }

                else {
                    Toast.makeText(getContext(), "You can't delete a comment that not belongs to you", Toast.LENGTH_LONG).show();
                }
            }});

        return rowView;

    }
}

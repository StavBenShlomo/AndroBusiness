package com.example.AndroBusiness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddComment extends DialogFragment {

    private String stringComment, userEmail;
    private EditText userComment;
    private ImageView button_new_comment;
    final Comment comment = new Comment() ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.add_comment, container, false);

        final Product selectedProduct = DataBaseManager.getInstance().getSelectedProduct();

        userComment = (EditText) v.findViewById(R.id.user_comment);
        button_new_comment = (ImageView) v.findViewById(R.id.button_new_comment);


        // Add comment  ----------------------------------------------------------------------------
        button_new_comment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                stringComment = userComment.getText().toString();
                userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                comment.setContent(stringComment);
                comment.setSerialNum(selectedProduct.getSerialNum());
                comment.setUserEmail(userEmail);

                // Check whether the user has entered data
                if (!(stringComment.isEmpty())) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("comments")
                            .document(comment.getID())
                            .set(comment)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DataBaseManager.getInstance().addComment(comment);
                            dismiss();
                        }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                             System.out.println(e);
                        }
                     });
                }
                else
                    userComment.setError("The comment can't be empty");
                }});

        return v;
    }

}

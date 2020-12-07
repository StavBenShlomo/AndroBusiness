package com.example.AndroBusiness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class EditProduct extends DialogFragment {

    private Context context;

    private Button save, retake_photo;
    private ImageView edit_imageView;
    private EditText edit_productName, edit_productdescription, edit_productPrice, edit_prouductQuantity;
    private String productName_save, productdescription_save, productPrice_save, prouductQuantity_save;
    private int check = 0;
    private static myInterface listener;
    private Product selectedProduct;

    // IMG
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;
    private Bitmap imgBitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.edit_product, container, false);

       selectedProduct = DataBaseManager.getInstance().getSelectedProduct();

        // Get the img from the Firestore
        final ImageView imageView = v.findViewById(R.id.edit_imageView);
        String imageUrl = selectedProduct.getImage();
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
                                Glide.with(getActivity())
                                        .load(downloadUrl)
                                        .into(imageView);
                            } else {
                                System.out.println( "Getting download url was not successful."+
                                        task.getException());
                            }
                        }
                    });
        }


        save = (Button) v.findViewById(R.id.save);
        edit_productName = (EditText) v.findViewById(R.id.edit_productName);
        edit_productdescription = (EditText) v.findViewById(R.id.edit_productdescription);
        edit_productPrice = (EditText) v.findViewById(R.id.edit_productPrice);
        edit_prouductQuantity = (EditText) v.findViewById(R.id.edit_prouductQuantity);
        retake_photo = (Button) v.findViewById(R.id.retake_photo);
        edit_imageView = (ImageView) v.findViewById(R.id.edit_imageView);

        edit_productName.setText(selectedProduct.getName());
        edit_productdescription.setText(selectedProduct.getDescription());
        edit_productPrice.setText(String.valueOf(selectedProduct.getPrice()));
        edit_prouductQuantity.setText(String.valueOf(selectedProduct.getQuantity()));


        // Take a img from the camera
        retake_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }});


        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                check = 0;
                productName_save = edit_productName.getText().toString();
                productdescription_save = edit_productdescription.getText().toString();
                productPrice_save = edit_productPrice.getText().toString();
                prouductQuantity_save = edit_prouductQuantity.getText().toString();


                selectedProduct.setDescription(productdescription_save);

                // Checking the data the user has entered
                if (!(productName_save.isEmpty())) {
                    selectedProduct.setName(productName_save);
                }
                else {
                    edit_productName.setError("the Name can't be null ");
                    check++;
                }
                if (productPrice_save.matches("[0-9]+[.]{1,}[0-9]+") || productPrice_save.matches("[0-9]{1,}") ) {
                    selectedProduct.setPrice(Double.valueOf(productPrice_save));
                }
                else {
                    edit_productPrice.setError("the Price need to be a number (double)");
                    check++;
                }
                if (prouductQuantity_save.matches("[0-9]{1,}") ) {
                    selectedProduct.setQuantity(Integer.valueOf(prouductQuantity_save));
                }
                else {
                    edit_prouductQuantity.setError("the Quantity need to be a number (int)");
                    check++;
                }


                // Save if all data is valid
                if (check == 0) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("products")
                            .document(selectedProduct.getSerialNum())
                            .set(selectedProduct)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DataBaseManager.getInstance().updateProduct(selectedProduct);
                                    listener=MyProductsList.getListener();
                                    if(listener!=null){
                                        listener.refresh();
                                    }
                                    dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });
                    Toast.makeText(getContext(), "save", Toast.LENGTH_SHORT).show();
                }

            }});

        return  v;
    }



    // Save the image to Firestore -----------------------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imgBitmap = (Bitmap) data.getExtras().get("data");
                uploadImage(imgBitmap);
            }
        }
    }

    private void uploadImage(Bitmap imgBitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();
            final String imgName = selectedProduct.getSerialNum() + ".jpg";
            StorageReference imageRef = storageRef.child(imgName);

            StorageReference mountainImagesRef = storageRef.child(imgName);
            selectedProduct.setImage(imgName);

            edit_imageView.setImageBitmap(imgBitmap);

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    System.out.println(exception);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    selectedProduct.setImage(imgName);
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

}

package com.example.AndroBusiness;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class AddProduct extends Fragment {

    private Button new_photo, save;
    private ImageView new_imageView;
    private EditText new_productName, new_productdescription, new_productPrice, new_prouductQuantity;
    private String productName_save, productdescription_save, productPrice_save, prouductQuantity_save;
    private int check = 0;

    final Product product = new Product();

    // IMG
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 111;
    private Bitmap imgBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v= inflater.inflate(R.layout.add_product, container, false);

        save = (Button) v.findViewById(R.id.save_new);
        new_productName = (EditText) v.findViewById(R.id.new_productName);
        new_productdescription = (EditText) v.findViewById(R.id.new_productdescription);
        new_productPrice = (EditText) v.findViewById(R.id.new_productPrice);
        new_prouductQuantity = (EditText) v.findViewById(R.id.new_prouductQuantity);
        new_photo = (Button) v.findViewById(R.id.new_photo);
        new_imageView = (ImageView) v.findViewById(R.id.new_imageView);


        // Take a picture from the camera
        new_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }});


        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                check = 0;
                productName_save = new_productName.getText().toString();
                productdescription_save = new_productdescription.getText().toString();
                productPrice_save = new_productPrice.getText().toString();
                prouductQuantity_save = new_prouductQuantity.getText().toString();


                product.setDescription(productdescription_save);

                // Checking the data the user has entered
                if (!(productName_save.isEmpty())) {
                    product.setName(productName_save);
                }
                else {
                    new_productName.setError("The Name can't be empty");
                    check++;
                }
                if (productPrice_save.matches("[0-9]+[.]{1,}[0-9]+") || productPrice_save.matches("[0-9]{1,}") ) {
                    product.setPrice(Double.valueOf(productPrice_save));
                }
                else {
                    new_productPrice.setError("The price need to be a number");
                    check++;
                }
                if (prouductQuantity_save.matches("[0-9]{1,}") ) {
                    product.setQuantity(Integer.valueOf(prouductQuantity_save));
                }
                else {
                    new_prouductQuantity.setError("The quantity need to be a number");
                    check++;
                }

                // Save if all data is valid
                if (check == 0) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("products")
                            .document(product.getSerialNum())
                            .set(product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DataBaseManager.getInstance().addProduct(product);
                                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });
                }
            }});

        return v;
    }


    // Save the image to Firestore ----------------------------------------------------------------------------
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
            final String imgName = product.getSerialNum() + ".jpg";
            StorageReference imageRef = storageRef.child(imgName);

            StorageReference mountainImagesRef = storageRef.child(imgName);
            product.setImage(imgName);

            new_imageView.setImageBitmap(imgBitmap);

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
                    product.setImage(imgName);
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}

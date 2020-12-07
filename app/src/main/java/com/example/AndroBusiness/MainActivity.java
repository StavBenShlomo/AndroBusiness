package com.example.AndroBusiness;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DataBaseManager.getInstance().openDataBase(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AllProductsList fragment = new AllProductsList();
        fragmentTransaction.replace(R.id.rootview, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        if (DataBaseManager.getInstance().checkdb()){
            Firebasefirestore();
        }

    }

    //insert sqllite from firebac
    public void Firebasefirestore(){
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getReference();
                                Product product = document.toObject(Product.class);
                                DataBaseManager.getInstance().addProduct(product);
                            }
                        }
                    }
                });

        db.collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getReference();
                                Comment comment = document.toObject(Comment.class);

                                    DataBaseManager.getInstance().addComment(comment);
                            }
                        }
                    }
                });

        db.collection("purchases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // document.getReference();
                                Purchase purchase = document.toObject(Purchase.class);
                                DataBaseManager.getInstance().addPurchase(purchase);
                                }
                            }
                        }
                    });
    }


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataBaseManager.getInstance().closeDataBase();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fm = getSupportFragmentManager();
        switch (item.getItemId()) {

            case R.id.app_bar_singout: {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                MainActivity.this.startActivity(intent);
                DataBaseManager.getInstance().onUpgrade();
                return true;
            }

            case R.id.app_bar_wishlist: {
                MyLikedList myLikedList = new MyLikedList();
                FragmentTransaction ft = fm.beginTransaction();
                //ft.addToBackStack(null);
                ft.replace(R.id.rootview, myLikedList);
                ft.commit();
                Toast.makeText(this, "Wishlist", Toast.LENGTH_SHORT).show();
                return true;
            }

            case R.id.app_bar_new: {
                AddProduct addProduct = new AddProduct();
                FragmentTransaction ft = fm.beginTransaction();
                //ft.addToBackStack(null);
                ft.replace(R.id.rootview, addProduct);
                ft.commit();
                Toast.makeText(this, "Add Product", Toast.LENGTH_SHORT).show();
                return true;
            }

            case R.id.app_bar_myproducts: {
                MyProductsList myProductsList =new MyProductsList();
                FragmentTransaction ft= fm.beginTransaction();
                //ft.addToBackStack(null);
                ft.replace(R.id.rootview, myProductsList);
                ft.commit();
                Toast.makeText(this,"My Products", Toast.LENGTH_SHORT).show();
                return true;
            }

            case R.id.app_bar_allproducts: {
                AllProductsList allProductsList = new AllProductsList();
                FragmentTransaction ft= fm.beginTransaction();
                //ft.addToBackStack(null);
                ft.replace(R.id.rootview, allProductsList);
                ft.commit();
                Toast.makeText(this,"All Products List", Toast.LENGTH_SHORT).show();
                return true;
            }

            case R.id.app_bar_purchases: {
                MyPurchasesList myPurchasesList = new MyPurchasesList();
                FragmentTransaction ft= fm.beginTransaction();
                //ft.addToBackStack(null);
                ft.replace(R.id.rootview, myPurchasesList);
                ft.commit();
                Toast.makeText(this,"My Purchases", Toast.LENGTH_SHORT).show();
                return true;
            }

            case R.id.app_bar_comments: {
                MyCommentsList myCommentsList = new MyCommentsList();
                FragmentTransaction ft= fm.beginTransaction();
                //ft.addToBackStack(null);
                ft.replace(R.id.rootview, myCommentsList);
                ft.commit();
                Toast.makeText(this,"My Comments", Toast.LENGTH_SHORT).show();
                return true;
            }

            case R.id.app_bar_summary: {
                Summary summary = new Summary();
                FragmentTransaction ft= fm.beginTransaction();
                //ft.addToBackStack(null);
                ft.replace(R.id.rootview, summary);
                ft.commit();
                Toast.makeText(this,"Summary", Toast.LENGTH_SHORT).show();
                return true;
            }

        }

    return false;

    }

}

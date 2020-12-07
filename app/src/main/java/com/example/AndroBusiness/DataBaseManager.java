package com.example.AndroBusiness;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    private static DataBaseManager instance = null;
    private Context context = null;
    private static SQLiteDB db = null;
    private Product selectedProduct = null;

    private DataBaseManager(){ }


    public static boolean checkdb(){
        return db==null ;
    }

    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
        }
    }

    private void clean() {
    }

    public Context getContext() {
        return context;

    }

    public void openDataBase(Context context) {
        this.context = context;
        if (context != null) {
            db = new SQLiteDB(context);
            db.open();
        }
    }

    public void closeDataBase() {
        if(db!=null){
            db.close();
        }
    }

    public void onUpgrade() {
        if (db != null ) {
            db.onUpgrade(db.getDb(), 1,1 );
        }
    }


    // Product -----------------------------------------------------
    public void addProduct(Product item) {
        if (db != null) {
            db.addProduct(item);
        }
    }

    public Product readProduct(String id) {
        Product result = null;
        if (db != null) {
            result = db.readProduct(id);
        }
        return result;
    }

    public List<Product> getAllProduct() {
        List<Product> result = new ArrayList<Product>();
        if (db != null) {
            result = db.getAllProduct();
        }
        return result;
    }

    public void updateProduct(Product item) {
        if (db != null && item != null) {
            db.updateProduct(item);
        }
    }

    public void deleteProduct(Product item) {
        if (db != null) {
            db.deleteProduct(item);
        }
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }


    // Comment -----------------------------------------------------
    public void addComment(Comment item) {
        if (db != null) {
            db.addComment(item);
        }
    }

    public void deleteComment(Comment item) {
        if (db != null) {
            db.deleteComment(item);
        }
    }

    // Purchase -----------------------------------------------------
    public void addPurchase(Purchase item) {
        if (db != null) {
            db.addPurchase(item);
        }
    }


}


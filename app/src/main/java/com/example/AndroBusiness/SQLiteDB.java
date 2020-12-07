package com.example.AndroBusiness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDB  extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "productDB7";

        // item table
        private static final String TABLE_PRODUCT_NAME = "product";
        private static final String PRODUCT_COLUMN_SERIALNUM = "serialNum";
        private static final String PRODUCT_COLUMN_NAME = "name";
        private static final String PRODUCT_COLUMN_IMAGE = "image";
        private static final String PRODUCT_COLUMN_DESCRIPTION = "description";
        private static final String PRODUCT_COLUMN_PRICE = "price";
        private static final String PRODUCT_COLUMN_QUANTITY = "quantity";
        private static final String PRODUCT_COLUMN_EMAIL = "email";


        // item table
        private static final String TABLE_COMMENT_NAME = "comment";
        private static final String COMMENT_COLUMN_ID = "ID";
        private static final String COMMENT_COLUMN_SERIALNUM = "serialNum";
        private static final String COMMENT_COLUMN_CONTENT = "content";
        private static final String COMMENT_COLUMN_USEREMAIL = "userEmail";


        // purchase table
        private static final String TABLE_PURCHASE_NAME = "purchase";
        private static final String PURCHASE_COLUMN_IDPURCHASE = "IDPurchase";
        private static final String PURCHASE_COLUMN_USEREMAILPURCHASE = "userEmailPurchase";
        private static final String PURCHASE_COLUMN_PRODUCTSERIALNUM = "productSerialNum";
        private static final String PURCHASE_COLUMN_PRODUCTNAME = "productName";
        private static final String PURCHASE_COLUMN_PRODUCTIMAGE = "productImage";
        private static final String PURCHASE_COLUMN_PRODUCTDESCRIPTION = "productDescription";
        private static final String PURCHASE_COLUMN_PRODUCTPRICE = "productPrice";
        private static final String PURCHASE_COLUMN_PRODUCTQUANTITY = "productQuantity";
        private static final String PURCHASE_COLUMN_PRODUCTUSEREMAIL = "productUserEmail";


        private static final String[] TABLE_PRODUCT_COLUMNS = {PRODUCT_COLUMN_SERIALNUM, PRODUCT_COLUMN_NAME,
                PRODUCT_COLUMN_IMAGE, PRODUCT_COLUMN_DESCRIPTION,PRODUCT_COLUMN_PRICE, PRODUCT_COLUMN_QUANTITY,
                PRODUCT_COLUMN_EMAIL};

        private static final String[] TABLE_COMMENT_COLUMNS = {COMMENT_COLUMN_ID, COMMENT_COLUMN_SERIALNUM,
                COMMENT_COLUMN_CONTENT, COMMENT_COLUMN_USEREMAIL  };

        private static final String[] TABLE_PURCHASE_COLUMNS = { PURCHASE_COLUMN_IDPURCHASE, PURCHASE_COLUMN_USEREMAILPURCHASE,
                PURCHASE_COLUMN_PRODUCTSERIALNUM, PURCHASE_COLUMN_PRODUCTNAME, PURCHASE_COLUMN_PRODUCTIMAGE,
                PURCHASE_COLUMN_PRODUCTDESCRIPTION, PURCHASE_COLUMN_PRODUCTPRICE, PURCHASE_COLUMN_PRODUCTQUANTITY,
                PURCHASE_COLUMN_PRODUCTUSEREMAIL };


        private SQLiteDatabase db = null;


        public SQLiteDB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        public SQLiteDatabase getDb() {
            return db;
        }


        @Override public void onCreate(SQLiteDatabase db) {
            try {
                // SQL statement to create item table
                String CREATE_ITEM_TABLE = "create table if not exists " + TABLE_PRODUCT_NAME +" ( "
                        + PRODUCT_COLUMN_SERIALNUM +" TEXT PRIMARY KEY, "
                        + PRODUCT_COLUMN_NAME +" TEXT, "
                        + PRODUCT_COLUMN_IMAGE +" TEXT, "
                        + PRODUCT_COLUMN_DESCRIPTION +" TEXT, "
                        + PRODUCT_COLUMN_PRICE +" TEXT, "
                        + PRODUCT_COLUMN_QUANTITY +" TEXT, "
                        + PRODUCT_COLUMN_EMAIL +" TEXT) ";
                db.execSQL(CREATE_ITEM_TABLE);

                // SQL statement to create item table
                String CREATE_COMMENT_TABLE = "create table if not exists " + TABLE_COMMENT_NAME +" ( "
                        + COMMENT_COLUMN_ID +" TEXT PRIMARY KEY, "
                        + COMMENT_COLUMN_SERIALNUM +" TEXT , " // ??FOREIGN KEY
                        + COMMENT_COLUMN_CONTENT +" TEXT, "
                        + COMMENT_COLUMN_USEREMAIL +" TEXT) ";
                db.execSQL(CREATE_COMMENT_TABLE);

                // SQL statement to create item table
                String CREATE_PURCHASE_TABLE = "create table if not exists " + TABLE_PURCHASE_NAME +" ( "
                        + PURCHASE_COLUMN_IDPURCHASE +" TEXT PRIMARY KEY, "
                        + PURCHASE_COLUMN_USEREMAILPURCHASE +" TEXT , "

                        + PURCHASE_COLUMN_PRODUCTSERIALNUM +" TEXT, "
                        + PURCHASE_COLUMN_PRODUCTNAME +" TEXT, "
                        + PURCHASE_COLUMN_PRODUCTIMAGE +" TEXT, "
                        + PURCHASE_COLUMN_PRODUCTDESCRIPTION +" TEXT, "
                        + PURCHASE_COLUMN_PRODUCTPRICE +" TEXT, "
                        + PURCHASE_COLUMN_PRODUCTQUANTITY +" TEXT, "
                        + PURCHASE_COLUMN_PRODUCTUSEREMAIL +" TEXT) ";
                db.execSQL(CREATE_PURCHASE_TABLE);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PRODUCT_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_COMMENT_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PURCHASE_NAME);
            onCreate(db);
        }

        public void addProduct(Product item) {

            try {
                // make values to be inserted
                ContentValues values = new ContentValues();

                values.put(PRODUCT_COLUMN_SERIALNUM , item.getSerialNum());
                values.put(PRODUCT_COLUMN_NAME , item.getName());
                values.put(PRODUCT_COLUMN_IMAGE , item.getImage());
                values.put(PRODUCT_COLUMN_DESCRIPTION , item.getDescription());
                values.put(PRODUCT_COLUMN_PRICE , item.getPrice());
                values.put(PRODUCT_COLUMN_QUANTITY , item.getQuantity());
                values.put(PRODUCT_COLUMN_EMAIL , item.getEmail());

                // insert item
                db.insert(TABLE_PRODUCT_NAME, null, values);

            } catch (Throwable t) {
                t.printStackTrace();

            }
        }



        public Product readProduct(String id) {
            Product item = null;
            Cursor cursor = null;
            try {
                // get  query
            /*cursor = db
                    .query(TABLE_PRODUCT_NAME,
                            TABLE_PRODUCT_COLUMNS,
                            PRODUCT_COLUMN_SERIALNUM + " = ?",
                            new String[] { id },
                            null, null,
                            null, null); */



                // if results !=null, parse the first one
                if(cursor!=null && cursor.getCount()>0){

                    cursor.moveToFirst();

                    item = cursorToItemProduct(cursor);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            finally {
                if(cursor!=null){
                    cursor.close();
                }
            }
            return item;
        }



        public List<Product> getAllProduct() {
            List<Product> result = new ArrayList<Product>();
            Cursor cursor = null;
            try {
            /*cursor = db.query(TABLE_PRODUCT_NAME, TABLE_PRODUCT_COLUMNS, null, null,
                    null, null, null); */

                String query = "SELECT * FROM " + TABLE_PRODUCT_NAME;
                // Cursor point to a location in your results
                cursor = db.rawQuery(query, null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Product item = cursorToItemProduct(cursor);
                    result.add(item);
                    cursor.moveToNext();
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
            finally {
                // make sure to close the cursor
                if(cursor!=null){
                    cursor.close();
                }
            }

            return result;
        }

        private Product cursorToItemProduct(Cursor cursor) {
            Product result = new Product();
            try {
                result = new Product();
                result.setSerialNum(cursor.getString(0));
                result.setName(cursor.getString(1));
                result.setImage(cursor.getString(2));
                result.setDescription(cursor.getString(3));
                result.setPrice(cursor.getDouble(4));
                result.setQuantity(cursor.getInt(5));
                result.setEmail(cursor.getString(6));

            } catch (Throwable t) {
                t.printStackTrace();
            }

            return result;
        }


        public int updateProduct(Product item) {
            int cnt = 0;
            try {

                // make values to be inserted
                ContentValues values = new ContentValues();
                values.put(PRODUCT_COLUMN_SERIALNUM, item.getSerialNum());
                values.put(PRODUCT_COLUMN_NAME, item.getName());
                values.put(PRODUCT_COLUMN_IMAGE, item.getImage());
                values.put(PRODUCT_COLUMN_DESCRIPTION, item.getDescription());
                values.put(PRODUCT_COLUMN_PRICE, item.getPrice());
                values.put(PRODUCT_COLUMN_QUANTITY, item.getQuantity());
                values.put(PRODUCT_COLUMN_EMAIL, item.getEmail());


                // update
                cnt = db.update(TABLE_PRODUCT_NAME, values, PRODUCT_COLUMN_SERIALNUM + " = ?",
                        new String[] { String.valueOf(item.getSerialNum()) });
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return cnt;
        }



        public void deleteProduct(Product item) {

            try {

                // delete item
                db.delete(TABLE_PRODUCT_NAME, PRODUCT_COLUMN_SERIALNUM + " = ?",
                        new String[] { item.getSerialNum() });
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }

        public void addComment(Comment item) {

            try {
                // make values to be inserted
                ContentValues values = new ContentValues();

                values.put(COMMENT_COLUMN_ID , item.getID());
                values.put(COMMENT_COLUMN_SERIALNUM , item.getSerialNum());
                values.put(COMMENT_COLUMN_CONTENT , item.getContent());
                values.put(COMMENT_COLUMN_USEREMAIL , item.getUserEmail());

                // insert item
                db.insert(TABLE_COMMENT_NAME, null, values);

            } catch (Throwable t) {
                t.printStackTrace();

            }
        }



        public Comment readComment(String id) {
            Comment item = null;
            Cursor cursor = null;
            try {
                // get  query
            /*cursor = db
                    .query(TABLE_PRODUCT_NAME,
                           TABLE_COMMENT_COLUMNS,
                            PRODUCT_COLUMN_SERIALNUM + " = ?",
                            new String[] { id },
                            null, null,
                            null, null); */

                // if results !=null, parse the first one
                if(cursor!=null && cursor.getCount()>0){

                    cursor.moveToFirst();

                    item = cursorToItemComment(cursor);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            finally {
                if(cursor!=null){
                    cursor.close();
                }
            }
            return item;
        }


        public List<Comment> getAllCommets() {
            List<Comment> result = new ArrayList<Comment>();
            Cursor cursor = null;
            try {
            /*cursor = db.query(TABLE_PRODUCT_NAME, TABLE_COMMENT_COLUMNS, null, null,
                    null, null, null); */

                String query = "SELECT * FROM " + TABLE_COMMENT_NAME;
                // Cursor point to a location in your results
                cursor = db.rawQuery(query, null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Comment item = cursorToItemComment(cursor);
                    result.add(item);
                    cursor.moveToNext();
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
            finally {
                // make sure to close the cursor
                if(cursor!=null){
                    cursor.close();
                }
            }

            return result;
        }

        private Comment cursorToItemComment(Cursor cursor) {
            Comment result = new Comment();
            try {
                result = new Comment();
                result.setID(cursor.getString(0));
                result.setSerialNum(cursor.getString(1));
                result.setContent(cursor.getString(2));
                result.setUserEmail(cursor.getString(3));

            } catch (Throwable t) {
                t.printStackTrace();
            }

            return result;
        }


        public int updateComment(Comment item) {
            int cnt = 0;
            try {

                // make values to be inserted
                ContentValues values = new ContentValues();
                values.put(COMMENT_COLUMN_ID, item.getID());
                values.put(COMMENT_COLUMN_SERIALNUM, item.getSerialNum());
                values.put(COMMENT_COLUMN_CONTENT, item.getContent());
                values.put(COMMENT_COLUMN_USEREMAIL, item.getUserEmail());

                // update
                cnt = db.update(TABLE_COMMENT_NAME, values, COMMENT_COLUMN_ID + " = ?",
                        new String[] { String.valueOf(item.getID()) });
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return cnt;
        }



        public void deleteComment(Comment item) {

            try {

                // delete item
                db.delete(TABLE_COMMENT_NAME, COMMENT_COLUMN_ID+ " = ?",
                        new String[] { item.getID() });
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }


        public void addPurchase(Purchase item) {

            try {
                // make values to be inserted
                ContentValues values = new ContentValues();

                values.put(PURCHASE_COLUMN_IDPURCHASE , item.getIDPurchase());
                values.put(PURCHASE_COLUMN_USEREMAILPURCHASE , item.getUserEmailPurchase());
                values.put(PURCHASE_COLUMN_PRODUCTSERIALNUM , item.getProductSerialNum());
                values.put(PURCHASE_COLUMN_PRODUCTNAME , item.getProductName());
                values.put(PURCHASE_COLUMN_PRODUCTIMAGE , item.getProductImage());
                values.put(PURCHASE_COLUMN_PRODUCTDESCRIPTION , item.getProductDescription());
                values.put(PURCHASE_COLUMN_PRODUCTPRICE , item.getProductPrice());
                values.put(PURCHASE_COLUMN_PRODUCTQUANTITY , item.getProductQuantity());
                values.put(PURCHASE_COLUMN_PRODUCTUSEREMAIL , item.getProductUserEmail());

                // insert item
                db.insert(TABLE_PURCHASE_NAME, null, values);

            } catch (Throwable t) {
                t.printStackTrace();

            }
        }



        public Purchase readPurchase(String id) {
            Purchase item = null;
            Cursor cursor = null;
            try {
                // get  query
            /*cursor = db
                    .query(TABLE_PRODUCT_NAME,
                            TABLE_ITEM_COLUMNS,
                            PRODUCT_COLUMN_SERIALNUM + " = ?",
                            new String[] { id },
                            null, null,
                            null, null); */



                // if results !=null, parse the first one
                if(cursor!=null && cursor.getCount()>0){

                    cursor.moveToFirst();

                    item = cursorToItemPurchase(cursor);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            finally {
                if(cursor!=null){
                    cursor.close();
                }
            }
            return item;
        }


        public List<Purchase> getAllPurchase() {
            List<Purchase> result = new ArrayList<Purchase>();
            Cursor cursor = null;
            try {
            /*cursor = db.query(TABLE_PRODUCT_NAME, TABLE_ITEM_COLUMNS, null, null,
                    null, null, null); */

                String query = "SELECT * FROM " + TABLE_PURCHASE_NAME;
                // Cursor point to a location in your results
                cursor = db.rawQuery(query, null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Purchase item = cursorToItemPurchase(cursor);
                    result.add(item);
                    cursor.moveToNext();
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
            finally {
                // make sure to close the cursor
                if(cursor!=null){
                    cursor.close();
                }
            }

            return result;
        }

        private Purchase cursorToItemPurchase(Cursor cursor) {
            Purchase result = new Purchase();
            try {
                result = new Purchase();
                result.setIDPurchase(cursor.getString(0));
                result.setUserEmailPurchase(cursor.getString(1));
                result.setProductSerialNum(cursor.getString(2));
                result.setProductName(cursor.getString(3));
                result.setProductImage(cursor.getString(4));
                result.setProductDescription(cursor.getString(5));
                result.setProductPrice(cursor.getDouble(6));
                result.setProductQuantity(cursor.getInt(7));
                result.setProductUserEmail(cursor.getString(8));

            } catch (Throwable t) {
                t.printStackTrace();
            }

            return result;
        }


        public int updatePurchase(Purchase item) {
            int cnt = 0;
            try {

                // make values to be inserted
                ContentValues values = new ContentValues();
                values.put(PURCHASE_COLUMN_IDPURCHASE, item.getIDPurchase());
                values.put(PURCHASE_COLUMN_USEREMAILPURCHASE, item.getUserEmailPurchase());
                values.put(PURCHASE_COLUMN_PRODUCTSERIALNUM, item.getProductSerialNum());
                values.put(PURCHASE_COLUMN_PRODUCTNAME, item.getProductName());
                values.put(PURCHASE_COLUMN_PRODUCTIMAGE, item.getProductImage());
                values.put(PURCHASE_COLUMN_PRODUCTDESCRIPTION, item.getProductDescription());
                values.put(PURCHASE_COLUMN_PRODUCTPRICE, item.getProductPrice());
                values.put(PURCHASE_COLUMN_PRODUCTQUANTITY, item.getProductQuantity());
                values.put(PURCHASE_COLUMN_PRODUCTUSEREMAIL, item.getProductUserEmail());

                // update
                cnt = db.update(TABLE_PURCHASE_NAME, values, PURCHASE_COLUMN_IDPURCHASE + " = ?",
                        new String[] { String.valueOf(item.getIDPurchase()) });
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return cnt;
        }



        public void deletePurchase(Purchase item) {
            try {
                // delete item
                db.delete(TABLE_PURCHASE_NAME, PURCHASE_COLUMN_IDPURCHASE + " = ?",
                        new String[]{item.getIDPurchase()});
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        public void open() {
            try {
                db = getWritableDatabase();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        public void close() {
            try {
                db.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }



    }


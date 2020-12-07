package com.example.AndroBusiness;

import java.util.UUID;

public class Purchase {

    // Purchase  Variables
    private String IDPurchase;
    private String userEmailPurchase;

    // Product Variables
    private String productSerialNum;
    private String productName;
    private String productImage;
    private String productDescription;
    private double productPrice;
    private int productQuantity;
    private String productUserEmail;


    // Constructors
    public Purchase() {
        this.IDPurchase = UUID.randomUUID().toString();
    }

    public Purchase(String productSerialNum, String productName, String productImage, String productDescription,
                    double productPrice, int productQuantity, String productUserEmail, String IDPurchase,
                    String userEmailPurchase ) {
        setProductSerialNum(productSerialNum);
        setProductName(productName);
        setProductImage(productImage) ;
        setProductDescription(productDescription);
        setProductPrice(productPrice);
        setProductQuantity(productQuantity) ;
        setProductUserEmail(productUserEmail);
        setIDPurchase(IDPurchase);
        setUserEmailPurchase(userEmailPurchase);
    }


    // Setters & Getters
    public String getProductSerialNum() {
        return productSerialNum;
    }

    public void setProductSerialNum(String productSerialNum) {
        this.productSerialNum = productSerialNum;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }


    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }


    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }


    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }


    public String getProductUserEmail() {
        return productUserEmail;
    }

    public void setProductUserEmail(String productUserEmail) {
        this.productUserEmail = productUserEmail;
    }


    public String getIDPurchase() {
        return IDPurchase;
    }

    public void setIDPurchase(String IDPurchase) {
        this.IDPurchase = IDPurchase;
    }


    public String getUserEmailPurchase() {
        return userEmailPurchase;
    }

    public void setUserEmailPurchase(String userEmailPurchase) {
        this.userEmailPurchase = userEmailPurchase;
    }
}

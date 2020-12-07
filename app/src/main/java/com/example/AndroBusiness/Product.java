package com.example.AndroBusiness;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.UUID;

public class Product {

    // Variables
    private String serialNum;
    private String name;
    private String image;
    private String description;
    private double price;
    private int quantity;
    private String email;
    private ArrayList<String> likeArrayList;
    private ArrayList<String> unlikeArrayList;

    // Constructors
    public Product() {
        this.serialNum = UUID.randomUUID().toString();
        this.email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        likeArrayList = new ArrayList<String>();
        unlikeArrayList = new ArrayList<String>();
    }

    public Product(String serialNum, String name, String image,
                   String description, double price, int quantity, String email ) {
        setSerialNum(serialNum);
        setName(name);
        setImage(image);
        setDescription(description);
        setPrice(price);
        setQuantity(quantity);
        setEmail(email);
    }


    // Setters & Getters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    // likeArrayList ---------------------------------------------------------------------------
    public ArrayList<String> getLikeArrayList() {
        return likeArrayList;
    }

    public void setLikeArrayList(ArrayList<String> likeArrayList) {
        this.likeArrayList = likeArrayList;
    }

    public void addToLikeArrayList(String email) {
        likeArrayList.add(email);
    }
    public void removeFromLikeArrayList(String email) {
        likeArrayList.remove(email);
    }



    // unlikeArrayList --------------------------------------------------------------------------
    public ArrayList<String> getUnlikeArrayList() {
        return unlikeArrayList;
    }

    public void setUnlikeArrayList(ArrayList<String> unlikeArrayList) {
        this.unlikeArrayList = unlikeArrayList;
    }

    public void addToUnlikeArrayList(String email) {
        unlikeArrayList.add(email);
    }
    public void removeFromUnlikeArrayList(String email) {
        unlikeArrayList.remove(email);
    }

}

package com.example.AndroBusiness;

import java.util.UUID;

public class Comment {

    // Variables
    private String ID;
    private String content;
    private String userEmail;
    private String serialNum;

    // Constructors
    public Comment() {
        this.ID = UUID.randomUUID().toString();
    }

    public Comment(String content, String userEmail, String serialNum, String ID) {
        setContent(content);
        setUserEmail(userEmail);
        setSerialNum(serialNum);
        setID(ID);
    }

    // Setters & Getters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}

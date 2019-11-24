package com.example.androidlabs;

import java.io.Serializable;

public class Message implements Serializable {
    /**
     * The content of the message
     */
    String message;


    /**
     * boolean to determine, who is sender of this message
     */
    boolean isSent;
    long id;


    /**
     * Constructor to make a Message object
     */
    public Message(String message, boolean isMine, long id) {
        super();
        this.message = message;
        this.isSent = isMine;
        this.id = id;

    }


    public String getMessage() {
        return this.message;
    }

    public Long getId() {
        return this.id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMine() {
        return isSent;
    }

    public void setMine(boolean isMine) {
        this.isSent = isMine;
    }


}
package com.uit.uitnowfordrivers;

public class Message {
    public String senderId;
    public String message;
    public long dateTime;

    public Message() {
    }

    public Message(String senderId, String message, long dateTime) {
        this.senderId = senderId;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}

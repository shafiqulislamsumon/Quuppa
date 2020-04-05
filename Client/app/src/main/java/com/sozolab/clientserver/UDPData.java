package com.sozolab.clientserver;

public class UDPData {
    String message;

    public UDPData(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

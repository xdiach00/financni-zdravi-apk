package com.digikouc.financnizdravi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data data;

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }
}
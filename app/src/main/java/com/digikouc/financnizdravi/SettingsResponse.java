package com.digikouc.financnizdravi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SettingsResponse implements Serializable {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

}
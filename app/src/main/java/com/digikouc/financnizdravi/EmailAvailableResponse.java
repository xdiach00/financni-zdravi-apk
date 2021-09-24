package com.digikouc.financnizdravi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmailAvailableResponse implements Serializable {

    private Boolean status;
    @SerializedName("message")
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
package com.digikouc.financnizdravi;

public class DeleteAccountRequest {

    private String api_token;
    private String password;

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.example.android.task3.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Luteh on 2/6/2017.
 */

public class User {

    private int id;
    private String email;
    private String password;
    private String token_auth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken_auth() {
        return token_auth;
    }

    public void setToken_auth(String token_auth) {
        this.token_auth = token_auth;
    }
}



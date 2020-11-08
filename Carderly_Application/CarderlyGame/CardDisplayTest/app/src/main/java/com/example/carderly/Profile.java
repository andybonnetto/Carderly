package com.example.carderly;

import java.io.Serializable;

public class Profile implements Serializable {
    private static final String TAG = "Profile";
    protected String username;
    protected String password;
    protected String photoPath;
    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

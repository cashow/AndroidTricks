package com.cashow.databindingdemo.model;

import android.util.Log;
import android.view.View;

import java.util.HashMap;

public class User {
    public String firstName;
    public String lastName;
    public boolean isAdult;
    public HashMap<String, String> nameMap;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        nameMap = new HashMap<>();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }


    public void onClickFriend(View view) {
        Log.d("User", "onClickFriend");
    }

    public void onSaveClick(String firstName) {
        Log.d("User", "onSaveClick " + firstName);
    }
}

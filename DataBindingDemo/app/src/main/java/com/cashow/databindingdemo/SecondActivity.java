package com.cashow.databindingdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cashow.databindingdemo.model.User;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContactItem binding = DataBindingUtil.setContentView(this, R.layout.activity_second);

        User user = new User("Hello", "world");
        binding.setUser(user);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user);
        userList.add(user);
        binding.setUserList(userList);
    }
}

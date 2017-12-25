package com.cashow.databindingdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cashow.databindingdemo.databinding.ActivityForthBinding;
import com.cashow.databindingdemo.model.User;

public class ForthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityForthBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_forth);

        User user = new User("Hello", "world");
        user.nameMap.put("aa", "bb");
        user.nameMap.put("cc", "dd");
        user.nameMap.put("ee", "ff");
        binding.setUser(user);
    }
}

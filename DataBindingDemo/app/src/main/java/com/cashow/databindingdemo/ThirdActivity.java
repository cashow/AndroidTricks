package com.cashow.databindingdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cashow.databindingdemo.databinding.ActivityThirdBinding;
import com.cashow.databindingdemo.model.User;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityThirdBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_third);

        User user = new User("Hello", "world");
        user.nameMap.put("aa", "bb");
        user.nameMap.put("cc", "dd");
        user.nameMap.put("ee", "ff");
        binding.setUser(user);
    }
}

package com.cashow.presentation.common.view.activity;

import android.os.Bundle;
import android.widget.Button;

import com.cashow.presentation.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_LoadData)
    Button btn_LoadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    /**
     * Goes to the user list screen.
     */
    @OnClick(R.id.btn_LoadData)
    void navigateToUserList() {
        this.navigator.navigateToUserList(this);
    }
}

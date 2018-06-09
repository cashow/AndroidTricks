package com.cashow.socketdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cashow.socketdemo.event.OnClientSendInfoEvent;
import com.cashow.socketdemo.event.OnServerGetInfoEvent;
import com.cashow.socketdemo.model.ClientInfo;
import com.cashow.socketdemo.util.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.text_client_info)
    TextView textClientInfo;
    @BindView(R.id.text_server_info)
    TextView textServerInfo;

    public static final String CONNECT_END = "end";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startServer();
    }

    private void startServer() {
        DemoServerService.startDemoService(this);
    }

    @OnClick(R.id.button_connect_server)
    void onConnectServerClick() {
        ClientInfo clientInfo = new ClientInfo(10001, "test info " + System.currentTimeMillis() % 1000);
        String content = new Gson().toJson(clientInfo);
        content += "\n" + CONNECT_END + "\n";
        new ConnectThread(Utils.getLocalIp(this), content).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientSendInfoEvent(OnClientSendInfoEvent event) {
        textClientInfo.append(event.getContent() + "\n");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerGetInfoEvent(OnServerGetInfoEvent event) {
        textServerInfo.append(event.getContent() + "\n");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}

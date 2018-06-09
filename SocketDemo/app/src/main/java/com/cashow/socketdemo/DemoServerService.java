package com.cashow.socketdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cashow.socketdemo.event.OnServerGetInfoEvent;
import com.cashow.socketdemo.model.ServerInfo;
import com.cashow.socketdemo.util.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DemoServerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startDemoServer();
        return START_STICKY;
    }

    public static void startDemoService(Context context) {
        Intent intent = new Intent(context, DemoServerService.class);
        context.startService(intent);
    }

    public static void stopDemoService(Context context) {
        Intent intent = new Intent(context, DemoServerService.class);
        context.stopService(intent);
    }

    private void startDemoServer() {
        new Thread() {
            public void run() {
                OutputStream output;
                try {
                    ServerSocket serverSocket = new ServerSocket(30000);

                    EventBus.getDefault().post(new OnServerGetInfoEvent("start server on " + Utils.getLocalIp(getApplicationContext()) + ":30000"));

                    while (true) {
                        try {
                            Socket socket = serverSocket.accept();

                            //获取输入信息
                            BufferedReader bff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            //读取信息
                            String result = "";
                            String buffer = "";
                            while ((buffer = bff.readLine()) != null) {
                                result = result + buffer;
                                if (buffer.contains(MainActivity.CONNECT_END)) {
                                    break;
                                }
                            }

                            //向client发送消息
                            output = socket.getOutputStream();
                            ServerInfo serverInfo = new ServerInfo(result);
                            String serverContent = new Gson().toJson(serverInfo);
                            output.write(serverContent.getBytes("utf-8"));
                            output.flush();
                            socket.shutdownOutput();

                            EventBus.getDefault().post(new OnServerGetInfoEvent(result));
                            bff.close();
                            output.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            ;
        }.start();
    }
}

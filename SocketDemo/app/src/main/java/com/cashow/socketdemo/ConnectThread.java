package com.cashow.socketdemo;

import com.cashow.socketdemo.event.OnClientSendInfoEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

class ConnectThread extends Thread {
    private String content;
    private String ip;

    public ConnectThread(String ip, String content) {
        this.ip = ip;
        this.content = content;
    }

    @Override
    public void run() {
        //定义消息
        try {
            //连接服务器 并设置连接超时为5秒
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, 30000), 1000);

            //获取输入输出流
            OutputStream ou = socket.getOutputStream();
            //获取输出输出流
            BufferedReader bff = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            //向服务器发送信息
            ou.write(content.getBytes("utf-8"));
            ou.flush();

            //读取发来服务器信息
            String result = "服务端返回值：";
            String buffer = "";
            while ((buffer = bff.readLine()) != null) {
                result = result + buffer;
            }
            //发送消息 修改UI线程中的组件
            EventBus.getDefault().post(new OnClientSendInfoEvent(result.toString()));
            //关闭各种输入输出流
            bff.close();
            ou.close();
            socket.close();
        } catch (SocketTimeoutException aa) {
            //发送消息 修改UI线程中的组件
            EventBus.getDefault().post(new OnClientSendInfoEvent("服务端返回值：服务器连接失败！请检查网络是否打开"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
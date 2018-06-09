package com.cashow.socketdemo.event;

public class OnClientSendInfoEvent {
    private String content;

    public OnClientSendInfoEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

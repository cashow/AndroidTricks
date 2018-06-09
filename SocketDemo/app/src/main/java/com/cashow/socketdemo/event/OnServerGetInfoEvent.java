package com.cashow.socketdemo.event;

public class OnServerGetInfoEvent {
    private String content;

    public OnServerGetInfoEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

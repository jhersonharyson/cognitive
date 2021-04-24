package com.example.demo.listener;

public class BaseListenerInterface {
    private String data;

    public BaseListenerInterface(String data) {
        this.data = data;
    }

    public BaseListenerInterface() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

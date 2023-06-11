package com.example.servletlearn.basic.bean;

import java.beans.JavaBean;

@JavaBean
public class School {
    private String name;
    private String address;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}

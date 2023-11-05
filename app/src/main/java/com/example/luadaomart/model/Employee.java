package com.example.luadaomart.model;

import java.io.Serializable;

public class Employee implements Serializable {
    private String id;
    private String password;
    private String name;

    public Employee() {
    }

    public Employee(String id, String name, String password) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

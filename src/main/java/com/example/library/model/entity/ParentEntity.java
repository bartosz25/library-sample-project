package com.example.library.model.entity;

import javax.persistence.Transient;

abstract public class ParentEntity {
    protected String token;
    protected String action;
    
    public void setToken(String token) {
        this.token = token;
    }

    @Transient
    public String getToken() {
        return token;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Transient
    public String getAction() {
        return action;
    }
}
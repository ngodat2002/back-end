package com.backendsem4.backend.dto;

public class BaseModel<T> {
    public int status = 1;
    public String message = "success";

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T data;

}


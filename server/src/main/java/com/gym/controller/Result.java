package com.gym.controller;

import lombok.Data;

@Data
public class Result {
    String code;
    Object data;
    String message;
    Result()
    {

    }
    public Result(String code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    Result(String code, Object data) {
        this.code = code;
        this.data = data;
    }

}

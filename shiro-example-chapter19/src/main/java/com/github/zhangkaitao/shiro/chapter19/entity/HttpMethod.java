package com.github.zhangkaitao.shiro.chapter19.entity;

/**
 * Created by xtao on 2015/11/13.
 */
public enum HttpMethod {
    GET(0,"GET"),POST(1,"POST"),PUT(2,"PUT"),DELETE(3,"DELETE");
    private int value;
    private String name;

    HttpMethod(int value, String name){
        this.value = value;
        this.name = name;
    }
    public int getValue() {
        return value;
    }

    public String getName(){
        return name;
    }

    public static HttpMethod getMethodByValue(int value){
        for(HttpMethod httpMethod: HttpMethod.values()){
            if(httpMethod.getValue() == value){
                return httpMethod;
            }
        }
        return null;
    }
}

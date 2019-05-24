package com.example.myapplication;

public class ScanResponseObj {

    private String  age;
    private String  gender;
    private String  code;
    private String  num;
    private String  code1;
    private String  error;
    private int  width;
    private int top;
    private int left;
    public void setWidth(int width) {
        this.width = width;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getHeight() {
        return height;
    }

    private int height;

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getnum() {
        return num;
    }

    public void setnum(String num) {
        this.num = num;
    }
    public String getgender() {
        return gender;
    }

    public void setgender(String gender) {
        this.gender = gender;
    }
    public String getage() {
        return age;
    }

    public void setage(String age) {
        this.age = age;
    }
    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }
}

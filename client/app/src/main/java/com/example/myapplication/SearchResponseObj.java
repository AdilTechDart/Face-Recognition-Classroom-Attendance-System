package com.example.myapplication;

public class SearchResponseObj {
    private String name;
    private String num;
    private String flag;
    private String  width;
    private String top;

    public void setWidth(String width) {
        this.width = width;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public String getTop() {
        return top;
    }

    public String getLeft() {
        return left;
    }

    public String getHeight() {
        return height;
    }

    private String left;
    private String height;

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setname(String name) {
        this.name = name;
    }

    public void setnum(String num) {
        this.num = num;
    }

    public String getname() {
        return name;
    }

    public String getnum() {
        return num;
    }
}

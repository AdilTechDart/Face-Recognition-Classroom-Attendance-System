package com.example.myapplication;

public class Student {
    private String name;
    private int image;
    private String state;
    private String state1;

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public String getState1() {
        return state1;
    }

    public Student(String name, int image, String state, String state1)
    {this.image=image;
        this.name=name;
    this.state=state;
    this.state1=state1;}

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public String getname(){
        return name;
    }
    public int getimage(){
        return image;
    }
}


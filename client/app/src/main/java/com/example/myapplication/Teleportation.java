package com.example.myapplication;

public class Teleportation {
    private String  course;

    public void setCourse(String course) {
        this.course = course;
    }

    public void setCoursenumber(String coursenumber) {
        this.coursenumber = coursenumber;
    }


    public String getCourse() {
        return course;
    }

    public String getCoursenumber() {
        return coursenumber;
    }



    private String  coursenumber;
    private String  studentPlist;

    public void setStudentPlist(String studentPlist) {
        this.studentPlist = studentPlist;
    }

    public void setStudentUPlist(String sudentUPlist) {
        this.studentUPlist = sudentUPlist;
    }

    public String getStudentPlist() {
        return studentPlist;
    }

    public String getStudentUPlist() {
        return studentUPlist;
    }

    private String  studentUPlist;
    public Teleportation(String course, String coursenumber,String studentPlist,String studentUPlist) {
       this.course=course;
       this.coursenumber=coursenumber;
       this.studentPlist=studentPlist;
       this.studentUPlist=studentUPlist;
    }
}

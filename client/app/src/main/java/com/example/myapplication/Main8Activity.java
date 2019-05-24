package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Main8Activity extends AppCompatActivity {
    private List<Student> studentList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        Intent intent = getIntent();
        String flag=intent.getStringExtra("FLAG");
        if(flag.equals("T")){
        String Pnamemsg = intent.getStringExtra("Pname");
        String UPnamemsg=intent.getStringExtra("UPname");
        String[] studentplistname = Pnamemsg.split(",");
        String[] studentuplistname=UPnamemsg.split(",");
        for (int i = 0 ; i <studentuplistname.length ; i++ ) {
            Student s1 = new Student(studentuplistname[i], R.drawable.delete,"缺勤","");
            studentList.add(s1);
        } for (int i = 0 ; i <studentplistname.length ; i++ ) {
            Student s2 = new Student(studentplistname[i], R.drawable.tag,"","已签到");
            studentList.add(s2);
        }}
        else if(flag.equals("F"))
            { String UPnamemsg1=intent.getStringExtra("UPname1");
              String[] studentuplistname1=UPnamemsg1.split(",");
              for(int i=0;i<((String[]) studentuplistname1).length;i++){
                  Student s3 = new Student(studentuplistname1[i], R.drawable.delete,"缺勤","");
                  studentList.add(s3);
              }

            }
        else if(flag.equals("A"))
        { String Pnamemsg1=intent.getStringExtra("Pname1");
            String[] studentplistname1=Pnamemsg1.split(",");
            for(int i=0;i<((String[]) studentplistname1).length;i++){
                Student s4 = new Student(studentplistname1[i], R.drawable.delete,"","已签到");
                studentList.add(s4);
            }

        }


        StudentAdapter adapter=new StudentAdapter(Main8Activity.this,R.layout.student,studentList);
        ListView listview =(ListView)findViewById(R.id.list_view);
        listview.setAdapter(adapter);


    }

}

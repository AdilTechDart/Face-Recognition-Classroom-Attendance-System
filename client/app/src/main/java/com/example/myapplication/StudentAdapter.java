package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    private int resourceId;
    public StudentAdapter(Context context, int textViewResourceId, List<Student>objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Student student =getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView studentimage =(ImageView)view.findViewById(R.id.studentstate_image);
        TextView studentname=(TextView)view.findViewById(R.id.student_name);
        TextView studentstate=(TextView)view.findViewById(R.id.student_state);
        TextView studentstate1=(TextView)view.findViewById(R.id.student_state1);
        studentstate.setText(student.getState());
        studentimage.setImageResource(student.getimage());
        studentname.setText(student.getname());
        studentstate1.setText(student.getState1());

        return view;
    }

}
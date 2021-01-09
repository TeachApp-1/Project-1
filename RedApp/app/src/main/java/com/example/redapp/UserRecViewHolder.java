package com.example.redapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserRecViewHolder extends RecyclerView.ViewHolder
{
    public TextView userName,userEmail,userSchool,userClassroom;
    public Button userSearchBtn;

    public UserRecViewHolder(@NonNull View itemView)
    {
        super(itemView);

        userName = itemView.findViewById(R.id.userNameTX);
        userEmail = itemView.findViewById(R.id.userEmailTX);
        userSchool = itemView.findViewById(R.id.userSchoolTX);
        userClassroom = itemView.findViewById(R.id.userClassroomTX);
    }
}

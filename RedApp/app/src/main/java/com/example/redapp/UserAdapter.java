package com.example.redapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserRecViewHolder>
{
    UserSearchActivity userSearchActivity;
    ArrayList<UserModel> userModelArrayList;

    @NonNull
    @Override
    public UserRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(userSearchActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.row_item,parent,false);
        return new UserRecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecViewHolder holder, int position)
    {
        holder.userName.setText(userModelArrayList.get(position).getName());
        holder.userEmail.setText(userModelArrayList.get(position).getEmail());
        holder.userSchool.setText(userModelArrayList.get(position).getSchool());
        holder.userClassroom.setText(userModelArrayList.get(position).getClassroom());
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    public UserSearchActivity getUserSearchActivity() {
        return userSearchActivity;
    }

    public void setUserSearchActivity(UserSearchActivity userSearchActivity) {
        this.userSearchActivity = userSearchActivity;
    }

    public ArrayList<UserModel> getUserModelArrayList() {
        return userModelArrayList;
    }

    public void setUserModelArrayList(ArrayList<UserModel> userModelArrayList) {
        this.userModelArrayList = userModelArrayList;
    }

    public UserAdapter(UserSearchActivity userSearchActivity, ArrayList<UserModel> userModelArrayList) {
        this.userSearchActivity = userSearchActivity;
        this.userModelArrayList = userModelArrayList;
    }

}

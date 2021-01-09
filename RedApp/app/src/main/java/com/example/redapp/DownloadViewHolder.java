package com.example.redapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DownloadViewHolder extends RecyclerView.ViewHolder {

    static TextView name;
    TextView link;
    public Button download;

    public DownloadViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.download_name);
        link = itemView.findViewById(R.id.download_link);
        download = itemView.findViewById(R.id.download_button);
    }
}

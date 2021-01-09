package com.example.redapp;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadViewHolder> {
    DownloadMaterial downloadMaterial;
    ArrayList<DownModel> downModels;

    public DownloadAdapter(DownloadMaterial downloadMaterial, ArrayList<DownModel> downModels) {
        this.downloadMaterial = downloadMaterial;
        this.downModels = downModels;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(downloadMaterial.getBaseContext());
        View view = layoutInflater.inflate(R.layout.download_elements, null, false);

        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        holder.name.setText(downModels.get(position).getName());
        holder.link.setText(downModels.get(position).getLink());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(DownloadViewHolder.name.getContext(), downModels.get(position).getName(), ".pdf" , DIRECTORY_DOWNLOADS, downModels.get(position).getLink());
            }
        });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return downModels.size();
    }
}

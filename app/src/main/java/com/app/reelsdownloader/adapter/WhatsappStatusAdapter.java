package com.app.reelsdownloader.adapter;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.model.WhatsappStatusModel;
import com.app.reelsdownloader.util.Utils;
import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WhatsappStatusAdapter extends RecyclerView.Adapter<WhatsappStatusAdapter.ViewHolder> {
    public String SaveFilePath = (Utils.RootDirectoryWhatsappShow + "/");

    public Context context;
    private ArrayList<WhatsappStatusModel> fileArrayList;
    private LayoutInflater layoutInflater;

    public WhatsappStatusAdapter(Context context, ArrayList<WhatsappStatusModel> fileArrayList) {
        this.context = context;
        this.fileArrayList = fileArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = layoutInflater.inflate(R.layout.items_whatsapp_view, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final WhatsappStatusModel whatsappStatusModel = fileArrayList.get(i);
        if (whatsappStatusModel.getUri().toString().endsWith(".mp4")) {
            viewHolder.ivPlay.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivPlay.setVisibility(View.GONE);
        }
        Glide.with(context).load(whatsappStatusModel.getPath()).into(viewHolder.pcw);
        viewHolder.tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.createFileFolder(context);
                String path = whatsappStatusModel.getPath();
                String substring = path.substring(path.lastIndexOf("/") + 1);
                try {
                    FileUtils.copyFileToDirectory(new File(path), new File(SaveFilePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String substring2 = substring.substring(12);
                MediaScannerConnection.scanFile(context, new String[]{new File(SaveFilePath + substring2).getAbsolutePath()}, new String[]{whatsappStatusModel.getUri().toString().endsWith(".mp4") ? "video/*" : "image/*"}, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                    }
                    @Override
                    public void onScanCompleted(String str, Uri uri) {
                    }
                });
                new File(SaveFilePath, substring).renameTo(new File(SaveFilePath, substring2));
                Toast.makeText(context, context.getResources().getString(R.string.saved_to) + SaveFilePath + substring2, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        ArrayList<WhatsappStatusModel> arrayList = fileArrayList;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout RLM;
        public ImageView ivPlay;
        public ImageView pcw;
        public RelativeLayout rlMain;
        public TextView tvDownload;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            RLM = itemView.findViewById(R.id.RLM);
            ivPlay = itemView.findViewById(R.id.iv_play);
            pcw = itemView.findViewById(R.id.pcw);
            rlMain = itemView.findViewById(R.id.rl_main);
            tvDownload = itemView.findViewById(R.id.tv_download);
        }
    }
}

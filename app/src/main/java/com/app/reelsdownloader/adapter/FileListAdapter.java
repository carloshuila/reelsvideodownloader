package com.app.reelsdownloader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.interfaces.FileListClickInterface;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<File> fileArrayList;

    private FileListClickInterface fileListClickInterface;
    private LayoutInflater layoutInflater;

    public FileListAdapter(Context context, ArrayList<File> fileArrayList, FileListClickInterface fileListClickInterface) {
        this.context = context;
        this.fileArrayList = fileArrayList;
        this.fileListClickInterface = fileListClickInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = layoutInflater.inflate(R.layout.items_file_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final File file = fileArrayList.get(i);
        try {
            if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".mp4")) {
                viewHolder.ivPlay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivPlay.setVisibility(View.GONE);
            }
            Glide.with(context).load(file.getPath()).into(viewHolder.pc);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileListClickInterface.getPosition(i, file);
            }
        });
    }

    @Override
    public int getItemCount() {
        ArrayList<File> arrayList = fileArrayList;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlay;
        private ImageView pc;
        private RelativeLayout rlMain;
        private TextView tvFileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlay = itemView.findViewById(R.id.iv_play);
            pc = itemView.findViewById(R.id.pc);
            rlMain = itemView.findViewById(R.id.rl_main);
            tvFileName = itemView.findViewById(R.id.tv_fileName);
        }

    }
}

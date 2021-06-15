package com.app.reelsdownloader.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.adapter.WhatsappStatusAdapter;
import com.app.reelsdownloader.model.WhatsappStatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class WhatsappVideoFragment extends Fragment {
    private File[] allfiles;
    private RecyclerView rvFileList;
    private SwipeRefreshLayout swiperefresh;
    private TextView tvNoResult;
    private ArrayList<WhatsappStatusModel> statusModelArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_whatsapp_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFileList = view.findViewById(R.id.rv_fileList);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        tvNoResult = view.findViewById(R.id.tv_NoResult);
        initViews();
    }

    private void initViews() {
        statusModelArrayList = new ArrayList<>();
        getData();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public final void onRefresh() {
                statusModelArrayList = new ArrayList<>();
                getData();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getData() {
        allfiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses").listFiles();
        File[] listFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses").listFiles();
        if (listFiles == null) listFiles = new File[0];
        try {
            Arrays.sort(allfiles, new Comparator<File>() {
                @Override
                public int compare(File obj, File obj2) {
                    File file = (File) obj;
                    File file2 = (File) obj2;
                    if (file.lastModified() > file2.lastModified()) {
                        return -1;
                    }
                    return file.lastModified() < file2.lastModified() ? 1 : 0;
                }
            });
            for (int i = 0; i < allfiles.length; i++) {
                File file = allfiles[i];
                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                    statusModelArrayList.add(new WhatsappStatusModel("WhatsStatus: " + (i + 1), Uri.fromFile(file), allfiles[i].getAbsolutePath(), file.getName()));
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Arrays.sort(listFiles, new Comparator<File>() {
            @Override
            public int compare(File obj, File obj2) {
                File file = (File) obj;
                File file2 = (File) obj2;
                if (file.lastModified() > file2.lastModified()) {
                    return -1;
                }
                return file.lastModified() < file2.lastModified() ? 1 : 0;
            }
        });
        for (int i2 = 0; i2 < listFiles.length; i2++) {
            File file2 = listFiles[i2];
            if (Uri.fromFile(file2).toString().endsWith(".mp4")) {
                statusModelArrayList.add(new WhatsappStatusModel("WhatsStatusB: " + (i2 + 1), Uri.fromFile(file2), listFiles[i2].getAbsolutePath(), file2.getName()));
            }
        }
        if (statusModelArrayList.size() != 0) {
            tvNoResult.setVisibility(View.GONE);
        } else {
            tvNoResult.setVisibility(View.VISIBLE);
        }
        rvFileList.setAdapter(new WhatsappStatusAdapter(getActivity(), statusModelArrayList));
    }

}

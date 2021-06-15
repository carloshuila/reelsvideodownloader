package com.app.reelsdownloader.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.app.reelsdownloader.activity.FullViewActivity;
import com.app.reelsdownloader.activity.GalleryActivity;
import com.app.reelsdownloader.adapter.FileListAdapter;
import com.app.reelsdownloader.interfaces.FileListClickInterface;
import com.app.reelsdownloader.util.Utils;

import java.io.File;
import java.util.ArrayList;

public class InstaDownloadedFragment extends Fragment implements FileListClickInterface {
    private GalleryActivity activity;
    public RecyclerView rvFileList;
    public SwipeRefreshLayout swiperefresh;
    public TextView tvNoResult;

    private ArrayList<File> fileArrayList;
    private FileListAdapter fileListAdapter;

    public static InstaDownloadedFragment newInstance(String str) {
        InstaDownloadedFragment instaDownloadedFragment = new InstaDownloadedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("m", str);
        instaDownloadedFragment.setArguments(bundle);
        return instaDownloadedFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (GalleryActivity) context;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            getArguments().getString("m");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (GalleryActivity) getActivity();
        getAllFiles();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFileList = view.findViewById(R.id.rv_fileList);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        tvNoResult = view.findViewById(R.id.tv_NoResult);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public final void onRefresh() {
                getAllFiles();
                swiperefresh.setRefreshing(false);
            }
        });
    }


    private void getAllFiles() {
        fileArrayList = new ArrayList<>();
        File[] listFiles = Utils.RootDirectoryInstaShow.listFiles();
        if (listFiles != null) {
            for (File add : listFiles) {
                fileArrayList.add(add);
            }
            fileListAdapter = new FileListAdapter(activity, fileArrayList, this);
            rvFileList.setAdapter(fileListAdapter);
        }
    }

    public void getPosition(int i, File file) {
        Intent intent = new Intent(activity, FullViewActivity.class);
        intent.putExtra("ImageDataFile", fileArrayList);
        intent.putExtra("Position", i);
        activity.startActivity(intent);
    }
}

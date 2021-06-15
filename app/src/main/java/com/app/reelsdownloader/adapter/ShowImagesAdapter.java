package com.app.reelsdownloader.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.activity.FullViewActivity;
import com.app.reelsdownloader.util.Utils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class ShowImagesAdapter extends PagerAdapter {

    private Context context;
    private FullViewActivity fullViewActivity;

    private ArrayList<File> imageList;
    private LayoutInflater inflater;

    @Override
    public int getItemPosition(Object obj) {
        return -2;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public ShowImagesAdapter(Context context, ArrayList<File> imageList, FullViewActivity fullViewActivity) {
        this.context = context;
        this.imageList = imageList;
        this.fullViewActivity = fullViewActivity;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, final int position) {
        View inflate = inflater.inflate(R.layout.slidingimages_layout, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.im_vpPlay);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.im_share);
        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.im_delete);
        Glide.with(context)
                .load(imageList.get(position).getPath())
                .into((ImageView) inflate.findViewById(R.id.im_fullViewImage));
        viewGroup.addView(inflate, 0);
        if (imageList.get(position).getName().substring(imageList.get(position).getName().lastIndexOf(".")).equals(".mp4")) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.parse(imageList.get(position).getPath()), "video/*");
                    context.startActivity(intent);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((File) imageList.get(position)).delete()) {
                    fullViewActivity.deleteFileAA(position);
                }
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((File) imageList.get(position)).getName().substring(((File) imageList.get(position)).getName().lastIndexOf(".")).equals(".mp4")) {
                    Utils.shareVideo(context, ((File) imageList.get(position)).getPath());
                } else {
                    Utils.shareImage(context, ((File) imageList.get(position)).getPath());
                }
            }
        });
        return inflate;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view.equals(obj);
    }
}

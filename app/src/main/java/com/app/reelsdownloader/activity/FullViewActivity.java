package com.app.reelsdownloader.activity;

import android.animation.Animator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.adapter.ShowImagesAdapter;
import com.app.reelsdownloader.util.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class FullViewActivity extends AppCompatActivity implements View.OnClickListener {

    private int Position = 0;

    private FullViewActivity activity;
    private ArrayList<File> fileArrayList;
    private ShowImagesAdapter showImagesAdapter;

    private ImageView imClose;
    private ViewPager vpView;

    private static boolean isFabOpen;
    private FloatingActionButton fab_whatsaap;
    private FloatingActionButton fab_share;
    private FloatingActionButton fab_delete;
    private FloatingActionButton fab_main;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_full_view);
        activity = this;
        if (getIntent().getExtras() != null) {
            fileArrayList = (ArrayList) getIntent().getSerializableExtra("ImageDataFile");
            Position = getIntent().getIntExtra("Position", 0);
        }

        vpView = findViewById(R.id.vp_view);
        imClose = findViewById(R.id.im_close);

        fab_main = findViewById(R.id.fab_main);
        fab_delete = findViewById(R.id.fab_delete);
        fab_share = findViewById(R.id.fab_share);
        fab_whatsaap = findViewById(R.id.fab_whatsaap);

        fab_main.setOnClickListener(this);
        initViews();
    }

    public void initViews() {
        showImagesAdapter = new ShowImagesAdapter(this, fileArrayList, this);
        vpView.setAdapter(showImagesAdapter);
        vpView.setCurrentItem(Position);
        vpView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                Position = i;
            }
        });

        imClose.setOnClickListener(this);

        fab_delete.setOnClickListener(this);
        fab_share.setOnClickListener(this);
        fab_whatsaap.setOnClickListener(this);
        fab_main.setOnClickListener(this);
    }

    private void ShowFabMenu() {
        isFabOpen = true;
        fab_delete.setVisibility(View.VISIBLE);
        fab_share.setVisibility(View.VISIBLE);
        fab_whatsaap.setVisibility(View.VISIBLE);

        fab_main.setImageResource(R.drawable.ic_close);

        fab_main.animate().rotation(180f);
        fab_whatsaap.animate().translationY(-getResources().getDimension(R.dimen.standard_12))
                .rotation(0f);
        fab_share.animate().translationY(-getResources().getDimension(R.dimen.standard_12))
                .rotation(0f);
        fab_delete.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_12))
                .rotation(0f);
        fab_delete.setRippleColor(getResources().getColor(R.color.colorblackBackground));
        fab_share.setRippleColor(getResources().getColor(R.color.colorblackBackground));
        fab_whatsaap.setRippleColor(getResources().getColor(R.color.colorblackBackground));
    }

    private void CloseFabMenu() {
        isFabOpen = false;
        fab_main.setImageResource(R.drawable.ic_menu);
        fab_main.animate().rotation(0f);
        fab_whatsaap.animate()
                .translationY(0f)
                .rotation(90f);
        fab_share.animate()
                .translationY(0f)
                .rotation(90f);
        fab_delete.animate()
                .translationY(0f)
                .rotation(90f).setListener(new FabAnimatorListener(new FloatingActionButton[]{fab_whatsaap, fab_share, fab_delete}));
    }

    private class FabAnimatorListener extends Object implements Animator.AnimatorListener {
        View[] viewsToHide;

        public FabAnimatorListener(View[] viewsToHide) {
            this.viewsToHide = viewsToHide;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!isFabOpen)
                if (viewsToHide != null && viewsToHide.length > 0) {
                    for (View view : viewsToHide) {
                        view.setVisibility(View.GONE);
                    }
                }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main:
                if (!isFabOpen)
                    ShowFabMenu();
                else
                    CloseFabMenu();
                break;
            case R.id.fab_share:
                if (((File) fileArrayList.get(Position)).getName().contains(".mp4")) {
                    Utils.shareVideo(activity, ((File) fileArrayList.get(Position)).getPath());
                    return;
                }
                Utils.shareImage(activity, ((File) fileArrayList.get(Position)).getPath());

                break;
            case R.id.fab_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton((CharSequence) getResources().getString(R.string.yes), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (((File) fileArrayList.get(Position)).delete()) {
                            deleteFileAA(Position);
                        }
                    }
                });
                builder.setNegativeButton((CharSequence) getResources().getString(R.string.no), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog create = builder.create();
                create.setTitle(getResources().getString(R.string.do_u_want_to_dlt));
                create.show();
                break;
            case R.id.im_close:
                onBackPressed();
                break;
            case R.id.fab_whatsaap:
                if (((File) fileArrayList.get(Position)).getName().contains(".mp4")) {
                    Utils.shareImageVideoOnWhatsapp(activity, ((File) fileArrayList.get(Position)).getPath(), true);
                } else {
                    Utils.shareImageVideoOnWhatsapp(activity, ((File) fileArrayList.get(Position)).getPath(), false);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = this;
    }

    public void deleteFileAA(int i) {
        fileArrayList.remove(i);
        showImagesAdapter.notifyDataSetChanged();
        Utils.setToast(activity, getResources().getString(R.string.file_deleted));
        if (fileArrayList.size() == 0) {
            onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


}

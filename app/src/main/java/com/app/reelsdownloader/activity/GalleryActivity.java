package com.app.reelsdownloader.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.fragment.InstaDownloadedFragment;
import com.app.reelsdownloader.fragment.WhatsAppDowndlededFragment;
import com.app.reelsdownloader.util.AdsUtils;
import com.app.reelsdownloader.util.Utils;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    private GalleryActivity activity;
    private AdView adView;
    private ImageView imBack;
    private TabLayout tabs;
    private ViewPager viewpager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gallery);
        activity = this;
        adView = findViewById(R.id.adView);
        imBack = findViewById(R.id.imBack);
        tabs = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.viewpager);

        AdsUtils.showGoogleBannerAd(this, adView);
        initViews();
    }

    public void initViews() {
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_instagram_tab);
        tabs.getTabAt(1).setIcon(R.drawable.ic_whatsapp_tab);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            }
        });
        Utils.createFileFolder(this);

        imBack.setOnClickListener(this);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), 1);
        viewPagerAdapter.addFragment(new InstaDownloadedFragment(), "Instagram");
        viewPagerAdapter.addFragment(new WhatsAppDowndlededFragment(), "WhatsApp");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imBack:
                onBackPressed();
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        ViewPagerAdapter(FragmentManager fragmentManager, int i) {
            super(fragmentManager, i);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(str);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return mFragmentTitleList.get(i);
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}

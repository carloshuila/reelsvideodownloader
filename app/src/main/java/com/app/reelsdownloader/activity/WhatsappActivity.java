package com.app.reelsdownloader.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.fragment.WhatsappImageFragment;
import com.app.reelsdownloader.fragment.WhatsappVideoFragment;
import com.app.reelsdownloader.util.AdsUtils;
import com.app.reelsdownloader.util.Utils;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class WhatsappActivity extends AppCompatActivity implements View.OnClickListener {
    private WhatsappActivity activity;
    private ImageView LLOpenWhatsapp;
    private RelativeLayout RLTab;
    private RelativeLayout RLTopLayout;
    private AdView adView;
    private ImageView imBack;
    private ImageView imInfo;
    private RelativeLayout rr;
    private TabLayout tabs;
    private ViewPager viewpager;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_whatsapp);
        this.activity = this;
        Utils.createFileFolder(this);

        LLOpenWhatsapp = findViewById(R.id.LLOpenWhatsapp);
        RLTab = findViewById(R.id.RLTab);
        RLTopLayout = findViewById(R.id.RLTopLayout);
        adView = findViewById(R.id.adView);
        imBack = findViewById(R.id.imBack);
        imInfo = findViewById(R.id.imInfo);
        rr = findViewById(R.id.rr);
        tabs = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.viewpager);

        initViews();
        AdsUtils.showGoogleBannerAd(activity, adView);
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = this;

        if (adView != null) {
            adView.resume();
        }
    }

    private void initViews() {
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);

        tabs.getTabAt(0).setIcon(R.drawable.ic_images);
        tabs.getTabAt(1).setIcon(R.drawable.ic_videos_tab);

        imBack.setOnClickListener(this);
        LLOpenWhatsapp.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), 1);
        viewPagerAdapter.addFragment(new WhatsappImageFragment(), getResources().getString(R.string.images));
        viewPagerAdapter.addFragment(new WhatsappVideoFragment(), getResources().getString(R.string.videos));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imBack:
                onBackPressed();
                break;
            case R.id.LLOpenWhatsapp:
                Utils.OpenApp(activity, "com.whatsapp");
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
}

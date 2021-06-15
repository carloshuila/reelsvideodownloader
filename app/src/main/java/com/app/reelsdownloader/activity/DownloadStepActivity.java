package com.app.reelsdownloader.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.util.AdsUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DownloadStepActivity extends AppCompatActivity {
    private DownloadStepActivity activity;
    private Context context;
    private ImageView imBack;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_step_download);
        context = DownloadStepActivity.this;
        activity = this;
        ((AdView) findViewById(R.id.adViewhow)).loadAd(new AdRequest.Builder().build());
        AdsUtils.showGoogleInterstitialAd(context,activity);
        imBack=findViewById(R.id.imBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

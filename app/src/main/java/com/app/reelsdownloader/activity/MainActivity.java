package com.app.reelsdownloader.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.app.reelsdownloader.R;
import com.app.reelsdownloader.util.AdsUtils;
import com.app.reelsdownloader.util.ClipboardListener;
import com.app.reelsdownloader.util.Utils;

import java.util.List;
import java.util.Objects;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String CopyKey = "";
    String CopyValue = "";
    MainActivity activity;

    public ClipboardManager clipBoard;
    boolean doubleBackToExitPressedOnce = false;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};


    private ImageView gallery;
    private ImageView imRateApp;
    private ImageView imShareApp;
    private ImageView instaLogo;
    private LinearLayout main;

    private CardView cardGallery;
    private RelativeLayout rvInsta;
    private CardView cardRateApp;
    private CardView cardShareApp;
    private RelativeLayout rvWhatsApp;
    private ImageView whatsappLogo;

    private LinearLayout adChoicesContainer;
    private LinearLayout adUnit;
    private RelativeLayout background;
    private TextView cta;
    private LinearLayout ctaParent;
    private RoundedImageView icon;
    private MediaView mediaView;
    private NativeAdView nativeAdView;
    private TextView primary;
    private TextView secondary;
    private TextView tertiary;
    private LinearLayout thirdLine;
    private RelativeLayout topParent;
    private AdView adView;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.activity = this;
        adView = findViewById(R.id.adView);
        gallery = findViewById(R.id.gallery);
        imRateApp = findViewById(R.id.im_rateApp);
        imShareApp = findViewById(R.id.im_shareApp);
        instaLogo = findViewById(R.id.insta_logo);
        main = findViewById(R.id.main);

        cardGallery = findViewById(R.id.cardGallery);
        rvInsta = findViewById(R.id.rvInsta);
        cardRateApp = findViewById(R.id.cardRateApp);
        cardShareApp = findViewById(R.id.cardShareApp);
        rvWhatsApp = findViewById(R.id.rvWhatsApp);
        whatsappLogo = findViewById(R.id.whatsapp_logo);
        initAdView();
        initViews();

        if (AdmobAds.SHOW_ADS) {
            AdmobAds.loadNativeAds(this, (View) null);
        } else {
            findViewById(R.id.adsContainer).setVisibility(View.GONE);
        }

        AdsUtils.showGoogleBannerAd(this, adView);
    }

    private void initAdView() {
        adChoicesContainer = findViewById(R.id.ad_choices_container);
        adUnit = findViewById(R.id.ad_unit);
        background = findViewById(R.id.background);
        cta = findViewById(R.id.cta);
        ctaParent = findViewById(R.id.cta_parent);
        icon = findViewById(R.id.icon);
        mediaView = findViewById(R.id.media_view);
        nativeAdView = findViewById(R.id.native_ad_view);
        primary = findViewById(R.id.primary);
        secondary = findViewById(R.id.secondary);
        tertiary = findViewById(R.id.tertiary);
        thirdLine = findViewById(R.id.third_line);
        topParent = findViewById(R.id.top_parent);
    }


    public void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (activity.getIntent().getExtras() != null) {
            for (String str : activity.getIntent().getExtras().keySet()) {
                CopyKey = str;
                String string = activity.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity.getIntent().getExtras().getString(CopyKey);
                } else {
                    CopyValue = "";
                }
                callText(string);
            }
        }
        ClipboardManager clipboardManager = clipBoard;
        if (clipboardManager != null) {
            clipboardManager.addPrimaryClipChangedListener(new ClipboardListener() {
                public void onPrimaryClipChanged() {
                    try {
                        CharSequence text = clipBoard.getPrimaryClip().getItemAt(0).getText();
                        Objects.requireNonNull(text);
                        showNotification(text.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }
        rvInsta.setOnClickListener(this);
        rvWhatsApp.setOnClickListener(this);
        cardGallery.setOnClickListener(this);
        cardShareApp.setOnClickListener(this);
        cardRateApp.setOnClickListener(this);
        Utils.createFileFolder(this);
    }

    private void callText(String str) {
        try {
            if (!str.contains("instagram.com")) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(101);
            } else {
                callInstaActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardGallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                break;
            case R.id.rvInsta:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
                break;
            case R.id.cardRateApp:
                Utils.RateApp(activity);
                break;
            case R.id.cardShareApp:
                Utils.ShareApp(activity);
                break;
            case R.id.rvWhatsApp:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(102);
                } else {
                    callWhatsappActivity();
                }
                break;
            default:
                break;
        }
    }

    public void callInstaActivity() {
        Intent intent = new Intent(activity, InstagramActivity.class);
        intent.putExtra("CopyIntent", CopyValue);
        startActivity(intent);
    }

    public void callWhatsappActivity() {
        startActivity(new Intent(activity, WhatsappActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            XXPermissions.with(MainActivity.this)
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .request(new OnPermissionCallback() {

                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (all) {
                            }
                        }
                    });
//            Toast.makeText(MainActivity.this,"It is detected that you have just returned from the permission setting interface",Toast.LENGTH_LONG).show();
        }

    }

    public void callGalleryActivity() {
        startActivity(new Intent(activity, GalleryActivity.class));
    }

    public void showNotification(String str) {
        if (str.contains("instagram.com")) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", str);
            PendingIntent activity2 = PendingIntent.getActivity(activity, 0, intent, FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(getResources().getString(R.string.app_name), getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLockscreenVisibility(1);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(1, new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setColor(getResources()
                            .getColor(R.color.colorPrimary))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_stat_name))
                    .setDefaults(-1)
                    .setPriority(1)
                    .setContentTitle("Copied text")
                    .setContentText(str)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(activity2, true)
                    .build());
        }
    }

    private void checkPermissions(int i) {
        XXPermissions.with(MainActivity.this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            if (i == 101) {
                                callInstaActivity();
                            } else if (i == 102) {
                                callWhatsappActivity();
                            } else if (i == 105) {
                                callGalleryActivity();
                            }
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        doubleBackToExitPressedOnce = true;
        Utils.setToast(activity, getResources().getString(R.string.pls_bck_again));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }

        activity = this;
        clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }
}

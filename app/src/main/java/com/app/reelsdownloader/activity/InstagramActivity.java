package com.app.reelsdownloader.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.app.reelsdownloader.R;
import com.app.reelsdownloader.api.CommonClassForAPI;
import com.app.reelsdownloader.model.Edge;
import com.app.reelsdownloader.model.EdgeSidecarToChildren;
import com.app.reelsdownloader.model.ResponseModel;
import com.app.reelsdownloader.util.AdsUtils;
import com.app.reelsdownloader.util.SharePrefs;
import com.app.reelsdownloader.util.Utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class InstagramActivity extends AppCompatActivity implements View.OnClickListener {
    private String PhotoUrl;
    private String VideoUrl;
    private InstagramActivity activity;
    private ClipboardManager clipBoard;
    private CommonClassForAPI commonClassForAPI;
    private Context context;
    private ImageView imgInfo;

    private DisposableObserver<JsonObject> instaObserver = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject jsonObject) {
            Utils.hideProgressDialog(activity);
            try {
//                Log.e("onNext: ", jsonObject.toString());
                ResponseModel responseModel = (ResponseModel) new Gson().fromJson(jsonObject.toString(), new TypeToken<ResponseModel>() {
                }.getType());
                EdgeSidecarToChildren edge_sidecar_to_children = responseModel.getGraphql().getShortcode_media().getEdge_sidecar_to_children();
                if (edge_sidecar_to_children != null) {
                    List<Edge> edges = edge_sidecar_to_children.getEdges();
                    for (int i = 0; i < edges.size(); i++) {
                        if (edges.get(i).getNode().isIs_video()) {
                            VideoUrl = edges.get(i).getNode().getVideo_url();
                            String str = Utils.RootDirectoryInsta;
                            Utils.startDownload(VideoUrl, str, activity, getVideoFilenameFromURL(VideoUrl));
                            etText.setText("");
                            VideoUrl = "";
                        } else {
                            PhotoUrl = edges.get(i).getNode().getDisplay_resources().get(edges.get(i).getNode().getDisplay_resources().size() - 1).getSrc();
                            String str2 = Utils.RootDirectoryInsta;
                            Utils.startDownload(PhotoUrl, str2, activity, getImageFilenameFromURL(PhotoUrl));
                            PhotoUrl = "";
                            etText.setText("");
                        }
                    }
                } else if (responseModel.getGraphql().getShortcode_media().isIs_video()) {
                    VideoUrl = responseModel.getGraphql().getShortcode_media().getVideo_url();
                    String str3 = Utils.RootDirectoryInsta;
                    Utils.startDownload(VideoUrl, str3, activity, getVideoFilenameFromURL(VideoUrl));
                    VideoUrl = "";
                    etText.setText("");
                } else {
                    PhotoUrl = responseModel.getGraphql().getShortcode_media().getDisplay_resources().get(responseModel.getGraphql().getShortcode_media().getDisplay_resources().size() - 1).getSrc();
                    String str4 = Utils.RootDirectoryInsta;
                    Utils.startDownload(PhotoUrl, str4, activity, getImageFilenameFromURL(PhotoUrl));
                    PhotoUrl = "";
                    etText.setText("");
                }
                showInterstitial();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable th) {
            Utils.hideProgressDialog(activity);
            Toast.makeText(activity, "Please enable Download from Private Account", Toast.LENGTH_LONG).show();
            th.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };
    private InterstitialAd mInterstitialAd;

    private ImageView LLOpenInstagram;
    private LinearLayout RLDownloadLayout;

    private RelativeLayout RLLoginInstagram;

    private Switch switchLogin;
    private AdView adView;
    private TextInputEditText etText;
    private ImageView imBack;

    private LinearLayout lnrMain;
    private TextView loginBtn1;

    private TextView tvLogin;
    private TextView tvLoginInstagram;
    private TextView tvPaste;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_instagram);
        this.activity = this;
        this.context = InstagramActivity.this;
        this.commonClassForAPI = CommonClassForAPI.getInstance(this);
        Utils.createFileFolder(this);

        LLOpenInstagram = findViewById(R.id.LLOpenInstagram);
        RLDownloadLayout = findViewById(R.id.RLDownloadLayout);

        RLLoginInstagram = findViewById(R.id.RLLoginInstagram);

        switchLogin = findViewById(R.id.switchLogin);
        adView = findViewById(R.id.adView);
        etText = findViewById(R.id.et_text);
        imBack = findViewById(R.id.imBack);

        lnrMain = findViewById(R.id.lnr_main);
        loginBtn1 = findViewById(R.id.login_btn1);

        tvLogin = findViewById(R.id.tvLogin);
        tvLoginInstagram = findViewById(R.id.tvLoginInstagram);
        tvPaste = findViewById(R.id.tv_paste);

        imgInfo = (ImageView) findViewById(R.id.imgInfo);
        imgInfo.setOnClickListener(this);

        AdsUtils.showGoogleBannerAd(this, adView);
        InterstitialAdsINIT();
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgInfo:
                startActivity(new Intent(InstagramActivity.this, DownloadStepActivity.class));
                break;
            case R.id.imBack:
                onBackPressed();
                break;
            case R.id.tv_paste:
                PasteText();
                break;
            case R.id.LLOpenInstagram:
                Utils.OpenApp(activity, "com.instagram.android");
                break;
            case R.id.login_btn1:
                String obj = etText.getText().toString();
                if (obj.equals("")) {
                    Utils.setToast(activity, getResources().getString(R.string.enter_url));
                } else if (!Patterns.WEB_URL.matcher(obj).matches()) {
                    Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
                } else {
                    GetInstagramData();
                    showInterstitial();
                }
                break;
            case R.id.tvLogin:
                startActivityForResult(new Intent(activity, LoginActivity.class), 100);
                break;
            case R.id.RLLoginInstagram:
                loginToInstagram();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        this.activity = this;
        this.context = this;
        this.clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        PasteText();

        if (adView != null) {
            adView.resume();
        }
    }

    /**
     * Called when leaving the activity.
     */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    private void initViews() {
        this.clipBoard = (ClipboardManager) this.activity.getSystemService(Context.CLIPBOARD_SERVICE);
        new GridLayoutManager(getApplicationContext(), 1).setOrientation(RecyclerView.HORIZONTAL);
        if (SharePrefs.getInstance(this.activity).getBoolean(SharePrefs.ISINSTALOGIN).booleanValue()) {
            layoutCondition();
            switchLogin.setChecked(true);
        } else {
            switchLogin.setChecked(false);
        }

        new GridLayoutManager(getApplicationContext(), 3).setOrientation(RecyclerView.VERTICAL);

        imBack.setOnClickListener(this::onClick);
        loginBtn1.setOnClickListener(this::onClick);
        tvPaste.setOnClickListener(this::onClick);
        LLOpenInstagram.setOnClickListener(this::onClick);
        tvLogin.setOnClickListener(this::onClick);
        RLLoginInstagram.setOnClickListener(this::onClick);


    }

    private void loginToInstagram() {
        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISINSTALOGIN).booleanValue()) {
            startActivityForResult(new Intent(activity, LoginActivity.class), 100);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISINSTALOGIN, false);
                SharePrefs.getInstance(activity).putString(SharePrefs.COOKIES, "");
                SharePrefs.getInstance(activity).putString(SharePrefs.CSRF, "");
                SharePrefs.getInstance(activity).putString(SharePrefs.SESSIONID, "");
                SharePrefs.getInstance(activity).putString(SharePrefs.USERID, "");
                if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISINSTALOGIN).booleanValue()) {
                    switchLogin.setChecked(true);
                } else {
                    switchLogin.setChecked(false);
                    tvLogin.setVisibility(View.VISIBLE);
                }
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog create = builder.create();
        create.setTitle(getResources().getString(R.string.do_u_want_to_download_media_from_pvt));
        create.show();

    }

    public void layoutCondition() {
        tvLogin.setVisibility(View.GONE);
    }

    private void GetInstagramData() {
        try {
            Utils.createFileFolder(this);
            String host = new URL(etText.getText().toString()).getHost();
            if (host.equals("www.instagram.com")) {
                callDownload(etText.getText().toString());
            } else {
                Utils.setToast(this.activity, getResources().getString(R.string.enter_valid_url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            etText.setText("");
            String stringExtra = getIntent().getStringExtra("CopyIntent");
            if (stringExtra.equals("")) {
                if (this.clipBoard.hasPrimaryClip()) {
                    if (this.clipBoard.getPrimaryClipDescription().hasMimeType("text/plain")) {
                        ClipData.Item itemAt = this.clipBoard.getPrimaryClip().getItemAt(0);
                        if (itemAt.getText().toString().contains("instagram.com")) {
                            etText.setText(itemAt.getText().toString());
                        }
                    } else if (this.clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("instagram.com")) {
                        etText.setText(this.clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }
                }
            } else if (stringExtra.contains("instagram.com")) {
                etText.setText(stringExtra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlWithoutParameters(String str) {
        try {
            URI uri = new URI(str);
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), (String) null, uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.setToast(this.activity, getResources().getString(R.string.enter_valid_url));
            return "";
        }
    }

    private void callDownload(String str) {
        String str2 = getUrlWithoutParameters(str) + "?__a=1";
        try {
            if (!new Utils(this.activity).isNetworkAvailable()) {
                Utils.setToast(this.activity, getResources().getString(R.string.no_internet_conn));
            } else if (this.commonClassForAPI != null) {
                Utils.showProgressDialog(this.activity);
                this.commonClassForAPI.callResult(this.instaObserver, str2, "ds_user_id=" + SharePrefs.getInstance(this.activity).getString(SharePrefs.USERID) + "; sessionid=" + SharePrefs.getInstance(this.activity).getString(SharePrefs.SESSIONID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageFilenameFromURL(String str) {
        try {
            return new File(new URL(str).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public String getVideoFilenameFromURL(String str) {
        try {
            return new File(new URL(str).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.instaObserver.dispose();

        if (adView != null) {
            adView.destroy();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        try {
            super.onActivityResult(i, i2, intent);
            if (i == 100 && i2 == -1) {
                intent.getStringExtra("key");
                if (SharePrefs.getInstance(this.activity).getBoolean(SharePrefs.ISINSTALOGIN).booleanValue()) {
                    switchLogin.setChecked(true);
                    layoutCondition();
                    return;
                }
                switchLogin.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InterstitialAdsINIT() {
        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        InterstitialAd.load(
                this,
                getResources().getString(R.string.admob_interstitial_ad),
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);

                    }
                });
    }


    public void showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }


}

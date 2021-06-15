package com.app.reelsdownloader.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.reelsdownloader.R;

import java.io.File;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.DOWNLOAD_SERVICE;

public class Utils {
    public static String RootDirectoryInsta = "/ReelDownload/Instagram/";
    public static File RootDirectoryInstaShow = new File(Environment.getExternalStorageDirectory() + "/Download/ReelDownload/Instagram");
    public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/Download/ReelDownload/Whatsapp");
    private static Context context;
    public static Dialog customDialog;

    public Utils(Context context2) {
        context = context2;
    }


    public static void setToast(Context context2, String str) {
        Toast makeText = Toast.makeText(context2, str, Toast.LENGTH_SHORT);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();
    }

    public static void createFileFolder(Context context) {
        if (!RootDirectoryInstaShow.exists()) {
            RootDirectoryInstaShow.mkdirs();
        }
        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }
    }

    public static void showProgressDialog(Activity activity) {
        Dialog dialog = customDialog;
        if (dialog != null) {
            dialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, (ViewGroup) null);
        customDialog.setCancelable(false);
        customDialog.setContentView(inflate);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog(Activity activity) {
        Dialog dialog = customDialog;
        if (dialog != null && dialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void startDownload(String str, String str2, Context context2, String str3) {
        setToast(context2, context2.getResources().getString(R.string.download_started));
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setAllowedNetworkTypes(3);
        request.setNotificationVisibility(1);
        request.setTitle(str3 + "");
        request.setVisibleInDownloadsUi(true);
        String str4 = Environment.DIRECTORY_DOWNLOADS;
        request.setDestinationInExternalPublicDir(str4, str2 + str3);
        ((DownloadManager) context2.getSystemService(DOWNLOAD_SERVICE)).enqueue(request);
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context2, new String[]{new File(Environment.DIRECTORY_DOWNLOADS + "/" + str2 + str3).getAbsolutePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String str, Uri uri) {
                    }
                });
                return;
            }
            context2.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(Environment.DIRECTORY_DOWNLOADS + "/" + str2 + str3))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareImage(Context context2, String str) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", context2.getResources().getString(R.string.share_txt));
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(MediaStore.Images.Media.insertImage(context2.getContentResolver(), str, "", (String) null)));
            intent.setType("image/*");
            context2.startActivity(Intent.createChooser(intent, context2.getResources().getString(R.string.share_image_via)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareImageVideoOnWhatsapp(Context context2, String str, boolean z) {
        Uri parse = Uri.parse(str);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setPackage("com.whatsapp");
        intent.putExtra("android.intent.extra.TEXT", "");
        intent.putExtra("android.intent.extra.STREAM", parse);
        if (z) {
            intent.setType("video/*");
        } else {
            intent.setType("image/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context2.startActivity(intent);
        } catch (Exception e) {
            setToast(context2, context2.getResources().getString(R.string.whatsapp_not_installed));
        }
    }

    public static void shareVideo(Context context2, String str) {
        Uri parse = Uri.parse(str);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("video/mp4");
        intent.putExtra("android.intent.extra.STREAM", parse);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context2.startActivity(Intent.createChooser(intent, "Share Video using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context2, context2.getResources().getString(R.string.no_app_installed), Toast.LENGTH_LONG).show();
        }
    }

    public static void RateApp(Context context2) {
        String packageName = context2.getPackageName();
        try {
            context2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            context2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public static void MoreApp(Context context2) {
        String packageName = context2.getPackageName();
        try {
            context2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            context2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public static void ShareApp(Context context2) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.SUBJECT", context2.getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", context2.getString(R.string.share_app_message) + ("\nhttps://play.google.com/store/apps/details?id=" + context2.getPackageName()));
        intent.setType("text/plain");
        context2.startActivity(Intent.createChooser(intent, "Share"));
    }

    public static void OpenApp(Context context2, String str) {
        Intent launchIntentForPackage = context2.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage != null) {
            context2.startActivity(launchIntentForPackage);
        } else {
            setToast(context2, context2.getResources().getString(R.string.app_not_available));
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.equalsIgnoreCase("0");
    }
}

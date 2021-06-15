package com.app.reelsdownloader.util;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.hjq.permissions.IPermissionInterceptor;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.app.reelsdownloader.R;


import java.util.ArrayList;
import java.util.List;

public class PermissionInterceptor implements IPermissionInterceptor {

//    @Override
//    public void requestPermissions(FragmentActivity activity, OnPermissionCallback callback, List<String> permissions) {
//        // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
//        new AlertDialog.Builder(activity)
//                .setTitle(R.string.common_permission_hint)
//                .setMessage(R.string.common_permission_message)
//                .setPositiveButton(R.string.common_permission_granted, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        PermissionFragment.beginRequest(activity, new ArrayList<>(permissions), callback);
//                    }
//                })
//                .setNegativeButton(R.string.common_permission_denied, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//    }

    @Override
    public void grantedPermissions(FragmentActivity activity, OnPermissionCallback callback, List<String> permissions, boolean all) {
        // 回调授权失败的方法
        callback.onGranted(permissions, all);
    }

    @Override
    public void deniedPermissions(FragmentActivity activity, OnPermissionCallback callback, List<String> permissions, boolean never) {
        // 回调授权失败的方法
        callback.onDenied(permissions, never);
        if (never) {
            showPermissionDialog(activity, permissions);
            return;
        }
        Toast.makeText(activity,"Storage Permission Failed...!",Toast.LENGTH_LONG).show();

    }

    protected void showPermissionFailedDialog(FragmentActivity activity, List<String> permissions) {
        // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
        new AlertDialog.Builder(activity)
                .setTitle(R.string.common_permission_hint)
                .setCancelable(false)
                .setMessage(R.string.common_permission_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showPermissionDialog(activity, permissions);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
            }
        })
                .show();
    }

    /**
     * 显示授权对话框
     */
    protected void showPermissionDialog(FragmentActivity activity, List<String> permissions) {
        // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
        new AlertDialog.Builder(activity)
                .setTitle(R.string.common_permission_alert)
                .setCancelable(false)
                .setMessage(getPermissionHint(activity, permissions))
                .setPositiveButton(R.string.common_permission_goto, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XXPermissions.startPermissionActivity(activity, permissions);
                    }
                })
                .show();
    }

    /**
     * 根据权限获取提示
     */
    protected String getPermissionHint(Context context, List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return context.getString(R.string.common_permission_fail_2);
        }

        List<String> hints = new ArrayList<>();
        for (String permission : permissions) {
            switch (permission) {
                case Permission.READ_EXTERNAL_STORAGE:
                case Permission.WRITE_EXTERNAL_STORAGE:
                case Permission.MANAGE_EXTERNAL_STORAGE: {
                    String hint = context.getString(R.string.common_permission_storage);
                    if (!hints.contains(hint)) {
                        hints.add(hint);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        if (!hints.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String text : hints) {
                if (builder.length() == 0) {
                    builder.append(text);
                } else {
                    builder.append("、")
                            .append(text);
                }
            }
            builder.append(" ");
            return context.getString(R.string.common_permission_fail_3, builder.toString());
        }

        return context.getString(R.string.common_permission_fail_2);
    }
}

package com.xsir.pgyerappupdate.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:apply dynamic permissions utils class.
 */
public class PermissionUtils {

    private String[] mPermissions;
    private static PermissionUtils mInstance;
    private OnPermissionCallback mOnPermissionCallback;

    private PermissionUtils() {
    }

    public static PermissionUtils getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionUtils();
        }
        return mInstance;
    }

    /**
     * 请求申请权限
     *
     * @param context              上下文
     * @param permissions          权限数组
     * @param onPermissionCallback 授权结果的回调接口
     */
    public void request(Context context, String[] permissions, OnPermissionCallback onPermissionCallback) {
        // 如果 >= 6.0 才去申请动态权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mPermissions = permissions;
            this.mOnPermissionCallback = onPermissionCallback;
            if (!isGranted(context, permissions)) {
                PermissionActivity.actionStart(context);
            } else {
                this.mOnPermissionCallback.onGranted();
            }
        } else {
            onPermissionCallback.onGranted();
        }
    }

    /**
     * 判断多个权限是否已经全部授权
     *
     * @param context     上下文
     * @param permissions 权限数组
     * @return 授权结果
     */
    private static boolean isGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            boolean result = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    /**
     * 定义一个授权结果的接口回调
     */
    public interface OnPermissionCallback {
        void onGranted(); // 授权成功

        void onDenied(); // 权限被拒
    }

    public static class PermissionActivity extends Activity {

        private static final int REQUEST_CODE = 520;

        public static void actionStart(Context context) {
            Intent intent = new Intent(context, PermissionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActivityCompat.requestPermissions(this, mInstance.mPermissions, REQUEST_CODE);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == REQUEST_CODE && isGranted(this, mInstance.mPermissions)) {
                if (mInstance.mOnPermissionCallback != null) {
                    mInstance.mOnPermissionCallback.onGranted();
                }
            } else {
                if (mInstance.mOnPermissionCallback != null) {
                    mInstance.mOnPermissionCallback.onDenied();
                }
            }
            PermissionActivity.this.finish();
        }
    }

}

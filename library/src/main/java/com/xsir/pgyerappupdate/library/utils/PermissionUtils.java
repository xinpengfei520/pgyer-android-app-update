package com.xsir.pgyerappupdate.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:apply dynamic permissions utils class.
 */
public class PermissionUtils {

    private static PermissionUtils mInstance;
    private OnPermissionCallback mOnPermissionCallback;
    private List<String> mPermissions;

    private PermissionUtils() {
    }

    public static PermissionUtils getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionUtils();
        }
        return mInstance;
    }

    public void request(Context context, String[] permissions, OnPermissionCallback onPermissionCallback) {
        this.mOnPermissionCallback = onPermissionCallback;
        mPermissions = Arrays.asList(permissions);
        if (!isGranted(context, mPermissions)) {
            PermissionActivity.actionStart(context);
        } else {
            this.mOnPermissionCallback.onGranted();
        }
    }

    private static boolean isGranted(Context context, List<String> permissions) {
        for (String permission : permissions) {
            if (!isGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isGranted(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    public interface OnPermissionCallback {
        void onGranted();

        void onDenied();
    }

    public static class PermissionActivity extends Activity {

        private static final int REQUEST_CODE = 520;

        public static void actionStart(final Context context) {
            Intent intent = new Intent(context, PermissionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (isGranted(this, mInstance.mPermissions)) {
                if (mInstance.mOnPermissionCallback != null) {
                    mInstance.mOnPermissionCallback.onGranted();
                }
            } else {
                String[] permissions = (String[]) mInstance.mPermissions.toArray();
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            }
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
            finish();
        }
    }

}

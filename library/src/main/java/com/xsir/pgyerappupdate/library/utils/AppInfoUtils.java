package com.xsir.pgyerappupdate.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:获取 App 基本信息工具类
 */
public class AppInfoUtils {

    private static final String TAG = "AppInfoUtils";

    /**
     * 返回当前程序版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        String versionName = "";
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            XLogUtils.e(TAG, "VersionInfo" + "Exception" + e);
        }

        return versioncode;
    }

    /**
     * 获取本地软件版本号名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            XLogUtils.d(TAG, "当前 App 版本号:" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return localVersion;
    }

    /**
     * 获取包名
     *
     * @param context 上下文
     * @return 包名
     */
    public static String getPackageName(Context context) {
        if (context == null) {
            return "";
        }
        Context appContext = context.getApplicationContext();

        return appContext.getPackageName();
    }
}

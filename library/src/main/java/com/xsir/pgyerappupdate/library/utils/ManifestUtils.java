package com.xsir.pgyerappupdate.library.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by x-sir on 2017/12/19.
 * Function:AndroidManifest 工具类
 */
public class ManifestUtils {

    /**
     * 蒲公英 apiKey
     */
    public static final String PGYER_API_KEY = "PGYER_API_KEY";
    /**
     * 蒲公英 appKey
     */
    public static final String PGYER_APP_KEY = "PGYER_APP_KEY";


    /**
     * 根据 meta data 的 name 获取 value
     *
     * @param context 上下文
     * @param key     metadata 的 key
     * @return metadata 的 value
     */
    public static String getMetaDataValueByName(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }
}

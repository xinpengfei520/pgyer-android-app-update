package com.xsir.pgyerappupdate.library;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.WindowManager;

import com.xsir.pgyerappupdate.library.cons.Constants;
import com.xsir.pgyerappupdate.library.service.DownLoadService;
import com.xsir.pgyerappupdate.library.utils.AppInfoUtils;
import com.xsir.pgyerappupdate.library.utils.HttpClientUtils;
import com.xsir.pgyerappupdate.library.utils.ManifestUtils;
import com.xsir.pgyerappupdate.library.utils.PermissionUtils;
import com.xsir.pgyerappupdate.library.utils.ThreadUtils;
import com.xsir.pgyerappupdate.library.utils.ToastUtils;
import com.xsir.pgyerappupdate.library.utils.XLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:蒲公英 API 管理工具类
 */
public class PgyerApi {

    private static final String TAG = "PgyerApi";
    private static Context mContext;
    private static WeakReference<Activity> weakReference;

    /**
     * 检查更新 APP
     *
     * @param activity 当前 APP 对象
     */
    public static void checkUpdate(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null!");
        }

        mContext = activity.getApplicationContext();
        weakReference = new WeakReference<>(activity);

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionUtils.getInstance().request(mContext, permissions, new PermissionUtils.OnPermissionCallback() {
            @Override
            public void onGranted() {
                XLogUtils.i(TAG, "onGranted()");
                sendNetworkRequest();
            }

            @Override
            public void onDenied() {
                XLogUtils.e(TAG, "onDenied()");
                denyAlertDialog();
            }
        });
    }

    private static void denyAlertDialog() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLong(mContext, "温馨提示：您拒绝了此权限！这可能导致APP无法下载更新，建议您稍后可在系统设置中，手动开启权限。");
            }
        });
    }

    /**
     * 发送网络请求
     */
    private static void sendNetworkRequest() {
        String apiKey = ManifestUtils.getMetaDataValueByName(mContext, Constants.PGYER_API_KEY);
        String appKey = ManifestUtils.getMetaDataValueByName(mContext, Constants.PGYER_APP_KEY);

        if (TextUtils.isEmpty(apiKey) || TextUtils.isEmpty(appKey)) {
            throw new IllegalArgumentException("apiKey or appKey is empty string, please config in AndroidManifest.xml file.");
        }

        // 拼接请求的参数
        String postStringParam = String.format("?_api_key=%s&appKey=%s&buildVersion=%s&buildBuildVersion=%s", apiKey, appKey, "", "");
        HttpClientUtils.sendOkHttpRequest(Constants.PGYER_APP_CHECK_URL + postStringParam, new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                XLogUtils.i(TAG, "onSuccess():" + json);
                parseJson(json);
            }

            @Override
            public void onError(final String errorMsg) {
                XLogUtils.e(TAG, "onError():" + errorMsg);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort(mContext, errorMsg);
                    }
                });
            }
        });
    }

    /**
     * 解析 json
     *
     * @param json 请求成功返回的 json
     */
    private static void parseJson(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    String data = jsonObject.getString("data");
                    JSONObject dataObject = new JSONObject(data);
                    String downloadURL = dataObject.getString("downloadURL");
                    int buildVersionNo = dataObject.getInt("buildVersionNo");
                    String buildUpdateDescription = dataObject.getString("buildUpdateDescription");
                    if (buildVersionNo > AppInfoUtils.getVersionCode(mContext)) {
                        showUpdateAppDialog(downloadURL, buildUpdateDescription);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示更新的 Dialog
     *
     * @param downloadURL            最新 apk 下载的 URL
     * @param buildUpdateDescription 最新 apk 的更新描述
     */
    private static void showUpdateAppDialog(final String downloadURL, final String buildUpdateDescription) {
        final String defaultDescribe = "为了更好的用户体验，强烈推荐更新！";
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Activity activity = weakReference.get();
                if (activity != null && !activity.isFinishing()) {
                    try {
                        new AlertDialog.Builder(activity)
                                .setTitle("发现新版本")
                                .setCancelable(false)
                                .setMessage(TextUtils.isEmpty(buildUpdateDescription) ? defaultDescribe : buildUpdateDescription)
                                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DownLoadService.startAction(mContext, downloadURL, AppInfoUtils.getPackageName(mContext));
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    } catch (WindowManager.BadTokenException e) {
                        XLogUtils.e(TAG, e.toString());
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

package com.xsir.pgyerappupdate.library;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xsir.pgyerappupdate.library.service.DownLoadService;
import com.xsir.pgyerappupdate.library.utils.AppInfoUtils;
import com.xsir.pgyerappupdate.library.utils.HttpClientUtils;
import com.xsir.pgyerappupdate.library.utils.ManifestUtils;
import com.xsir.pgyerappupdate.library.utils.PermissionUtils;
import com.xsir.pgyerappupdate.library.utils.ThreadUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:蒲公英 API 管理工具类
 */
public class PgyerApi {

    private static final String TAG = "PgyerApi";
    private static final String PGYER_APP_CHECK_URL = "https://www.pgyer.com/apiv2/app/check";

    /**
     * 检查更新 APP
     *
     * @param activity 当前 APP 对象
     */
    public static void checkUpdate(final Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null!");
        }

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionUtils.getInstance().request(activity, permissions, new PermissionUtils.OnPermissionCallback() {
            @Override
            public void onGranted() {
                Log.i(TAG, "onGranted()");
                sendNetworkRequest(activity);
            }

            @Override
            public void onDenied() {
                Log.e(TAG, "onDenied()");
                new AlertDialog.Builder(activity)
                        .setTitle("温馨提示")
                        .setCancelable(false)
                        .setMessage("您拒绝了此权限！这可能导致你 APP 以后无法下载更新，严重影响用户体验，强烈建议您稍后可在系统设置中为此 APP 手动开启相关权限。")
                        .setNegativeButton("确定", null)
                        .show();
            }
        });
    }

    /**
     * 发送网络请求
     *
     * @param activity 当前 APP 对象
     */
    private static void sendNetworkRequest(final Activity activity) {
        String apiKey = ManifestUtils.getMetaDataValueByName(activity, ManifestUtils.PGYER_API_KEY);
        String appKey = ManifestUtils.getMetaDataValueByName(activity, ManifestUtils.PGYER_APP_KEY);

        if (TextUtils.isEmpty(apiKey) || TextUtils.isEmpty(appKey)) {
            throw new IllegalArgumentException("apiKey or appKey is empty string, please config in AndroidManifest.xml file.");
        }

        // 拼接请求的参数
        String postStringParam = String.format("_api_key=%s&appKey=%s&buildVersion=%s&buildBuildVersion=%s", apiKey, appKey, "", "");
        HttpClientUtils.post(PGYER_APP_CHECK_URL, postStringParam, new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                Log.i(TAG, "onSuccess():" + json);
                parseJson(activity, json);
            }

            @Override
            public void onError(final String errorMsg) {
                Log.e(TAG, "onError():" + errorMsg);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 解析 json
     *
     * @param activity 当前 APP 对象
     * @param json     请求成功返回的 json
     */
    private static void parseJson(Activity activity, String json) {
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
                    if (buildVersionNo > AppInfoUtils.getVersionCode(activity)) {
                        showUpdateAppDialog(activity, downloadURL, buildUpdateDescription);
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
     * @param activity               当前 APP 对象
     * @param downloadURL            最新 apk 下载的 URL
     * @param buildUpdateDescription 最新 apk 的更新描述
     */
    private static void showUpdateAppDialog(final Activity activity, final String downloadURL, final String buildUpdateDescription) {
        final String defaultDescribe = "为了更好的用户体验，强烈推荐更新！";
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle("发现新版本")
                        .setCancelable(false)
                        .setMessage(TextUtils.isEmpty(buildUpdateDescription) ? defaultDescribe : buildUpdateDescription)
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DownLoadService.startAction(activity, downloadURL, AppInfoUtils.getPackageName(activity));
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }
}

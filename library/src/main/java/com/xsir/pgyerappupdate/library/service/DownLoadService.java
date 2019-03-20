package com.xsir.pgyerappupdate.library.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.xsir.pgyerappupdate.library.cons.Constants;
import com.xsir.pgyerappupdate.library.provider.CustomFileProvider;
import com.xsir.pgyerappupdate.library.utils.ToastUtils;
import com.xsir.pgyerappupdate.library.utils.XLogUtils;

import java.io.File;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:更新下载 apk 的服务
 */
public class DownLoadService extends Service {

    private String url;
    private String apkName = "";
    private DownloadManager manager;
    private DownloadCompleteReceiver receiver;
    private static final String APK_NAME = "apkName";
    private static final String DOWNLOAD_URL = "downloadUrl";
    private static final String TAG = "DownLoadService";

    private void initDownManager() {
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        down.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        down.setMimeType(mimeString);
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        down.setVisibleInDownloadsUi(true);
        down.setDestinationInExternalPublicDir("/" + apkName + "/", apkName + ".apk");
        down.setTitle(apkName);
        manager.enqueue(down);
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.showShort(getApplicationContext(), "正在下载中...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra(DOWNLOAD_URL);
        apkName = intent.getStringExtra(APK_NAME);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + apkName + "/" + apkName + ".apk";
        File file = new File(path);
        if (file.exists()) {
            boolean deleteResult = deleteFileWithPath(path);
            XLogUtils.i(TAG, "deleteResult===" + deleteResult);
        }

        try {
            initDownManager();
        } catch (Exception e) {
            e.printStackTrace();

            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent0 = new Intent(Intent.ACTION_VIEW, uri);
                intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent0);
            } catch (Exception ex) {
                e.printStackTrace();
                ToastUtils.showShort(getApplicationContext(), "下载失败!");
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (manager.getUriForDownloadedFile(downId) != null) {
                    installAPK(context, getRealFilePath(context, manager.getUriForDownloadedFile(downId)));
                } else {
                    ToastUtils.showShort(getApplicationContext(), "下载失败!");
                }

                DownLoadService.this.stopSelf();
            }
        }

        private void installAPK(Context context, String path) {
            File file = new File(path);
            if (file.exists()) {
                openFile(file, context);
            } else {
                ToastUtils.showShort(getApplicationContext(), "下载失败!");
            }
        }
    }

    public String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;

        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(
                    uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);

            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }

                cursor.close();
            }
        }

        return data;
    }

    /**
     * 打开文件
     *
     * @param file    file
     * @param context 上下文
     */
    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = CustomFileProvider.getUriForFile(context, Constants.AUTHORITY, file);
            XLogUtils.i(TAG, "file uri path:" + uriForFile.getPath());

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));

        } else {
            intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        }

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(getApplicationContext(), "没有找到打开此类文件的程序!");
        }
    }

    public String getMIMEType(File file) {
        String fileName = file.getName();
        String lowerCase = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowerCase);
    }

    public static boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File f = new File(filePath);
        checker.checkDelete(filePath);
        if (f.isFile()) {
            return f.delete();
        }

        return false;
    }

    public static void startAction(Context context, String url, String apkName) {
        Intent intent = new Intent(context, DownLoadService.class);
        intent.putExtra(DOWNLOAD_URL, url);
        intent.putExtra(APK_NAME, apkName);
        context.startService(intent);
    }
}


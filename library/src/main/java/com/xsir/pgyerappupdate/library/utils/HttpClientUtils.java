package com.xsir.pgyerappupdate.library.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:network request utils class.
 */
public class HttpClientUtils {

    private static final String TAG = "HttpClientUtils";

    public static void get(final String requestUrl, final HttpClientUtils.OnRequestCallBack callBack) {
        new Thread() {
            public void run() {
                getRequest(requestUrl, callBack);
            }
        }.start();
    }

    public static void post(final String requestUrl, final String params, final HttpClientUtils.OnRequestCallBack callBack) {
        new Thread() {
            public void run() {
                postRequest(requestUrl, params, callBack);
            }
        }.start();
    }

    private static void getRequest(String requestUrl, HttpClientUtils.OnRequestCallBack callBack) {
        boolean isSuccess = false;
        String message;

        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设定请求的方法为"POST"，默认是GET
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            // User-Agent  IE9的标识
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            /*
             * 当我们要获取我们请求的http地址访问的数据时就是使用connection.getInputStream().read()方式时我们就需要setDoInput(true)，
             * 根据api文档我们可知doInput默认就是为true。我们可以不用手动设置了，如果不需要读取输入流的话那就setDoInput(false)。
             * 当我们要采用非get请求给一个http网络地址传参 就是使用connection.getOutputStream().write() 方法时我们就需要setDoOutput(true), 默认是false
             */
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            connection.setDoInput(true);
            // 设置是否向httpUrlConnection输出，如果是post请求，参数要放在http正文内，因此需要设为true, 默认是false;
            //connection.setDoOutput(true);//Android  4.0 GET时候 用这句会变成POST  报错java.io.FileNotFoundException
            connection.setUseCaches(false);
            connection.connect();//
            int contentLength = connection.getContentLength();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();//会隐式调用connect()
                baos = new ByteArrayOutputStream();
                int readLen;
                byte[] bytes = new byte[1024];
                while ((readLen = inputStream.read(bytes)) != -1) {
                    baos.write(bytes, 0, readLen);
                }
                String result = baos.toString();
                Log.i(TAG, " result:" + result);

                message = result;
                isSuccess = true;
            } else {
                message = "请求失败 code:" + connection.getResponseCode();
            }

        } catch (MalformedURLException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                message = e.getMessage();
                e.printStackTrace();
            }
        }
        if (isSuccess) {
            callBack.onSuccess(message);
        } else {
            callBack.onError(message);
        }
    }

    private static void postRequest(String requestUrl, String params, HttpClientUtils.OnRequestCallBack callBack) {
        boolean isSuccess = false;
        String message;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设定请求的方法为"POST"，默认是GET
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            // User-Agent  IE9的标识
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            /*
             * 当我们要获取我们请求的http地址访问的数据时就是使用connection.getInputStream().read()方式时我们就需要setDoInput(true)，
             * 根据api文档我们可知doInput默认就是为true。我们可以不用手动设置了，如果不需要读取输入流的话那就setDoInput(false)。
             * 当我们要采用非get请求给一个http网络地址传参 就是使用connection.getOutputStream().write() 方法时我们就需要setDoOutput(true), 默认是false
             */
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            connection.setDoInput(true);
            // 设置是否向httpUrlConnection输出，如果是post请求，参数要放在http正文内，因此需要设为true, 默认是false;
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // set  params three way  OutputStreamWriter
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8");
            // 发送请求params参数
            out.write(params);
            out.flush();
            connection.connect();

            int contentLength = connection.getContentLength();
            if (connection.getResponseCode() == 200) {
                // 会隐式调用connect()
                inputStream = connection.getInputStream();
                baos = new ByteArrayOutputStream();
                int readLen;
                byte[] bytes = new byte[1024];
                while ((readLen = inputStream.read(bytes)) != -1) {
                    baos.write(bytes, 0, readLen);
                }
                String backStr = baos.toString();
                Log.i(TAG, "backStr:" + backStr);

                message = backStr;
                isSuccess = true;
            } else {
                message = "请求失败 code:" + connection.getResponseCode();
            }

        } catch (MalformedURLException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                message = e.getMessage();
                e.printStackTrace();
            }
        }
        if (isSuccess) {
            callBack.onSuccess(message);
        } else {
            callBack.onError(message);
        }
    }

    public interface OnRequestCallBack {
        void onSuccess(String json);

        void onError(String errorMsg);
    }
}

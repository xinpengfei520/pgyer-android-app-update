package com.xsir.pgyerappupdate.library.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by x-sir on 2019/1/3 :)
 * Function:network request utils class.
 */
public class HttpClientUtils {

    private static final String TAG = "HttpClientUtils";

    public static void sendOkHttpRequest(String url, final HttpClientUtils.OnRequestCallBack callBack) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (callBack != null) {
                    callBack.onError(e.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        String respResult = response.body().string();
                        XLogUtils.i(TAG, "respResult===" + respResult);
                        callBack.onSuccess(respResult);
                    }
                }
            }
        });
    }

    public interface OnRequestCallBack {
        void onSuccess(String json);

        void onError(String errorMsg);
    }
}
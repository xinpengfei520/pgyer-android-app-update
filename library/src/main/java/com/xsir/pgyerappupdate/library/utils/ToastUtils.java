package com.xsir.pgyerappupdate.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by x-sir on 2018/7/22 :)
 * Function:Toast effect.
 */
public class ToastUtils {

    /**
     * show short toast.
     *
     * @param message show content.
     */
    public static void showShort(Context context, String message) {
        if (context == null) {
            return;
        }

        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * show long toast.
     *
     * @param message show content.
     */
    public static void showLong(Context context, String message) {
        if (context == null) {
            return;
        }

        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}

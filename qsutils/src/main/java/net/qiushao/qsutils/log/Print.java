package net.qiushao.qsutils.log;

import android.util.Log;

import java.util.Objects;

/**
 * Created by shaoqiu on 2015-8-12.
 */
public class Print {
    private static String TAG = "debug";

    public static void setTag(String tag) {
        TAG = tag;
    }

    public static String getTag() {
        return TAG;
    }

    public static void d(Objects msg) {
        Log.d(TAG, msg.toString());
    }
}

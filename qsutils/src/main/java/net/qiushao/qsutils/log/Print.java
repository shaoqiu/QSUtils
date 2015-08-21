package net.qiushao.qsutils.log;

import android.util.Log;

/**
 * Created by shaoqiu on 2015-8-12.
 */
public class Print {
    private static String TAG = "debug";
    private static boolean debug = true;
    private static final String COLOR_CYAN = "\033[0;36m";
    private static final String COLOR_GREEN = "\033[0;32m";
    private static final String COLOR_RED = "\033[0;31m";
    private static final String COLOR_BLUE = "\033[1;34m";
    private static final String COLOR_CLOSE = "\033[0m";

    public static boolean isEnable() {
        return debug;
    }

    public static void setEnable(boolean enable) {
        Print.debug = enable;
    }

    public static void setTag(String tag) {
        TAG = tag;
    }

    public static String getTag() {
        return TAG;
    }


    public static void d(Object msg) {
        if (debug) {
            final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            final int size = elements.length;
            StackTraceElement element = null;

            for (int i = 2; i < size; i++) {
                String file = elements[i].getFileName();
                String method = elements[i].getMethodName();
                if ("Print.java".equalsIgnoreCase(file) && "d".equalsIgnoreCase(method)) {
                    element = elements[i + 1];
                    break;
                }
            }
            if (null != element) {
                StringBuilder sb = new StringBuilder();
                sb.append(COLOR_CYAN);
                sb.append(" ").append(element.getFileName());
                sb.append(COLOR_GREEN);
                sb.append(" ").append(element.getMethodName()).append("()");
                sb.append(COLOR_RED);
                sb.append(" ").append(element.getLineNumber());
                sb.append(COLOR_BLUE);
                sb.append(" Msg[");
                sb.append(COLOR_CLOSE);
                sb.append(msg.toString());
                sb.append(COLOR_BLUE);
                sb.append("]");
                sb.append(COLOR_CLOSE);
                Log.d(TAG, sb.toString());
            }
        }
    }
}

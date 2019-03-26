package com.example.roman.appfortest.utils;

import android.renderscript.ScriptIntrinsicResize;
import android.util.Log;
import android.view.View;

import com.example.roman.appfortest.BuildConfig;

import io.reactivex.annotations.Nullable;

public class Utils {
    private Utils() {}

    public static Long imitateLoading (long howLongToWait) {
        try {
            Thread.sleep(howLongToWait);
        } catch (InterruptedException e) {
            if (isDebug()) Log.e("Utils", e.getMessage(), e);
        }
        return howLongToWait;
    }

    public static void setVisible(@Nullable View view, boolean isVisible) {
        int visibility = isVisible ? View.VISIBLE : View.GONE;
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public static boolean isDebug() {
            return BuildConfig.DEBUG;
        }



}

package com.test.audio;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class DirectoryUtils {

    private static final String LOG_TAG = "DirectoryUtils";

    public static void getEnvironmentDirectories() {
        Log.i(LOG_TAG, "getRootDirectory(): "
                + Environment.getRootDirectory().toString());
        Log.i(LOG_TAG, "getDataDirectory(): "
                + Environment.getDataDirectory().toString());
        Log.i(LOG_TAG, "getDownloadCacheDirectory(): "
                + Environment.getDownloadCacheDirectory().toString());
        Log.i(LOG_TAG, "getExternalStorageDirectory(): "
                + Environment.getExternalStorageDirectory().toString());
        Log.i(
                LOG_TAG,
                "getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES): "
                        + Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString());
    }

    public static void getApplicationDirectories(Context context) {

        Log.i(LOG_TAG, "context.getFilesDir(): "
                + context.getFilesDir().toString());
        Log.i(LOG_TAG, "context.getCacheDir(): "
                + context.getCacheDir().toString());
        Log.i(
                LOG_TAG,
                "context.getExternalFilesDir(Environment.DIRECTORY_MOVIES): "
                        + context
                        .getExternalFilesDir(Environment.DIRECTORY_MOVIES));
        Log.i(
                LOG_TAG,
                "context.getExternalCacheDir(): "
                        + context.getExternalCacheDir());
    }

}
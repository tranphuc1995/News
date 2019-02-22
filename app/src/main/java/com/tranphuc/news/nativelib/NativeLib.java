package com.tranphuc.news.nativelib;

public class NativeLib {


    public static native String getDomain();

    static {
        System.loadLibrary("native-lib");
    }
}

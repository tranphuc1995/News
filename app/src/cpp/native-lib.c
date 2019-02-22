//
// Created by LAP11022-local on 5/17/2018.
//

#include <string.h>
#include <jni.h>
#include <stdlib.h>

JNIEXPORT jstring JNICALL Java_com_tranphuc_news_nativelib_NativeLib_getDomain( JNIEnv* env, jobject thiz )
{
    return (*env)->NewStringUTF(env, "https://www.24h.com.vn/");
}

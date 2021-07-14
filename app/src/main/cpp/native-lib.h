#include <jni.h>
#include <string>
#include "picosha2.h"

#ifndef JNITEST_NATIVE_LIB_H
#define JNITEST_NATIVE_LIB_H

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mksmbrtsh_myhashapplication_MyFirebaseMessagingService_stringFromJNI(JNIEnv *env, jobject thiz, jstring str);
#endif //JNITEST_NATIVE_LIB_H
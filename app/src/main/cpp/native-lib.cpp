#include "native-lib.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mksmbrtsh_myhashapplication_MyFirebaseMessagingService_stringFromJNI(JNIEnv *env, jobject thiz, jstring str) {
    const char *convertedValue = env->GetStringUTFChars(str, JNI_FALSE);
    std::string src_str = std::string(convertedValue);
    std::vector<unsigned char> hash(picosha2::k_digest_size);
    picosha2::hash256(src_str.begin(), src_str.end(), hash.begin(), hash.end());
    env->ReleaseStringUTFChars(str, convertedValue);
    std::string hex_str = picosha2::bytes_to_hex_string(hash.begin(), hash.end());
    return env->NewStringUTF(hex_str.c_str());
}

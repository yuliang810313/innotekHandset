/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_hdhe_nfc_SerialPort */

#ifndef _Included_com_hdhe_nfc_SerialPort
#define _Included_com_hdhe_nfc_SerialPort
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_hdhe_nfc_SerialPort
 * Method:    open
 * Signature: (II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_com_hdhe_nfc_SerialPort_open
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     com_hdhe_nfc_SerialPort
 * Method:    close
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_hdhe_nfc_SerialPort_close
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_hdhe_nfc_SerialPort
 * Method:    poweOn
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_hdhe_nfc_SerialPort_poweOn
  (JNIEnv *, jobject);

/*
 * Class:     com_hdhe_nfc_SerialPort
 * Method:    powerOff
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_hdhe_nfc_SerialPort_powerOff
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif

//
// Created by Administrator on 2016/7/5.
//

#include <stdio.h>
#include <stdlib.h>
#include "com_hong_rise_JniDemoActivity.h"

int get_current_pressure(){
    //C工程师提供
    return rand()%300;
}


JNIEXPORT jint JNICALL Java_com_hong_rise_JniDemoActivity_getCurrentPressure
  (JNIEnv * env, jobject obj){
    return get_current_pressure();
  }
LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE:= pressure
LOCAL_SRC_FILES:= pressure.c
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)
LOCAL_LDLIBS += -llog
#把c语言调用的log函数对应的函数库加入到编译的运行时里面
#liblog.so

include $(BUILD_SHARED_LIBRARY)

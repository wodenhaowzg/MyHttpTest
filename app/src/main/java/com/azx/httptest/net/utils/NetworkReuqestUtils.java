package com.azx.httptest.net.utils;

import android.text.TextUtils;

import com.azx.httptest.net.bean.RequestBean;

public class NetworkReuqestUtils {

    public static boolean checkRequestIllegal(RequestBean bean) {
        if (TextUtils.isEmpty(bean.mUrl)) {
            return true;
        }
        return false;
    }
}

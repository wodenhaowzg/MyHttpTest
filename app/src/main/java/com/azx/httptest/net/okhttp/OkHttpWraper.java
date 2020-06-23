package com.azx.httptest.net.okhttp;

import android.util.Log;

import com.azx.httptest.net.NetworkConstants;
import com.azx.httptest.net.NetworkExecuter;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.utils.NetworkReuqestUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpWraper implements NetworkExecuter {
    private static final String TAG = "OkHttpWraper";
    private OkHttpClient mOkHttpClient;
    private OkHttpEventListener mOkHttpEventListener;

    public OkHttpWraper() {
        initClient();
    }

    private void initClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }

        if (mOkHttpEventListener == null) {
            mOkHttpEventListener = new OkHttpEventListener();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.callTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.dns(Dns.SYSTEM);
        builder.eventListener(mOkHttpEventListener);
    }

    private Request buildRequest(RequestBean requestBean) {
        if (NetworkReuqestUtils.checkRequestIllegal(requestBean)) {
            return null;
        }

        HttpUrl url = HttpUrl.parse(requestBean.mUrl);
        if (url == null) {
            return null;
        }

        Request request = null;
        if (requestBean.mNetworkRequestType == NetworkConstants.NetworkRequestType.GET) {
            request = new Request.Builder().url(url).get().build();
        } else if (requestBean.mNetworkRequestType == NetworkConstants.NetworkRequestType.POST) {
            RequestBody body = RequestBody.create("", MediaType.parse());
            request = new Request.Builder().url(url).post(body).build();
        }
        Log.d(TAG, "build request : " + request);
        return request;
    }

    @Override
    public boolean executeRequest(RequestBean bean) {
        Request request = buildRequest(bean);
        if (request == null) {
            return false;
        }

        if (mOkHttpClient != null) {
            mOkHttpClient.newCall(request);
        }
        return false;
    }
}

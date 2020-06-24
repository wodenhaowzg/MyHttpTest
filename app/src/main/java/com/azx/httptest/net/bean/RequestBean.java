package com.azx.httptest.net.bean;

import com.azx.httptest.net.NetworkConstants;

public class RequestBean {

    public NetworkConstants.NetworkRequestType mNetworkRequestType;
    public String mUrl;

    @Override
    public String toString() {
        return "RequestBean{" +
                "mNetworkRequestType=" + mNetworkRequestType +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}

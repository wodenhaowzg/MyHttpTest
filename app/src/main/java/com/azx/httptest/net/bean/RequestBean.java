package com.azx.httptest.net.bean;

import com.azx.httptest.net.NetworkConstants;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class RequestBean {

    public NetworkConstants.NetworkRequestType mNetworkRequestType;
    public String mUrl;
    public Subscriber<ResponseBean> mSubscriber;
    public Observer<ResponseBean> mObserver;

    @Override
    public String toString() {
        return "RequestBean{" +
                "mNetworkRequestType=" + mNetworkRequestType +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}

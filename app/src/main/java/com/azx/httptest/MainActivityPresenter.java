package com.azx.httptest;

import android.util.Log;

import com.azx.httptest.net.NetworkConstants;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.bean.ResponseBean;
import com.azx.httptest.net.model.NetworkRequestTaskModel;
import com.azx.httptest.net.okhttp.OkHttpWraper;
import com.azx.httptest.net.utils.MyLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class MainActivityPresenter implements MainContract.Presenter {

    private NetworkRequestTaskModel networkRequestTaskModel;
    private MainContract.View mMainView;

    public MainActivityPresenter(MainContract.View view) {
        mMainView = view;
        this.networkRequestTaskModel = new NetworkRequestTaskModel();
        OkHttpWraper wraper = new OkHttpWraper();
        networkRequestTaskModel.setNetworkExecuter(wraper);
    }


    public boolean requestAsyncNews() {
        RequestBean bean = new RequestBean();
        bean.mUrl = "https://api.apiopen.top/getWangYiNews";
        bean.mNetworkRequestType = NetworkConstants.NetworkRequestType.GET;
//        bean.mSubscriber = new Subscriber<ResponseBean>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                s.request(1);
//            }
//
//            @Override
//            public void onNext(ResponseBean responseBean) {
//                mMainView.showNews(responseBean.mResponseStr);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                mMainView.showNews(t.getLocalizedMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };

        bean.mObserver = new Observer<ResponseBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean responseBean) {
                mMainView.showNews(responseBean.mResponseStr);
            }

            @Override
            public void onError(Throwable e) {
                mMainView.showNews(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        return networkRequestTaskModel.requestAsync(bean);
    }
}

package com.azx.httptest.net.task;

import android.util.Log;

import com.azx.httptest.net.NetworkConstants;
import com.azx.httptest.net.NetworkExecuter;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.bean.ResponseBean;
import com.azx.httptest.net.utils.MyLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class NetworkRequestTask implements FlowableOnSubscribe<ResponseBean>, ObservableOnSubscribe<ResponseBean> {

    private static final String TAG = "NetworkRequestTask";
    private NetworkExecuter mNetworkExecuter;
    private RequestBean mRequestBean;

    public void executeFlowable(Flowable<ResponseBean> flowable, RequestBean bean, NetworkExecuter networkExecuter) {
        mRequestBean = bean;
        mNetworkExecuter = networkExecuter;
        MyLog.d(TAG, "start request network : " + mRequestBean.toString());
        flowable.subscribe(bean.mSubscriber);
    }

    public void executeObservable(Observable<ResponseBean> observable, RequestBean bean, NetworkExecuter networkExecuter) {
        mRequestBean = bean;
        mNetworkExecuter = networkExecuter;
        MyLog.d(TAG, "start request network : " + mRequestBean.toString());
        observable.subscribe(bean.mObserver);
    }

    @Override
    public void subscribe(FlowableEmitter<ResponseBean> emitter) throws Exception {
        Log.d(TAG, "executing request network : " + mRequestBean.toString());
        ResponseBean responseBean = runTask();
        if (responseBean == null || responseBean.mResponseResult != NetworkConstants.NetworkResponseResult.SUCCESS) {
            emitter.onError(new IOException("error response "));
        } else {
            emitter.onNext(responseBean);
        }
    }

    @Override
    public void subscribe(ObservableEmitter<ResponseBean> emitter) throws Exception {
        ResponseBean responseBean = runTask();
        if (responseBean == null || responseBean.mResponseResult != NetworkConstants.NetworkResponseResult.SUCCESS) {
            emitter.onError(new IOException("error response "));
        } else {
            emitter.onNext(responseBean);
        }
    }

    private ResponseBean runTask() {
        return mNetworkExecuter.executeRequest(mRequestBean);
    }
}

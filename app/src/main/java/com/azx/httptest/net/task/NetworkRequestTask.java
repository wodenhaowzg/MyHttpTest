package com.azx.httptest.net.task;

import android.util.Log;

import com.azx.httptest.net.NetworkExecuter;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.bean.ResponseBean;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class NetworkRequestTask {

    private static final String TAG = "NetworkRequestTask";
    private NetworkExecuter mNetworkExecuter;
    private RequestBean mRequestBean;

    public Flowable<ResponseBean> execute(RequestBean bean, NetworkExecuter networkExecuter) {
        mNetworkExecuter = networkExecuter;
        mRequestBean = bean;
        return Flowable.create(new FlowableOnSubscribe<ResponseBean>() {

            public void subscribe(FlowableEmitter<ResponseBean> emitter) {
                Log.d(TAG, "Start request network : " + mRequestBean.toString());
                ResponseBean responseBean = mNetworkExecuter.executeRequest(mRequestBean);
                emitter.onNext(responseBean);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }
}

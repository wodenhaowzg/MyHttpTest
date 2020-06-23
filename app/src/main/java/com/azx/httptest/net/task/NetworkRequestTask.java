package com.azx.httptest.net.task;

import com.azx.httptest.net.NetworkExecuter;
import com.azx.httptest.net.bean.RequestBean;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class NetworkRequestTask {

    private NetworkExecuter mNetworkExecuter;
    private RequestBean mRequestBean;

    public boolean execute(RequestBean bean, NetworkExecuter networkExecuter) {
        mNetworkExecuter = networkExecuter;
        mRequestBean = bean;
        Flowable<String> stringFlowable = Flowable.create(new FlowableOnSubscribe<String>() {

            public void subscribe(FlowableEmitter<String> emitter) {
                boolean request = mNetworkExecuter.executeRequest(mRequestBean);
                if (request) {

                }
                emitter.onNext();
            }
        }, BackpressureStrategy.BUFFER).;
        stringFlowable.subscribe();
        return true;
    }
}

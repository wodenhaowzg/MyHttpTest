package com.azx.httptest.net.model;

import com.azx.httptest.net.NetworkExecuter;
import com.azx.httptest.net.NetworkRequest;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.bean.ResponseBean;
import com.azx.httptest.net.task.NetworkRequestTask;
import com.azx.httptest.net.utils.MyLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetworkRequestTaskModel implements NetworkRequest {

    private static final String TAG = "NetworkRequestTaskModel";
    private NetworkExecuter mNetworkExecuter;
    private boolean enableBackpressure;

    public void setNetworkExecuter(NetworkExecuter executer) {
        mNetworkExecuter = executer;
    }

    @Override
    public boolean requestAsync(RequestBean bean) {
        NetworkExecuter networkExecuter = mNetworkExecuter;
        if (networkExecuter == null) {
            MyLog.e(TAG, "request async failed! NetworkExecuter is null");
            return false;
        }

        NetworkRequestTask networkRequestTask = new NetworkRequestTask();
        if (enableBackpressure) {
            Flowable<ResponseBean> requestBeanFlowable = Flowable.create(networkRequestTask, BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            networkRequestTask.executeFlowable(requestBeanFlowable, bean, networkExecuter);
        } else {
            Observable<ResponseBean> responseBeanObservable = Observable.create(networkRequestTask)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            networkRequestTask.executeObservable(responseBeanObservable, bean, networkExecuter);
        }
        return true;
    }
}

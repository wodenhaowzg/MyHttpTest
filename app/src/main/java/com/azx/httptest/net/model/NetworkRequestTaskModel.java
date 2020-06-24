package com.azx.httptest.net.model;

import com.azx.httptest.net.NetworkExecuter;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.bean.ResponseBean;
import com.azx.httptest.net.task.NetworkRequestTask;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetworkRequestTaskModel implements Subscriber<ResponseBean> {

    private NetworkRequestTask mNetworkRequestTask;
    private NetworkExecuter mNetworkExecuter;
    private List<RequestBean> mRequestBeans;

    public NetworkRequestTaskModel(RequestBean bean) {
        this.mRequestBeans = new ArrayList<>();
        mRequestBeans.add(bean);
    }

    public NetworkRequestTaskModel(List<RequestBean> beans) {
        this.mRequestBeans = beans;
    }

    public void setNetworkExecuter(NetworkExecuter executer) {
        mNetworkExecuter = executer;
    }

    public boolean executeFirstTask() {
        RequestBean bean = mRequestBeans.get(0);
        if (mNetworkRequestTask == null) {
            mNetworkRequestTask = new NetworkRequestTask();
            Flowable<ResponseBean> flowable = mNetworkRequestTask.execute(bean, mNetworkExecuter);
            flowable.subscribeOn(Schedulers.single()).subscribe(this);
            return true;
        }
        return false;
    }

    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(ResponseBean responseBean) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}

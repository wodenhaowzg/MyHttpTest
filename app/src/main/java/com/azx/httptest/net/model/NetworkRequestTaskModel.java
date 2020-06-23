package com.azx.httptest.net.model;

import com.azx.httptest.net.NetworkExecuter;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.task.NetworkRequestTask;

import java.util.ArrayList;
import java.util.List;

public class NetworkRequestTaskModel {

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
            return mNetworkRequestTask.execute(bean, mNetworkExecuter);
        }
        return false;
    }

}

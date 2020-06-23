package com.azx.httptest.net;

import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.model.NetworkRequestTaskModel;
import com.azx.httptest.net.okhttp.OkHttpWraper;

public class NetworkTaskModelBuilder {

    public NetworkConstants.NetworkWorker mWorker = NetworkConstants.NetworkWorker.OKHTTP;

    public NetworkRequestTaskModel buildNetworkRequestModel(RequestBean bean) {
        NetworkRequestTaskModel model = new NetworkRequestTaskModel(bean);
        if (mWorker == NetworkConstants.NetworkWorker.OKHTTP) {
            OkHttpWraper wraper = new OkHttpWraper();
            model.setNetworkExecuter(wraper);
        }
        return model;
    }
}

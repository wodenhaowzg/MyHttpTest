package com.azx.httptest.net;

import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.model.NetworkRequestTaskModel;

public class NetworkOkhttpRequest implements NetworkRequest {

    private NetworkTaskModelBuilder mNetworkTaskModelBuilder;

    public NetworkOkhttpRequest() {
        this.mNetworkTaskModelBuilder = new NetworkTaskModelBuilder();
    }

    @Override
    public void requestAsync(RequestBean bean) {
        NetworkRequestTaskModel networkRequestTaskModel = mNetworkTaskModelBuilder.buildNetworkRequestModel(bean);
        networkRequestTaskModel.executeFirstTask();
    }
}

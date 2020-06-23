package com.azx.httptest.net;

import com.azx.httptest.net.bean.RequestBean;

public interface NetworkRequest {

    void requestAsync(RequestBean bean);
}

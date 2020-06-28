package com.azx.httptest.net;

import com.azx.httptest.net.bean.RequestBean;

public interface NetworkRequest {

    boolean requestAsync(RequestBean bean);
}

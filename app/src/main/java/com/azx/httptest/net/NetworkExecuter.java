package com.azx.httptest.net;

import com.azx.httptest.net.bean.RequestBean;

public interface NetworkExecuter {

    boolean executeRequest(RequestBean bean);
}

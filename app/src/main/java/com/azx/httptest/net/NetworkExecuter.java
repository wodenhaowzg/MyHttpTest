package com.azx.httptest.net;

import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.bean.ResponseBean;

public interface NetworkExecuter {

    ResponseBean executeRequest(RequestBean bean);
}

package com.azx.httptest.net;

public class NetworkConstants {

    public static final int NETWORK_CONNECT_TIMEOUT = 10;
    public static final int NETWORK_READ_TIMEOUT = 10;
    public static final int NETWORK_WRITE_TIMEOUT = 10;
    public static final int NETWORK_CALL_TIMEOUT = 10;

    public enum NetworkWorker {
        OKHTTP
    }

    public enum NetworkRequestType {
        GET, POST
    }

    public enum NetworkResponseResult {
        SUCCESS, FAILED
    }
}

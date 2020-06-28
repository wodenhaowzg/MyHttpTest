package com.azx.httptest.net.okhttp;

import android.util.Log;

import com.azx.httptest.net.NetworkConstants;
import com.azx.httptest.net.NetworkExecuter;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.bean.ResponseBean;
import com.azx.httptest.net.utils.MyLog;
import com.azx.httptest.net.utils.NetworkReuqestUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpWraper implements NetworkExecuter {
    private static final String TAG = "OkHttpWraper";
    private OkHttpClient mOkHttpClient;
    private OkHttpEventListener mOkHttpEventListener;

    public OkHttpWraper() {
        initClient();
    }

    private void initClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }

        if (mOkHttpEventListener == null) {
            mOkHttpEventListener = new OkHttpEventListener();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.callTimeout(NetworkConstants.NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(NetworkConstants.NETWORK_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(NetworkConstants.NETWORK_READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(NetworkConstants.NETWORK_WRITE_TIMEOUT, TimeUnit.SECONDS);
        builder.dns(Dns.SYSTEM);
        builder.eventListener(mOkHttpEventListener);
    }

    private Request buildRequest(RequestBean requestBean) {
        if (NetworkReuqestUtils.checkRequestIllegal(requestBean)) {
            return null;
        }

        HttpUrl url = HttpUrl.parse(requestBean.mUrl);
        if (url == null) {
            return null;
        }

        Request request = null;
        if (requestBean.mNetworkRequestType == NetworkConstants.NetworkRequestType.GET) {
            request = new Request.Builder().url(url).get().build();
        } else if (requestBean.mNetworkRequestType == NetworkConstants.NetworkRequestType.POST) {
            RequestBody body = RequestBody.create(MediaType.parse("text"), "");
            request = new Request.Builder().url(url).post(body).build();
        }
        MyLog.d(TAG, "build request : " + request);
        return request;
    }

    @Override
    public ResponseBean executeRequest(RequestBean bean) {
        Request request = buildRequest(bean);
        if (request == null) {
            return null;
        }

        if (mOkHttpClient == null) {
            return null;
        }

        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        int runningCallsCount = dispatcher.runningCallsCount();
        int queuedCallsCount = dispatcher.queuedCallsCount();
        int maxRequests = dispatcher.getMaxRequests();
        if (runningCallsCount + queuedCallsCount > maxRequests) {
            // FIXME okhttp 默认请求列表是64，这里要处理超出的情况，简单的处理方式就是直接返回
            MyLog.e(TAG, "execute request failed! max call!");
            return null;
        }

        ResponseBean responseBean = new ResponseBean();
        try {
            String resquestMsg = printRequest(request);
            MyLog.d(TAG, "send request : " + resquestMsg);
            Response response = mOkHttpClient.newCall(request).execute();
            String responseMsg = printResponse(response);
            MyLog.d(TAG, "receive response : " + responseMsg);

            boolean successful = response.isSuccessful();
            if (successful) {
                responseBean.mResponseResult = NetworkConstants.NetworkResponseResult.SUCCESS;
                // NOTICE  ResponseBody 实现了 Closeable ，属于一次性流，使用一次后就会自动关闭，不可再使用。以下方法会触发关闭
                /*
                  `Response.close()`
                  `Response.body().close()`
                  `Response.body().source().close()`
                  `Response.body().charStream().close()`
                  `Response.body().byteStream().close()`
                  `Response.body().bytes()`
                  `Response.body().string()`
                 */
                ResponseBody body = response.body();
                if (body != null) {
                    responseBean.mResponseStr = body.string();
                }
            } else {
                responseBean.mResponseResult = NetworkConstants.NetworkResponseResult.FAILED;
            }
        } catch (IOException e) {
            e.printStackTrace();
            MyLog.e(TAG, "execute request exception: " + e.getLocalizedMessage());
            responseBean.mResponseResult = NetworkConstants.NetworkResponseResult.FAILED;
        }
        return responseBean;
    }

    private String printRequest(Request request) {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n")
                .append(request.method()).append(" ").append(request.url().toString())
                .append("\r\n")
                .append("Request.headers()");

        if (request.headers().size() > 0) {
            sb.append("\r\n").append(request.headers().toString());
        } else {
            sb.append("Request.headers() = null");
        }
        RequestBody body = request.body();
        sb.append("Request.body()").append("\r\n");
        if (body != null) {
            MediaType mediaType = body.contentType();
            sb.append("RequestBody.contentType() = MediaType = ").append(mediaType).append("\r\n");
            if (mediaType != null) {
                sb.append("MediaType.toString() = ").append(mediaType.toString()).append("\r\n");
                Charset charset = mediaType.charset();
                sb.append("MediaType.charset() = ").append(charset).append("\r\n");
                String type = mediaType.type();
                sb.append("MediaType.type() = ").append(type).append("\r\n");
                String subtype = mediaType.subtype();
                sb.append("MediaType.subtype() = ").append(subtype).append("\r\n");
            }

            try {
                sb.append("RequestBody.contentLength() = ").append(body.contentLength()).append("\r\n");
                boolean duplex = body.isDuplex();
                sb.append("RequestBody.isDuplex() = ").append(duplex).append("\r\n");
                boolean oneShot = body.isOneShot();
                sb.append("RequestBody.isOneShot() = ").append(oneShot).append("\r\n");
                String string = body.toString();
                sb.append("RequestBody.string() = ").append(string).append("\r\n");
            } catch (IOException e) {
                e.printStackTrace();
                sb.append("RequestBody.string() = ").append("null").append("\r\n");
            }
        } else {
            sb.append("Request.body() = null");
        }
        return sb.toString();
    }

    private String printResponse(Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n")
                .append(response.protocol()).append(" ").append(response.code()).append(" ").append(response.isSuccessful())
                .append("\r\n")
                .append("Response.headers()")
                .append("\r\n")
                .append((response.headers().size() > 0 ? response.headers().toString() : "null"));
        ResponseBody body = response.body();
        sb.append("Response.body()").append("\r\n");
        if (body != null) {
            MediaType mediaType = body.contentType();
            sb.append("ResponseBody.contentType() = MediaType = ").append(mediaType).append("\r\n");
            if (mediaType != null) {
                sb.append("MediaType.toString() = ").append(mediaType.toString()).append("\r\n");
                Charset charset = mediaType.charset();
                sb.append("MediaType.charset() = ").append(charset).append("\r\n");
                String type = mediaType.type();
                sb.append("MediaType.type() = ").append(type).append("\r\n");
                String subtype = mediaType.subtype();
                sb.append("MediaType.subtype() = ").append(subtype).append("\r\n");
            }
            sb.append("ResponseBody.contentLength() = ").append(body.contentLength()).append("\r\n");
//            try {
//                String string = body.string();
//                sb.append("ResponseBody.string() = ").append(string).append("\r\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//                sb.append("ResponseBody.string() = ").append("null").append("\r\n");
//            }
        }
        return sb.toString();
    }
}

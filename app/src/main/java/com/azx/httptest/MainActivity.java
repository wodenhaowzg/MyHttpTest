package com.azx.httptest;

import android.databinding.DataBinderMapper;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.azx.httptest.databinding.ActivityMainBinding;
import com.azx.httptest.net.NetworkConstants;
import com.azx.httptest.net.bean.RequestBean;
import com.azx.httptest.net.bean.ResponseBean;
import com.azx.httptest.net.model.NetworkRequestTaskModel;
import com.azx.httptest.net.okhttp.OkHttpWraper;
import com.azx.httptest.net.utils.MyLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainContract.View {

    private static final String TAG = "MainActivity";
    private OkHttpClient client;
    private MainActivityPresenter presenter;
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mActivityMainBinding.mainStart.setOnClickListener(this);

        presenter = new MainActivityPresenter(this);
//        try {
//            SSLSocketFactory sslSocketFactory = initCertificate();
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.socketFactory(sslSocketFactory);
//            client = builder.build();
//
//            final Request request = new Request.Builder()
//                    .url("https://raw.github.com/square/okhttp/master/README.md")
//                    .build();
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Response response = client.newCall(request).execute();
//                        Log.d("ddd", response.body().string());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private SSLSocketFactory initCertificate() throws Exception {
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // From https://www.washington.edu/itconnect/security/ca/load-der.crt
        InputStream inputStream = getResources().openRawResource(R.raw.charles);
        BufferedInputStream caInput = new BufferedInputStream(inputStream);
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            Log.d("ddd", "ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLSv1");
        context.init(null, tmf.getTrustManagers(), null);
        // Tell the URLConnection to use a SocketFactory from our SSLContext
        return context.getSocketFactory();
    }

    public static String convertStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        presenter.requestAsyncNews();
    }

    @Override
    public void showNews(String news) {
        mActivityMainBinding.mainShowResult.setText(news);
    }


//    public void test() throws Exception {
////        // Create a MockWebServer. These are lean enough that you can create a new
////        // instance for every unit test.
////        MockWebServer server = new MockWebServer();
////        server.protocols()
////        // Schedule some responses.
////        server.enqueue(new MockResponse().setBody("hello, world!"));
////        server.enqueue(new MockResponse().setBody("sup, bra?"));
////        server.enqueue(new MockResponse().setBody("yo dog"));
////
////        // Start the server.
////        server.start();
////
////        // Ask the server for its URL. You'll need this to make HTTP requests.
////        HttpUrl baseUrl = server.url("/v1/chat/");
////
////        // Exercise your application code, which should make those HTTP requests.
////        // Responses are returned in the same order that they are enqueued.
////        Chat chat = new Chat(baseUrl);
////
////        chat.loadMore();
////        assertEquals("hello, world!", chat.messages());
////
////        chat.loadMore();
////        chat.loadMore();
////        assertEquals(""
////                + "hello, world!\n"
////                + "sup, bra?\n"
////                + "yo dog", chat.messages());
////
////        // Optional: confirm that your app made the HTTP requests you were expecting.
////        RecordedRequest request1 = server.takeRequest();
////        assertEquals("/v1/chat/messages/", request1.getPath());
////        assertNotNull(request1.getHeader("Authorization"));
////
////        RecordedRequest request2 = server.takeRequest();
////        assertEquals("/v1/chat/messages/2", request2.getPath());
////
////        RecordedRequest request3 = server.takeRequest();
////        assertEquals("/v1/chat/messages/3", request3.getPath());
////
////        // Shut down the server. Instances cannot be reused.
////        server.shutdown();
////    }

}

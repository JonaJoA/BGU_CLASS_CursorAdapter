package com.jonajoapps.sample;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HTTP;

public class NetworkManager {

    public static final String TAG = "NetworkManager";

    private static NetworkManager sInstance;
    private LooperThread mNetThread;

    public enum ReqMethod {GET, POST, DELETE, PUT}

    private NetworkManager() {
        mNetThread = new LooperThread("NetworkThread");
        mNetThread.start();
        mNetThread.waitUntilReady();
    }

    public static String getServerUrl() {
        return "http://192.168.43.17:5000/";    //Development
    }

    public static NetworkManager getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkManager();
        }
        return sInstance;
    }

    public void addRequest(BaseRequest request) {
        Message message = new Message();
        message.obj = request;
        mNetThread.addMessage(message);
    }

    private void getData(BaseRequest request) {
        ServerCallback callback = request.getCallback();

        try {
            String uri = getServerUrl() + request.getServiceUrl();

            Log.e(TAG, "Get Send: " + uri);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri);

            httpGet.setHeader(HTTP.CONTENT_ENCODING, HTTP.UTF_8);

            HttpResponse response = httpclient.execute(httpGet);
            Log.e(TAG, "Get Received: " + response.toString());

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                Log.e(TAG, "Get Received: " + responseString);


                out.close();
                if (callback != null) {
                    callback.onSuccess(responseString, statusCode);
                }
            } else {
                //Closes the connection.
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    entity.getContent().close();
                }
                Log.e(TAG, "Get Received: " + "Http Response: " + response.toString());
                Log.e(TAG, "Get Received: " + "Http Response: " + statusCode);

                if (callback != null) {
                    callback.onFailure("Http Response: " + statusCode, statusCode);
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "Get Received Error: " + e.getMessage());
            if (callback != null) {
                callback.onFailure(null, -1);
            }
        }
    }

    class LooperThread extends HandlerThread {
        public NetHandler mHandler;

        public LooperThread(String name) {
            super(name);
        }

        public synchronized void waitUntilReady() {
            mHandler = new NetHandler(this.getLooper());
        }

        public void addMessage(Message message) {
            mHandler.sendMessage(message);
        }
    }

    private class NetHandler extends Handler {
        public NetHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseRequest req = (BaseRequest) msg.obj;

            new Thread() {
                @Override
                public void run() {
                    sendRequest(req);
                }
            }.start();
        }

        private void sendRequest(BaseRequest req) {
            switch (req.getMethod()) {
                case GET: {
                    getData(req);
                    break;
                }
            }
        }
    }
}

package com.jonajoapps.sample;

/**
 * Created by Administrator on 5/6/2016.
 */
public abstract class BaseRequest {

    public static final String EXTRA_ACCESS_TOKEN = "at";
    public static final String EXTRA_REFRESH_TOKEN = "rt";
    private ServerCallback mCallback;

    public BaseRequest(ServerCallback mCallback) {
        this.mCallback = mCallback;
    }

    public ServerCallback getCallback() {
        return mCallback;
    }

    public abstract NetworkManager.ReqMethod getMethod();

    public abstract String getServiceUrl();

    public String getJsonEntity() {
        return null;
    }

    public String getMockObject() {
        return null;
    }
}

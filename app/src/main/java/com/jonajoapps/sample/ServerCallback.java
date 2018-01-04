package com.jonajoapps.sample;

/**
 * Created by shayl on 11/11/2015.
 */

public interface ServerCallback<T> {
    void onSuccess(T res, int statusCode);

    void onFailure(T err, int statusCode);
}

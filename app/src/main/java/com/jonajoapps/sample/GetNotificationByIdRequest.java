package com.jonajoapps.sample;

import static com.jonajoapps.sample.NetworkManager.ReqMethod.GET;

/**
 * Created by SHAY on 7/26/2017.
 */

public class GetNotificationByIdRequest extends BaseRequest {

    String id = "";

    public GetNotificationByIdRequest(String byId, ServerCallback mCallback) {
        super(mCallback);
        if (byId != null) {
            id = byId;
        }
    }

    @Override
    public NetworkManager.ReqMethod getMethod() {
        return GET;
    }

    //get papi/notifications?skip=&limit=sort=[ts|level] --> get all
    @Override
    public String getServiceUrl() {
        return "sessions/" + id;
    }
}

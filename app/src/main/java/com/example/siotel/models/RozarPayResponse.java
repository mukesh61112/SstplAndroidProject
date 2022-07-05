package com.example.siotel.models;

public class RozarPayResponse {
   Response responseObject;

    public RozarPayResponse() {
    }

    public RozarPayResponse(Response response) {
        this.responseObject = response;
    }

    public Response getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Response responseObject) {
        this.responseObject = responseObject;
    }
}

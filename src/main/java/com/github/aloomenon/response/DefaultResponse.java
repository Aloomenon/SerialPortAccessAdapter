package com.github.aloomenon.response;

public class DefaultResponse implements Response {

    private static final String DEFAULT_RESPONSE = "OK";

    @Override
    public String getResponse() {
        return DEFAULT_RESPONSE;
    }

}

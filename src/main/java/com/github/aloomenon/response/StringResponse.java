package com.github.aloomenon.response;

public class StringResponse implements Response {

    private final String text;

    public StringResponse(String text) {
        this.text = text;
    }

    @Override
    public String getResponse() {
        return text;
    }

}

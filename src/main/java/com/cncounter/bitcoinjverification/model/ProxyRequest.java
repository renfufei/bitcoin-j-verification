package com.cncounter.bitcoinjverification.model;

import lombok.Data;

import java.util.Map;

@Data
public class ProxyRequest {
    public static final String POST = "post";
    public static final String GET = "get";
    private String url;
    private String method;
    private String body;
    private Map<String, String> headers;
    private String proxy = "https://cncounter.com/proxy/http1.json";

    public static ProxyRequest get(String url) {
        ProxyRequest request = new ProxyRequest();
        request.setMethod(GET);
        request.setUrl(url);
        return request;
    }

    public static ProxyRequest post(String url, String body) {
        ProxyRequest request = new ProxyRequest();
        request.setMethod(POST);
        request.setUrl(url);
        request.setBody(body);
        return request;
    }
}

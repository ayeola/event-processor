package com.saltech.event_processor.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpHeader {

    protected static HttpHeaders getHeader() {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return httpHeader;
    }
}

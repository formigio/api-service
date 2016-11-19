package io.commitr.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Created by peter on 11/19/16.
 */
public class XHeaderInterceptor implements ClientHttpRequestInterceptor {

    private final String name;
    private final String value;

    public XHeaderInterceptor(String name, String value) {
        Assert.hasLength(name, "Header name must not be empty");
        this.name = name;
        this.value = value;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();

        if(!headers.containsKey(name)) {
            headers.set(name, value);
        }

        return execution.execute(request, body);
    }
}

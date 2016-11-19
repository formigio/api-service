package io.commitr.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Created by peter on 11/19/16.
 */
public class ContentTypeInterceptor implements ClientHttpRequestInterceptor {

    private final MediaType mediaType;

    public ContentTypeInterceptor(MediaType mediaType) {
        Assert.notNull(mediaType, "Content-Type must not be empty");
        Assert.isTrue(!mediaType.isWildcardType(), "'Content-Type' cannot contain wildcard type '*'");
        Assert.isTrue(!mediaType.isWildcardSubtype(), "'Content-Type' cannot contain wildcard subtype '*'");
        this.mediaType = mediaType;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();

        if(headers.getContentType() != null) {
            headers.set(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        }
        return execution.execute(request, body);
    }
}
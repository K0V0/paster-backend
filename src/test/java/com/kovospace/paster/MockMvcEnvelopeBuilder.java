package com.kovospace.paster;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class MockMvcEnvelopeBuilder {
    private String url;
    private HttpMethod httpMethod;
    private MediaType mediaType;
    private String mediaContent;
    private String jwtToken;
    private String apiKey;

    /*public MockMvcEnvelope build() {
        return new MockMvcEnvelope(this);
    }
*/
    public MockMvcEnvelopeBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public MockMvcEnvelopeBuilder withHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public MockMvcEnvelopeBuilder withMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public MockMvcEnvelopeBuilder withMediaContent(String mediaContent) {
        this.mediaContent = mediaContent;
        return this;
    }

    public MockMvcEnvelopeBuilder withJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
        return this;
    }

    public MockMvcEnvelopeBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getMediaContent() {
        return mediaContent;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getApiKey() {
        return apiKey;
    }
}

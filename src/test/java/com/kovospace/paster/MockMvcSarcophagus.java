package com.kovospace.paster;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class MockMvcSarcophagus {

    private MockMvc mockMvc;
    private MockHttpServletRequestBuilder mockHttpServletRequestBuilder;

    public MockMvcSarcophagus(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private String url;
    private HttpMethod httpMethod;
    private MediaType mediaType;
    private String mediaContent;
    private String jwtToken;
    private String apiKey;

    public MockMvcSarcophagus withUrl(String url) {
        this.url = url;
        return this;
    }

    public MockMvcSarcophagus withHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public MockMvcSarcophagus withMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public MockMvcSarcophagus withMediaContent(String mediaContent) {
        this.mediaContent = mediaContent;
        return this;
    }

    public MockMvcSarcophagus withJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
        return this;
    }

    public MockMvcSarcophagus withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }


    public ResultActions run() throws Exception {

        if ((this.httpMethod == null && this.url != null) || (this.httpMethod != null && this.url == null)) {
            throw new Exception("At least URL and http method is needed to fire the request.");
        }

        switch (this.httpMethod) {
            case GET:
                mockHttpServletRequestBuilder = get(this.url);
                break;
            case POST:
                mockHttpServletRequestBuilder = post(this.url);
                break;
            default:
                throw new Exception("Unsupported/unknown http method.");
        }

        if (this.apiKey != null) {
            mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                    .header("X-API-KEY", "dummyApiKey");
        }

        if (this.jwtToken != null) {
            mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                    .header("Authorization", this.jwtToken);
        }

        if ((this.mediaType == null && this.mediaContent != null) || (this.mediaType != null && this.mediaContent == null)) {
            throw new Exception("Media content must always come together with media type.");
        } else if (this.mediaType != null && this.mediaContent != null) {
            mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                    .contentType(this.mediaType)
                    .content(this.mediaContent);
        }

        return mockMvc.perform(mockHttpServletRequestBuilder);
    }
}

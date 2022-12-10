package com.kovospace.paster;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class MockMvcSarcophagus {

    @Value("${app.api-key-header}")
    private String API_KEY_HEADER;

    @Value("${app.language-header}")
    private String LANGUAGE_HEADER;

    private MockMvc mockMvc;
    private MockHttpServletRequestBuilder mockHttpServletRequestBuilder;

    public MockMvcSarcophagus(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private String url;
    private HttpMethod httpMethod;
    private MediaType mediaType;
    private Object mediaContent;
    private String jwtToken;
    private String apiKey;
    private String languageCode;

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

    public MockMvcSarcophagus withMediaContent(Object mediaContent) {
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

    public MockMvcSarcophagus withApiKey(String apiKey, String header) {
        this.API_KEY_HEADER = header;
        this.apiKey = apiKey;
        return this;
    }

    public MockMvcSarcophagus withLanguage(String languageCode) {
        this.LANGUAGE_HEADER = "Accept-Language";
        this.languageCode = languageCode;
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
            case PUT:
                mockHttpServletRequestBuilder = put(this.url);
                break;
            case DELETE:
                mockHttpServletRequestBuilder = delete(this.url);
                break;
            case PATCH:
                mockHttpServletRequestBuilder = patch(this.url);
                break;
            default:
                throw new Exception("Unsupported/unknown http method.");
        }

        if (this.languageCode != null) {
            mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                    .header(LANGUAGE_HEADER, languageCode);
        }

        if (this.apiKey != null) {
            mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                    .header(API_KEY_HEADER, this.apiKey);
        }

        if (this.jwtToken != null) {
            mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                    .header("Authorization", this.jwtToken);
        }

        if (this.mediaType != null) {
            mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                    .contentType(this.mediaType);
        }

        if (this.mediaContent != null) {
            if (this.mediaContent instanceof String) {
                mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                        .content((String) this.mediaContent);
            } else {
                mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                        .content((byte[]) this.mediaContent);
            }
        }

        /*if ((this.mediaType == null && this.mediaContent != null) || (this.mediaType != null && this.mediaContent == null)) {
            throw new Exception("Media content must always come together with media type.");
        } else if (this.mediaType != null && this.mediaContent != null) {
            mockHttpServletRequestBuilder = mockHttpServletRequestBuilder
                    .contentType(this.mediaType)
                    .content(this.mediaContent);
        }*/

        return mockMvc.perform(mockHttpServletRequestBuilder);
    }
}

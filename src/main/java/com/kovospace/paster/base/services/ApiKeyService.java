package com.kovospace.paster.base.services;

public interface ApiKeyService {

    boolean isValid(String apiKey);

    boolean isValid(String token, String ipAddress);

}

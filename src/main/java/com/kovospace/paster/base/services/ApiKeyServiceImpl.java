package com.kovospace.paster.base.services;

import com.kovospace.paster.base.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private ApiKeyRepository repo;

    @Autowired
    public ApiKeyServiceImpl(ApiKeyRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean isValid(String apiKey) {
        return repo.existsByKey(apiKey);
    }
}

package com.kovospace.paster.base.services;

import com.kovospace.paster.base.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Override
    @Transactional
    public boolean isValid(String token, String ipAddress) {
        return repo.findByKey(token)
                .map(ak -> {
                    if (ak.getDomains().size() == 0) { return true; }
                    return ak.getDomains()
                            .stream()
                            .anyMatch(d ->  d.getDomain().equalsIgnoreCase(ipAddress));
                })
                .orElse(false);
    }
}

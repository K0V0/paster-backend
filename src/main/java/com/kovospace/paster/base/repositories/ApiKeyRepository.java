package com.kovospace.paster.base.repositories;

import com.kovospace.paster.base.models.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    boolean existsByToken(String token);

    Optional<ApiKey> findByToken(String token);

}

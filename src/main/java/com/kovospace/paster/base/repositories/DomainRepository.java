package com.kovospace.paster.base.repositories;

import com.kovospace.paster.base.models.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<Domain, Long> {

}

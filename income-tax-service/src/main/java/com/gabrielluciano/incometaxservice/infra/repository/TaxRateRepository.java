package com.gabrielluciano.incometaxservice.infra.repository;

import com.gabrielluciano.incometaxservice.domain.model.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
}

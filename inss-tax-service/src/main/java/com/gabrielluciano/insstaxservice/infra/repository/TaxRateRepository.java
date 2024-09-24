package com.gabrielluciano.insstaxservice.infra.repository;

import com.gabrielluciano.insstaxservice.domain.model.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
}

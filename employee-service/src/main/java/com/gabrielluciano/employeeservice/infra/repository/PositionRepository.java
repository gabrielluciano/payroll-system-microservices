package com.gabrielluciano.employeeservice.infra.repository;

import com.gabrielluciano.employeeservice.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}

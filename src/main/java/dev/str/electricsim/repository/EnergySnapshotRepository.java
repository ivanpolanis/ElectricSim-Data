package dev.str.electricsim.repository;

import dev.str.electricsim.entity.EnergySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergySnapshotRepository extends JpaRepository<EnergySnapshot, Long> {
}

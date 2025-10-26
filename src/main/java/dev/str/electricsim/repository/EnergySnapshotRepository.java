package dev.str.electricsim.repository;

import dev.str.electricsim.model.EnergySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergySnapshotRepository extends JpaRepository<EnergySnapshot, Long> {
}

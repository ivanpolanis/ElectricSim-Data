package dev.str.electricsim.repository;

import dev.str.electricsim.entity.SunDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SunDataRepository extends JpaRepository<SunDataEntity, Long> {
    Optional<SunDataEntity> findByDate(LocalDate date);
    boolean existsByDate(LocalDate date);
}

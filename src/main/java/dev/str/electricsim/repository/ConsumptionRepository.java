package dev.str.electricsim.repository;

import dev.str.electricsim.entity.ConsumptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<ConsumptionEntity, OffsetDateTime> {
    boolean existsByDay(LocalDate day);

    List<ConsumptionEntity> findAllByDay(LocalDate day);

    List<ConsumptionEntity> findAllByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
}

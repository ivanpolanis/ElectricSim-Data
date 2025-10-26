package dev.str.electricsim.repository;

import dev.str.electricsim.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface WeatherRepository  extends JpaRepository<WeatherEntity, OffsetDateTime> {
    boolean existsByDay(LocalDate day);

    List<WeatherEntity> findAllByDay(LocalDate day);

    List<WeatherEntity> findAllByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
}

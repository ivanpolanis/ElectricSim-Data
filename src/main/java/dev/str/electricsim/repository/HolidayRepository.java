package dev.str.electricsim.repository;

import dev.str.electricsim.entity.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HolidayRepository extends JpaRepository<HolidayEntity, LocalDate> {
    boolean existsByDay(LocalDate date);
    HolidayEntity findByDay(LocalDate date);
}

package dev.str.electricsim.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "holidays")
public class HolidayEntity {

    @Id
    LocalDate day;
    boolean isWorkingDay;
    boolean isHoliday;

    public HolidayEntity() {
    }

    public HolidayEntity(LocalDate day, Boolean isWorkingDay, Boolean isHoliday) {
        this.day = day;
        this.isWorkingDay = isWorkingDay;
        this.isHoliday = isHoliday;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public boolean isWorkingDay() {
        return isWorkingDay;
    }

    public void setWorkingDay(boolean workingDay) {
        isWorkingDay = workingDay;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }
}

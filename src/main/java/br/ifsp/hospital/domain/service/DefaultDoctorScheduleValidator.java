package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.Doctor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class DefaultDoctorScheduleValidator implements DoctorScheduleValidator {

    @Override
    public boolean isWithinWorkingHours(Doctor doctor, LocalDateTime scheduledAt) {
        DayOfWeek day = scheduledAt.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return false;
        }
        int hour = scheduledAt.getHour();
        return hour >= 8 && hour < 18;
    }

    @Override
    public boolean isAvailable(Doctor doctor, LocalDateTime scheduledAt) {
        return true;
    }
}
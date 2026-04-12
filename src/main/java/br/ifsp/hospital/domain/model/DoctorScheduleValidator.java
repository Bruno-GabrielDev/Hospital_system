package br.ifsp.hospital.domain.model;

import java.time.LocalDateTime;

public interface DoctorScheduleValidator {
    boolean isAvailable(Doctor doctor, LocalDateTime newScheduledAt);
    boolean isWithinWorkingHours(Doctor doctor, LocalDateTime newScheduledAt);
}

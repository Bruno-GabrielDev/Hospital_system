package br.ifsp.hospital.domain.service;

import br.ifsp.hospital.domain.model.AppointmentStatus;
import br.ifsp.hospital.domain.model.Doctor;
import br.ifsp.hospital.domain.model.MedicalAppointment;
import br.ifsp.hospital.domain.repository.AppointmentRepository;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DefaultDoctorScheduleValidator implements DoctorScheduleValidator {

    private final AppointmentRepository appointmentRepository;

    public DefaultDoctorScheduleValidator(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

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
        List<MedicalAppointment> existingAppointments = appointmentRepository
                .findByDoctorIdAndScheduledAt(doctor.getId(), scheduledAt);

        boolean hasActiveAppointment = existingAppointments.stream()
                .anyMatch(appt -> appt.getStatus() != AppointmentStatus.CANCELED);

        return !hasActiveAppointment;
    }
}
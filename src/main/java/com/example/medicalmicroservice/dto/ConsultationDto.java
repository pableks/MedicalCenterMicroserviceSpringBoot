package com.example.medicalmicroservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ConsultationDto {

    private Long id;

    @NotNull(message = "Consultation date and time is required")
    private LocalDateTime dateTime;

    @NotBlank(message = "Reason for consultation is required")
    private String notes;

    @NotNull(message = "Doctor is required")
    private DoctorDto doctor;

    @NotNull(message = "Patient is required")
    private PatientDto patient;

    public ConsultationDto() {
    }

    public ConsultationDto(LocalDateTime dateTime, DoctorDto doctor, PatientDto patient, String notes) {
        this.dateTime = dateTime;
        this.doctor = doctor;
        this.patient = patient;
        this.notes = notes;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DoctorDto getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDto doctor) {
        this.doctor = doctor;
    }

    public PatientDto getPatient() {
        return patient;
    }

    public void setPatient(PatientDto patient) {
        this.patient = patient;
    }

    public String getDoctorFullName() {
        if (doctor != null) {
            return doctor.getFirstName() + " " + doctor.getLastName();
        }
        return null;
    }

    public String getPatientFullName() {
        if (patient != null) {
            return patient.getFirstName() + " " + patient.getLastName();
        }
        return null;
    }
}
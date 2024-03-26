package com.example.medicalmicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class MedicalRecordDto {
    
    @NotNull(message = "ID is required")
    private Integer id;

    @NotBlank(message = "Record number is required")
    private String recordNumber;

    @NotNull(message = "Record date is required")
    private LocalDate recordDate;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    @NotBlank(message = "Treatment is required")
    private String treatment;

    private List<ConsultationDto> consultations;

    public MedicalRecordDto() {
    }

    public MedicalRecordDto(Integer id, String recordNumber, LocalDate recordDate, String diagnosis, String treatment) {
        this.id = id;
        this.recordNumber = recordNumber;
        this.recordDate = recordDate;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public List<ConsultationDto> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<ConsultationDto> consultations) {
        this.consultations = consultations;
    }
}

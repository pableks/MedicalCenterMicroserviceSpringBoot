package com.example.medicalmicroservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class PatientDto {

    
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?\\d{10,14}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotNull(message = "Medical record is required")
    private MedicalRecordDto medicalRecord;

    public PatientDto() {
    }

    public PatientDto(Long id, String firstName, String lastName, LocalDate dateOfBirth, String email, String phoneNumber, MedicalRecordDto medicalRecord) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.medicalRecord = medicalRecord;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MedicalRecordDto getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecordDto medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public String getPatientFullName() {
        return firstName + " " + lastName;
    }
}
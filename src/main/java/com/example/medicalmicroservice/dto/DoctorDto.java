package com.example.medicalmicroservice.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class DoctorDto {
    
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?\\d{10,14}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotNull(message = "List of consultations is required")
    private List<ConsultationDto> consultations;

    public DoctorDto() {
    }

    public DoctorDto(String firstName, String lastName, String specialization, String email, String phoneNumber, List<ConsultationDto> consultations) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.consultations = consultations;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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

    public List<ConsultationDto> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<ConsultationDto> consultations) {
        this.consultations = consultations;
    }

    // Method to get full name
    public String getDoctorFullName() {
        return firstName + " " + lastName;
    }
}

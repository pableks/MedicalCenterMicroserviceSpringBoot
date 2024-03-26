package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.dto.PatientDto;
import com.example.medicalmicroservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private List<PatientDto> patients = new ArrayList<>();

    public PatientDto createPatient(PatientDto patientDto) {
        if (patientDto.getMedicalRecord() != null && medicalRecordIdExists(patientDto.getMedicalRecord().getId())) {
            throw new IllegalArgumentException("Medical record ID already exists");
        }
        patientDto.setId(generateId());
        patients.add(patientDto);
        return patientDto;
    }

    public List<PatientDto> getAllPatients() {
        return patients;
    }

    public PatientDto getPatientById(Long id) {
        Optional<PatientDto> optionalPatient = patients.stream()
                .filter(patient -> patient.getId().equals(id))
                .findFirst();

        return optionalPatient.orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
    }

    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        PatientDto existingPatient = getPatientById(id);
        if (patientDto.getMedicalRecord() != null && !existingPatient.getMedicalRecord().getId().equals(patientDto.getMedicalRecord().getId()) && medicalRecordIdExists(patientDto.getMedicalRecord().getId())) {
            throw new IllegalArgumentException("Medical record ID already exists");
        }
        existingPatient.setFirstName(patientDto.getFirstName());
        existingPatient.setLastName(patientDto.getLastName());
        existingPatient.setDateOfBirth(patientDto.getDateOfBirth());
        existingPatient.setEmail(patientDto.getEmail());
        existingPatient.setPhoneNumber(patientDto.getPhoneNumber());
        existingPatient.setMedicalRecord(patientDto.getMedicalRecord());
        return existingPatient;
    }

    public void deletePatient(Long id) {
        PatientDto existingPatient = getPatientById(id);
        patients.remove(existingPatient);
    }

    private Long generateId() {
        return patients.size() + 1L;
    }

    public boolean medicalRecordIdExists(Integer medicalRecordId) {
        return patients.stream()
                .anyMatch(patient -> patient.getMedicalRecord().getId().equals(medicalRecordId));
    }
}
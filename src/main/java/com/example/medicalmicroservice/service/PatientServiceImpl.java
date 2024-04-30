package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.exception.ResourceNotFoundException;
import com.example.medicalmicroservice.model.Patient;
import com.example.medicalmicroservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public Patient getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with user id: " + userId));
    }

    @Override
    public Patient updatePatient(Patient patient) {
        Patient existingPatient = getPatientById(patient.getId());
        existingPatient.setName(patient.getName());
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        existingPatient.setGender(patient.getGender());
        existingPatient.setContactNumber(patient.getContactNumber());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setAddress(patient.getAddress());
        return patientRepository.save(existingPatient);
    }

    // Implementations for additional CRUD operations, if needed
}
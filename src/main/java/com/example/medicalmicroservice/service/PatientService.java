
package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.model.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();
    Patient getPatientById(Long id);
    Patient getPatientByUserId(Long userId);
    Patient updatePatient(Patient patient);
  
}
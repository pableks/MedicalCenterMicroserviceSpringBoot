package com.example.medicalmicroservice.controller;

import com.example.medicalmicroservice.dto.MedicalRecordDto;
import com.example.medicalmicroservice.dto.PatientDto;
import com.example.medicalmicroservice.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
        createMockupPatients();
    }

    private void createMockupPatients() {
        MedicalRecordDto medicalRecord1 = new MedicalRecordDto(1, "MR-001", LocalDate.now(), "Diagnosis 1", "Treatment 1");
        MedicalRecordDto medicalRecord2 = new MedicalRecordDto(2, "MR-002", LocalDate.now(), "Diagnosis 2", "Treatment 2");
        MedicalRecordDto medicalRecord3 = new MedicalRecordDto(3, "MR-003", LocalDate.now(), "Diagnosis 3", "Treatment 3");

        PatientDto patient1 = new PatientDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "john@example.com", "+1234567890", medicalRecord1);
        PatientDto patient2 = new PatientDto(2L,"Jane", "Smith", LocalDate.of(1985, 5, 10), "jane@example.com", "+9876543210", medicalRecord2);
        PatientDto patient3 = new PatientDto(3L,"Mike", "Johnson", LocalDate.of(1980, 12, 15), "mike@example.com", "+5555555555", medicalRecord3);

        patientService.createPatient(patient1);
        patientService.createPatient(patient2);
        patientService.createPatient(patient3);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
}
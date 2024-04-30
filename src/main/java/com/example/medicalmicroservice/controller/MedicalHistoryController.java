package com.example.medicalmicroservice.controller;

import com.example.medicalmicroservice.model.MedicalHistory;
import com.example.medicalmicroservice.model.Patient;
import com.example.medicalmicroservice.model.User;
import com.example.medicalmicroservice.service.MedicalHistoryService;
import com.example.medicalmicroservice.service.PatientService;
import com.example.medicalmicroservice.util.SessionUtil;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-histories")
public class MedicalHistoryController {
    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private PatientService patientService;

    @GetMapping
public ResponseEntity<List<MedicalHistory>> getAllMedicalHistoriesForCurrentUser(HttpSession session) {
    User currentUser = SessionUtil.getUserFromSession();
    if (currentUser != null) {
        if (currentUser.getRole().equals("doctor")) {
            // Doctors can access medical histories of all patients
            List<MedicalHistory> medicalHistories = medicalHistoryService.getAllMedicalHistories();
            return ResponseEntity.ok(medicalHistories);
        } else if (currentUser.getRole().equals("patient")) {
            Patient patient = patientService.getPatientByUserId(currentUser.getId());
            if (patient != null) {
                List<MedicalHistory> medicalHistories = medicalHistoryService.getMedicalHistoriesByPatientId(patient.getId());
                return ResponseEntity.ok(medicalHistories);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistory> getMedicalHistoryById(@PathVariable Long id, HttpSession session) {
        User currentUser = SessionUtil.getUserFromSession();
        if (currentUser != null) {
            if (currentUser.getRole().equals("doctor")) {
                // Doctors can access any medical history
                MedicalHistory medicalHistory = medicalHistoryService.getMedicalHistoryById(id);
                return medicalHistory != null ? ResponseEntity.ok(medicalHistory) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else if (currentUser.getRole().equals("patient")) {
                Patient patient = patientService.getPatientByUserId(currentUser.getId());
                if (patient != null) {
                    MedicalHistory medicalHistory = medicalHistoryService.getMedicalHistoryById(id);
                    if (medicalHistory != null && medicalHistory.getPatient().getId().equals(patient.getId())) {
                        return ResponseEntity.ok(medicalHistory);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping
    public ResponseEntity<MedicalHistory> createMedicalHistory(@RequestBody MedicalHistory medicalHistory,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            Patient patient = patientService.getPatientByUserId(currentUser.getId());
            if (patient != null) {
                medicalHistory.setPatient(patient);
                MedicalHistory createdMedicalHistory = medicalHistoryService.createMedicalHistory(medicalHistory);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdMedicalHistory);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistory> updateMedicalHistory(@PathVariable Long id,
            @RequestBody MedicalHistory medicalHistory, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            Patient patient = patientService.getPatientByUserId(currentUser.getId());
            if (patient != null) {
                MedicalHistory existingMedicalHistory = medicalHistoryService.getMedicalHistoryById(id);
                if (existingMedicalHistory != null
                        && existingMedicalHistory.getPatient().getId().equals(patient.getId())) {
                    medicalHistory.setId(id);
                    medicalHistory.setPatient(patient);
                    MedicalHistory updatedMedicalHistory = medicalHistoryService.updateMedicalHistory(medicalHistory);
                    return ResponseEntity.ok(updatedMedicalHistory);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalHistory(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            Patient patient = patientService.getPatientByUserId(currentUser.getId());
            if (patient != null) {
                MedicalHistory medicalHistory = medicalHistoryService.getMedicalHistoryById(id);
                if (medicalHistory != null && medicalHistory.getPatient().getId().equals(patient.getId())) {
                    medicalHistoryService.deleteMedicalHistory(id);
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
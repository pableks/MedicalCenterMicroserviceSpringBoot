package com.example.medicalmicroservice.controller;


import com.example.medicalmicroservice.model.Patient;
import com.example.medicalmicroservice.model.User;
import com.example.medicalmicroservice.service.PatientService;
import com.example.medicalmicroservice.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private static final Logger log = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<Object> getAllPatients() {
        User currentUser = SessionUtil.getUserFromSession();
        if (currentUser != null && currentUser.getRole().equals("doctor")) {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        }
        log.error("Access denied. Only doctors can view all patients.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Access denied. Only doctors can view all patients."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPatientById(@PathVariable Long id) {
        User currentUser = SessionUtil.getUserFromSession();
        if (currentUser != null) {
            if (currentUser.getRole().equals("doctor")) {
                Patient patient = patientService.getPatientById(id);
                if (patient != null) {
                    return ResponseEntity.ok(patient);
                }
                log.error("Patient not found with ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Patient not found with ID " + id));
            } else if (currentUser.getRole().equals("patient")) {
                Patient patient = patientService.getPatientByUserId(currentUser.getId());
                if (patient != null && patient.getId().equals(id)) {
                    return ResponseEntity.ok(patient);
                }
            }
        }
        log.error("Access denied. You are not authorized to view this patient.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Access denied. You are not authorized to view this patient."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        User currentUser = SessionUtil.getUserFromSession();
        if (currentUser != null && currentUser.getRole().equals("patient")) {
            Patient existingPatient = patientService.getPatientByUserId(currentUser.getId());
            if (existingPatient != null && existingPatient.getId().equals(id)) {
                patient.setId(id);
                Patient updatedPatient = patientService.updatePatient(patient);
                if (updatedPatient != null) {
                    return ResponseEntity.ok(updatedPatient);
                }
                log.error("Error updating patient with ID {}", id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Error updating patient with ID " + id));
            }
        }
        log.error("Access denied. You are not authorized to update this patient.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Access denied. You are not authorized to update this patient."));
    }

    // Other patient-related methods

    static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
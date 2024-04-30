package com.example.medicalmicroservice.controller;

import com.example.medicalmicroservice.model.Consultation;
import com.example.medicalmicroservice.model.Doctor;
import com.example.medicalmicroservice.model.MedicalHistory;
import com.example.medicalmicroservice.model.Patient;
import com.example.medicalmicroservice.model.User;
import com.example.medicalmicroservice.service.ConsultationService;
import com.example.medicalmicroservice.service.DoctorService;
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
@RequestMapping("/api/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicalHistoryService medicalHistoryService; // Add this field

    @PostMapping
    public ResponseEntity<Consultation> createConsultation(@RequestBody Consultation consultation, HttpSession session) {
    User currentUser = SessionUtil.getUserFromSession();
    if (currentUser != null && currentUser.getRole().equals("doctor")) {
        Doctor doctor = doctorService.getDoctorByUserId(currentUser.getId());
        if (doctor != null) {
            Patient patient = patientService.getPatientById(consultation.getPatient().getId());
            if (patient != null) {
                MedicalHistory medicalHistory = consultation.getMedicalHistory();
                if (medicalHistory == null) {
                    medicalHistory = new MedicalHistory();
                }
                medicalHistory.setPatient(patient); // Set the patient in the medical history

                // Persist the MedicalHistory entity first
                MedicalHistory persistedMedicalHistory = medicalHistoryService.createMedicalHistory(medicalHistory);

                consultation.setDoctor(doctor);
                consultation.setPatient(patient);
                consultation.setMedicalHistory(persistedMedicalHistory);
                Consultation createdConsultation = consultationService.createConsultation(consultation);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdConsultation);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}

    @GetMapping
    public ResponseEntity<List<Consultation>> getAllConsultations(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.getRole().equals("doctor")) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getId());
            if (doctor != null) {
                List<Consultation> consultations = consultationService.getConsultationsByDoctorId(doctor.getId());
                return ResponseEntity.ok(consultations);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user")
public ResponseEntity<List<Consultation>> getConsultationsByUser(HttpSession session) {
    User currentUser = SessionUtil.getUserFromSession();
    if (currentUser != null) {
        if (currentUser.getRole().equals("patient")) {
            Patient patient = patientService.getPatientByUserId(currentUser.getId());
            if (patient != null) {
                List<Consultation> consultations = consultationService.getConsultationsByPatientId(patient.getId());
                return ResponseEntity.ok(consultations);
            }
        } else if (currentUser.getRole().equals("doctor")) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getId());
            if (doctor != null) {
                List<Consultation> consultations = consultationService.getConsultationsByDoctorId(doctor.getId());
                return ResponseEntity.ok(consultations);
            }
        }
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}

    @GetMapping("/{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.getRole().equals("doctor")) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getId());
            if (doctor != null) {
                Consultation consultation = consultationService.getConsultationById(id);
                if (consultation != null && consultation.getDoctor().getId().equals(doctor.getId())) {
                    return ResponseEntity.ok(consultation);
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

    @PutMapping("/{id}")
    public ResponseEntity<Consultation> updateConsultation(@PathVariable Long id,
            @RequestBody Consultation consultation, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.getRole().equals("doctor")) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getId());
            if (doctor != null) {
                Consultation existingConsultation = consultationService.getConsultationById(id);
                if (existingConsultation != null && existingConsultation.getDoctor().getId().equals(doctor.getId())) {
                    consultation.setId(id);
                    consultation.setDoctor(doctor);
                    Consultation updatedConsultation = consultationService.updateConsultation(consultation);
                    return ResponseEntity.ok(updatedConsultation);
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
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.getRole().equals("doctor")) {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getId());
            if (doctor != null) {
                Consultation consultation = consultationService.getConsultationById(id);
                if (consultation != null && consultation.getDoctor().getId().equals(doctor.getId())) {
                    consultationService.deleteConsultation(id);
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
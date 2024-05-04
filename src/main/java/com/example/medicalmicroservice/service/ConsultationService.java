package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.dto.ConsultationDto;
import com.example.medicalmicroservice.dto.DoctorDto;
import com.example.medicalmicroservice.dto.MedicalRecordDto;
import com.example.medicalmicroservice.dto.PatientDto;
import com.example.medicalmicroservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {

    private List<ConsultationDto> consultations = new ArrayList<>();
    private final PatientService patientService;
    private final DoctorService doctorService;

    public ConsultationService(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    public ConsultationDto createConsultation(ConsultationDto consultationDto) {
        consultationDto.setId(generateId());

        // Retrieve the patient and doctor based on their IDs
        PatientDto patient = patientService.getPatientById(consultationDto.getPatient().getId());
        DoctorDto doctor = doctorService.getDoctorById(consultationDto.getDoctor().getId());

        consultationDto.setPatient(patient);
        consultationDto.setDoctor(doctor);

        // Add the consultation to the patient's medical record
        MedicalRecordDto medicalRecord = patient.getMedicalRecord();
        if (medicalRecord.getConsultations() == null) {
            medicalRecord.setConsultations(new ArrayList<>());
        }
        medicalRecord.getConsultations().add(consultationDto);

        consultations.add(consultationDto);
        return consultationDto;
    }

    public List<ConsultationDto> getAllConsultations() {
        return consultations;
    }

    public ConsultationDto getConsultationById(Long id) {
        Optional<ConsultationDto> optionalConsultation = consultations.stream()
                .filter(consultation -> consultation.getId().equals(id))
                .findFirst();

        return optionalConsultation.orElseThrow(() -> new ResourceNotFoundException("Consultation", "id", id));
    }

    public ConsultationDto updateConsultation(Long id, ConsultationDto consultationDto) {
        ConsultationDto existingConsultation = getConsultationById(id);

        existingConsultation.setDateTime(consultationDto.getDateTime());
        existingConsultation.setNotes(consultationDto.getNotes());

        // Retrieve the patient and doctor based on their IDs
        PatientDto patient = patientService.getPatientById(consultationDto.getPatient().getId());
        DoctorDto doctor = doctorService.getDoctorById(consultationDto.getDoctor().getId());

        existingConsultation.setPatient(patient);
        existingConsultation.setDoctor(doctor);

        return existingConsultation;
    }

    public void deleteConsultation(Long id) {
        ConsultationDto existingConsultation = getConsultationById(id);

        // Remove the consultation from the patient's medical record
        PatientDto patient = existingConsultation.getPatient();
        MedicalRecordDto medicalRecord = patient.getMedicalRecord();
        medicalRecord.getConsultations().remove(existingConsultation);

        consultations.remove(existingConsultation);
    }

    private Long generateId() {
        return consultations.size() + 1L;
    }
}
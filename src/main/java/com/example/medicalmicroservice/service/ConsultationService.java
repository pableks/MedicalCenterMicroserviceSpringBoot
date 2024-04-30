package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.model.Consultation;

import java.util.List;

public interface ConsultationService {
    Consultation createConsultation(Consultation consultation);
    List<Consultation> getConsultationsByPatientId(Long patientId);

    List<Consultation> getConsultationsByDoctorId(Long doctorId);
    Consultation getConsultationById(Long id);
    Consultation updateConsultation(Consultation consultation);
    void deleteConsultation(Long id);

}
package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.exception.ResourceNotFoundException;
import com.example.medicalmicroservice.model.Consultation;
import com.example.medicalmicroservice.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;


    @Override
    public Consultation createConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    public List<Consultation> getConsultationsByPatientId(Long patientId) {
        return consultationRepository.findByPatientId(patientId);
    }

    @Override
    public List<Consultation> getConsultationsByDoctorId(Long doctorId) {
        return consultationRepository.findByDoctorId(doctorId);
    }

    @Override
    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with id: " + id));
    }

    @Override
    public Consultation updateConsultation(Consultation consultation) {
        Consultation existingConsultation = getConsultationById(consultation.getId());
        existingConsultation.setPatient(consultation.getPatient());
        existingConsultation.setDoctor(consultation.getDoctor());
        existingConsultation.setMedicalHistory(consultation.getMedicalHistory());
        existingConsultation.setConsultationDate(consultation.getConsultationDate());
        existingConsultation.setDiagnosis(consultation.getDiagnosis());
        existingConsultation.setTreatment(consultation.getTreatment());
        return consultationRepository.save(existingConsultation);
    }

    
    

    @Override
    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }
}
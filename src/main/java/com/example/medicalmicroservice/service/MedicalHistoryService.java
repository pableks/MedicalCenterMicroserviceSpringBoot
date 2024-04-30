package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.model.MedicalHistory;
import java.util.List;

public interface MedicalHistoryService {
    List<MedicalHistory> getAllMedicalHistories();
    List<MedicalHistory> getMedicalHistoriesByPatientId(Long patientId);
    List<MedicalHistory> getMedicalHistoriesWithConsultationsByPatientId(Long patientId);

    MedicalHistory getMedicalHistoryById(Long id);
    MedicalHistory getMedicalHistoryByIdWithConsultations(Long id); // Add this method
    MedicalHistory createMedicalHistory(MedicalHistory medicalHistory);
    MedicalHistory updateMedicalHistory(MedicalHistory medicalHistory);
    void deleteMedicalHistory(Long id);
}
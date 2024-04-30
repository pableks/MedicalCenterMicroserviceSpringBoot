    package com.example.medicalmicroservice.service;

    import com.example.medicalmicroservice.exception.ResourceNotFoundException;
    import com.example.medicalmicroservice.model.MedicalHistory;
    import com.example.medicalmicroservice.repository.MedicalHistoryRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class MedicalHistoryServiceImpl implements MedicalHistoryService {

        @Autowired
        private MedicalHistoryRepository medicalHistoryRepository;

        @Override
        public List<MedicalHistory> getAllMedicalHistories() {
            return medicalHistoryRepository.findAll();
        }

        @Override
        public List<MedicalHistory> getMedicalHistoriesByPatientId(Long patientId) {
            return medicalHistoryRepository.findByPatientId(patientId);
        }

        @Override
        public MedicalHistory getMedicalHistoryByIdWithConsultations(Long id) {
            return medicalHistoryRepository.findByIdWithConsultations(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Medical History not found with id: " + id));
        }

        @Override
        public List<MedicalHistory> getMedicalHistoriesWithConsultationsByPatientId(Long patientId) {
            return medicalHistoryRepository.findByPatientIdWithConsultations(patientId);
        }

        @Override
        public MedicalHistory getMedicalHistoryById(Long id) {
            return medicalHistoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Medical History not found with id: " + id));
        }

        @Override
        public MedicalHistory createMedicalHistory(MedicalHistory medicalHistory) {
            return medicalHistoryRepository.save(medicalHistory);
        }

        @Override
        public MedicalHistory updateMedicalHistory(MedicalHistory medicalHistory) {
            MedicalHistory existingMedicalHistory = getMedicalHistoryById(medicalHistory.getId());
            existingMedicalHistory.setPatient(medicalHistory.getPatient());
            existingMedicalHistory.setConsultations(medicalHistory.getConsultations());
            // Set other fields as needed...
            return medicalHistoryRepository.save(existingMedicalHistory);
        }

        @Override
        public void deleteMedicalHistory(Long id) {
            if (!medicalHistoryRepository.existsById(id)) {
                throw new ResourceNotFoundException("Medical History not found with id: " + id);
            }
            medicalHistoryRepository.deleteById(id);
        }
    }
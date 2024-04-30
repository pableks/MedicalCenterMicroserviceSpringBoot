package com.example.medicalmicroservice.repository;

import com.example.medicalmicroservice.model.MedicalHistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
List<MedicalHistory> findByPatientId(Long patientId);
@Query("SELECT m FROM MedicalHistory m LEFT JOIN FETCH m.consultations WHERE m.id = :id")
Optional<MedicalHistory> findByIdWithConsultations(@Param("id") Long id);

@Query("SELECT m FROM MedicalHistory m LEFT JOIN FETCH m.consultations WHERE m.patient.id = :patientId")
List<MedicalHistory> findByPatientIdWithConsultations(@Param("patientId") Long patientId);
}

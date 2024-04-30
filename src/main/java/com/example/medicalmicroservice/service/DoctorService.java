package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.model.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(Long id);
    Doctor getDoctorByUserId(Long userId);
    Doctor updateDoctor(Doctor doctor);
}
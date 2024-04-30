package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.exception.ResourceNotFoundException;
import com.example.medicalmicroservice.model.Doctor;
import com.example.medicalmicroservice.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public Doctor getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with user id: " + userId));
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        Doctor existingDoctor = getDoctorById(doctor.getId());
        existingDoctor.setName(doctor.getName());
        existingDoctor.setSpecialty(doctor.getSpecialty());
        // Update other fields as necessary
        return doctorRepository.save(existingDoctor);
    }

    // Implementations for additional CRUD operations, if needed
}
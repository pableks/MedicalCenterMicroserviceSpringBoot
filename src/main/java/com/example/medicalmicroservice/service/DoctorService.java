package com.example.medicalmicroservice.service;

import com.example.medicalmicroservice.dto.DoctorDto;
import com.example.medicalmicroservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private List<DoctorDto> doctors = new ArrayList<>();

    public DoctorDto createDoctor(DoctorDto doctorDto) {
        doctorDto.setId(generateId());
        doctors.add(doctorDto);
        return doctorDto;
    }

    public List<DoctorDto> getAllDoctors() {
        return doctors;
    }

    public DoctorDto getDoctorById(Long id) {
        Optional<DoctorDto> optionalDoctor = doctors.stream()
                .filter(doctor -> doctor.getId().equals(id))
                .findFirst();

        return optionalDoctor.orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
    }

    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        DoctorDto existingDoctor = getDoctorById(id);

        existingDoctor.setFirstName(doctorDto.getFirstName());
        existingDoctor.setLastName(doctorDto.getLastName());
        existingDoctor.setSpecialization(doctorDto.getSpecialization());
        // Update other fields as needed

        return existingDoctor;
    }

    public void deleteDoctor(Long id) {
        DoctorDto existingDoctor = getDoctorById(id);
        doctors.remove(existingDoctor);
    }

    private Long generateId() {
        return doctors.size() + 1L;
    }
}
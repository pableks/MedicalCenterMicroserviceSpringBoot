package com.example.medicalmicroservice.controller;

import com.example.medicalmicroservice.dto.ConsultationDto;
import com.example.medicalmicroservice.dto.DoctorDto;
import com.example.medicalmicroservice.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
        createMockupDoctors();
    }

    private void createMockupDoctors() {
        // Create mockup consultations
        ConsultationDto consultation1 = new ConsultationDto(/* Consultation details */);
        ConsultationDto consultation2 = new ConsultationDto(/* Consultation details */);
        ConsultationDto consultation3 = new ConsultationDto(/* Consultation details */);
    
        // Create mockup doctor with consultations
        DoctorDto doctor1 = new DoctorDto("Tomas", "Heim", "Cardiology", "john@example.com", "+1234567890", List.of(consultation1));
        DoctorDto doctor2 = new DoctorDto("Jessica", "Rebolledo", "Pediatrics", "jane@example.com", "+9876543210", List.of(consultation2));
        
        // Create mockup doctor Carlos Valverde
        DoctorDto doctor3 = new DoctorDto("Carlos", "Valverde", "Neurology", "carlos@example.com", "+1122334455", List.of(consultation3));
    
        // Persist mockup doctors
        doctorService.createDoctor(doctor1);
        doctorService.createDoctor(doctor2);
        doctorService.createDoctor(doctor3);
    }
    

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
}

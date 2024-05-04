package com.centrosalud.centrosalud.service;

import java.util.List;
import java.util.Optional;

import com.centrosalud.centrosalud.model.Profesional;



public interface ProfesionalService {

    List<Profesional> getAllProfesionales();
    Optional<Profesional> getProfesionalById(Long id);
    Profesional createProfesional(Profesional profesional);
    void deleteProfesional(Long id);
    
}

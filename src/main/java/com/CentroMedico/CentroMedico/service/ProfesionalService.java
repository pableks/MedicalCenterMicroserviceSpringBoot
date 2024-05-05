package com.CentroMedico.CentroMedico.service;

import java.util.List;
import java.util.Optional;

import com.CentroMedico.CentroMedico.model.Profesional;



public interface ProfesionalService {

    List<Profesional> getAllProfesionales();
    Optional<Profesional> getProfesionalById(Long id);
    Profesional createProfesional(Profesional profesional);
    Profesional updateProfesional(Profesional profesional);
    void deleteProfesional(Long id);

}

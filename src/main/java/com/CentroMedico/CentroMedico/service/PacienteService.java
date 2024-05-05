package com.CentroMedico.CentroMedico.service;

import java.util.List;
import java.util.Optional;

import com.CentroMedico.CentroMedico.model.Paciente;

public interface PacienteService {

    List<Paciente> getAllPacientes();
    Optional<Paciente> getPacienteById(Long id);
    Paciente createPaciente(Paciente paciente);
    Paciente updatePaciente(Long id, Paciente paciente);
    void deletePaciente(Long id);


    
}

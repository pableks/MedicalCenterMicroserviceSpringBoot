package com.CentroMedico.CentroMedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CentroMedico.CentroMedico.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente,Long>{
    
}

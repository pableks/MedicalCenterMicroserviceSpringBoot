package com.centrosalud.centrosalud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.centrosalud.centrosalud.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente,Long>{
    
}

package com.CentroMedico.CentroMedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CentroMedico.CentroMedico.model.Especialidad;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    
}

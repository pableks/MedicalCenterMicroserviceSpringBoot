package com.CentroMedico.CentroMedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CentroMedico.CentroMedico.model.Profesional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long>{
    
}

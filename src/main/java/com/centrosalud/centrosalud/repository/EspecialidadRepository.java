package com.centrosalud.centrosalud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.centrosalud.centrosalud.model.Especialidad;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    
}

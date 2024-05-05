package com.CentroMedico.CentroMedico.service;

import java.util.List;
import java.util.Optional;

import com.CentroMedico.CentroMedico.model.Especialidad;

public interface EspecialidadService {

    List<Especialidad> getAllEspecialidad();
    Optional<Especialidad> getEspecialidadById(Long id);
    Especialidad createEspecialidad(Especialidad especialidad);
    void deleteEspecialidad(Long id);


    
}

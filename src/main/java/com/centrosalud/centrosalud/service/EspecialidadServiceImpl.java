package com.centrosalud.centrosalud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.centrosalud.centrosalud.model.Especialidad;
import com.centrosalud.centrosalud.repository.EspecialidadRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadServiceImpl implements EspecialidadService{

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Especialidad> getAllEspecialidad(){
        return especialidadRepository.findAll();
    }

    @Override
    public Optional<Especialidad> getEspecialidadById(Long id){
        return especialidadRepository.findById(id);
    }

    @Override
    public Especialidad createEspecialidad(Especialidad especialidad){
        return especialidadRepository.save(especialidad);
    }

    @Override
    public void deleteEspecialidad(Long id){
        especialidadRepository.deleteById(id);
    }
    
}

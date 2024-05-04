package com.centrosalud.centrosalud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.centrosalud.centrosalud.model.Profesional;
import com.centrosalud.centrosalud.repository.ProfesionalRepository;

import java.util.List;
import java.util.Optional;



@Service
public class ProfesionalServiceImpl implements ProfesionalService{

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Override
    public List<Profesional> getAllProfesionales(){
        return profesionalRepository.findAll();
    }

    @Override
    public Optional<Profesional> getProfesionalById(Long id){
        return profesionalRepository.findById(id);
    }

    @Override
    public Profesional createProfesional(Profesional profesional){
        return profesionalRepository.save(profesional);
    }

    @Override
    public void deleteProfesional(Long id){
        profesionalRepository.deleteById(id);
    }
    
}

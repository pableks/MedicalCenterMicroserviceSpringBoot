package com.CentroMedico.CentroMedico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CentroMedico.CentroMedico.model.Profesional;
import com.CentroMedico.CentroMedico.repository.ProfesionalRepository;

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

    @Override
    public Profesional updateProfesional(Profesional profesional) {
        return profesionalRepository.save(profesional);
    }

    
    
}

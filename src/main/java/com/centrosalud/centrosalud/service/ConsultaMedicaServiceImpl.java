package com.centrosalud.centrosalud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.centrosalud.centrosalud.model.ConsultaMedica;
import com.centrosalud.centrosalud.repository.ConsultaMedicaRepository;

@Service
public class ConsultaMedicaServiceImpl implements ConsultaMedicaService{

    @Autowired
    private ConsultaMedicaRepository consultaMedicaRepository;

    @Override
    public List<ConsultaMedica> getAllConsultaMedica(){
        return consultaMedicaRepository.findAll();
    }


    @Override
    public Optional<ConsultaMedica> getConsultaMedicaById(Long id){
        return consultaMedicaRepository.findById(id);
    }

    @Override
    public ConsultaMedica createConsultaMedica(ConsultaMedica consulta){
        return consultaMedicaRepository.save(consulta);
    }

    @Override
    public void deleteConsultaMedica(Long id){
        consultaMedicaRepository.deleteById(id);
    }
}

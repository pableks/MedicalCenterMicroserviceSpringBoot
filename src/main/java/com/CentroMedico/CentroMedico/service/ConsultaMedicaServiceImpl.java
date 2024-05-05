package com.CentroMedico.CentroMedico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CentroMedico.CentroMedico.model.ConsultaMedica;
import com.CentroMedico.CentroMedico.repository.ConsultaMedicaRepository;

@Service
public class ConsultaMedicaServiceImpl implements ConsultaMedicaService{

    @Autowired
    private ConsultaMedicaRepository consultaMedicaRepository;

    @Override
    public List<ConsultaMedica> getAllConsultaMedica(){
        return consultaMedicaRepository.findAll();
    }

    @Override
public Optional<ConsultaMedica> findExistingConsulta(ConsultaMedica consultaMedica) {
    return consultaMedicaRepository.findByFechaconsultaAndHorarioAndProfesionalAndPaciente(
        consultaMedica.getFechaconsulta(),
        consultaMedica.getHorario(),
        consultaMedica.getProfesional(),
        consultaMedica.getPaciente()
    );
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

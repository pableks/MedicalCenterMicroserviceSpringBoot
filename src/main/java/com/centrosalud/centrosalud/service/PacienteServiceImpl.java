package com.centrosalud.centrosalud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.centrosalud.centrosalud.model.Paciente;
import com.centrosalud.centrosalud.repository.PacienteRepository;

@Service
public class PacienteServiceImpl implements PacienteService{

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> getAllPacientes(){
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> getPacienteById(Long id){
        return pacienteRepository.findById(id);
    }


    @Override
    public Paciente createPaciente(Paciente paciente)
    {
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente updatePaciente(Long id, Paciente paciente)
    {
        if(pacienteRepository.existsById(id))
        {
            paciente.setIdpac(id);
            return pacienteRepository.save(paciente);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void deletePaciente(Long id)
    {
        pacienteRepository.deleteById(id);
    }
    
}

package com.CentroMedico.CentroMedico.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CentroMedico.CentroMedico.model.ConsultaMedica;
import com.CentroMedico.CentroMedico.model.Paciente;
import com.CentroMedico.CentroMedico.model.Profesional;

public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedica, Long>{
    Optional<ConsultaMedica> findByFechaconsultaAndHorarioAndProfesionalAndPaciente(
        LocalDate fechaconsulta, 
        LocalTime horario, 
        Profesional profesional, 
        Paciente paciente
    );
    
}

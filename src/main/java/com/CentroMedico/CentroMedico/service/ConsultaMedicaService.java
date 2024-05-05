package com.CentroMedico.CentroMedico.service;

import java.util.List;
import java.util.Optional;

import com.CentroMedico.CentroMedico.model.ConsultaMedica;

public interface ConsultaMedicaService {
    List<ConsultaMedica> getAllConsultaMedica();
    Optional<ConsultaMedica> getConsultaMedicaById(Long id);
    ConsultaMedica createConsultaMedica(ConsultaMedica consulta);
    void deleteConsultaMedica(Long id);
    Optional<ConsultaMedica> findExistingConsulta(ConsultaMedica consultaMedica);
}

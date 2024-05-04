package com.centrosalud.centrosalud.service;

import java.util.List;
import java.util.Optional;

import com.centrosalud.centrosalud.model.ConsultaMedica;

public interface ConsultaMedicaService {
    List<ConsultaMedica> getAllConsultaMedica();
    Optional<ConsultaMedica> getConsultaMedicaById(Long id);
    ConsultaMedica createConsultaMedica(ConsultaMedica consulta);
    void deleteConsultaMedica(Long id);
}

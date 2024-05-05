package com.CentroMedico.CentroMedico.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.CentroMedico.CentroMedico.model.Paciente;
import com.CentroMedico.CentroMedico.repository.PacienteRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PacienteServiceTest {
    
    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    public void guardarPacienteTest(){
        Paciente pac = new Paciente();
        pac.setNombres("Wayne Smith");
        Paciente rep = pacienteRepository.save(pac);
        assertNotNull(rep);
        assertNotNull(rep.getIdpac());
        assertEquals("Wayne Smith", rep.getNombres());
    }

    @Test
    public void eliminarPacienteTest(){
        Paciente pac = new Paciente();
        pac.setNombres("Wayne Smith");
        Paciente pacienteGuardado = pacienteRepository.save(pac);
        assertTrue(pacienteRepository.existsById(pacienteGuardado.getIdpac()));
        pacienteRepository.deleteById(pacienteGuardado.getIdpac());
        assertFalse(pacienteRepository.existsById(pacienteGuardado.getIdpac()));
    }

}

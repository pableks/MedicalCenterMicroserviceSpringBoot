package com.centrosalud.centrosalud.repository;

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

import com.centrosalud.centrosalud.model.Paciente;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PacienteServiceTest {
    
    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    public void guardarPacienteTest(){
        Paciente pac = new Paciente();
        pac.setNombres("Mark York");
        Paciente ret = pacienteRepository.save(pac);
        assertNotNull(ret);
        assertNotNull(ret.getIdpac());
        assertEquals("Mark York", ret.getNombres());
    }

    @Test
    public void eliminarPacienteTest(){
        Paciente pac = new Paciente();
        pac.setNombres("Mark York");
        Paciente pacienteGuardado = pacienteRepository.save(pac);
        assertTrue(pacienteRepository.existsById(pacienteGuardado.getIdpac()));
        pacienteRepository.deleteById(pacienteGuardado.getIdpac());
        assertFalse(pacienteRepository.existsById(pacienteGuardado.getIdpac()));
    }

}

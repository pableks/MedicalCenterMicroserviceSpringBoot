package com.CentroMedico.CentroMedico.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.CentroMedico.CentroMedico.controller.PacienteController;
import com.CentroMedico.CentroMedico.model.Paciente;
import com.CentroMedico.CentroMedico.service.ConsultaMedicaService;
import com.CentroMedico.CentroMedico.service.EspecialidadService;
import com.CentroMedico.CentroMedico.service.PacienteService;
import com.CentroMedico.CentroMedico.service.ProfesionalService;

import java.util.List;

@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;
    @MockBean
    private ProfesionalService profesionalService;
    @MockBean
    private EspecialidadService especialidadService;
    @MockBean
    private ConsultaMedicaService consultaMedicaService;

    @Test
    public void obtenerPacientesTest() throws Exception {
        Paciente paciente1 = new Paciente();
        paciente1.setNombres("Juan Perez");
        paciente1.setIdpac(1L);
        List<Paciente> pacientes = List.of(paciente1);

        when(pacienteService.getAllPacientes()).thenReturn(pacientes);

        mockMvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['_embedded'].pacienteList[0].nombres").value("Juan Perez"));
    }
}
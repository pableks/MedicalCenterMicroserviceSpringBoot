package com.centrosalud.centrosalud.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.centrosalud.centrosalud.model.Profesional;
import com.centrosalud.centrosalud.repository.ProfesionalRepository;


@ExtendWith(MockitoExtension.class)
public class ProfesionalServiceTest {

    @InjectMocks
    private ProfesionalServiceImpl profesionalService;

    @Mock
    private ProfesionalRepository profesionalRepositoryMock;


    @Test
    public void guardarProfesionalTest(){
        Profesional pro = new Profesional();
        pro.setNombres("Julia Montes");
        when(profesionalRepositoryMock.save(any())).thenReturn(pro);
     
        Profesional ret = profesionalService.createProfesional(pro);
        assertEquals("Julia Montes", ret.getNombres());
    }

    @Test
    public void eliminarProfesionalTest(){
        Profesional proeli = new Profesional();
        proeli.setIdprof(1L);
        proeli.setNombres("Raul Carrasco");

        profesionalService.deleteProfesional(proeli.getIdprof());
    }

}

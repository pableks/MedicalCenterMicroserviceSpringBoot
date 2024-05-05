package com.CentroMedico.CentroMedico.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.CentroMedico.CentroMedico.model.ConsultaMedica;
import com.CentroMedico.CentroMedico.model.Especialidad;
import com.CentroMedico.CentroMedico.model.Paciente;
import com.CentroMedico.CentroMedico.model.Profesional;
import com.CentroMedico.CentroMedico.service.ConsultaMedicaService;
import com.CentroMedico.CentroMedico.service.EspecialidadService;
import com.CentroMedico.CentroMedico.service.PacienteService;
import com.CentroMedico.CentroMedico.service.ProfesionalService;

import io.micrometer.core.ipc.http.HttpSender.Response;

import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private static final Logger log = LoggerFactory.getLogger(PacienteController.class);

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private ConsultaMedicaService consultaMedicaService;

    @GetMapping
    public CollectionModel<EntityModel<Paciente>> getAllPacientes() {
        List<Paciente> pacientes = pacienteService.getAllPacientes();
        log.info("GET /pacientes");
        log.info("Lista de todos los pacientes");

        List<EntityModel<Paciente>> pacienteResources = pacientes.stream()
                .map(paciente -> EntityModel.of(paciente,
                        WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(this.getClass()).getPacienteById(paciente.getIdpac()))
                                .withSelfRel()))
                .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPacientes());
        CollectionModel<EntityModel<Paciente>> resources = CollectionModel.of(pacienteResources,
                linkTo.withRel("pacientes"));
        return resources;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Paciente>> getPacienteById(@PathVariable Long id) {
        Optional<Paciente> optionalPaciente = pacienteService.getPacienteById(id);

        if (optionalPaciente.isEmpty()) {
            log.error("No se encontró el paciente con ID {}", id);
            throw new ResourceNotFoundException("No se encontró el paciente con ID " + id);
        }

        Paciente paciente = optionalPaciente.get();
        EntityModel<Paciente> pacienteModel = EntityModel.of(paciente);
        pacienteModel.add(linkTo(methodOn(this.getClass()).getPacienteById(id)).withSelfRel());

        return ResponseEntity.ok(pacienteModel);
    }

    @PostMapping
    public ResponseEntity<Object> createPaciente(@RequestBody Paciente paciente) {
        Paciente nuevoPaciente = pacienteService.createPaciente(paciente);
        if (nuevoPaciente == null) {
            log.error("Error al crear el paciente {}", paciente);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error al crear el paciente"));
        }

        EntityModel<Paciente> pacienteModel = EntityModel.of(nuevoPaciente);
        pacienteModel.add(linkTo(methodOn(this.getClass()).getPacienteById(nuevoPaciente.getIdpac())).withSelfRel());

        return ResponseEntity.ok(pacienteModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePaciente(@PathVariable Long id, @RequestBody Paciente paciente) {
        Paciente pacienteActualizado = pacienteService.updatePaciente(id, paciente);
        if (pacienteActualizado == null) {
            log.error("No se encontró el paciente con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No se encontró el paciente con ID " + id));
        }

        Link selfLink = linkTo(methodOn(this.getClass()).getPacienteById(id)).withSelfRel();
        EntityModel<Paciente> pacienteModel = EntityModel.of(pacienteActualizado, selfLink);

        return ResponseEntity.ok(pacienteModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePaciente(@PathVariable Long id) {
        Optional<Paciente> pac = pacienteService.getPacienteById(id);
        if (pac.isEmpty()) {
            log.error("El paciente no existe", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("El usuario que quiere eliminar no existe " + id));
        }
        log.info("Se ha eliminado el paciente");
        pacienteService.deletePaciente(id);
        return ResponseEntity.ok(pac);

    }

    static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}

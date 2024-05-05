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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/consultas")
public class ConsultaMedicaController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaMedicaController.class);
    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private ConsultaMedicaService consultaMedicaService;

    @GetMapping
    public CollectionModel<EntityModel<ConsultaMedica>> getAllConsultaMedicas() {
        List<ConsultaMedica> consultaMedicas = consultaMedicaService.getAllConsultaMedica();

        List<EntityModel<ConsultaMedica>> consultaMedicaResources = consultaMedicas.stream()
                .map((ConsultaMedica consultaMedica) -> EntityModel.of(consultaMedica,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                                .getConsultaMedicaById(consultaMedica.getIdcon())).withSelfRel()))
                .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllConsultaMedicas());
        CollectionModel<EntityModel<ConsultaMedica>> resources = CollectionModel.of(consultaMedicaResources,
                linkTo.withRel("consultas"));

        return resources;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConsultaMedica(@PathVariable Long id) {
        Optional<ConsultaMedica> optionalConsultaMedica = consultaMedicaService.getConsultaMedicaById(id);
        if (!optionalConsultaMedica.isPresent()) {
            log.error("Consulta médica no encontrada con ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Consulta médica no encontrada con ID: " + id);
        }
        consultaMedicaService.deleteConsultaMedica(id);
        return ResponseEntity.ok("Consulta médica eliminada con éxito.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Object>> getConsultaMedicaById(@PathVariable Long id) {
        Optional<ConsultaMedica> optionalConsultaMedica = consultaMedicaService.getConsultaMedicaById(id);
        if (!optionalConsultaMedica.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(EntityModel.of("Consulta médica no encontrada con ID: " + id));
        }

        ConsultaMedica consultaMedica = optionalConsultaMedica.get();
        Long patientId = consultaMedica.getPaciente().getIdpac(); // Assuming there is a getPaciente method

        Optional<Paciente> optionalPaciente = pacienteService.getPacienteById(patientId);
        if (!optionalPaciente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(EntityModel.of("Paciente no encontrado con ID: " + patientId));
        }

        Paciente paciente = optionalPaciente.get();

        // Create a simplified patient map with only necessary fields
        Map<String, Object> simplePaciente = new HashMap<>();
        simplePaciente.put("idpac", paciente.getIdpac());
        simplePaciente.put("rut", paciente.getRut());
        simplePaciente.put("nombres", paciente.getNombres());
        simplePaciente.put("apellidos", paciente.getApellidos());

        // Adding patient details directly to the response
        Map<String, Object> response = new HashMap<>();
        response.put("consultaMedica", consultaMedica);
        response.put("paciente", simplePaciente);

        Link selfLink = linkTo(methodOn(this.getClass()).getConsultaMedicaById(id)).withSelfRel();
        Link patientLink = linkTo(methodOn(PacienteController.class).getPacienteById(patientId)).withRel("paciente");

        return ResponseEntity.ok(EntityModel.of(response, selfLink, patientLink));
    }

    @PostMapping
    public ResponseEntity<?> createConsultas(@RequestBody ConsultaMedica consultaMedicaRequest) {
        validateProfesional(consultaMedicaRequest.getProfesional());
        validatePaciente(consultaMedicaRequest.getPaciente());

        Optional<ConsultaMedica> existingConsulta = consultaMedicaService.findExistingConsulta(consultaMedicaRequest);
        if (existingConsulta.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La consulta médica ya existe con detalles similares.");
        }

        // Ensure full details are fetched for Profesional and Paciente
        Profesional fullProfesional = profesionalService
                .getProfesionalById(consultaMedicaRequest.getProfesional().getIdprof())
                .orElseThrow(() -> new ResourceNotFoundException("No se pudo encontrar el profesional con ID: "
                        + consultaMedicaRequest.getProfesional().getIdprof()));
        Paciente fullPaciente = pacienteService.getPacienteById(consultaMedicaRequest.getPaciente().getIdpac())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se pudo encontrar el paciente con ID: " + consultaMedicaRequest.getPaciente().getIdpac()));

        if (fullProfesional == null || fullPaciente == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profesional o Paciente no válido.");
        }

        // Set full entities to the consultaMedicaRequest
        consultaMedicaRequest.setProfesional(fullProfesional);
        consultaMedicaRequest.setPaciente(fullPaciente);

        ConsultaMedica createdConsultaMedica = consultaMedicaService.createConsultaMedica(consultaMedicaRequest);

        // Build response with full entity details
        return buildCreatedResponse(createdConsultaMedica);
    }

    private void validatePaciente(Paciente paciente) {
        if (pacienteService.getAllPacientes().stream()
                .noneMatch(p -> p.getRut().equals(paciente.getRut()))) {
            log.error("El paciente no existe {}", paciente);
            throw new ResourceNotFoundException("El paciente no existe con RUT " + paciente.getRut());
        }
    }

    private void validateProfesional(Profesional profesional) {
        if (profesional == null || profesional.getIdprof() == null) {
            throw new IllegalArgumentException("Invalid professional provided");
        }
        Optional<Profesional> existingProfesional = profesionalService.getProfesionalById(profesional.getIdprof());
        if (!existingProfesional.isPresent()) {
            log.error("El profesional con ID {} no existe", profesional.getIdprof());
            throw new ResourceNotFoundException("El profesional con ID " + profesional.getIdprof() + " no existe");
        }
    }

    private ResponseEntity<EntityModel<Object>> buildCreatedResponse(ConsultaMedica consultaMedica) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> simplePaciente = new HashMap<>();

        Paciente paciente = consultaMedica.getPaciente();
        simplePaciente.put("idpac", paciente.getIdpac());
        simplePaciente.put("rut", paciente.getRut());
        simplePaciente.put("nombres", paciente.getNombres());
        simplePaciente.put("apellidos", paciente.getApellidos());

        response.put("consultaMedica", consultaMedica);
        response.put("paciente", simplePaciente);

        Link selfLink = linkTo(methodOn(this.getClass()).getConsultaMedicaById(consultaMedica.getIdcon()))
                .withSelfRel();
        Link patientLink = linkTo(methodOn(PacienteController.class).getPacienteById(paciente.getIdpac()))
                .withRel("paciente");

        EntityModel<Object> resource = EntityModel.of(response, selfLink, patientLink);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(consultaMedica.getIdcon()).toUri();
        return ResponseEntity.created(location).body(resource);
    }

}

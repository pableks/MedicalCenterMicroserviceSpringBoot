package com.CentroMedico.CentroMedico.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/profesionales")
public class ProfesionalController {

    private static final Logger log = LoggerFactory.getLogger(ProfesionalController.class);

    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping()
    public CollectionModel<EntityModel<Profesional>> getAllProfesionales() {
        List<Profesional> profesionales = profesionalService.getAllProfesionales();
        List<EntityModel<Profesional>> profesionalResources = profesionales.stream()
                .map(profesional -> {
                    Long id = profesional.getIdprof(); // Obtener el ID del profesional
                    Link selfLink = linkTo(methodOn(this.getClass()).getProfesionalById(id)).withSelfRel();
                    return EntityModel.of(profesional, selfLink);
                })
                .collect(Collectors.toList());

        Link linkTo = linkTo(methodOn(this.getClass()).getAllProfesionales()).withSelfRel();
        return CollectionModel.of(profesionalResources, linkTo.withRel("profesionales"));
    }

    @GetMapping("/{id}")
    public EntityModel<Profesional> getProfesionalById(@PathVariable Long id) {
        Optional<Profesional> optionalProfesional = profesionalService.getProfesionalById(id);

        if (optionalProfesional.isPresent()) {
            Profesional profesional = optionalProfesional.get();

            Link selfLink = linkTo(methodOn(this.getClass()).getProfesionalById(id)).withSelfRel();
            return EntityModel.of(profesional, selfLink);
        } else {
            throw new ResourceNotFoundException("Profesional no encontrado con ID: " + id);
        }
    }

    @PostMapping("/profesionales")
    public ResponseEntity<Object> createProfesional(@RequestBody Profesional profesional) {
        List<Profesional> listaprofesional = profesionalService.getAllProfesionales();
        for (Profesional p : listaprofesional) {
            if (p.getRut().equals(profesional.getRut())) {
                log.error("El profesional ya existe con el RUT {}", profesional.getRut());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("El profesional ya existe con el ID " + profesional.getRut()));
            }
        }

        Profesional createdProfesional = profesionalService.createProfesional(profesional);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdProfesional.getIdprof()).toUri();
        return ResponseEntity.created(location).body(createdProfesional);
    }




    @GetMapping("/especialidades/{id}")
    public EntityModel<Especialidad> getEspecialidadById(@PathVariable Long id) {
        Optional<Especialidad> optionalEspecialidad = especialidadService.getEspecialidadById(id);

        if (optionalEspecialidad.isPresent()) {
            Especialidad especialidad = optionalEspecialidad.get();

            Link selfLink = linkTo(methodOn(this.getClass()).getEspecialidadById(id)).withSelfRel();
            return EntityModel.of(especialidad, selfLink);
        } else {
            throw new ResourceNotFoundException("Especialidad no encontrada con ID: " + id);
        }
    }

    @GetMapping("/especialidades")
    public CollectionModel<EntityModel<Especialidad>> getAllEspecialidades() {
        List<Especialidad> especialidades = especialidadService.getAllEspecialidad();
        List<EntityModel<Especialidad>> especialidadResources = especialidades.stream()
                .map(especialidad -> {
                    Link link = linkTo(methodOn(this.getClass()).getEspecialidadById(especialidad.getIdes()))
                            .withSelfRel();
                    return EntityModel.of(especialidad, link);
                })
                .collect(Collectors.toList());

        Link linkTo = linkTo(methodOn(this.getClass()).getAllEspecialidades()).withSelfRel();
        return CollectionModel.of(especialidadResources, linkTo.withRel("especialidades"));
    }

    @PostMapping("/especialidades")
    public ResponseEntity<Object> createEspecialidad(@RequestBody Especialidad especialidad) {
        List<Especialidad> listaEspecialidades = especialidadService.getAllEspecialidad();
        for (Especialidad e : listaEspecialidades) {
            if (e.getNombre().equals(especialidad.getNombre())) {
                log.error("La especialidad ya existe {}", especialidad.getNombre());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("No se puede crear la especialidad " + especialidad.getNombre()));
            }
        }

        Especialidad nuevaEspecialidad = especialidadService.createEspecialidad(especialidad);
        if (nuevaEspecialidad == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al crear la especialidad " + especialidad.getNombre()));
        }

        Link linkToNewEspecialidad = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).createEspecialidad(especialidad))
                .withSelfRel();

        return ResponseEntity.created(linkToNewEspecialidad.toUri()).body(nuevaEspecialidad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Profesional>> updateProfesional(@PathVariable Long id,
            @RequestBody Profesional profesionalDetails) {
        Optional<Profesional> optionalProfesional = profesionalService.getProfesionalById(id);

        if (!optionalProfesional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Profesional profesional = optionalProfesional.get();
        profesional.setNombres(profesionalDetails.getNombres());
        profesional.setApellidos(profesionalDetails.getApellidos());
      
        profesional = profesionalService.updateProfesional(profesional); // Asegúrate de que el servicio devuelve el
                                                                         // profesional actualizado

        Link selfLink = linkTo(methodOn(this.getClass()).getProfesionalById(id)).withSelfRel();
        EntityModel<Profesional> resource = EntityModel.of(profesional, selfLink);

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
public ResponseEntity<?> deleteProfesional(@PathVariable Long id) {
    Optional<Profesional> optionalProfesional = profesionalService.getProfesionalById(id);

    if (!optionalProfesional.isPresent()) {
        log.error("Attempt to delete non-existing professional with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Professional not found with ID: " + id));
    }

    try {
        profesionalService.deleteProfesional(id);
        log.info("Professional deleted successfully with ID: {}", id);
        return ResponseEntity.ok().body(new ErrorResponse("Professional successfully deleted."));
    } catch (DataIntegrityViolationException e) {
        log.error("Failed to delete professional with ID: {} due to data integrity issues.", id);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Cannot delete professional as it is linked with other records."));
    }
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

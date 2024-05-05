package com.CentroMedico.CentroMedico.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.CentroMedico.CentroMedico.model.ConsultaMedica;
import com.CentroMedico.CentroMedico.model.Paciente;
import com.CentroMedico.CentroMedico.repository.ConsultaMedicaRepository;
import com.CentroMedico.CentroMedico.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class HistorialMedicoController {

    private final PacienteRepository pacienteRepository;
    private final ConsultaMedicaRepository consultaMedicaRepository; // Assuming you have a repository for ConsultaMedica

    public HistorialMedicoController(PacienteRepository pacienteRepository, ConsultaMedicaRepository consultaMedicaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.consultaMedicaRepository = consultaMedicaRepository;
    }

    @GetMapping("/historial-medico/{id}")
    public ResponseEntity<CollectionModel<EntityModel<ConsultaMedica>>> getConsultasMedicas(@PathVariable Long id) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (pacienteOptional.isPresent()) {
            Paciente paciente = pacienteOptional.get();
            List<ConsultaMedica> consultasMedicas = paciente.getConsultasMedicas();

            List<EntityModel<ConsultaMedica>> consultaMedicaResources = consultasMedicas.stream()
                    .map(consultaMedica -> EntityModel.of(consultaMedica,
                            linkTo(methodOn(this.getClass()).getConsultaMedica(consultaMedica.getIdcon())).withSelfRel()))
                    .collect(Collectors.toList());

            Link link = linkTo(methodOn(this.getClass()).getConsultasMedicas(id)).withSelfRel();
            CollectionModel<EntityModel<ConsultaMedica>> resources = CollectionModel.of(consultaMedicaResources, link);

            return ResponseEntity.ok(resources);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/consulta-medica/{id}")
    public ResponseEntity<EntityModel<ConsultaMedica>> getConsultaMedica(@PathVariable Long id) {
        Optional<ConsultaMedica> consultaMedicaOptional = consultaMedicaRepository.findById(id);
        if (consultaMedicaOptional.isPresent()) {
            ConsultaMedica consultaMedica = consultaMedicaOptional.get();
            EntityModel<ConsultaMedica> resource = EntityModel.of(consultaMedica,
                    linkTo(methodOn(this.getClass()).getConsultaMedica(id)).withSelfRel());

            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
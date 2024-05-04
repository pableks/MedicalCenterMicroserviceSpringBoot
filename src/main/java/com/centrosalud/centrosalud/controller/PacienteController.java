package com.centrosalud.centrosalud.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.centrosalud.centrosalud.model.ConsultaMedica;
import com.centrosalud.centrosalud.model.Especialidad;
import com.centrosalud.centrosalud.model.Paciente;
import com.centrosalud.centrosalud.model.Profesional;
import com.centrosalud.centrosalud.service.ConsultaMedicaService;
import com.centrosalud.centrosalud.service.EspecialidadService;
import com.centrosalud.centrosalud.service.PacienteService;
import com.centrosalud.centrosalud.service.ProfesionalService;

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
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacienteById(paciente.getIdpac())).withSelfRel()
            ))
            .collect(Collectors.toList());
    
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPacientes());
        CollectionModel<EntityModel<Paciente>> resources = CollectionModel.of(pacienteResources, linkTo.withRel("pacientes"));
        return resources;
    }



    /*@GetMapping
    public List<Paciente> getAllPacientes() {
        log.info("GET /pacientes");
        log.info("Retornando todos los pacientes");
        return pacienteService.getAllPacientes();
    }*/

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
                Link link = linkTo(methodOn(this.getClass()).getEspecialidadById(especialidad.getIdes())).withSelfRel();
                return EntityModel.of(especialidad, link);
            })
            .collect(Collectors.toList());

        Link linkTo = linkTo(methodOn(this.getClass()).getAllEspecialidades()).withSelfRel();
        return CollectionModel.of(especialidadResources, linkTo.withRel("especialidades"));
    }

    /*@GetMapping("/especialidades")
    public List<Especialidad> getAllEspecialidades() {
        log.info("GET /especialidades");
        log.info("Retornando todas las especialidades");
        return especialidadService.getAllEspecialidad();
    }*/
    

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

        Link linkToNewEspecialidad = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).createEspecialidad(especialidad))
            .withSelfRel();

        return ResponseEntity.created(linkToNewEspecialidad.toUri()).body(nuevaEspecialidad);
    }


    /*@PostMapping("/especialidades")
    public ResponseEntity<Object> createEspecialidad(@RequestBody Especialidad especialidad) {
        List<Especialidad> listaespecial = especialidadService.getAllEspecialidad();
        for(Especialidad e : listaespecial)
        {
            if(e.getNombre().equals(especialidad.getNombre())){
                log.error("La especialidad ya existe {}", especialidad.getNombre());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se puede crear la especialidad " + especialidad.getNombre()));   
            }
        }
        return ResponseEntity.ok(especialidadService.createEspecialidad(especialidad));
    }*/


   /*  @GetMapping("/profesionales")
    public List<Profesional> getAllProfesionales() {
        log.info("GET /profesionales");
        log.info("Retornando todos los profeisonales");
        return profesionalService.getAllProfesionales();
    }*/
    


    @GetMapping("/profesionales")
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

    @GetMapping("/profesionales/{id}")
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

    
   /* @PostMapping("/profesionales")
    public ResponseEntity<Object> createProfesional(@RequestBody Profesional profesional) 
    {
        List<Profesional> listaprofesional = profesionalService.getAllProfesionales();
        for(Profesional p : listaprofesional)
        {
            if(p.getRut().equals(profesional.getRut()))
            {
                log.error("El profesional ya existe ID {}", profesional);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("El profesional ya existe " + profesional));

            }

        }

        return ResponseEntity.ok(profesionalService.createProfesional(profesional));
    }*/

    @PostMapping("/profesionales")
    public ResponseEntity<Object> createProfesional(@RequestBody Profesional profesional) {
        List<Profesional> listaprofesional = profesionalService.getAllProfesionales();
        for (Profesional p : listaprofesional) {
            if (p.getRut().equals(profesional.getRut())) {
                log.error("El profesional ya existe con el RUT {}", profesional.getRut());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("El profesional ya existe con el ID " + profesional.getRut()));
            }
        }

        Profesional createdProfesional = profesionalService.createProfesional(profesional);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                            .buildAndExpand(createdProfesional.getIdprof()).toUri();
        return ResponseEntity.created(location).body(createdProfesional);
    }


    @GetMapping("/consultas")
    public CollectionModel<EntityModel<ConsultaMedica>> getAllConsultaMedicas() {
        List<ConsultaMedica> consultaMedicas = consultaMedicaService.getAllConsultaMedica();

        List<EntityModel<ConsultaMedica>> consultaMedicaResources = consultaMedicas.stream()
                .map((ConsultaMedica consultaMedica) -> EntityModel.of(consultaMedica,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getConsultaMedicaById(consultaMedica.getIdcon())).withSelfRel()
                ))
                .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllConsultaMedicas());
        CollectionModel<EntityModel<ConsultaMedica>> resources = CollectionModel.of(consultaMedicaResources, linkTo.withRel("consultas"));

        return resources;
    }

    @GetMapping("/consultas/{id}")
    public EntityModel<ConsultaMedica> getConsultaMedicaById(@PathVariable Long id) {
        Optional<ConsultaMedica> optionalConsultaMedica = consultaMedicaService.getConsultaMedicaById(id);

        if (optionalConsultaMedica.isPresent()) {
            ConsultaMedica consultaMedica = optionalConsultaMedica.get();

            Link selfLink = linkTo(methodOn(this.getClass()).getConsultaMedicaById(id)).withSelfRel();
            return EntityModel.of(consultaMedica, selfLink);
        } else {
            throw new ResourceNotFoundException("Consulta médica no encontrada con ID: " + id);
        }
    }


    /*@GetMapping("/consultas")
    public List<ConsultaMedica> getAllConsultaMedicas() {
        log.info("GET /consultas medicas");
        log.info("Retornando todas las consultas medicas");
        return consultaMedicaService.getAllConsultaMedica();
    }*/


    @PostMapping("/consultas")
    public ResponseEntity<EntityModel<ConsultaMedica>> createConsultas(@RequestBody ConsultaMedica consultaMedica) {
        List<Profesional> listaprofesional = profesionalService.getAllProfesionales();
        List<Paciente> listapaciente = pacienteService.getAllPacientes();
        boolean existeprof = false;
        boolean existepac = false;

        for (Profesional p : listaprofesional) {
            if (p.getRut().equals(consultaMedica.getProfesional().getRut())) {
                existeprof = true;
            }
        }

        if (!existeprof) {
            log.error("El profesional no existe {}", consultaMedica.getProfesional());
            throw new ResourceNotFoundException("El profesional no existe con ID " + consultaMedica.getProfesional().getRut());
        }

        for (Paciente pac : listapaciente) {
            if (pac.getRut().equals(consultaMedica.getPaciente().getRut())) {
                existepac = true;
            }
        }

        if (!existepac) {
            log.error("El paciente no existe {}", consultaMedica.getPaciente());
            throw new ResourceNotFoundException("El paciente no existe con ID " + consultaMedica.getPaciente().getRut());
        }

        ConsultaMedica newConsultaMedica = consultaMedicaService.createConsultaMedica(consultaMedica);

        EntityModel<ConsultaMedica> consultaMedicaModel = EntityModel.of(newConsultaMedica);
        consultaMedicaModel.add(linkTo(methodOn(this.getClass()).getConsultaMedicaById(newConsultaMedica.getIdcon())).withSelfRel());

        return ResponseEntity.created(linkTo(methodOn(this.getClass()).getConsultaMedicaById(newConsultaMedica.getIdcon())).toUri())
                .body(consultaMedicaModel);
    }



   /* @PostMapping("/consultas")
    public ResponseEntity<Object> createConsultas(@RequestBody ConsultaMedica consultaMedica) 
    {
        List<Profesional> listaprofesional = profesionalService.getAllProfesionales();
        List<Paciente> listapaciente = pacienteService.getAllPacientes();
        boolean existeprof = false;
        boolean existepac = false;
        for(Profesional p : listaprofesional)
        {
            if(p.getRut().equals(consultaMedica.getProfesional().getRut()))
            {
                existeprof = true;
            }
        }

        if(!existeprof)
        {
            log.error("El profesional no existe {}", consultaMedica.getProfesional());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("El profesional no existe ID " + consultaMedica.getProfesional()));

        }

        for(Paciente pac : listapaciente)
        {
            if(pac.getRut().equals(consultaMedica.getPaciente().getRut()))
            {
                existepac = true;
            }

        }

        if(!existepac)
        {
            log.error("El paciente no existe {}", consultaMedica.getPaciente());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("El paciente no existe ID " + consultaMedica.getPaciente()));
        }

        return ResponseEntity.ok(consultaMedicaService.createConsultaMedica(consultaMedica));
    }*/
    
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


   /* @GetMapping("/{id}")
    public ResponseEntity<Object> getPacienteById(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        if(paciente.isEmpty())
        {
            log.error("No se encontró el paciente con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro el paciente con ID " + id));
        }

        return ResponseEntity.ok(paciente);
        
    }*/


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
    
    /*@PostMapping
    public ResponseEntity<Object> createPaciente(@RequestBody Paciente paciente) {
        Paciente pac = pacienteService.createPaciente(paciente);
        if(pac == null)
        {
            log.error("Error al crear el estudiante {}", pac);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error al crear el estudiante"));
        }
        return ResponseEntity.ok(pac);
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePaciente(@PathVariable Long id, @RequestBody Paciente paciente) {
        Paciente pacienteActualizado = pacienteService.updatePaciente(id, paciente);
        if (pacienteActualizado == null) {
            log.error("No se encontró el paciente con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontró el paciente con ID " + id));
        }

        Link selfLink = linkTo(methodOn(this.getClass()).getPacienteById(id)).withSelfRel();
        EntityModel<Paciente> pacienteModel = EntityModel.of(pacienteActualizado, selfLink);

        return ResponseEntity.ok(pacienteModel);
    }

    /*@PutMapping("/{id}")
    public Paciente updatePaciente(@PathVariable String id, @RequestBody Paciente paciente) {
        return pacienteService.updatePaciente(null, paciente);
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePaciente(@PathVariable Long id)
    {
        Optional<Paciente> pac = pacienteService.getPacienteById(id);
        if(pac.isEmpty())
        {
            log.error("El paciente no existe", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("El usuario que quiere eliminar no existe " + id));
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

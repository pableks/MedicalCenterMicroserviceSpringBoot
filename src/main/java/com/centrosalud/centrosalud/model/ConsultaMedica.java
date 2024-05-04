package com.centrosalud.centrosalud.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "consultamedica")
public class ConsultaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcon")
    private Long idcon;

    @Column(name = "fechaconsulta")
    private Date fechaconsulta;

    @Column(name = "horario")
    private Time horario;

    @ManyToOne
    @JoinColumn(name = "idprof")
    private Profesional profesional;

    @ManyToOne
    @JoinColumn(name = "idpac")
    private Paciente paciente;

    @Column(name = "diagnostico")
    private String diagnostico;

    @Column(name = "tratamiento")
    private String tratamiento;

    // Getters and setters
    public Long getIdcon() {
        return idcon;
    }

    public void setIdcon(Long idcon) {
        this.idcon = idcon;
    }

    public Date getFechaconsulta() {
        return fechaconsulta;
    }

    public void setFechaconsulta(Date fechaconsulta) {
        this.fechaconsulta = fechaconsulta;
    }

    public Time getHorario() {
        return horario;
    }

    public void setHorario(Time horario) {
        this.horario = horario;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
}


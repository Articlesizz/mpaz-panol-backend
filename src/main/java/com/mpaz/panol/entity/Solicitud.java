package com.mpaz.panol.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_ticket", nullable = false, unique = true, length = 50)
    private String codigoTicket;

    @Column(name = "docente_nombre", nullable = false, length = 100)
    private String docenteNombre;

    @Column(name = "docente_email", nullable = false, length = 150)
    private String docenteEmail;

    @Column(name = "sala_espacio", nullable = false, length = 150)
    private String salaEspacio;

    @Column(name = "fecha_uso", nullable = false, length = 50)
    private String fechaUso;

    @Column(name = "hora_inicio", nullable = false, length = 10)
    private String horaInicio;

    @Column(name = "hora_devolucion", nullable = false, length = 10)
    private String horaDevolucion;

    @Column(name = "equipos_resumen", nullable = false, columnDefinition = "TEXT")
    private String equiposResumen;

    @Column(name = "horas_anticipacion", nullable = false)
    private Integer horasAnticipacion = 5;

    @Column(name = "cumple_anticipacion", nullable = false)
    private Boolean cumpleAnticipacion = true;

    @Column(name = "estado_ticket", nullable = false, length = 50)
    private String estadoTicket = "Pendiente";

    @Column(name = "justificacion_rechazo", columnDefinition = "TEXT")
    private String justificacionRechazo;

    @Column(name = "fecha_creacion", nullable = false, length = 50)
    private String fechaCreacion;

    public Solicitud() {
    }

    public Solicitud(Long id, String codigoTicket, String docenteNombre, String docenteEmail, String salaEspacio, String fechaUso, String horaInicio, String horaDevolucion, String equiposResumen, Integer horasAnticipacion, Boolean cumpleAnticipacion, String estadoTicket, String justificacionRechazo, String fechaCreacion) {
        this.id = id;
        this.codigoTicket = codigoTicket;
        this.docenteNombre = docenteNombre;
        this.docenteEmail = docenteEmail;
        this.salaEspacio = salaEspacio;
        this.fechaUso = fechaUso;
        this.horaInicio = horaInicio;
        this.horaDevolucion = horaDevolucion;
        this.equiposResumen = equiposResumen;
        this.horasAnticipacion = horasAnticipacion;
        this.cumpleAnticipacion = cumpleAnticipacion;
        this.estadoTicket = estadoTicket;
        this.justificacionRechazo = justificacionRechazo;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoTicket() {
        return codigoTicket;
    }

    public void setCodigoTicket(String codigoTicket) {
        this.codigoTicket = codigoTicket;
    }

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    public String getDocenteEmail() {
        return docenteEmail;
    }

    public void setDocenteEmail(String docenteEmail) {
        this.docenteEmail = docenteEmail;
    }

    public String getSalaEspacio() {
        return salaEspacio;
    }

    public void setSalaEspacio(String salaEspacio) {
        this.salaEspacio = salaEspacio;
    }

    public String getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(String fechaUso) {
        this.fechaUso = fechaUso;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraDevolucion() {
        return horaDevolucion;
    }

    public void setHoraDevolucion(String horaDevolucion) {
        this.horaDevolucion = horaDevolucion;
    }

    public String getEquiposResumen() {
        return equiposResumen;
    }

    public void setEquiposResumen(String equiposResumen) {
        this.equiposResumen = equiposResumen;
    }

    public Integer getHorasAnticipacion() {
        return horasAnticipacion;
    }

    public void setHorasAnticipacion(Integer horasAnticipacion) {
        this.horasAnticipacion = horasAnticipacion;
    }

    public Boolean getCumpleAnticipacion() {
        return cumpleAnticipacion;
    }

    public void setCumpleAnticipacion(Boolean cumpleAnticipacion) {
        this.cumpleAnticipacion = cumpleAnticipacion;
    }

    public String getEstadoTicket() {
        return estadoTicket;
    }

    public void setEstadoTicket(String estadoTicket) {
        this.estadoTicket = estadoTicket;
    }

    public String getJustificacionRechazo() {
        return justificacionRechazo;
    }

    public void setJustificacionRechazo(String justificacionRechazo) {
        this.justificacionRechazo = justificacionRechazo;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

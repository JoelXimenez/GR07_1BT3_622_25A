package org.apptrueque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "remitente_email", nullable = false, length = 100)
    private String remitenteEmail;

    @Column(name = "destinatario_email", nullable = false, length = 100)
    private String destinatarioEmail;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    // Constructor vacío requerido por JPA
    public Mensaje() {
        this.fechaHora = LocalDateTime.now(); // inicialización automática
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemitenteEmail() {
        return remitenteEmail;
    }

    public void setRemitenteEmail(String remitenteEmail) {
        this.remitenteEmail = remitenteEmail;
    }

    public String getDestinatarioEmail() {
        return destinatarioEmail;
    }

    public void setDestinatarioEmail(String destinatarioEmail) {
        this.destinatarioEmail = destinatarioEmail;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}

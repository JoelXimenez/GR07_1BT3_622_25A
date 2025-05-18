package org.apptrueque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    private String usuarioDestino;

    private String usuarioRemitente;

    private LocalDateTime fecha = LocalDateTime.now();

    // Getters y Setters
    public Long getId() { return id; }

    public String getMensaje() { return mensaje; }

    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getUsuarioDestino() { return usuarioDestino; }

    public void setUsuarioDestino(String usuarioDestino) { this.usuarioDestino = usuarioDestino; }

    public String getUsuarioRemitente() { return usuarioRemitente; }

    public void setUsuarioRemitente(String usuarioRemitente) { this.usuarioRemitente = usuarioRemitente; }

    public LocalDateTime getFecha() { return fecha; }

    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
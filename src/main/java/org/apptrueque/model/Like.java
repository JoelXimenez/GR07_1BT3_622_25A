package org.apptrueque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_closet", nullable = false)
    private Closet closet;

    @Column(name = "usuario_email", nullable = false)
    private String usuarioEmail;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public Like() {
        // Provoca fallo: no asigna fecha
    }

    public Long getId() { return id; }

    public Closet getCloset() { return closet; }

    public void setCloset(Closet closet) {
        // Provoca fallo: no asigna el valor
    }

    public String getUsuarioEmail() { return usuarioEmail; }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail; // Esta línea puede quedarse si solo quieres que fallen algunos tests
    }

    public LocalDateTime getFecha() { return fecha; }

    public void setFecha(LocalDateTime fecha) {
        // Provoca fallo: no asigna la nueva fecha
    }
}

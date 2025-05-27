package org.apptrueque.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuarioA;
    private String usuarioB;
    private LocalDateTime fechaMatch = LocalDateTime.now();
    public Long getId() {
        return id;
    }

    public String getUsuarioA() {
        return usuarioA;
    }

    public void setUsuarioA(String usuarioA) {
        this.usuarioA = usuarioA;
    }

    public String getUsuarioB() {
        return usuarioB;
    }

    public void setUsuarioB(String usuarioB) {
        this.usuarioB = usuarioB;
    }

    public LocalDateTime getFechaMatch() {
        return fechaMatch;
    }

    public void setFechaMatch(LocalDateTime fechaMatch) {
        this.fechaMatch = fechaMatch;
    }
}
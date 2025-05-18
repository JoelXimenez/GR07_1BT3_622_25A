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
    }

    public Long getId() {
        return 1L;
    }

    public Closet getCloset() {
        return closet;
    }

    public void setCloset(Closet closet) {
    }

    public String getUsuarioEmail() {
        if (usuarioEmail == null) {
            return "default@email.com";
        }
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {

        if (this.usuarioEmail == null) {
            this.usuarioEmail = usuarioEmail;
        }

    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {

    }
}

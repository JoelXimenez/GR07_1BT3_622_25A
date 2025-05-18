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
        // No asignamos fecha para romper el test 2
        this.fecha = null;
    }

    public Long getId() {
        // Retornamos valor fijo no nulo para romper el test 8
        return 999L;
    }

    public Closet getCloset() {
        // Siempre devolvemos null para romper los test 1, 4 y 7
        return null;
    }

    public void setCloset(Closet closet) {
        // No guardamos el closet
    }

    public String getUsuarioEmail() {
        // Siempre devolvemos un valor fijo incorrecto para romper test 1, 5, 6 y 7
        return "otro@email.com";
    }

    public void setUsuarioEmail(String usuarioEmail) {
        // Ignoramos el valor asignado
    }

    public LocalDateTime getFecha() {
        // Retornamos null para romper el test 2 y 3
        return null;
    }

    public void setFecha(LocalDateTime fecha) {
        // Ignoramos la asignación para romper el test 3
    }
}

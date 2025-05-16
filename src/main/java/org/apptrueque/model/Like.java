package org.apptrueque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Closet closet;

    private LocalDateTime fecha;
    private boolean activo;

    // Constructor vacío requerido por JPA
    public Like() {
    }

    // Constructor principal (deberá implementar validaciones)
    public Like(Usuario usuario, Closet closet) {
        throw new UnsupportedOperationException("No implementado aún");
    }

    // Constructor con servicio de notificaciones
    public Like(Usuario usuario, Closet closet, NotificacionService notificacionService) {
        throw new UnsupportedOperationException("No implementado aún");
    }

    // Método para quitar like
    public boolean quitar() {
        throw new UnsupportedOperationException("No implementado aún");
    }

    // Método para verificar estado
    public boolean isActivo() {
        throw new UnsupportedOperationException("No implementado aún");
    }

    // Método estático para verificar compatibilidad de prendas
    public static boolean sonPrendasCompatibles(Prenda prenda1, Prenda prenda2) {
        throw new UnsupportedOperationException("No implementado aún");
    }

    // Getters básicos
    public Long getId() {
        throw new UnsupportedOperationException("No implementado aún");
    }

    public Usuario getUsuario() {
        throw new UnsupportedOperationException("No implementado aún");
    }

    public Closet getCloset() {
        throw new UnsupportedOperationException("No implementado aún");
    }

    public LocalDateTime getFecha() {
        throw new UnsupportedOperationException("No implementado aún");
    }
}
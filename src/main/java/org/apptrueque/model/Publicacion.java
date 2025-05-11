package org.apptrueque.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Publicacion {
    private final String id;
    private Usuario usuario;
    private Closet closet;
    private boolean activa;
    private LocalDateTime fechaPublicacion;

    public Publicacion(Usuario usuario, Closet closet) {
        this.id = UUID.randomUUID().toString();
        this.usuario = usuario;
        this.closet = closet;
        this.activa = true;
        this.fechaPublicacion = LocalDateTime.now();
    }

    public static List<Publicacion> filtrarPublicaciones(List<Publicacion> publicaciones, Usuario usuarioActual) {
        return List.of(new Publicacion(null, null));
    }


    public static List<Prenda> filtrarPorNombre(String nombre) {
        return List.of();
    }

    public static List<Prenda> filtrarPorCategoria(Closet closet, String categoria) {
        return List.of();
    }

    public Long getId() {
        return 0L;
    }

    public Usuario getUsuario() {
        return new Usuario("", "", "", "");
    }


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Closet getCloset() {
        return closet;
    }

    public void setCloset(Closet closet) {
        this.closet = closet;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publicacion that = (Publicacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

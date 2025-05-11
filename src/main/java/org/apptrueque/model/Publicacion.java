package org.apptrueque.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Publicacion {
    private final String id;  // Cambiado a String para UUID
    private Usuario usuario;
    private Closet closet;
    private boolean activa;
    private LocalDateTime fechaPublicacion;

    public Publicacion(Usuario usuario, Closet closet) {
        this.id = UUID.randomUUID().toString(); // Prueba 8
        this.usuario = usuario;
        this.closet = closet;
        this.activa = true; // Prueba 2
        this.fechaPublicacion = LocalDateTime.now();
    }

    // ----- Métodos para las pruebas -----
    public static List<Publicacion> filtrarPublicaciones(List<Publicacion> publicaciones, Usuario usuarioActual) {
        if (publicaciones == null) return List.of(); // Prueba 1
        return publicaciones.stream()
                .filter(Publicacion::isActiva) // Prueba 2
                .filter(p -> usuarioActual == null || !p.getUsuario().equals(usuarioActual)) // Prueba 3
                .sorted(Comparator.comparing(Publicacion::getFechaPublicacion).reversed()) // Prueba 6
                .collect(Collectors.toList());
    }

    // Método para Test 4
    public static List<Prenda> filtrarPorNombre(Closet closet, String nombre) {
        if (closet == null || nombre == null) return List.of();

        try {
            return closet.getPrendas().stream()
                    .filter(p -> p != null && p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    // Método para Test 5 (versión definitiva)
    public static List<Prenda> filtrarPorCategoria(Closet closet, String categoria) {
        // 1. Validaciones básicas
        if (closet == null || categoria == null) {
            return Collections.emptyList();
        }

        // 2. Obtención segura de prendas
        List<Prenda> prendas;
        try {
            prendas = closet.getPrendas(); // Usa el getter existente
            if (prendas == null) {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }

        // 3. Filtrado real (case-insensitive)
        return prendas.stream()
                .filter(Objects::nonNull) // Filtra prendas nulas
                .filter(p -> p.getCategoria() != null) // Filtra categorías nulas
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    // ----- Getters -----
    public String getId() { return id; } // Prueba 8
    public Usuario getUsuario() { return usuario; } // Prueba 7
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fecha) { this.fechaPublicacion = fecha; }
}
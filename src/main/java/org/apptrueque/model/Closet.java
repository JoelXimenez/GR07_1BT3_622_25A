package org.apptrueque.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "closet")
public class Closet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean publicado;

    @OneToMany(mappedBy = "closet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Prenda> prendas = new ArrayList<>();

    // Constructor vacío (requerido por JPA)
    public Closet() {
        this.publicado = false;
    }

    // Métodos de negocio

    public void publicar() {
        this.publicado = true;
    }

    public void ocultar() {
        this.publicado = false;
    }

    public void agregarPrenda(Prenda prenda) {
        prendas.add(prenda);
        prenda.setCloset(this); // Importante para la relación bidireccional
    }

    public boolean validarMinimoPrendas() {
        return prendas.size() >= 1;
    }

    public boolean validarMaximoPrendas() {
        return prendas.size() <= 20;
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }

    public List<Prenda> getPrendas() { return prendas; }
    public void setPrendas(List<Prenda> prendas) { this.prendas = prendas; }
}

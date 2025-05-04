package org.apptrueque.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "closets") // ✅ nombre correcto de la tabla
public class Closet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCloset") // ✅ nombre correcto de la columna PK
    private Long idCloset;

    private boolean publicado;

    @OneToMany(mappedBy = "closet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Prenda> prendas = new ArrayList<>();

    public Closet() {
        this.publicado = false;
    }

    public void publicar() {
        this.publicado = true;
    }

    public void ocultar() {
        this.publicado = false;
    }

    public void agregarPrenda(Prenda prenda) {
        prendas.add(prenda);
        prenda.setCloset(this);
    }

    public boolean validarMinimoPrendas() {
        return prendas.size() >= 1;
    }

    public boolean validarMaximoPrendas() {
        return prendas.size() <= 20;
    }

    // Getters y Setters

    public Long getIdCloset() {
        return idCloset;
    }

    public void setIdCloset(Long idCloset) {
        this.idCloset = idCloset;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    public List<Prenda> getPrendas() {
        return prendas;
    }

    public void setPrendas(List<Prenda> prendas) {
        this.prendas = prendas;
    }
}

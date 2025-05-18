package org.apptrueque.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "closets")
public class Closet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCloset;

    @OneToOne(mappedBy = "closetActual")
    private Usuario usuario;

    @OneToMany(mappedBy = "closet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Prenda> prendas = new ArrayList<>();

    @Column(nullable = false)
    private boolean publicado;  // Campo añadido

    @Column(length = 20)
    private String edad; // Adultos, Niños, Bebes, Ancianos, Jovenes


    public Closet() {
        this.publicado = false;  // Valor predeterminado
    }

    public void publicar() {
        this.publicado = true;  // Cambia el estado a 'publicado'
        System.out.println("📢 Closet publicado.");
    }

    // Getters y setters
    public Long getIdCloset() {
        return idCloset;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public List<Prenda> getPrendas() {
        return prendas;
    }

    public boolean isPublicado() {
        return publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    public void agregarPrenda(Prenda p1) {

    }
}

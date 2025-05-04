package org.apptrueque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prendas")
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 20)
    private String talla;

    @Column(length = 50)
    private String estado;

    @Column(length = 50)
    private String categoria;

    @Column(length = 255)
    private String imagenURL;

    @ManyToOne
    @JoinColumn(name = "closet_id", referencedColumnName = "idCloset", nullable = false)
    private Closet closet;

    public Prenda() {
    }

    public Prenda(String nombre, String descripcion, String talla, String estado, String categoria, String imagenURL) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.talla = talla;
        this.estado = estado;
        this.categoria = categoria;
        this.imagenURL = imagenURL;
    }

    public void guardar() {
        EntityManager em = null;
        try {
            em = org.apptrueque.util.JpaUtil.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(this);
            em.getTransaction().commit();
            System.out.println("✅ Prenda guardada exitosamente: " + nombre);
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("❌ Error al guardar la prenda.");
        } finally {
            if (em != null) em.close();
        }
    }

    public void editar() {
        EntityManager em = null;
        try {
            em = org.apptrueque.util.JpaUtil.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.merge(this);
            em.getTransaction().commit();
            System.out.println("✅ Prenda actualizada exitosamente: " + nombre);
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("❌ Error al actualizar la prenda.");
        } finally {
            if (em != null) em.close();
        }
    }

    public void eliminar() {
        EntityManager em = null;
        try {
            em = org.apptrueque.util.JpaUtil.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            Prenda prendaABorrar = em.find(Prenda.class, this.id);
            if (prendaABorrar != null) {
                em.remove(prendaABorrar);
                em.getTransaction().commit();
                System.out.println("✅ Prenda eliminada exitosamente: " + nombre);
            }
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("❌ Error al eliminar la prenda.");
        } finally {
            if (em != null) em.close();
        }
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public Closet getCloset() {
        return closet;
    }

    public void setCloset(Closet closet) {
        this.closet = closet;
    }
}

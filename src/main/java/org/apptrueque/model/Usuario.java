package org.apptrueque.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(length = 10)
    private String cedula;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fechaRegistro;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "closet_id", referencedColumnName = "idCloset") // CORREGIDO
    private Closet closetActual;

    public Usuario() {
        this.fechaRegistro = new Date();
    }

    public Usuario(String cedula, String nombre, String email, String password) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.fechaRegistro = new Date();
        this.closetActual = new Closet();
    }

    public static void registrar(Usuario usuario) {
        EntityManager em = null;
        try {
            em = org.apptrueque.util.JpaUtil.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            System.out.println("✅ Usuario registrado exitosamente: " + usuario.getNombre());
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.out.println("❌ Error al registrar el usuario.");
        } finally {
            if (em != null) em.close();
        }
    }

    public static Usuario obtenerPorEmail(String email) {
        EntityManager em = null;
        Usuario usuario = null;
        try {
            em = org.apptrueque.util.JpaUtil.getEntityManagerFactory().createEntityManager();
            usuario = em.createQuery(
                            "SELECT u FROM Usuario u " +
                                    "LEFT JOIN FETCH u.closetActual c " +
                                    "LEFT JOIN FETCH c.prendas " +
                                    "WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            usuario = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
        return usuario;
    }

    public void editarPerfil(String nuevoNombre, String nuevoEmail, String nuevaPassword) {
        EntityManager em = null;
        try {
            em = org.apptrueque.util.JpaUtil.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();
            this.nombre = nuevoNombre;
            this.email = nuevoEmail;
            this.password = nuevaPassword;
            em.merge(this);
            em.getTransaction().commit();
            System.out.println("✅ Perfil actualizado correctamente");
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.out.println("❌ Error al actualizar el perfil.");
        } finally {
            if (em != null) em.close();
        }
    }

    public void publicarCloset() {
        if (closetActual != null) {
            closetActual.publicar();
        }
    }

    // Getters y setters

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Closet getClosetActual() {
        return closetActual;
    }

    public void setClosetActual(Closet closetActual) {
        this.closetActual = closetActual;
    }
}

package org.apptrueque.model;

import jakarta.persistence.*;
import org.apptrueque.util.JpaUtil;

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

    public Usuario() {
        this.fechaRegistro = new Date();
    }

    public Usuario(String cedula, String nombre, String email, String password) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.fechaRegistro = new Date();
    }

    // ➡️ Método para registrar un usuario usando JpaUtil
    public static void registrar(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManagerFactory().createEntityManager();
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
            if (em != null) {
                em.close();
            }
        }
    }

    // ➡️ Método para obtener usuario por email usando JpaUtil
    public static Usuario obtenerPorEmail(String email) {
        EntityManager em = null;
        Usuario usuario = null;
        try {
            em = JpaUtil.getEntityManagerFactory().createEntityManager();
            usuario = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            usuario = null;  // No encontró el usuario
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al buscar el usuario.");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return usuario;
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}

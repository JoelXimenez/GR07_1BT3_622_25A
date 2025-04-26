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

    // Constructor vacío (necesario para JPA)
    public Usuario() {
        this.fechaRegistro = new Date();
    }

    // Constructor con parámetros
    public Usuario(String cedula, String nombre, String email, String password) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.fechaRegistro = new Date();
    }
    public static Usuario obtenerPorEmail(String email) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        Usuario usuario = null;

        try {
            emf = Persistence.createEntityManagerFactory("apptruequePU");
            em = emf.createEntityManager();
            usuario = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            usuario = null;  // Si no se encuentra, el usuario es null
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener el usuario.");
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
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
    public static void registrar(Usuario usuario) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("apptruequePU");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(usuario);  // Persistimos el usuario en la base de datos
            em.getTransaction().commit();
            System.out.println("Usuario registrado exitosamente: " + usuario.getNombre());
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Si hay un error, hacemos rollback
            }
            e.printStackTrace();
            System.out.println("Error al registrar el usuario.");
        } finally {
            if (em != null) {
                em.close();  // Cerramos el EntityManager
            }
            if (emf != null) {
                emf.close();  // Cerramos el EntityManagerFactory
            }
        }
    }

}

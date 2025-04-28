package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import org.apptrueque.model.Closet;
import org.apptrueque.model.Prenda;
import org.apptrueque.model.Usuario;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;

@WebServlet(name = "AgregarPrendaServlet", value = "/AgregarPrendaServlet")
public class AgregarPrendaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Recoger datos del formulario
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String talla = request.getParameter("talla");
        String estado = request.getParameter("estado");
        String categoria = request.getParameter("categoria");
        String imagenURL = request.getParameter("imagenURL");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        em.getTransaction().begin();
        try {
            // Recargar el usuario desde la base de datos
            usuario = em.find(Usuario.class, usuario.getCedula());

            Closet closet = usuario.getClosetActual();

            if (closet == null) {
                closet = new Closet();
                usuario.setClosetActual(closet);
                em.persist(closet);
                em.merge(usuario);
            }

            // Crear nueva prenda
            Prenda nuevaPrenda = new Prenda(nombre, descripcion, talla, estado, categoria, imagenURL);

            // Asociar la prenda al closet
            closet.agregarPrenda(nuevaPrenda);

            // Guardar cambios
            em.merge(closet);
            em.getTransaction().commit();

            System.out.println("✅ Prenda agregada correctamente al closet de " + usuario.getNombre());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.out.println("❌ Error al agregar prenda.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
            // Siempre redirigir de vuelta al closet
            response.sendRedirect("MiClosetServlet");
        }
    }
}

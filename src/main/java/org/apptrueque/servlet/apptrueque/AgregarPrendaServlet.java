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

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        // Obtener parámetros del formulario
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String talla = request.getParameter("talla");
        String estado = request.getParameter("estado");
        String categoria = request.getParameter("categoria");
        String imagenURL = request.getParameter("imagenURL");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            // Obtener usuario y closet desde esta sesión de EntityManager
            Usuario usuario = em.find(Usuario.class, usuarioSesion.getCedula());

            Closet closet = usuario.getClosetActual();

            if (closet == null || closet.getIdCloset() == null) {
                throw new IllegalStateException("❌ El closet no existe o no tiene ID válido.");
            }

            // Reasociar el closet a esta sesión (garantizar que esté 'managed')
            closet = em.find(Closet.class, closet.getIdCloset());

            // Crear y guardar la prenda
            Prenda prenda = new Prenda(nombre, descripcion, talla, estado, categoria, imagenURL);
            prenda.setCloset(closet);  // asociación correcta
            em.persist(prenda);

            em.getTransaction().commit();

            System.out.println("✅ Prenda guardada correctamente para el usuario: " + usuario.getNombre());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.out.println("❌ Error al guardar la prenda.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
            response.sendRedirect("MiClosetServlet");
        }
    }
}

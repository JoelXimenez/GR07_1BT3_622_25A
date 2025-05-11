package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apptrueque.model.*;
import org.apptrueque.util.JpaUtil;
import jakarta.persistence.EntityManager;
import java.io.IOException;

@WebServlet("/PublicarClosetServlet")
public class PublicarClosetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("home.jsp");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        String mensaje;

        try {
            em.getTransaction().begin();

            // Recargar usuario y closet desde BD
            usuario = em.find(Usuario.class, usuario.getCedula());
            Closet closet = usuario.getClosetActual();

            // Validar publicación usando lógica del modelo (Prueba 3 y 4 implícitas)
            if (closet.getPrendas().size() >= 3 && closet.getPrendas().size() <= 10) {
                closet.setPublicado(true);
                em.merge(closet);
                mensaje = "¡Closet publicado con éxito!";
            } else {
                mensaje = "El closet debe tener entre 3 y 10 prendas.";
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            mensaje = "Error al publicar el closet: " + e.getMessage();
        } finally {
            em.close();
        }

        session.setAttribute("mensaje", mensaje);
        response.sendRedirect("miCloset.jsp");
    }
}
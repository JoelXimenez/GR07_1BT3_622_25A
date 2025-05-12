package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apptrueque.model.*;
import org.apptrueque.util.JpaUtil;
import jakarta.persistence.EntityManager;
import java.io.IOException;

@WebServlet("/DespublicarClosetServlet")
public class DespublicarClosetServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long idCloset = Long.parseLong(request.getParameter("idCloset"));

        // Llamada al metodo que despublica el closet
        if (despublicarCloset(idCloset)) {
            response.sendRedirect("perfil.jsp");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al despublicar el closet.");
        }
    }

    // Metodo que maneja la lógica de despublicar el closet
    private boolean despublicarCloset(Long idCloset) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();

            Closet closet = em.find(Closet.class, idCloset);

            if (closet != null) {
                closet.setPublicado(false); // Despublicar
                em.merge(closet);
            } else {
                return false; // No se encontró el closet
            }

            em.getTransaction().commit();
            return true; // Éxito
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false; // Error al despublicar
        } finally {
            em.close();
        }
    }
}
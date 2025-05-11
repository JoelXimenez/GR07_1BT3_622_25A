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
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            Closet closet = em.find(Closet.class, idCloset);

            if (closet != null) {
                closet.setPublicado(false); // Despublicar
                em.merge(closet);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        response.sendRedirect("perfil.jsp");
    }
}
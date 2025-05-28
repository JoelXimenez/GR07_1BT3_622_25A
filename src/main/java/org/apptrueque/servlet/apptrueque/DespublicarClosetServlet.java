package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apptrueque.model.Closet;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;

@WebServlet("/DespublicarClosetServlet")
public class DespublicarClosetServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long idCloset = parseIdCloset(request);
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Closet closet = em.find(Closet.class, idCloset);
            if (closet != null) {
                despublicarClosetYEliminarMatches(em, closet);
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

    private Long parseIdCloset(HttpServletRequest request) {
        String idParam = request.getParameter("idCloset");
        return Long.parseLong(idParam);
    }

    private void despublicarClosetYEliminarMatches(EntityManager em, Closet closet) {
        closet.setPublicado(false);
        em.merge(closet);

        String emailUsuario = closet.getUsuario().getEmail();
        int matchesEliminados = eliminarMatchesPorUsuario(em, emailUsuario);

        System.out.println("Matches eliminados para usuario " + emailUsuario + ": " + matchesEliminados);
    }

    private int eliminarMatchesPorUsuario(EntityManager em, String emailUsuario) {
        Query deleteMatches = em.createQuery(
                "DELETE FROM Match m WHERE m.usuarioA = :email OR m.usuarioB = :email"
        );
        deleteMatches.setParameter("email", emailUsuario);
        return deleteMatches.executeUpdate();
    }
}

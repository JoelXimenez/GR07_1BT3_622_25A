package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apptrueque.model.Match;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;

@WebServlet("/EliminarMatchServlet")
public class EliminarMatchServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long idMatch = parseIdMatch(request);
        if (idMatch == null) {
            response.sendRedirect("perfil.jsp");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Match match = em.find(Match.class, idMatch);
            if (match != null) {
                eliminarMatchYLikes(em, match);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new ServletException("Error al eliminar match y likes relacionados", e);
        } finally {
            em.close();
        }

        response.sendRedirect("perfil.jsp");
    }

    private Long parseIdMatch(HttpServletRequest request) {
        String idMatchStr = request.getParameter("idMatch");
        if (idMatchStr == null) return null;
        try {
            return Long.parseLong(idMatchStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void eliminarMatchYLikes(EntityManager em, Match match) {
        String usuarioA = match.getUsuarioA();
        String usuarioB = match.getUsuarioB();

        em.remove(match);

        int likesEliminados = eliminarLikes(em, usuarioA, usuarioB);

        System.out.println("Likes eliminados relacionados al match: " + likesEliminados);
    }

    private int eliminarLikes(EntityManager em, String usuarioA, String usuarioB) {
        Query deleteLikes = em.createQuery(
                "DELETE FROM Like l WHERE " +
                        " (l.usuarioEmail = :a AND EXISTS (" +
                        "   SELECT 1 FROM Closet c WHERE c = l.closet AND c.usuario.email = :b" +
                        " )) OR " +
                        " (l.usuarioEmail = :b AND EXISTS (" +
                        "   SELECT 1 FROM Closet c WHERE c = l.closet AND c.usuario.email = :a" +
                        " ))"
        );
        deleteLikes.setParameter("a", usuarioA);
        deleteLikes.setParameter("b", usuarioB);
        return deleteLikes.executeUpdate();
    }
}

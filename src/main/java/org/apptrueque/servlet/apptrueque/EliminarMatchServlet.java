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

        String idMatchStr = request.getParameter("idMatch");
        if (idMatchStr == null) {
            response.sendRedirect("perfil.jsp");
            return;
        }

        Long idMatch;
        try {
            idMatch = Long.parseLong(idMatchStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("perfil.jsp");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Match match = em.find(Match.class, idMatch);
            if (match != null) {
                String usuarioA = match.getUsuarioA();
                String usuarioB = match.getUsuarioB();

                // Eliminar Match
                em.remove(match);

                // Eliminar Likes correspondientes (bidireccional)
                Query deleteLikes = em.createQuery(
                        "DELETE FROM Like l WHERE (l.usuarioEmail = :a AND l.closet.usuario.email = :b) OR (l.usuarioEmail = :b AND l.closet.usuario.email = :a)"
                );
                deleteLikes.setParameter("a", usuarioA);
                deleteLikes.setParameter("b", usuarioB);
                int likesEliminados = deleteLikes.executeUpdate();

                System.out.println("Likes eliminados relacionados al match: " + likesEliminados);
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
}

package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.*;
import org.apptrueque.util.JpaUtil;
import java.io.IOException;
import java.util.List;

@WebServlet("/mensajeria")
public class MensajeriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String usuario1 = request.getParameter("usuario1");
        String usuario2 = request.getParameter("usuario2");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Object[]> mensajes = em.createQuery(
                            "SELECT m.remitenteEmail, m.contenido, m.fechaHora FROM Mensaje m " +
                                    "WHERE (m.remitenteEmail = :u1 AND m.destinatarioEmail = :u2) " +
                                    "   OR (m.remitenteEmail = :u2 AND m.destinatarioEmail = :u1) " +
                                    "ORDER BY m.fechaHora", Object[].class)
                    .setParameter("u1", usuario1)
                    .setParameter("u2", usuario2)
                    .getResultList();

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < mensajes.size(); i++) {
                Object[] m = mensajes.get(i);
                json.append("{")
                        .append("\"remitente\":\"").append(m[0]).append("\",")
                        .append("\"contenido\":\"").append(escapeJson(m[1].toString())).append("\",")
                        .append("\"fecha\":\"").append(m[2].toString()).append("\"")
                        .append("}");
                if (i < mensajes.size() - 1) json.append(",");
            }
            json.append("]");

            response.setContentType("application/json");
            response.getWriter().write(json.toString());

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String remitente = request.getParameter("remitente");
        String destinatario = request.getParameter("destinatario");
        String contenido = request.getParameter("contenido");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.createNativeQuery("INSERT INTO mensaje (remitente_email, destinatario_email, contenido) " +
                            "VALUES (?, ?, ?)")
                    .setParameter(1, remitente)
                    .setParameter(2, destinatario)
                    .setParameter(3, contenido)
                    .executeUpdate();
            tx.commit();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            em.close();
        }
    }

    private String escapeJson(String text) {
        return text.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}

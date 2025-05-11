package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import org.apptrueque.model.Mensaje;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/ObtenerMensajesServlet")
public class ObtenerMensajesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String remitente = request.getParameter("remitente");
        String destinatario = request.getParameter("destinatario");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Mensaje> mensajes = em.createQuery(
                        "SELECT m FROM Mensaje m WHERE " +
                                "(m.remitenteEmail = :remitente AND m.destinatarioEmail = :destinatario) OR " +
                                "(m.remitenteEmail = :destinatario AND m.destinatarioEmail = :remitente) " +
                                "ORDER BY m.fechaHora ASC", Mensaje.class)
                .setParameter("remitente", remitente)
                .setParameter("destinatario", destinatario)
                .getResultList();
        em.close();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Mensaje m : mensajes) {
            out.println("<div><b>" +
                    (m.getRemitenteEmail().equals(remitente) ? "Tú" : m.getRemitenteEmail()) +
                    ":</b> " + m.getContenido() + "<br><small>" +
                    m.getFechaHora().format(formatter) + "</small></div>");
        }
    }
}

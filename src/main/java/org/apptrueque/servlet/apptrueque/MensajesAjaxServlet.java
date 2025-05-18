package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.persistence.*;

import org.apptrueque.model.Mensaje;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/MensajesAjaxServlet")
public class MensajesAjaxServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remitente = request.getParameter("remitente");
        String destinatario = request.getParameter("destinatario");

        if (remitente == null || destinatario == null) return;

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Mensaje> mensajes = em.createQuery(
                            "SELECT m FROM Mensaje m WHERE " +
                                    "(m.remitenteEmail = :r AND m.destinatarioEmail = :d) OR " +
                                    "(m.remitenteEmail = :d AND m.destinatarioEmail = :r) ORDER BY m.fechaHora", Mensaje.class)
                    .setParameter("r", remitente)
                    .setParameter("d", destinatario)
                    .getResultList();

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            for (Mensaje m : mensajes) {
                String clase = m.getRemitenteEmail().equals(remitente) ? "sent" : "received";
                String fechaHora = m.getFechaHora().toLocalDate().toString() + " " +
                        String.format("%02d:%02d", m.getFechaHora().getHour(), m.getFechaHora().getMinute());

                out.println("<div class='message " + clase + "'>");
                out.println(m.getContenido());
                out.println("<div class='timestamp'>" + fechaHora + "</div>");
                out.println("</div>");
            }
        } finally {
            em.close();
        }
    }
}

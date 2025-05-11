package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apptrueque.model.Mensaje;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "EnviarMensajeServlet", value = "/EnviarMensajeServlet")
public class EnviarMensajeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String remitente = request.getParameter("remitente");
        String destinatario = request.getParameter("destinatario");
        String contenido = request.getParameter("contenido");

        if (remitente != null && destinatario != null && contenido != null &&
                !remitente.isEmpty() && !destinatario.isEmpty() && !contenido.isEmpty()) {

            Mensaje mensaje = new Mensaje();
            mensaje.setRemitenteEmail(remitente);
            mensaje.setDestinatarioEmail(destinatario);
            mensaje.setContenido(contenido);
            mensaje.setFechaHora(LocalDateTime.now());

            EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();
                em.persist(mensaje);
                tx.commit();
                System.out.println("✅ MENSAJE GUARDADO: [" + remitente + " ➜ " + destinatario + "] - " + contenido);
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw new ServletException("Error al guardar el mensaje", e);
            } finally {
                em.close();
            }
        }

        response.setStatus(HttpServletResponse.SC_OK); // evita redirección si se usa AJAX
    }
}

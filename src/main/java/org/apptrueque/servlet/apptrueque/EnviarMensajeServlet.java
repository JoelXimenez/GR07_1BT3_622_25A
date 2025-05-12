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

        if (validarParametros(remitente, destinatario, contenido)) {
            Mensaje mensaje = new Mensaje();
            mensaje.setRemitenteEmail(remitente);
            mensaje.setDestinatarioEmail(destinatario);
            mensaje.setContenido(contenido);
            mensaje.setFechaHora(LocalDateTime.now());

            guardarMensaje(mensaje); // Guardamos el mensaje
            response.setStatus(HttpServletResponse.SC_OK); // Respuesta OK si todo es correcto
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Respuesta de error si los parámetros son inválidos
            return; // Termina la ejecución aquí para evitar que se procese el mensaje si los datos son incorrectos
        }
    }

    private static void guardarMensaje(Mensaje mensaje) throws ServletException {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try (em) {
            tx.begin();
            em.persist(mensaje);
            tx.commit();
            System.out.println("✅ MENSAJE GUARDADO: [" + mensaje.getRemitenteEmail() + " ➜ " + mensaje.getDestinatarioEmail() + "] - " + mensaje.getContenido());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new ServletException("Error al guardar el mensaje", e);
        }
    }

    private boolean validarParametros(String remitente, String destinatario, String contenido) {
        return remitente != null && destinatario != null && contenido != null &&
                !remitente.isEmpty() && !destinatario.isEmpty() && !contenido.isEmpty();
    }

}

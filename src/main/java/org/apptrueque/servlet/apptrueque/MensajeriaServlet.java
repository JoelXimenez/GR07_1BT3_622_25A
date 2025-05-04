package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.persistence.*;
import org.apptrueque.model.Mensaje;
import org.apptrueque.model.Usuario;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/mensajeria")
public class MensajeriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        String remitente = usuarioSesion.getEmail();
        String destinatario = request.getParameter("destinatario");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Usuario> usuarios = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.email <> :actual", Usuario.class)
                    .setParameter("actual", remitente)
                    .getResultList();
            request.setAttribute("usuarios", usuarios);
            request.setAttribute("remitente", remitente);
            request.setAttribute("destinatario", destinatario);

            if (destinatario != null && !destinatario.isEmpty()) {
                List<Mensaje> mensajes = em.createQuery(
                                "SELECT m FROM Mensaje m WHERE " +
                                        "(m.remitenteEmail = :rem AND m.destinatarioEmail = :dest) OR " +
                                        "(m.remitenteEmail = :dest AND m.destinatarioEmail = :rem) " +
                                        "ORDER BY m.fechaHora", Mensaje.class)
                        .setParameter("rem", remitente)
                        .setParameter("dest", destinatario)
                        .getResultList();
                request.setAttribute("mensajes", mensajes);
            }

            request.getRequestDispatcher("mensajeria.jsp").forward(request, response);
        } finally {
            em.close();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autenticado");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        String remitente = usuarioSesion.getEmail();
        String destinatario = request.getParameter("destinatario");
        String contenido = request.getParameter("contenido");

        if (destinatario == null || destinatario.trim().isEmpty() ||
                contenido == null || contenido.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos incompletos");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Mensaje mensaje = new Mensaje();
            mensaje.setRemitenteEmail(remitente);
            mensaje.setDestinatarioEmail(destinatario);
            mensaje.setContenido(contenido);
            mensaje.setFechaHora(LocalDateTime.now());

            em.persist(mensaje);
            tx.commit();

            response.sendRedirect("mensajeria?destinatario=" + destinatario);

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace(); // Mostrar error en consola
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al guardar mensaje: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}

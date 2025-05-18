package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.persistence.*;

import org.apptrueque.model.Mensaje;
import org.apptrueque.model.Usuario;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/MensajeServlet")
public class MensajeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String contenido = request.getParameter("contenido");
        String destinatario = request.getParameter("destinatario");

        if (contenido != null && !contenido.trim().isEmpty() && destinatario != null) {
            EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
            try {
                em.getTransaction().begin();
                Mensaje mensaje = new Mensaje();
                mensaje.setRemitenteEmail(usuario.getEmail());
                mensaje.setDestinatarioEmail(destinatario);
                mensaje.setContenido(contenido);
                em.persist(mensaje);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }

        response.sendRedirect("MensajeServlet?destinatario=" + destinatario);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String receptor = request.getParameter("destinatario");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Usuario> usuarios = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.email <> :email", Usuario.class)
                    .setParameter("email", usuario.getEmail())
                    .getResultList();

            request.setAttribute("usuarios", usuarios);
            request.setAttribute("receptor", receptor);
            request.getRequestDispatcher("chat.jsp").forward(request, response);
        } finally {
            em.close();
        }
    }
}



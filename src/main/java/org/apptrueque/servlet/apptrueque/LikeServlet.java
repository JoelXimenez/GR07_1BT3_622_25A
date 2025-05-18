package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apptrueque.model.Closet;
import org.apptrueque.model.Like;
import org.apptrueque.model.Notificacion;
import org.apptrueque.model.Usuario;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/LikeServlet")
public class LikeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idClosetParam = request.getParameter("id");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (idClosetParam == null || usuario == null) {
            response.sendRedirect("publicaciones.jsp");
            return;
        }

        Long idCloset;
        try {
            idCloset = Long.parseLong(idClosetParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("publicaciones.jsp");
            return;
        }

        String emailUsuario = usuario.getEmail();

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Closet closet = em.find(Closet.class, idCloset);
            if (closet == null) {
                em.getTransaction().rollback();
                response.sendRedirect("publicaciones.jsp");
                return;
            }

            // Verificar si ya dio like
            TypedQuery<Like> query = em.createQuery(
                    "SELECT l FROM Like l WHERE l.closet.idCloset = :idCloset AND l.usuarioEmail = :email", Like.class);
            query.setParameter("idCloset", idCloset);
            query.setParameter("email", emailUsuario);

            List<Like> resultados = query.getResultList();

            if (resultados.isEmpty()) {
                processNewLike(em, closet, usuario);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new ServletException("Error al procesar el like", e);
        } finally {
            em.close();
        }

        response.sendRedirect("publicaciones.jsp");
    }

    private void processNewLike(EntityManager em, Closet closet, Usuario usuario) {
        // Crear nuevo Like
        Like nuevoLike = new Like();
        nuevoLike.setCloset(closet);
        nuevoLike.setUsuarioEmail(usuario.getEmail());
        nuevoLike.setFecha(LocalDateTime.now());
        em.persist(nuevoLike);

        // Crear notificación
        Notificacion noti = new Notificacion();
        noti.setUsuarioDestino(closet.getUsuario().getEmail());  // destinatario (email)
        noti.setUsuarioRemitente(usuario.getEmail());
        noti.setMensaje(usuario.getNombre() + " dio like a tu closet publicado.");  // usa nombre
        em.persist(noti);
}
}
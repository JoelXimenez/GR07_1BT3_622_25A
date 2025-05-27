package org.apptrueque.servlet.apptrueque;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apptrueque.model.*;
import org.apptrueque.util.JpaUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/LikeServlet")

public class LikeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response)
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
        String nombreUsuario = usuario.getNombre();
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Closet closet = em.find(Closet.class, idCloset);
            if (closet == null) {
                em.getTransaction().rollback();
                response.sendRedirect("publicaciones.jsp");
                return;
            }
            String emailCloset = closet.getUsuario().getEmail();
            String nombreCloset = closet.getUsuario().getNombre();
            // Buscar si ya existe el like
            TypedQuery<Like> query = em.createQuery(
                    "SELECT l FROM Like l WHERE l.closet.idCloset = :idCloset AND l.usuarioEmail = :email", Like.class);
            query.setParameter("idCloset", idCloset);
            query.setParameter("email", emailUsuario);
            List<Like> resultados = query.getResultList();
            if (!resultados.isEmpty()) {
                // Ya dio like → eliminar like y match si existe
                Like likeExistente = resultados.get(0);
                em.remove(em.contains(likeExistente) ? likeExistente :
                        em.merge(likeExistente));
                // Eliminar el match si existe
                TypedQuery<Match> matchQuery = em.createQuery(
                        "SELECT m FROM Match m WHERE (m.usuarioA = :a AND m.usuarioB =:b) OR (m.usuarioA = :b AND m.usuarioB = :a)",
                Match.class);
                matchQuery.setParameter("a", emailUsuario);
                matchQuery.setParameter("b", emailCloset);
                List<Match> matches = matchQuery.getResultList();
                for (Match m : matches) {
                    em.remove(em.contains(m) ? m : em.merge(m));
                }
            } else {
                // Nuevo like
                Like nuevoLike = new Like();
                nuevoLike.setCloset(closet);
                nuevoLike.setUsuarioEmail(emailUsuario);
                nuevoLike.setFecha(LocalDateTime.now());
                em.persist(nuevoLike);
                // Notificación por like con nombre
                Notificacion noti = new Notificacion(
                        emailCloset,
                        nombreUsuario + " dio like a tu closet publicado.",
                        emailUsuario
                );
                em.persist(noti);
                // Verificar si el otro ya había dado like (match)
                TypedQuery<Like> likeInverso = em.createQuery(
                        "SELECT l FROM Like l WHERE l.closet.usuario.email = :miEmail AND l.usuarioEmail = :otroEmail", Like.class);
                likeInverso.setParameter("miEmail", emailUsuario);
                likeInverso.setParameter("otroEmail", emailCloset);
                if (!likeInverso.getResultList().isEmpty()) {
                    // Crear match
                    Match match = new Match();
                    match.setUsuarioA(emailUsuario);
                    match.setUsuarioB(emailCloset);
                    match.setFechaMatch(LocalDateTime.now());
                    em.persist(match);
                    // Notificaciones de match para ambos con nombres
                    Notificacion n1 = new Notificacion(
                            emailUsuario,
                            "¡Has hecho match con " + nombreCloset + "!",
                            emailCloset
                    );
                    Notificacion n2 = new Notificacion(
                            emailCloset,
                            "¡Has hecho match con " + nombreUsuario + "!",
                            emailUsuario
                    );
                    em.persist(n1);
                    em.persist(n2);
                }
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new ServletException("Error al procesar el like", e);
        } finally {
            em.close();
        }
        response.sendRedirect("publicaciones.jsp");
    }
}
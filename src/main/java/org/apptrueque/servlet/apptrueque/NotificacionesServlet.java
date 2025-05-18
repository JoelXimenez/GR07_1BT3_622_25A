package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.persistence.EntityManager;
import org.apptrueque.model.Notificacion;
import org.apptrueque.model.Usuario;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/NotificacionesServlet")
public class NotificacionesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = usuario.getEmail();

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Notificacion> notificaciones;

        try {
            em.getTransaction().begin();

            // Obtener todas las notificaciones para ese usuario ordenadas por fecha descendente
            notificaciones = em.createQuery(
                            "SELECT n FROM Notificacion n WHERE n.usuarioDestino = :usuario ORDER BY n.fecha DESC",
                            Notificacion.class)
                    .setParameter("usuario", email)
                    .getResultList();

            // Eliminar notificaciones si hay más de 5
            if (notificaciones.size() > 5) {
                List<Notificacion> aEliminar = notificaciones.subList(5, notificaciones.size());
                for (Notificacion n : aEliminar) {
                    em.remove(em.contains(n) ? n : em.merge(n));
                }
                notificaciones = notificaciones.subList(0, 5);  // conservar solo las últimas 5
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new ServletException("Error al cargar notificaciones", e);
        } finally {
            em.close();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("[");
        for (int i = 0; i < notificaciones.size(); i++) {
            Notificacion n = notificaciones.get(i);
            out.print("{");
            out.print("\"id\":" + n.getId() + ",");  // ✅ <--- AÑADIDO
            out.print("\"mensaje\":\"" + escapeJson(n.getMensaje()) + "\",");
            out.print("\"usuarioRemitente\":\"" + escapeJson(n.getUsuarioRemitente()) + "\"");
            out.print("}");
            if (i < notificaciones.size() - 1) out.print(",");
        }
        out.print("]");
        out.flush();

    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\"", "\\\"").replace("\n", "\\n").replace("\r","");
    }
}
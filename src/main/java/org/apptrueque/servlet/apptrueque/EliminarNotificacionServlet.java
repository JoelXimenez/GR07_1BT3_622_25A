package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.*;

import org.apptrueque.model.Notificacion;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;

@WebServlet("/EliminarNotificacionServlet")
public class EliminarNotificacionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Falta el ID");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long id = Long.parseLong(idParam);
            Notificacion noti = em.find(Notificacion.class, id);

            if (noti != null) {
                em.getTransaction().begin();
                em.remove(noti);
                em.getTransaction().commit();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Notificación eliminada");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Notificación no encontrada");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error interno");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
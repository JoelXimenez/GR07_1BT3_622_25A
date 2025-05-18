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
        try {
            validateRequestParameters(request);
            deleteNotification(request.getParameter("id"), response);
        } catch (IllegalArgumentException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno");
            e.printStackTrace();
        }
    }

    private void validateRequestParameters(HttpServletRequest request) {
        boolean isIdMissing = request.getParameter("id") == null || request.getParameter("id").isEmpty();
        if (isIdMissing) {
            throw new IllegalArgumentException("Falta el ID");
        }
    }

    private void deleteNotification(String idParam, HttpServletResponse response) throws IOException {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            Long id = Long.parseLong(idParam);
            Notificacion notificacion = em.find(Notificacion.class, id);

            if (notificacion == null) {
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Notificación no encontrada");
                return;
            }

            executeDeleteTransaction(em, notificacion);
            sendSuccessResponse(response, "Notificación eliminada");
        } finally {
            em.close();
        }
    }

    private void executeDeleteTransaction(EntityManager em, Notificacion notificacion) {
        em.getTransaction().begin();
        em.remove(notificacion);
        em.getTransaction().commit();
    }

    private void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(message);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(message);
    }
}
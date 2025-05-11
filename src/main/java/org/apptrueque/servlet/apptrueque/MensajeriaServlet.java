package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apptrueque.model.Mensaje;
import org.apptrueque.model.Usuario;
import org.apptrueque.util.JpaUtil;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

@WebServlet("/mensajeria")
public class MensajeriaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        String remitente = (String) sesion.getAttribute("usuarioLogueado");

        if (remitente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        List<Usuario> usuarios = em.createQuery(
                        "SELECT u FROM Usuario u WHERE u.email <> :actual", Usuario.class)
                .setParameter("actual", remitente)
                .getResultList();

        request.setAttribute("usuarios", usuarios);
        em.close();

        RequestDispatcher dispatcher = request.getRequestDispatcher("mensajeria.jsp");
        dispatcher.forward(request, response);
    }
}

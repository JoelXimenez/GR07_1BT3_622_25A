package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Suponiendo que Usuario tiene getEmail()
        org.apptrueque.model.Usuario usuario = (org.apptrueque.model.Usuario) session.getAttribute("usuario");

        // Redirige al JSP con el email como parámetro
        response.sendRedirect("chat.jsp?usuario=" + usuario.getEmail());
    }
}

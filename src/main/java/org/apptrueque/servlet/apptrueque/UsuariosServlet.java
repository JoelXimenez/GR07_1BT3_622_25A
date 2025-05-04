package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.*;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuariosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String excluir = request.getParameter("excluir");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Object[]> usuarios = em.createQuery(
                            "SELECT u.email, u.nombre FROM Usuario u WHERE u.email <> :email", Object[].class)
                    .setParameter("email", excluir)
                    .getResultList();

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < usuarios.size(); i++) {
                Object[] u = usuarios.get(i);
                json.append("{")
                        .append("\"email\":\"").append(u[0]).append("\",")
                        .append("\"nombre\":\"").append(u[1]).append("\"}");
                if (i < usuarios.size() - 1) json.append(",");
            }
            json.append("]");

            response.setContentType("application/json");
            response.getWriter().write(json.toString());
        } finally {
            em.close();
        }
    }
}

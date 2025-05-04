package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apptrueque.model.Usuario;
import org.apptrueque.model.Closet;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;

@WebServlet(name = "MiClosetServlet", value = "/MiClosetServlet")
public class MiClosetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // Traemos al usuario con su closet y prendas asociadas en una sola consulta
            Usuario usuario = em.createQuery(
                            "SELECT u FROM Usuario u " +
                                    "LEFT JOIN FETCH u.closetActual c " +
                                    "LEFT JOIN FETCH c.prendas " +
                                    "WHERE u.cedula = :cedula", Usuario.class)
                    .setParameter("cedula", usuarioSesion.getCedula())
                    .getSingleResult();

            Closet closet = usuario.getClosetActual();

            // Si no tiene closet asociado, se crea uno nuevo
            if (closet == null) {
                tx.begin();

                closet = new Closet();
                em.persist(closet);
                em.flush(); // Se asegura de que se genere el ID

                usuario.setClosetActual(closet);
                em.merge(usuario);

                tx.commit();
                System.out.println("✅ Closet creado con ID: " + closet.getIdCloset());
            }

            // Cargar closet en atributos
            request.setAttribute("closet", closet);
            session.setAttribute("usuario", usuario); // Actualizar sesión

            request.getRequestDispatcher("miCloset.jsp").forward(request, response);

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            response.sendRedirect("login.jsp");
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}

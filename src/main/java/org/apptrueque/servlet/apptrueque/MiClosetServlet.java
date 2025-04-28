package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apptrueque.model.Usuario;
import org.apptrueque.model.Closet;
import org.apptrueque.model.Prenda;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MiClosetServlet", value = "/MiClosetServlet")
public class MiClosetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

            Usuario usuarioBD = em.find(Usuario.class, usuarioSesion.getCedula());

            if (usuarioBD != null) {
                Closet closet = usuarioBD.getClosetActual();

                if (closet == null) {
                    closet = new Closet();
                    usuarioBD.setClosetActual(closet);

                    em.getTransaction().begin();
                    em.persist(closet);
                    em.merge(usuarioBD);
                    em.getTransaction().commit();
                }

                // 🔥 Aquí hacemos la consulta CORRECTA para traer las prendas
                TypedQuery<Prenda> query = em.createQuery(
                        "SELECT p FROM Prenda p WHERE p.closet.id = :closetId", Prenda.class);
                query.setParameter("closetId", closet.getId());
                List<Prenda> prendas = query.getResultList();

                // 🔥 Actualizamos el closet con la lista verdadera
                closet.setPrendas(prendas);

                request.setAttribute("closet", closet);
                session.setAttribute("usuario", usuarioBD);

                request.getRequestDispatcher("miCloset.jsp").forward(request, response);

            } else {
                response.sendRedirect("login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}

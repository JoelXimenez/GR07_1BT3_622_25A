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
        procesarMiCloset(request, response);
    }

    private void procesarMiCloset(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioSesion = obtenerSesionUsuario(session, response);
        if (usuarioSesion == null) return;

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Usuario usuario = obtenerUsuarioConCloset(em, usuarioSesion);
            Closet closet = usuario.getClosetActual();

            if (closet == null) {
                closet = crearClosetSiNoExiste(em, tx, usuario);
            }

            request.setAttribute("closet", closet);
            session.setAttribute("usuario", usuario);
            request.getRequestDispatcher("miCloset.jsp").forward(request, response);

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            response.sendRedirect("login.jsp");
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    private Usuario obtenerSesionUsuario(HttpSession session, HttpServletResponse response) throws IOException {
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return null;
        }
        return (Usuario) session.getAttribute("usuario");
    }

    private Usuario obtenerUsuarioConCloset(EntityManager em, Usuario usuarioSesion) {
        return em.createQuery(
                        "SELECT u FROM Usuario u " +
                                "LEFT JOIN FETCH u.closetActual c " +
                                "LEFT JOIN FETCH c.prendas " +
                                "WHERE u.cedula = :cedula", Usuario.class)
                .setParameter("cedula", usuarioSesion.getCedula())
                .getSingleResult();
    }

    private Closet crearClosetSiNoExiste(EntityManager em, EntityTransaction tx, Usuario usuario) {
        Closet closet = new Closet();
        tx.begin();
        em.persist(closet);
        em.flush();
        usuario.setClosetActual(closet);
        em.merge(usuario);
        tx.commit();
        System.out.println("✅ Closet creado con ID: " + closet.getIdCloset());
        return closet;
    }
}

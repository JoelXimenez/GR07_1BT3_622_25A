package org.apptrueque.servlet.apptrueque;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apptrueque.model.Closet;
import org.apptrueque.model.Prenda;
import org.apptrueque.model.Usuario;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/PublicarClosetServlet")
public class PublicarClosetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("miCloset.jsp");
            return;
        }

        // ✅ Aquí recuperamos el valor del selector
        String edad = request.getParameter("edad");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        String mensaje;

        try {
            em.getTransaction().begin();

            usuario = em.find(Usuario.class, usuario.getCedula());
            Closet closet = usuario.getClosetActual();

            List<Prenda> prendas = em.createQuery(
                            "SELECT p FROM Prenda p WHERE p.closet.idCloset = :idCloset", Prenda.class)
                    .setParameter("idCloset", closet.getIdCloset())
                    .getResultList();

            int cantidad = prendas.size();

            if (cantidad >= 3 && cantidad <= 10) {
                closet.setPublicado(true);
                closet.setEdad(edad);  // ✅ ahora edad no es null
                em.merge(closet);
                mensaje = "¡Closet publicado exitosamente con " + cantidad + " prendas!";
            } else {
                mensaje = "El closet debe tener entre 3 y 10 prendas para ser publicado. Actualmente tiene " + cantidad + ".";
            }

            em.getTransaction().commit();
            session.setAttribute("usuario", usuario);
            session.setAttribute("mensajePublicacion", mensaje);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            session.setAttribute("mensajePublicacion", "Error al publicar el closet.");
        } finally {
            em.close();
        }

        response.sendRedirect("miCloset.jsp");
    }
}

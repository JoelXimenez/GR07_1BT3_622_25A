package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import org.apptrueque.model.Prenda;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;

@WebServlet(name = "EditarPrendaServlet", value = "/EditarPrendaServlet")
public class EditarPrendaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String talla = request.getParameter("talla");
        String estado = request.getParameter("estado");
        String categoria = request.getParameter("categoria");

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Prenda prenda = em.find(Prenda.class, id);

            if (prenda != null) {
                if (nombre != null && !nombre.trim().isEmpty()) prenda.setNombre(nombre);
                if (descripcion != null && !descripcion.trim().isEmpty()) prenda.setDescripcion(descripcion);
                if (talla != null && !talla.trim().isEmpty()) prenda.setTalla(talla);
                if (estado != null && !estado.trim().isEmpty()) prenda.setEstado(estado);
                if (categoria != null && !categoria.trim().isEmpty()) prenda.setCategoria(categoria);

                em.merge(prenda);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }

        response.sendRedirect("MiClosetServlet"); // SIEMPRE regresar al closet
    }
}

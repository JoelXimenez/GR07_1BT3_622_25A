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
        editarPrenda(request);
        response.sendRedirect("MiClosetServlet");
    }

    private void editarPrenda(HttpServletRequest request) {
        Prenda prendaFormulario = recolectarDatosFormulario(request);

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Prenda prenda = em.find(Prenda.class, prendaFormulario.getId());
            if (prenda != null) {
                actualizarPrenda(prenda, prendaFormulario);
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
    }

    private void actualizarPrenda(Prenda original, Prenda nuevosDatos) {
        if (nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().trim().isEmpty())
            original.setNombre(nuevosDatos.getNombre());

        if (nuevosDatos.getDescripcion() != null && !nuevosDatos.getDescripcion().trim().isEmpty())
            original.setDescripcion(nuevosDatos.getDescripcion());

        if (nuevosDatos.getTalla() != null && !nuevosDatos.getTalla().trim().isEmpty())
            original.setTalla(nuevosDatos.getTalla());

        if (nuevosDatos.getEstado() != null && !nuevosDatos.getEstado().trim().isEmpty())
            original.setEstado(nuevosDatos.getEstado());

        if (nuevosDatos.getCategoria() != null && !nuevosDatos.getCategoria().trim().isEmpty())
            original.setCategoria(nuevosDatos.getCategoria());

        if (nuevosDatos.getImagenUrl() != null && !nuevosDatos.getImagenUrl().trim().isEmpty())
            original.setImagenUrl(nuevosDatos.getImagenUrl());
    }

    private Prenda recolectarDatosFormulario(HttpServletRequest request) {
        Prenda prenda = new Prenda();
        prenda.setId(Long.parseLong(request.getParameter("id")));
        prenda.setNombre(request.getParameter("nombre"));
        prenda.setDescripcion(request.getParameter("descripcion"));
        prenda.setTalla(request.getParameter("talla"));
        prenda.setEstado(request.getParameter("estado"));
        prenda.setCategoria(request.getParameter("categoria"));
        prenda.setImagenUrl(request.getParameter("imagenUrl"));
        return prenda;
    }
}

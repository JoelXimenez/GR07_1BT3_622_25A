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
        if (esValido(nuevosDatos.getNombre())) original.setNombre(nuevosDatos.getNombre());
        if (esValido(nuevosDatos.getDescripcion())) original.setDescripcion(nuevosDatos.getDescripcion());
        if (esValido(nuevosDatos.getTalla())) original.setTalla(nuevosDatos.getTalla());
        if (esValido(nuevosDatos.getEstado())) original.setEstado(nuevosDatos.getEstado());
        if (esValido(nuevosDatos.getCategoria())) original.setCategoria(nuevosDatos.getCategoria());
        if (nuevosDatos.getImagenUrl() != null && !nuevosDatos.getImagenUrl().trim().isEmpty())
            original.setImagenUrl(nuevosDatos.getImagenUrl());
    }

    private boolean esValido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private Prenda recolectarDatosFormulario(HttpServletRequest request) {
        Prenda prenda = new Prenda();
        prenda.setId(Long.parseLong(request.getParameter("id")));
        prenda.setNombre(request.getParameter("nombre"));
        prenda.setDescripcion(request.getParameter("descripcion"));
        prenda.setTalla(request.getParameter("talla"));
        prenda.setEstado(request.getParameter("estado"));
        prenda.setCategoria(request.getParameter("categoria"));
        prenda.setImagenUrl(request.getParameter("imagenUrl")); // Campo opcional
        return prenda;
    }
}

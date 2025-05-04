package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apptrueque.model.Prenda;
import org.apptrueque.model.Closet;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;

@WebServlet(name = "EliminarPrendaServlet", value = "/EliminarPrendaServlet")
public class EliminarPrendaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.parseLong(idParam);

            eliminarPrenda(id);
        }

        response.sendRedirect("MiClosetServlet");  // Regresamos a MiCloset
    }

    private void eliminarPrenda(Long id) {
        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            em = JpaUtil.getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Prenda prenda = em.find(Prenda.class, id);

            if (prenda != null) {
                eliminarPrendaDeCloset(em, prenda);
                em.remove(em.contains(prenda) ? prenda : em.merge(prenda)); // 💣 Ahora sí, eliminamos la prenda
            }

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private void eliminarPrendaDeCloset(EntityManager em, Prenda prenda) {
        Closet closet = prenda.getCloset();  // ⚡ Buscamos su closet
        if (closet != null) {
            closet.getPrendas().remove(prenda); // 🔥 Eliminamos la prenda de la lista del closet
            em.merge(closet); // Actualizamos el closet
        }
    }
}

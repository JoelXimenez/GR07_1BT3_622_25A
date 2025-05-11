package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apptrueque.model.Closet;
import org.apptrueque.model.Prenda;
import org.apptrueque.model.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/listarPrendas")
public class ListarPrendasServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Closet closet = usuario.getClosetActual();

        List<Prenda> prendas = closet.getPrendas();
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        if (prendas == null || prendas.isEmpty()) {
            out.println("<p>No tienes prendas registradas.</p>");
        } else {
            out.println("<table><thead><tr>");
            out.println("<th>Nombre</th><th>Descripción</th><th>Talla</th><th>Estado</th><th>Categoría</th><th>Acciones</th>");
            out.println("</tr></thead><tbody>");
            for (Prenda p : prendas) {
                out.println("<tr>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + (p.getDescripcion() != null ? p.getDescripcion() : "-") + "</td>");
                out.println("<td>" + (p.getTalla() != null ? p.getTalla() : "-") + "</td>");
                out.println("<td>" + (p.getEstado() != null ? p.getEstado() : "-") + "</td>");
                out.println("<td>" + (p.getCategoria() != null ? p.getCategoria() : "-") + "</td>");
                out.println("<td>");
                out.println("<button class='btn btn-verde' onclick='seleccionarPrenda(" + p.getId() + "); openModal(\"modalEditar\");'><i class=\"fas fa-edit\"></i></button>");
                out.println("<button class='btn btn-rojo' onclick='seleccionarPrenda(" + p.getId() + "); openModal(\"modalEliminar\");'><i class=\"fas fa-trash\"></i></button>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</tbody></table>");
        }
    }
}

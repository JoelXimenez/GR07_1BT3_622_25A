<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apptrueque.model.*, java.util.*, jakarta.persistence.*, org.apptrueque.util.JpaUtil" %>
<%@ page import="java.util.stream.Collectors" %>

<%
    // Obtener parámetros de filtro
    String filtroNombre = request.getParameter("filtroNombre");
    String filtroCategoria = request.getParameter("filtroCategoria");

    // Consulta base
    EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
    List<Closet> closets = em.createQuery(
            "SELECT c FROM Closet c LEFT JOIN FETCH c.prendas WHERE c.publicado = true", Closet.class
    ).getResultList();
    em.close();

%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Publicaciones - TruequeApp</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        /* Tus estilos originales (sin cambios) */
        body {
            font-family: 'Poppins', sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .contenedor {
            max-width: 900px;
            margin: auto;
        }
        .publicacion {
            background: white;
            padding: 20px;
            margin-bottom: 25px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .publicacion h3 {
            margin: 0 0 10px;
        }
        .publicacion ul {
            padding-left: 20px;
        }
        .btn-like {
            background-color: #007bff;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 10px;
        }
        .btn-like:hover {
            background-color: #0056b3;
        }

        /* Estilos nuevos SOLO para los filtros (mínimos) */
        .filtros {
            background: white;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .filtros input {
            padding: 8px;
            margin-right: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            width: 200px;
        }
        .filtros button {
            padding: 8px 15px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="contenedor">
    <h1>📰 Publicaciones de Closets</h1>

    <!-- Listado de publicaciones -->
    <% if (closets.isEmpty()) { %>
    <p>No hay closets publicados <%= (filtroNombre != null || filtroCategoria != null) ? "con esos filtros" : "todavía" %>.</p>
    <% } else {
        for (Closet closet : closets) {
            Usuario propietario = closet.getUsuario();
    %>
    <div class="publicacion">
        <h3>👤 <%= propietario.getNombre() %> publicó un closet</h3>
        <ul>
            <% for (Prenda prenda : closet.getPrendas()) { %>
            <li>
                <strong><%= prenda.getNombre() %></strong> -
                <%= prenda.getDescripcion() %>
                <span style="color: #666;">(<%= prenda.getCategoria() %>)</span>
            </li>
            <% } %>
        </ul>
        <button class="btn-like" onclick="alert('¡Te gustó esta publicación! ❤️')">
            <i class="fas fa-thumbs-up"></i> Me gusta
        </button>
    </div>
    <% }} %>

    <!-- Botón de regreso (original) -->
    <div style="text-align: center; margin-top: 30px;">
        <a href="home.jsp" style="
                display: inline-block;
                padding: 12px 25px;
                background-color: #28a745;
                color: white;
                text-decoration: none;
                border-radius: 8px;
                font-size: 16px;
                font-weight: bold;
                transition: background-color 0.3s ease;
            ">
            <i class="fas fa-arrow-left"></i> Regresar
        </a>
    </div>
</div>
</body>
</html>
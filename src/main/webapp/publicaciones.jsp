<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.persistence.*" %>
<%@ page import="org.apptrueque.util.JpaUtil" %>
<%@ page import="org.apptrueque.model.Closet" %>
<%@ page import="org.apptrueque.model.Prenda" %>
<%@ page import="org.apptrueque.model.Usuario" %>
<%@ page import="java.util.*" %>

<%
    EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
    List<Closet> closetsPublicados = em.createQuery(
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
    </style>
</head>
<body>

<div class="contenedor">
    <h1>📰 Publicaciones de Closets</h1>

    <% if (closetsPublicados.isEmpty()) { %>
    <p>No hay closets publicados todavía.</p>
    <% } else {
        for (Closet closet : closetsPublicados) {
            Usuario propietario = closet.getUsuario();
    %>
    <div class="publicacion">
        <h3>👤 <%= propietario.getNombre() %> publicó un closet</h3>
        <ul>
            <% for (Prenda prenda : closet.getPrendas()) { %>
            <li><strong><%= prenda.getNombre() %></strong>: <%= prenda.getDescripcion() %></li>
            <% } %>
        </ul>
        <button class="btn-like" onclick="alert('¡Te gustó esta publicación! ❤️')"><i class="fas fa-thumbs-up"></i> Me gusta</button>
    </div>
    <% }} %>
</div>
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

</body>
</html>

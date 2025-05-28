<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apptrueque.model.*, java.util.*, jakarta.persistence.*, org.apptrueque.util.JpaUtil" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String filtroEdad = request.getParameter("filtroEdad");
    String filtroCategoria = request.getParameter("filtroCategoria");
    String filtroUsuarioRemitente = request.getParameter("usuario");

    EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

    List<Closet> closets;
    String nombreRemitente = null;

    if (filtroUsuarioRemitente != null && !filtroUsuarioRemitente.isEmpty()) {
        // Cargar nombre del remitente
        try {
            Usuario usuarioRemitente = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", filtroUsuarioRemitente)
                    .getSingleResult();
            nombreRemitente = usuarioRemitente.getNombre();
        } catch (Exception e) {
            nombreRemitente = "usuario desconocido";
        }

        // Obtener closets del remitente
        closets = em.createQuery(
                "SELECT c FROM Closet c LEFT JOIN FETCH c.prendas WHERE c.publicado = true AND c.usuario.email = :email", Closet.class
        ).setParameter("email", filtroUsuarioRemitente).getResultList();

    } else {
        // Mostrar todos los closets excepto los del usuario actual
        closets = em.createQuery(
                "SELECT c FROM Closet c LEFT JOIN FETCH c.prendas WHERE c.publicado = true AND c.usuario.email <> :email", Closet.class
        ).setParameter("email", usuario.getEmail()).getResultList();
    }

    // Aplicar filtros de edad y categoría
    if ((filtroEdad != null && !filtroEdad.isEmpty()) || (filtroCategoria != null && !filtroCategoria.isEmpty())) {
        closets.removeIf(closet -> {
            boolean excluir = false;

            if (filtroEdad != null && !filtroEdad.isEmpty()) {
                if (closet.getEdad() == null || !closet.getEdad().equalsIgnoreCase(filtroEdad)) {
                    excluir = true;
                }
            }

            if (!excluir && filtroCategoria != null && !filtroCategoria.isEmpty()) {
                boolean algunaCoincide = closet.getPrendas().stream()
                        .anyMatch(p -> p.getCategoria() != null && p.getCategoria().equalsIgnoreCase(filtroCategoria));
                if (!algunaCoincide) {
                    excluir = true;
                }
            }

            return excluir;
        });
    }
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
        .btn-like:disabled {
            background-color: #aaa;
            cursor: not-allowed;
        }
        .btn-like:hover:not(:disabled) {
            background-color: #0056b3;
        }
        .likes-count {
            margin-top: 8px;
            font-size: 14px;
            color: #666;
        }
        .filtros {
            margin-bottom: 20px;
            background: #fff;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .filtros label {
            margin-right: 10px;
        }
        .filtros select {
            margin-right: 20px;
            padding: 5px;
        }
        .btn-reset {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 8px 14px;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="contenedor">
    <h1>📰 Publicaciones de Closets</h1>

    <% if (nombreRemitente != null) { %>
    <p style="margin-bottom: 20px;">
        Mostrando solo publicaciones del usuario:
        <strong><%= nombreRemitente %></strong>
        |
        <a href="publicaciones.jsp" class="btn-reset">Quitar filtro</a>
    </p>
    <% } %>

    <!-- Filtros -->
    <form method="get" class="filtros">
        <label for="filtroEdad">Edad:</label>
        <select name="filtroEdad" id="filtroEdad">
            <option value="">Todas</option>
            <option value="Adultos" <%= "Adultos".equals(filtroEdad) ? "selected" : "" %>>Adultos</option>
            <option value="Jovenes" <%= "Jovenes".equals(filtroEdad) ? "selected" : "" %>>Jóvenes</option>
            <option value="Niños" <%= "Niños".equals(filtroEdad) ? "selected" : "" %>>Niños</option>
            <option value="Bebes" <%= "Bebes".equals(filtroEdad) ? "selected" : "" %>>Bebés</option>
            <option value="Ancianos" <%= "Ancianos".equals(filtroEdad) ? "selected" : "" %>>Ancianos</option>
        </select>

        <label for="filtroCategoria">Categoría:</label>
        <select name="filtroCategoria" id="filtroCategoria">
            <option value="">Todas</option>
            <option value="Hombres" <%= "Hombres".equals(filtroCategoria) ? "selected" : "" %>>Hombres</option>
            <option value="Mujeres" <%= "Mujeres".equals(filtroCategoria) ? "selected" : "" %>>Mujeres</option>
            <option value="Unisex" <%= "Unisex".equals(filtroCategoria) ? "selected" : "" %>>Unisex</option>
        </select>

        <button type="submit">Filtrar</button>

        <% if (filtroEdad != null || filtroCategoria != null) { %>
        <a href="publicaciones.jsp" class="btn-reset">Quitar filtros</a>
        <% } %>
    </form>

    <!-- Conteo de resultados -->
    <p><strong><%= closets.size() %></strong> resultado(s) encontrados.</p>

    <% if (closets.isEmpty()) { %>
    <p>No hay closets publicados que coincidan con los filtros.</p>
    <% } else {
        for (Closet closet : closets) {
            Usuario propietario = closet.getUsuario();

            // Verificar si ya dio like
            TypedQuery<Like> likeQuery = em.createQuery(
                    "SELECT l FROM Like l WHERE l.closet.idCloset = :idCloset AND l.usuarioEmail = :email", Like.class);
            likeQuery.setParameter("idCloset", closet.getIdCloset());
            likeQuery.setParameter("email", usuario.getEmail());
            boolean yaDioLike = !likeQuery.getResultList().isEmpty();

            Long totalLikes = em.createQuery(
                            "SELECT COUNT(l) FROM Like l WHERE l.closet.idCloset = :idCloset", Long.class)
                    .setParameter("idCloset", closet.getIdCloset())
                    .getSingleResult();
    %>
    <div class="publicacion">
        <h3>👤 <%= propietario.getNombre() %> publicó un closet</h3>
        <p><strong>👥 Edad recomendada del closet:</strong> <%= closet.getEdad() != null ? closet.getEdad() : "No especificada" %></p>
        <ul>
            <% for (Prenda prenda : closet.getPrendas()) { %>
            <li style="margin-bottom: 10px;">
                <strong>👕 Nombre:</strong> <%= prenda.getNombre() %><br>
                <strong>📝 Descripción:</strong> <%= prenda.getDescripcion() %><br>
                <strong>📏 Talla:</strong> <%= prenda.getTalla() %><br>
                <strong>⚙️ Estado:</strong> <%= prenda.getEstado() %><br>
                <strong>🧬 Categoría:</strong> <%= prenda.getCategoria() %>
            </li>
            <% } %>
        </ul>

        <form action="LikeServlet" method="post" style="display: inline;">
            <input type="hidden" name="id" value="<%= closet.getIdCloset() %>"/>
            <button type="submit" class="btn-like">
                <i class="fas fa-thumbs-up"></i>
                <%= yaDioLike ? "Quitar Like" : "Me gusta" %>
            </button>
        </form>

        <div class="likes-count">
            👍 <%= totalLikes %> personas les gusta esto
        </div>
    </div>
    <% }} em.close(); %>

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
                transition: background-color 0.3s ease;">
            <i class="fas fa-arrow-left"></i> Regresar
        </a>
    </div>
</div>
</body>
</html>

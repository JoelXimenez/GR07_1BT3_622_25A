<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apptrueque.model.Usuario" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.apptrueque.model.Prenda" %>
<%@ page import="jakarta.persistence.EntityManager" %>
<%@ page import="org.apptrueque.util.JpaUtil" %>
<%@ page import="org.apptrueque.model.Closet" %>
<%@ page import="java.util.List" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil de Usuario</title>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(to right, #74ebd5, #ACB6E5);
            margin: 0;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .perfil-container {
            background: #ffffff;
            padding: 40px 30px;
            border-radius: 15px;
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
            width: 400px;
            position: relative;
            text-align: center;
        }
        .perfil-container h2 {
            color: #333;
            margin-bottom: 20px;
            font-size: 28px;
        }
        .datos-usuario p {
            font-size: 18px;
            color: #555;
            margin: 10px 0;
        }
        .boton {
            margin-top: 20px;
            display: inline-block;
            padding: 12px 25px;
            border-radius: 8px;
            text-decoration: none;
            font-size: 16px;
            font-weight: bold;
            transition: background-color 0.3s ease;
            cursor: pointer;
            border: none;
        }
        .editar-btn {
            background-color: #007bff;
            color: white;
        }
        .editar-btn:hover {
            background-color: #0056b3;
        }
        .regresar-btn {
            background-color: #28a745;
            color: white;
            margin-top: 10px;
        }
        .regresar-btn:hover {
            background-color: #1e7e34;
        }

        /* Modal */
        #modalEditarPerfil {
            display: none;
            position: fixed;
            top: 0; left: 0;
            width: 100%; height: 100%;
            background: rgba(0,0,0,0.6);
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }
        .modal-contenido {
            background: white;
            padding: 30px;
            border-radius: 15px;
            width: 400px;
            text-align: center;
            box-shadow: 0 8px 16px rgba(0,0,0,0.3);
            animation: aparecer 0.5s ease;
        }
        @keyframes aparecer {
            from { opacity: 0; transform: scale(0.8);}
            to { opacity: 1; transform: scale(1);}
        }
        .modal-contenido h2 {
            margin-bottom: 20px;
            color: #333;
        }
        .modal-contenido input {
            width: 90%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 8px;
            border: 1px solid #ccc;
            font-size: 16px;
        }
        .modal-contenido button {
            margin-top: 15px;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            background-color: #007bff;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }
        .modal-contenido button:hover {
            background-color: #0056b3;
        }
        .modal-cancelar {
            background-color: #dc3545;
            margin-left: 10px;
        }
        .modal-cancelar:hover {
            background-color: #c82333;
        }
    </style>
</head>

<body>

<div class="perfil-container">
    <h2>Perfil de Usuario</h2>

    <div class="datos-usuario">
        <p><strong>Nombre:</strong> <%= usuario.getNombre() %></p>
        <p><strong>Cédula:</strong> <%= usuario.getCedula() %></p>
        <p><strong>Email:</strong> <%= usuario.getEmail() %></p>
        <p><strong>Fecha Registro:</strong> <%= formatoFecha.format(usuario.getFechaRegistro()) %></p>
    </div>

    <!-- Botón Editar Perfil -->
    <button class="boton editar-btn" onclick="abrirModal('modalEditarPerfil')">Editar Perfil</button>


    <!-- Botón Ver Closets Publicados -->
    <button class="boton editar-btn" style="background-color: #17a2b8;" onclick="mostrarModalClosets()">
        👕 Ver closet
    </button>

    <!-- Botón Regresar -->
    <a class="boton regresar-btn" href="home.jsp">Regresar</a>
</div>

<!-- Modal para editar perfil -->
<div id="modalEditarPerfil" class="modal">
    <div class="modal-contenido">
        <h2>Editar Perfil</h2>
        <form action="editar-perfil" method="post">
            <input type="hidden" name="cedula" value="<%= usuario.getCedula() %>">

            <input type="text" name="nombre" placeholder="Nuevo Nombre">
            <input type="email" name="email" placeholder="Nuevo Email">
            <input type="password" name="password" placeholder="Nueva Contraseña">

            <p style="font-size: 14px; color: #777">Puedes dejar en blanco los campos que no desees modificar.</p>

            <button type="submit">Guardar Cambios</button>
            <button type="button" class="modal-cancelar" onclick="cerrarModal('modalEditarPerfil')">Cancelar</button>
        </form>

    </div>
</div>
<!-- Modal Ver Closets Publicados -->
<div id="modalClosetsPublicados" class="modal" style="display: none; justify-content: center; align-items: center;">
    <div class="modal-contenido" style="max-height: 80vh; overflow-y: auto; width: 600px; text-align: left; position: relative;">
        <span class="close" onclick="cerrarModal('modalClosetsPublicados')" style="position: absolute; top: 10px; right: 20px; font-size: 28px;">&times;</span>
        <%
            EntityManager em3 = JpaUtil.getEntityManagerFactory().createEntityManager();
            List<org.apptrueque.model.Closet> closets = em3.createQuery(
                            "SELECT c FROM Closet c LEFT JOIN FETCH c.prendas WHERE c.usuario.cedula = :cedula AND c.publicado = true",
                            org.apptrueque.model.Closet.class)
                    .setParameter("cedula", usuario.getCedula())
                    .getResultList();

            if (closets.isEmpty()) {
        %>
        <h3 style="text-align: center; color: #777; margin-top: 40px;">⚠️ Aún no has publicado tu closet.</h3>
        <%
        } else {
        %>
        <h2 style="text-align: center; color: #333;">👕 Tu closet</h2>
        <% for (org.apptrueque.model.Closet closet : closets) { %>
        <div style="border: 1px solid #ccc; margin-bottom: 20px; padding: 15px; border-radius: 8px; background-color: #f9f9f9;">
            <h4 style="margin-bottom: 5px;">👥 Closet para: <%= closet.getEdad() != null ? closet.getEdad() : "No especificado" %></h4>

            <% for (org.apptrueque.model.Prenda prenda : closet.getPrendas()) { %>
            <div style="border: 1px solid #ddd; padding: 10px; margin-bottom: 10px; border-radius: 6px; background: #fff;">
                <p><strong>👕 Nombre:</strong> <%= prenda.getNombre() %></p>
                <p><strong>📝 Descripción:</strong> <%= prenda.getDescripcion() %></p>
                <p><strong>📏 Talla:</strong> <%= prenda.getTalla() %></p>
                <p><strong>⚙️ Estado:</strong> <%= prenda.getEstado() %></p>
                <p><strong>🧬 Categoría:</strong> <%= prenda.getCategoria() %></p>
                <% if (prenda.getImagenUrl() != null && !prenda.getImagenUrl().isEmpty()) { %>
                <img src="<%= prenda.getImagenUrl() %>" alt="Imagen prenda" style="max-width: 100px; border-radius: 8px; margin-top: 5px;">
                <% } %>
            </div>
            <% } %>
            <form method="POST" action="DespublicarClosetServlet" style="text-align: right;">
                <input type="hidden" name="idCloset" value="<%= closet.getIdCloset() %>">
                <button type="submit" class="boton editar-btn" style="background-color: #dc3545;">❌ Quitar publicación</button>
            </form>
        </div>
        <% } } em3.close(); %>
    </div>
</div>



<script>
    function abrirModal(id) {
        document.getElementById(id).style.display = 'flex';
    }

    function cerrarModal(id) {
        document.getElementById(id).style.display = 'none';
    }

    function mostrarModalClosets() {
        abrirModal('modalClosetsPublicados');
    }
</script>

</body>
</html>

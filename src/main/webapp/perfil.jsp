<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apptrueque.model.Usuario" %>
<%@ page import="java.text.SimpleDateFormat" %>

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

    <button class="boton editar-btn" onclick="abrirModal()">Editar Perfil</button><br>
    <a class="boton regresar-btn" href="home.jsp">Regresar</a>
</div>

<!-- Modal para editar perfil -->
<div id="modalEditarPerfil" class="modal">
    <div class="modal-contenido">
        <h2>Editar Perfil</h2>
        <form action="editar-perfil" method="post">
            <input type="hidden" name="cedula" value="<%= usuario.getCedula() %>">
            <input type="text" name="nombre" value="<%= usuario.getNombre() %>" placeholder="Nuevo Nombre" required><br>
            <input type="email" name="email" value="<%= usuario.getEmail() %>" placeholder="Nuevo Email" required><br>
            <input type="password" name="password" placeholder="Nueva Contraseña" required><br>
            <button type="submit">Guardar Cambios</button>
            <button type="button" class="modal-cancelar" onclick="cerrarModal()">Cancelar</button>
        </form>
    </div>
</div>

<script>
    function abrirModal() {
        document.getElementById('modalEditarPerfil').style.display = 'flex';
    }
    function cerrarModal() {
        document.getElementById('modalEditarPerfil').style.display = 'none';
    }
</script>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apptrueque.model.Usuario" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
  Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
  SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
%>

<html>
<head>
  <title>Perfil de Usuario</title>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #f4f6f8;
      margin: 0;
      padding: 20px;
    }
    .perfil-container {
      width: 80%;
      margin: 30px auto;
      background-color: #fff;
      padding: 30px;
      border-radius: 12px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
      position: relative;
    }
    .editar-btn, .regresar-btn {
      padding: 10px 20px;
      border: none;
      border-radius: 8px;
      text-decoration: none;
      font-size: 16px;
      cursor: pointer;
    }
    .editar-btn {
      position: absolute;
      top: 20px;
      right: 20px;
      background-color: #007bff;
      color: white;
    }
    .regresar-btn {
      background-color: #28a745;
      color: white;
      margin-top: 20px;
      display: inline-block;
    }
    .datos-usuario {
      margin-bottom: 30px;
    }
    .datos-usuario h2 {
      color: #333;
      margin-bottom: 20px;
    }
    .datos-usuario p {
      font-size: 18px;
      margin: 8px 0;
      color: #555;
    }
  </style>
</head>
<body>

<div class="perfil-container">
  <a class="editar-btn" href="editarPerfil.jsp">Editar Perfil</a>

  <div class="datos-usuario">
    <h2>Datos del Usuario</h2>
    <p><strong>Nombre:</strong> <%= usuario != null ? usuario.getNombre() : "No disponible" %></p>
    <p><strong>Cédula:</strong> <%= usuario != null ? usuario.getCedula() : "No disponible" %></p>
    <p><strong>Email:</strong> <%= usuario != null ? usuario.getEmail() : "No disponible" %></p>
    <p><strong>Fecha de Registro:</strong>
      <%= (usuario != null && usuario.getFechaRegistro() != null)
              ? formatoFecha.format(usuario.getFechaRegistro())
              : "No disponible" %>
    </p>
  </div>

  <a class="regresar-btn" href="home.jsp">Regresar</a>
</div>

</body>
</html>

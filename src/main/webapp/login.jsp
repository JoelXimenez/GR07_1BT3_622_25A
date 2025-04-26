<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>TruequeApp - Iniciar Sesión</title>
    <style>
        body {
            background: linear-gradient(to right, #74ebd5, #ACB6E5);
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .login-container {
            background: white;
            padding: 40px 30px;
            border-radius: 15px;
            box-shadow: 0 8px 16px rgba(0,0,0,0.25);
            width: 300px;
            text-align: center;
        }

        .login-container h1 {
            margin-bottom: 25px;
            color: #333;
        }

        label {
            display: block;
            margin-bottom: 5px;
            text-align: left;
            color: #555;
            font-weight: bold;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }

        input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        .link-volver {
            margin-top: 15px;
            display: inline-block;
            color: #333;
            text-decoration: none;
            font-size: 14px;
        }

        .link-volver:hover {
            text-decoration: underline;
        }

        .error {
            color: red;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            padding: 10px;
            border-radius: 5px;
            margin-top: 15px;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h1>Iniciar Sesión</h1>
    <form action="login-servlet" method="post">
        <label for="email">Email:</label>
        <input type="text" id="email" name="email" required>

        <label for="password">Contraseña:</label>
        <input type="password" id="password" name="password" required>

        <input type="submit" value="Iniciar Sesión">
    </form>

    <%-- Mostrar el mensaje de error si existe --%>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <div class="error">
        <%= errorMessage %>
    </div>
    <%
        }
    %>

    <a href="singup.jsp" class="link-volver">¿No tienes cuenta?, Regístrate</a>
</div>
</body>
</html>

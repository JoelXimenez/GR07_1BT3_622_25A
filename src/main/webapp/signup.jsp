<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TruequeApp - Registro</title>
    <style>
        body {
            background-color: #f0f2f5;
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .registro-container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            width: 350px;
        }
        .registro-container h1 {
            text-align: center;
            margin-bottom: 30px;
            color: #333333;
        }
        .registro-container label {
            font-weight: bold;
            color: #555555;
        }
        .registro-container input[type="text"],
        .registro-container input[type="email"],
        .registro-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 20px;
            border: 1px solid #cccccc;
            border-radius: 5px;
        }
        .registro-container button,
        .registro-container a.button-cancelar {
            width: 100%; /* Este puede mantenerse si quieres que ocupen el ancho completo, pero se puede ajustar */
            padding: 12px;
            margin-top: 10px;
            display: inline-block;
            text-align: center;
            font-size: 16px;
            border-radius: 5px;
            text-decoration: none;
            box-sizing: border-box; /* Asegura que el padding no agrande el ancho */
        }
        .registro-container button {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        .registro-container button:hover {
            background-color: #45a049;
        }
        .registro-container a.button-cancelar {
            background-color: #f44336;
            color: white;
        }
        .registro-container a.button-cancelar:hover {
            background-color: #d32f2f;
        }

        /* Cambia el ancho aquí si quieres un tamaño específico */
        .registro-container button,
        .registro-container a.button-cancelar {
            width: calc(50% - 10px); /* Ajusta el tamaño de los botones */
            display: inline-block;
        }

        /* Separar un poco los botones */
        .registro-container .button-cancelar {
            margin-left: 10px;
        }

    </style>
</head>
<body>

<div class="registro-container">
    <h1>Registro de Usuario</h1>
    <form action="signup" method="post">
        <label for="cedula">Cédula:</label>
        <input type="text" id="cedula" name="cedula" required>

        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" required>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>

        <label for="password">Contraseña:</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Registrarse</button>
        <a href="login.jsp" class="button-cancelar">Cancelar</a>
    </form>
</div>

</body>
</html>

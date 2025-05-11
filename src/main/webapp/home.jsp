<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>TruequeApp - Inicio</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"> <!-- Iconos FontAwesome -->
    <style>
        body {
            background: linear-gradient(to right, #74ebd5, #ACB6E5);
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }

        .container {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .home-container {
            background-color: white;
            width: 90%;
            max-width: 400px;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 8px 16px rgba(0,0,0,0.15);
            text-align: center;
        }

        h1 {
            color: #333;
            margin-bottom: 30px;
        }

        .btn-container {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        /* Estilo general para todos los botones */
        .btn-container a {
            text-decoration: none;
            color: white;
            font-size: 18px;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            display: flex;
            justify-content: center;
            align-items: center;
            transition: background-color 0.3s ease;
        }

        /* Botones normales (verde) */
        .btn-verde {
            background-color: #4CAF50;
        }

        .btn-verde:hover {
            background-color: #45a049;
        }

        /* Botón especial de cerrar sesión (rojo) */
        .logout-btn {
            background-color: #f44336;
        }

        .logout-btn:hover {
            background-color: #d32f2f;
        }

        .btn-container a i {
            margin-right: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="home-container">
        <h1>Bienvenido a TruequeApp</h1>

        <div class="btn-container">
            <a href="miCloset.jsp" class="btn-verde"><i class="fas fa-tshirt"></i> Mi Closet</a>
            <a href="publicaciones.jsp" class="btn-verde"><i class="fas fa-bullhorn"></i> Publicaciones</a>
            <a href="chat.jsp" class="btn-verde"><i class="fas fa-comment-dots"></i> Mensajería</a>
            <a href="perfil.jsp" class="btn-verde"><i class="fas fa-user"></i> Mi Perfil</a>
            <a href="login.jsp" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Cerrar Sesión</a>
        </div>
    </div>
</div>

</body>
</html>

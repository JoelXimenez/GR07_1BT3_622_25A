<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>TruequeApp</title>
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
            max-width: 600px;
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
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }

        .btn-container a {
            text-decoration: none;
            color: white;
            font-size: 18px;
            padding: 15px;
            background-color: #4CAF50;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            display: flex;
            justify-content: center;
            align-items: center;
            transition: background-color 0.3s ease;
        }

        .btn-container a:hover {
            background-color: #45a049;
        }

        .btn-container a i {
            margin-right: 10px;
        }

        .logout-btn {
            background-color: #f44336;
            margin-top: 20px;
            padding: 12px;
            text-align: center;
            width: 100%;
        }

        .logout-btn:hover {
            background-color: #d32f2f;
        }

        .logout-btn i {
            margin-right: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="home-container">
        <h1>Bienvenido a TruequeApp</h1>

        <!-- Botones de navegación -->
        <div class="btn-container">
            <a href="crearPublicacion.jsp"><i class="fas fa-plus"></i> Crear Publicación</a>
            <a href="publicaciones.jsp"><i class="fas fa-th-list"></i> Publicaciones</a>
            <a href="perfil.jsp"><i class="fas fa-user"></i> Mi perfil</a>
            <a href="mensajeria.jsp"><i class="fas fa-comment-dots"></i> Mensajería</a>
            <a href="misPublicaciones.jsp"><i class="fas fa-archive"></i> Mis Publicaciones</a>
            <a href="proponerTrueque.jsp"><i class="fas fa-exchange-alt"></i> Proponer Trueque</a>
            <a href="truequesPendientes.jsp"><i class="fas fa-clock"></i> Trueques Pendientes</a>
        </div>

        <!-- Botón de Cerrar sesión -->
        <a href="login.jsp" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Cerrar Sesión</a>
    </div>
</div>

</body>
</html>

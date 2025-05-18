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
        <!-- Botón de notificaciones -->
        <div style="position: absolute; top: 20px; left: 20px;">
            <button onclick="abrirModal()" style="background-color: #ff9800; border: none; padding: 10px 15px; border-radius: 50%; cursor: pointer; color: white;">
                <i class="fas fa-bell"></i>
            </button>
        </div>

        <div class="btn-container">
            <a href="miCloset.jsp" class="btn-verde"><i class="fas fa-tshirt"></i> Mi Closet</a>
            <a href="publicaciones.jsp" class="btn-verde"><i class="fas fa-bullhorn"></i> Publicaciones</a>
            <a href="MensajeServlet" class="btn-verde"><i class="fas fa-comment-dots"></i> Mensajería</a>
            <a href="perfil.jsp" class="btn-verde"><i class="fas fa-user"></i> Mi Perfil</a>
            <a href="login.jsp" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Cerrar Sesión</a>
        </div>
        <!-- Modal -->
        <div id="modalNotificaciones" style="display: none; position: fixed; top: 70px; left: 20px; background-color: white; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.2); padding: 20px; z-index: 1000; width: 300px;">
            <h3 style="margin-top: 0;">Notificaciones</h3>
            <ul id="notificacionesLista" style="list-style: none; padding-left: 0;"></ul>
            <button onclick="cerrarModal()" style="margin-top: 10px;">Cerrar</button>
        </div>
    </div>
</div>
<script>
    function abrirModal() {
        document.getElementById('modalNotificaciones').style.display = 'block';
        fetch('NotificacionesServlet')
            .then(response => response.json())
            .then(data => {
                const lista = document.getElementById('notificacionesLista');
                lista.innerHTML = '';

                if (data.length === 0) {
                    lista.innerHTML = "<li style='color: #777;'>No hay notificaciones nuevas.</li>";
                    return;
                }

                data.forEach(notif => {
                    const li = document.createElement('li');
                    li.style.display = 'flex';
                    li.style.justifyContent = 'space-between';
                    li.style.alignItems = 'center';
                    li.style.marginBottom = '10px';
                    li.style.border = '1px solid #ddd';
                    li.style.borderRadius = '5px';
                    li.style.padding = '8px';

                    const link = document.createElement('a');
                    link.href = "publicaciones.jsp?usuario=" + encodeURIComponent(notif.usuarioRemitente);
                    link.style.textDecoration = 'none';
                    link.style.color = '#333';
                    link.textContent = notif.mensaje;
                    link.style.flex = '1';

                    const eliminarBtn = document.createElement('button');
                    eliminarBtn.textContent = '❌';
                    eliminarBtn.title = 'Eliminar notificación';
                    eliminarBtn.style.marginLeft = '10px';
                    eliminarBtn.style.border = 'none';
                    eliminarBtn.style.background = 'transparent';
                    eliminarBtn.style.cursor = 'pointer';
                    eliminarBtn.onclick = () => eliminarNotificacion(notif.id);

                    li.appendChild(link);
                    li.appendChild(eliminarBtn);
                    lista.appendChild(li);
                });
            });
    }

    function cerrarModal() {
        document.getElementById('modalNotificaciones').style.display = 'none';
    }

    function eliminarNotificacion(id) {
        fetch('EliminarNotificacionServlet?id=' + id) // <- método GET, más compatible
            .then(response => {
                if (response.ok) {
                    abrirModal(); // recargar notificaciones
                } else {
                    alert('No se pudo eliminar la notificación.');
                }
            });
    }
</script>
</body>
</html>
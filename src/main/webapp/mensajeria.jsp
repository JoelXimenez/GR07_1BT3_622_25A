<%@ page session="true" contentType="text/html; charset=UTF-8" %>
<%
    String usuarioActual = request.getParameter("usuario");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Chat - TruequeApp</title>
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', Tahoma, sans-serif;
            display: flex;
            height: 100vh;
            background: #ededed;
        }

        .sidebar {
            width: 25%;
            background: #fff;
            border-right: 1px solid #ccc;
            display: flex;
            flex-direction: column;
        }

        .search-box {
            padding: 15px;
            border-bottom: 1px solid #ccc;
        }

        .search-box input {
            width: 100%;
            padding: 8px;
            font-size: 14px;
            border-radius: 20px;
            border: 1px solid #ccc;
        }

        .user-list {
            flex: 1;
            overflow-y: auto;
        }

        .user-item {
            padding: 12px 15px;
            border-bottom: 1px solid #f0f0f0;
            cursor: pointer;
        }

        .user-item:hover {
            background-color: #f9f9f9;
        }

        .chat-area {
            flex: 1;
            display: flex;
            flex-direction: column;
            background-color: #e5ddd5;
        }

        .chat-header {
            background-color: #075e54;
            color: white;
            padding: 15px;
            font-weight: bold;
        }

        .chat-box {
            flex: 1;
            padding: 15px;
            overflow-y: auto;
        }

        .message {
            max-width: 60%;
            margin-bottom: 10px;
            padding: 10px 15px;
            border-radius: 10px;
            background-color: #dcf8c6;
            align-self: flex-end;
        }

        .message.other {
            background-color: white;
            align-self: flex-start;
        }

        .chat-input {
            display: flex;
            padding: 10px;
            background: #f0f0f0;
        }

        .chat-input input {
            flex: 1;
            padding: 10px;
            border-radius: 20px;
            border: 1px solid #ccc;
            font-size: 14px;
        }

        .chat-input button {
            margin-left: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 20px;
            padding: 10px 20px;
            font-size: 14px;
            cursor: pointer;
        }

        .chat-input button:hover {
            background-color: #388E3C;
        }
    </style>
</head>
<body>

<div class="sidebar">
    <div class="search-box">
        <input type="text" id="busqueda" placeholder="Buscar usuario...">
    </div>
    <div class="user-list" id="listaUsuarios">
        <!-- Lista de usuarios se carga aquí -->
    </div>
</div>
<div style="padding: 10px; border-top: 1px solid #ccc;">
    <form action="home.jsp" method="get">
        <button type="submit" style="
            background-color: #f44336;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 20px;
            font-size: 14px;
            cursor: pointer;
            width: 100%;
        ">Regresar a Inicio</button>
    </form>
</div>

<div class="chat-area">
    <div class="chat-header" id="chatHeader">Selecciona un usuario</div>
    <div class="chat-box" id="chatBox">
        <!-- Mensajes del chat -->
    </div>
    <div class="chat-input">
        <input type="text" id="mensajeInput" placeholder="Escribe un mensaje..." disabled>
        <button onclick="enviarMensaje()" disabled id="btnEnviar">Enviar</button>
    </div>
</div>

<script>
    const usuarioActual = "<%= usuarioActual %>";
    let usuarioDestino = null;

    function escapeHTML(str) {
        return str.replace(/&/g, "&amp;").replace(/</g, "&lt;")
            .replace(/>/g, "&gt;").replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    function cargarUsuarios() {
        fetch("usuarios?excluir=" + encodeURIComponent(usuarioActual))
            .then(res => res.json())
            .then(data => {
                const lista = document.getElementById("listaUsuarios");
                const filtro = document.getElementById("busqueda").value.toLowerCase();
                lista.innerHTML = "";
                data.forEach(user => {
                    if (user.email.toLowerCase().includes(filtro) || user.nombre.toLowerCase().includes(filtro)) {
                        const div = document.createElement("div");
                        div.className = "user-item";
                        div.innerText = user.nombre + " (" + user.email + ")";
                        div.onclick = () => seleccionarUsuario(user);
                        lista.appendChild(div);
                    }
                });
            });
    }

    function seleccionarUsuario(user) {
        usuarioDestino = user.email;
        document.getElementById("chatHeader").innerText = "Chateando con " + user.nombre;
        document.getElementById("mensajeInput").disabled = false;
        document.getElementById("btnEnviar").disabled = false;
        cargarMensajes();
    }

    function cargarMensajes() {
        if (!usuarioDestino) return;
        fetch("mensajeria?usuario1=" + encodeURIComponent(usuarioActual) + "&usuario2=" + encodeURIComponent(usuarioDestino))
            .then(res => res.json())
            .then(data => {
                const chat = document.getElementById("chatBox");
                chat.innerHTML = "";
                data.forEach(msg => {
                    const div = document.createElement("div");
                    div.className = "message " + (msg.remitente === usuarioActual ? "" : "other");
                    div.innerHTML = escapeHTML(msg.contenido);
                    chat.appendChild(div);
                });
                chat.scrollTop = chat.scrollHeight;
            });
    }

    function enviarMensaje() {
        const input = document.getElementById("mensajeInput");
        const contenido = input.value.trim();
        if (!contenido || !usuarioDestino) return;

        fetch("mensajeria", {
            method: "POST",
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: "remitente=" + encodeURIComponent(usuarioActual) +
                "&destinatario=" + encodeURIComponent(usuarioDestino) +
                "&contenido=" + encodeURIComponent(contenido)
        }).then(() => {
            input.value = "";
            cargarMensajes();
        });
    }

    document.getElementById("busqueda").addEventListener("input", cargarUsuarios);
    setInterval(() => {
        if (usuarioDestino) cargarMensajes();
    }, 3000);

    cargarUsuarios();
</script>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.persistence.*" %>
<%@ page import="org.apptrueque.model.Usuario" %>
<%@ page import="org.apptrueque.util.JpaUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apptrueque.model.Mensaje" %>
<%
    String remitente = (String) session.getAttribute("usuarioEmail");
    String destinatario = request.getParameter("destinatario");

    EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
    List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u WHERE u.email <> :email", Usuario.class)
            .setParameter("email", remitente)
            .getResultList();
    em.close();
    List<Mensaje> mensajes = new ArrayList<>();
    if (destinatario != null && !destinatario.isEmpty()) {
        EntityManager emMsg = JpaUtil.getEntityManagerFactory().createEntityManager();
        mensajes = emMsg.createQuery("SELECT m FROM Mensaje m WHERE " +
                        "(m.remitenteEmail = :remitente AND m.destinatarioEmail = :destinatario) OR " +
                        "(m.remitenteEmail = :destinatario AND m.destinatarioEmail = :remitente) " +
                        "ORDER BY m.fechaHora ASC", Mensaje.class)
                .setParameter("remitente", remitente)
                .setParameter("destinatario", destinatario)
                .getResultList();
        emMsg.close();
    }
%>

%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Chat - TruequeApp</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background-color: #e5ddd5;
        }

        .chat-container {
            display: flex;
            height: 100vh;
        }

        .sidebar {
            width: 300px;
            background-color: #fff;
            border-right: 1px solid #ccc;
            padding: 20px;
        }

        .sidebar h2 {
            margin-bottom: 10px;
        }

        .sidebar select {
            width: 100%;
            padding: 8px;
            font-size: 16px;
        }

        .chat-main {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            background: #ece5dd;
        }

        .chat-header {
            background-color: #075E54;
            color: white;
            padding: 10px 20px;
            font-size: 18px;
        }

        .chat-box {
            flex-grow: 1;
            padding: 20px;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
        }

        .mensaje {
            max-width: 60%;
            margin-bottom: 10px;
            padding: 10px 15px;
            border-radius: 10px;
            font-size: 15px;
            line-height: 1.4;
        }

        .mio {
            align-self: flex-end;
            background-color: #dcf8c6;
        }

        .otro {
            align-self: flex-start;
            background-color: #fff;
        }

        .chat-form {
            display: flex;
            padding: 10px 20px;
            background-color: #f0f0f0;
        }

        .chat-form textarea {
            flex-grow: 1;
            resize: none;
            padding: 10px;
            border-radius: 10px;
            border: 1px solid #ccc;
        }

        .chat-form button {
            background-color: #128C7E;
            color: white;
            border: none;
            padding: 10px 20px;
            margin-left: 10px;
            border-radius: 10px;
            cursor: pointer;
        }

        .chat-form button:hover {
            background-color: #075E54;
        }

        .volver {
            margin-top: 20px;
            display: block;
            text-align: center;
            color: #d32f2f;
            font-weight: bold;
            text-decoration: none;
        }

        .volver:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="chat-container">
    <div class="sidebar">
        <h2>Chatear con:</h2>
        <form id="formDestinatario" method="get" action="chat.jsp">
            <select name="destinatario" onchange="document.getElementById('formDestinatario').submit()">
                <option value="">-- Selecciona usuario --</option>
                <% for (Usuario u : usuarios) { %>
                <option value="<%= u.getEmail() %>" <%= u.getEmail().equals(destinatario) ? "selected" : "" %>>
                    <%= u.getNombre() %> (<%= u.getEmail() %>)
                </option>
                <% } %>
            </select>
        </form>

        <a href="home.jsp" class="volver"><i class="fas fa-arrow-left"></i> Volver</a>
    </div>

    <div class="chat-main">
        <% if (destinatario != null && !destinatario.isEmpty()) { %>
        <div class="chat-header">
            Conversación con: <%= destinatario %>
        </div>
        <div id="chat-box" class="chat-box">
            <% for (Mensaje m : mensajes) { %>
            <div class="mensaje <%= m.getRemitenteEmail().equals(remitente) ? "mio" : "otro" %>">
                <%= m.getContenido() %>
            </div>
            <% } %>
        </div>


        <form id="form-mensaje" class="chat-form">
            <input type="hidden" name="remitente" value="<%= remitente %>">
            <input type="hidden" name="destinatario" value="<%= destinatario %>">
            <textarea name="contenido" rows="2" placeholder="Escribe un mensaje..." required></textarea>
            <button type="submit">Enviar</button>
        </form>
        <% } else { %>
        <div class="chat-header">Selecciona un usuario a la izquierda</div>
        <% } %>
    </div>
</div>

<% if (destinatario != null && !destinatario.isEmpty()) { %>
<script>
    function cargarMensajes() {
        fetch('ObtenerMensajesServlet?remitente=<%= remitente %>&destinatario=<%= destinatario %>')
            .then(response => response.json())
            .then(mensajes => {
                const chatBox = document.getElementById('chat-box');
                chatBox.innerHTML = '';
                mensajes.forEach(msg => {
                    const div = document.createElement('div');
                    div.className = 'mensaje ' + (msg.remitente === '<%= remitente %>' ? 'mio' : 'otro');
                    div.innerHTML = msg.contenido;
                    chatBox.appendChild(div);
                });
                chatBox.scrollTop = chatBox.scrollHeight;
            });
    }

    document.getElementById('form-mensaje').addEventListener('submit', function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        fetch('EnviarMensajeServlet', {
            method: 'POST',
            body: formData
        }).then(() => {
            this.contenido.value = '';
            cargarMensajes();
        });
    });

    setInterval(cargarMensajes, 3000);
    cargarMensajes();
</script>
<% } %>

</body>
</html>

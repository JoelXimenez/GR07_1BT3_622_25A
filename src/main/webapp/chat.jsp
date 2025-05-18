<%@ page import="java.util.*, org.apptrueque.model.Usuario" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String usuarioLogueado = usuario.getEmail();
    String receptor = (String) request.getAttribute("receptor");
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");

    String nombreReceptor = "Selecciona un usuario";
    if (receptor != null && usuarios != null) {
        for (Usuario u : usuarios) {
            if (receptor.equals(u.getEmail())) {
                nombreReceptor = u.getNombre();
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Chat - TruequeApp</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #ece5dd;
            margin: 0;
            padding: 0;
        }
        .chat-container {
            display: flex;
            flex-direction: column;
            max-width: 700px;
            height: 90vh;
            margin: 30px auto;
            background-color: #fff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 0 10px rgba(0,0,0,0.2);
        }
        .chat-header {
            background-color: #075e54;
            color: white;
            padding: 15px;
            font-size: 18px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .chat-header .left-section {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .btn-regresar {
            background-color: white;
            color: #075e54;
            border: none;
            padding: 6px 12px;
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
            font-weight: bold;
        }
        .chat-messages {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
            background-color: #e5ddd5;
        }
        .message {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 7px;
            max-width: 60%;
            clear: both;
            position: relative;
        }
        .sent {
            background-color: #dcf8c6;
            float: right;
        }
        .received {
            background-color: #fff;
            float: left;
        }
        .timestamp {
            font-size: 11px;
            color: #555;
            margin-top: 5px;
            text-align: right;
        }
        .chat-input {
            display: flex;
            padding: 10px;
            background-color: #f0f0f0;
        }
        .chat-input textarea {
            flex: 1;
            padding: 10px;
            resize: none;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        .chat-input button {
            padding: 10px 20px;
            margin-left: 10px;
            background-color: #128c7e;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        select {
            padding: 8px;
            margin: 10px;
            width: 95%;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<div class="chat-container">
    <div class="chat-header">
        <div class="left-section">
            <form action="home.jsp" method="get" style="margin: 0;">
                <button class="btn-regresar" type="submit">Regresar</button>
            </form>
            <span><strong>Chat con:</strong> <%= nombreReceptor %></span>
        </div>
        <form method="get" action="MensajeServlet">
            <select name="destinatario" onchange="this.form.submit()">
                <option value="">-- Elige un usuario --</option>
                <% if (usuarios != null && !usuarios.isEmpty()) {
                    for (Usuario u : usuarios) {
                        String selected = receptor != null && receptor.equals(u.getEmail()) ? "selected" : "";
                %>
                <option value="<%= u.getEmail() %>" <%= selected %>><%= u.getNombre() %></option>
                <% } } else { %>
                <option disabled>No hay otros usuarios registrados</option>
                <% } %>
            </select>
        </form>
    </div>

    <% if (receptor != null) { %>
    <div class="chat-messages" id="chatMensajes"></div>

    <form method="post" action="MensajeServlet" class="chat-input">
        <textarea name="contenido" rows="2" placeholder="Escribe un mensaje..." required></textarea>
        <input type="hidden" name="destinatario" value="<%= receptor %>">
        <button type="submit">Enviar</button>
    </form>
    <% } %>
</div>

<script>
    function cargarMensajes() {
        const chatBox = document.getElementById("chatMensajes");
        const remitente = "<%= usuarioLogueado %>";
        const destinatario = "<%= receptor %>";

        if (!destinatario) return;

        fetch("MensajesAjaxServlet?remitente=" + remitente + "&destinatario=" + destinatario)
            .then(response => response.text())
            .then(html => {
                chatBox.innerHTML = html;
                chatBox.scrollTop = chatBox.scrollHeight;
            });
    }

    setInterval(cargarMensajes, 3000);
    window.onload = cargarMensajes;
</script>
</body>
</html>

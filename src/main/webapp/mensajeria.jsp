<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, org.apptrueque.model.Usuario, org.apptrueque.model.Mensaje" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mensajería - TruequeApp</title>
    <style>
        body {
            background: linear-gradient(to right, #74ebd5, #ACB6E5);
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 800px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }

        h1 {
            color: #333;
            text-align: center;
        }

        form {
            margin-top: 20px;
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        select, textarea, button {
            padding: 10px;
            font-size: 16px;
            border-radius: 6px;
            border: 1px solid #ccc;
        }

        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }

        button:hover {
            background-color: #45a049;
        }

        .mensajes {
            margin-top: 30px;
            max-height: 300px;
            overflow-y: auto;
            border-top: 1px solid #ccc;
            padding-top: 20px;
        }

        .mensaje {
            margin-bottom: 10px;
        }

        .mensaje.remitente {
            text-align: right;
            color: blue;
        }

        .mensaje.destinatario {
            text-align: left;
            color: green;
        }

        .btn-regresar {
            margin-top: 30px;
            display: block;
            padding: 10px 20px;
            background-color: #f44336;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            text-align: center;
            text-decoration: none;
        }

        .btn-regresar:hover {
            background-color: #d32f2f;
        }
    </style>
</head>

<body>

<div class="container">
    <h1>Mensajería</h1>

    <!-- Formulario para seleccionar destinatario -->
    <form method="get" action="mensajeria">
        <label for="destinatario">Selecciona un usuario para chatear:</label>
        <select name="destinatario" onchange="this.form.submit()">
            <option value="">-- Selecciona --</option>
            <%
                List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                String destinatario = (String) request.getAttribute("destinatario");
                if (usuarios != null) {
                    for (Usuario u : usuarios) {
            %>
            <option value="<%= u.getEmail() %>" <%= u.getEmail().equals(destinatario) ? "selected" : "" %>>
                <%= u.getEmail() %>
            </option>
            <%
                }
            } else {
            %>
            <option disabled>No hay usuarios disponibles</option>
            <% } %>
        </select>
    </form>

    <!-- Mostrar mensajes -->
    <div class="mensajes">
        <%
            List<Mensaje> mensajes = (List<Mensaje>) request.getAttribute("mensajes");
            String remitente = (String) request.getAttribute("remitente");
            if (mensajes != null) {
                for (Mensaje m : mensajes) {
                    boolean esRemitente = m.getRemitenteEmail().equals(remitente);
        %>
        <div class="mensaje <%= esRemitente ? "remitente" : "destinatario" %>">
            <strong><%= esRemitente ? "Tú" : m.getRemitenteEmail() %>:</strong> <%= m.getContenido() %>
            <br><small><%= m.getFechaHora() %></small>
        </div>
        <%
                }
            }
        %>
    </div>

    <!-- Formulario para enviar mensaje -->
    <form method="post" action="mensajeria">
        <input type="hidden" name="destinatario" value="<%= destinatario != null ? destinatario : "" %>">
        <textarea name="contenido" rows="4" placeholder="Escribe tu mensaje aquí..." required></textarea>
        <button type="submit">Enviar</button>
    </form>

    <a href="home.jsp" class="btn-regresar">Regresar</a>
</div>

</body>
</html>

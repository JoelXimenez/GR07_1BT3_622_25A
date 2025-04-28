<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apptrueque.model.Closet" %>
<%@ page import="org.apptrueque.model.Prenda" %>
<%@ page import="java.util.List" %>

<%
    Closet closet = (Closet) request.getAttribute("closet");
    List<Prenda> prendas = (closet != null) ? closet.getPrendas() : null;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Closet - TruequeApp</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body {
            background: linear-gradient(to right, #74ebd5, #ACB6E5);
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 1000px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
            text-align: center;
        }
        h1 {
            margin-bottom: 30px;
            color: #333;
        }
        .button-group {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 15px;
            margin-bottom: 30px;
        }
        .btn {
            padding: 12px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-size: 16px;
            display: flex;
            align-items: center;
            color: white;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .btn-verde { background-color: #4CAF50; }
        .btn-verde:hover { background-color: #45a049; }
        .btn-rojo { background-color: #f44336; }
        .btn-rojo:hover { background-color: #d32f2f; }
        .btn i { margin-right: 8px; }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid #ccc;
        }
        th, td {
            padding: 12px;
            text-align: center;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        /* Modales */
        .modal {
            display: none;
            position: fixed;
            z-index: 999;
            padding-top: 80px;
            left: 0; top: 0;
            width: 100%; height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.5);
        }
        .modal-content {
            background-color: #fff;
            margin: auto;
            padding: 20px;
            border: 1px solid #888;
            width: 500px;
            border-radius: 10px;
            text-align: center;
        }
        .modal-content input, .modal-content select {
            width: 90%;
            margin: 8px 0;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }
        .close:hover { color: black; }
    </style>
</head>

<body>

<div class="container">
    <h1>Mi Closet</h1>

    <div class="button-group">
        <button class="btn btn-verde" onclick="openModal('modalAgregar')"><i class="fas fa-plus"></i> Agregar Prenda</button>
        <button class="btn btn-verde" onclick="openModal('modalEditar')"><i class="fas fa-edit"></i> Editar Prenda</button>
        <button class="btn btn-verde" onclick="openModal('modalEliminar')"><i class="fas fa-trash"></i> Eliminar Prenda</button>
        <button class="btn btn-verde" onclick="abrirConfirmacionPublicar()"><i class="fas fa-bullhorn"></i> Publicar Closet</button>
        <a href="home.jsp" class="btn btn-rojo"><i class="fas fa-home"></i> Regresar</a>
    </div>

    <h2>Mis Prendas</h2>

    <% if (prendas != null && !prendas.isEmpty()) { %>
    <table>
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Talla</th>
            <th>Estado</th>
            <th>Categoría</th>
        </tr>
        </thead>
        <tbody>
        <% for (Prenda prenda : prendas) { %>
        <tr>
            <td><%= prenda.getNombre() %></td>
            <td><%= prenda.getDescripcion() != null ? prenda.getDescripcion() : "-" %></td>
            <td><%= prenda.getTalla() != null ? prenda.getTalla() : "-" %></td>
            <td><%= prenda.getEstado() != null ? prenda.getEstado() : "-" %></td>
            <td><%= prenda.getCategoria() != null ? prenda.getCategoria() : "-" %></td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <p>No tienes prendas registradas.</p>
    <% } %>

</div>

<!-- Modales -->
<div id="modalAgregar" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('modalAgregar')">&times;</span>
        <h2>Agregar Prenda</h2>
        <form action="AgregarPrendaServlet" method="post">
            <input type="text" name="nombre" placeholder="Nombre" required>
            <input type="text" name="descripcion" placeholder="Descripción">
            <input type="text" name="talla" placeholder="Talla">
            <input type="text" name="estado" placeholder="Estado">
            <input type="text" name="categoria" placeholder="Categoría">
            <input type="text" name="imagenURL" placeholder="URL Imagen (opcional)">
            <button type="submit" class="btn btn-verde">Guardar</button>
        </form>
    </div>
</div>

<div id="modalEditar" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('modalEditar')">&times;</span>
        <h2>Editar Prenda</h2>
        <form action="EditarPrendaServlet" method="post">
            <select name="id" required>
                <option value="">-- Selecciona una prenda --</option>
                <% if (prendas != null) {
                    for (Prenda prenda : prendas) { %>
                <option value="<%= prenda.getId() %>"><%= prenda.getNombre() %></option>
                <% } } %>
            </select>
            <input type="text" name="nombre" placeholder="Nuevo Nombre (opcional)">
            <input type="text" name="descripcion" placeholder="Nueva Descripción (opcional)">
            <input type="text" name="talla" placeholder="Nueva Talla (opcional)">
            <input type="text" name="estado" placeholder="Nuevo Estado (opcional)">
            <input type="text" name="categoria" placeholder="Nueva Categoría (opcional)">
            <button type="submit" class="btn btn-verde">Actualizar</button>
        </form>
    </div>
</div>

<div id="modalEliminar" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('modalEliminar')">&times;</span>
        <h2>Eliminar Prenda</h2>
        <form action="EliminarPrendaServlet" method="post">
            <select name="id" required>
                <option value="">-- Selecciona una prenda --</option>
                <% if (prendas != null) {
                    for (Prenda prenda : prendas) { %>
                <option value="<%= prenda.getId() %>"><%= prenda.getNombre() %></option>
                <% } } %>
            </select>
            <button type="submit" class="btn btn-rojo" onclick="return confirm('¿Seguro que deseas eliminar esta prenda?')">Eliminar</button>
        </form>
    </div>
</div>

<!-- Modal Confirmar Publicar -->
<div id="modalConfirmarPublicar" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('modalConfirmarPublicar')">&times;</span>
        <h2>¿Confirmar publicación?</h2>
        <button onclick="publicarCloset()" class="btn btn-verde">Sí, Publicar</button>
        <button onclick="closeModal('modalConfirmarPublicar')" class="btn btn-rojo">Cancelar</button>
    </div>
</div>

<!-- Modal Mensaje -->
<div id="modalMensaje" class="modal">
    <div class="modal-content" id="mensajeContenido">
        <h2 id="mensajeTexto"></h2>
        <button onclick="closeModal('modalMensaje')" class="btn btn-verde">Aceptar</button>
    </div>
</div>

<script>
    function openModal(id) {
        document.getElementById(id).style.display = 'block';
    }
    function closeModal(id) {
        document.getElementById(id).style.display = 'none';
    }

    function abrirConfirmacionPublicar() {
        openModal('modalConfirmarPublicar');
    }

    function publicarCloset() {
        closeModal('modalConfirmarPublicar');
        const cantidadPrendas = <%= (prendas != null) ? prendas.size() : 0 %>;

        if (cantidadPrendas >= 3 && cantidadPrendas <= 10) {
            document.getElementById('mensajeTexto').innerText = 'Clóset publicado exitosamente.';
        } else {
            document.getElementById('mensajeTexto').innerText = 'Debes tener entre 3 y 10 prendas para publicar tu clóset.';
        }

        openModal('modalMensaje');
    }
</script>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apptrueque.model.Prenda" %>
<%@ page import="jakarta.persistence.EntityManager" %>
<%@ page import="jakarta.persistence.TypedQuery" %>
<%@ page import="org.apptrueque.util.JpaUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apptrueque.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>


<%
    EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
    TypedQuery<Prenda> query = em.createQuery(
            "SELECT p FROM Prenda p WHERE p.closet.usuario.cedula = :cedula", Prenda.class);
    query.setParameter("cedula", usuario.getCedula());
    List<Prenda> prendas = query.getResultList();

    em.close();
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
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
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

        .btn-verde {
            background-color: #4CAF50;
        }

        .btn-verde:hover {
            background-color: #45a049;
        }

        .btn-rojo {
            background-color: #f44336;
        }

        .btn-rojo:hover {
            background-color: #d32f2f;
        }

        .btn i {
            margin-right: 8px;
        }

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

        .modal {
            display: none;
            position: fixed;
            z-index: 999;
            padding-top: 60px;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: auto;
            padding: 20px;
            border: 1px solid #888;
            width: 400px;
            border-radius: 10px;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }

        .close:hover {
            color: black;
        }

        input, select {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 6px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Mi Closet</h1>

    <div class="button-group">
        <button class="btn btn-verde" onclick="document.getElementById('modalAgregar').style.display='block'"><i
                class="fas fa-plus"></i> Agregar Prenda
        </button>
        <button class="btn btn-verde" onclick="abrirModalEditar()" type="button">
            <i class="fas fa-edit"></i> Editar Prenda
        </button>
        <form method="POST" action="EliminarPrendaServlet" id="formEliminar">
            <input type="hidden" name="id" id="eliminarId">
            <button class="btn btn-rojo" type="submit"><i class="fas fa-trash"></i> Eliminar Prenda</button>
        </form>
        <form method="POST" action="PublicarClosetServlet">
            <button class="btn btn-verde" type="submit"><i class="fas fa-upload"></i> Publicar Closet</button>
        </form>
        <a href="home.jsp" class="btn btn-rojo"><i class="fas fa-home"></i> Regresar</a>
    </div>

    <table>
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Talla</th>
            <th>Estado</th>
            <th>Categoría</th>
            <th>Seleccionar</th>
        </tr>
        </thead>
        <tbody>
        <% for (Prenda prenda : prendas) { %>
        <tr onclick="seleccionar(<%= prenda.getId() %>)">
            <td><%= prenda.getNombre() %>
            </td>
            <td><%= prenda.getDescripcion() %>
            </td>
            <td><%= prenda.getTalla() %>
            </td>
            <td><%= prenda.getEstado() %>
            </td>
            <td><%= prenda.getCategoria() %>
            </td>
            <td><input type="radio" name="seleccionPrenda" value="<%= prenda.getId() %>"
                       onclick="seleccionar(<%= prenda.getId() %>)"></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>

<!-- ... encabezado y tabla sin cambios ... -->
<!-- Modal Agregar -->
<div id="modalAgregar" class="modal">
    <div class="modal-content" style="width: 500px;">
        <span class="close" onclick="document.getElementById('modalAgregar').style.display='none'">&times;</span>
        <h2>Agregar Nueva Prenda</h2>
        <form method="POST" action="AgregarPrendaServlet" onsubmit="return validarCampos(this)">
            <input type="text" name="nombre" placeholder="Nombre" required>
            <input type="text" name="descripcion" placeholder="Descripción" required>
            <input type="text" name="talla" placeholder="Talla" required>
            <input type="text" name="estado" placeholder="Estado" required>
            <input type="text" name="categoria" placeholder="Categoría" required>
            <input type="text" name="imagenUrl" placeholder="URL de la imagen (opcional)">
            <button class="btn btn-verde" type="submit"><i class="fas fa-plus-circle"></i> Guardar</button>
            <button class="btn btn-rojo" type="button" onclick="document.getElementById('modalAgregar').style.display='none'"><i class="fas fa-times"></i> Cancelar</button>
        </form>
    </div>
</div>

<!-- Modal Editar -->
<div id="modalEditar" class="modal">
    <div class="modal-content" style="width: 500px;">
        <span class="close" onclick="document.getElementById('modalEditar').style.display='none'">&times;</span>
        <h2>Editar Prenda</h2>
        <form method="POST" action="EditarPrendaServlet">
            <input type="hidden" name="id" id="editId">
            <input type="text" name="nombre" id="editNombre" placeholder="Nombre (opcional)">
            <input type="text" name="descripcion" id="editDescripcion" placeholder="Descripción (opcional)">
            <input type="text" name="talla" id="editTalla" placeholder="Talla (opcional)">
            <input type="text" name="estado" id="editEstado" placeholder="Estado (opcional)">
            <input type="text" name="categoria" id="editCategoria" placeholder="Categoría (opcional)">
            <input type="text" name="imagenUrl" id="editImagenUrl" placeholder="URL de la imagen (opcional)">
            <p style="font-size: 14px; color: #777">Deja en blanco los campos que no deseas modificar.</p>
            <button class="btn btn-verde" type="submit"><i class="fas fa-save"></i> Actualizar</button>
            <button class="btn btn-rojo" type="button" onclick="document.getElementById('modalEditar').style.display='none'"><i class="fas fa-times"></i> Cancelar</button>
        </form>
    </div>
</div>

<!-- Modal Confirmar Eliminar -->
<div id="modalEliminarConfirmar" class="modal">
    <div class="modal-content">
        <span class="close" onclick="document.getElementById('modalEliminarConfirmar').style.display='none'">&times;</span>
        <h3>¿Seguro que deseas eliminar esta prenda?</h3>
        <div style="display: flex; justify-content: center; gap: 15px;">
            <button class="btn btn-rojo" onclick="document.getElementById('formEliminar').submit()">Sí, eliminar</button>
            <button class="btn btn-verde" onclick="document.getElementById('modalEliminarConfirmar').style.display='none'">No, cancelar</button>
        </div>
    </div>
</div>

<script>
    let prendaSeleccionada = false;

    function seleccionar(id, nombre, descripcion, talla, estado, categoria, imagenUrl) {
        prendaSeleccionada = true;
        document.getElementById("eliminarId").value = id;
        document.getElementById("editId").value = id;
        document.getElementById("editNombre").value = nombre || '';
        document.getElementById("editDescripcion").value = descripcion || '';
        document.getElementById("editTalla").value = talla || '';
        document.getElementById("editEstado").value = estado || '';
        document.getElementById("editCategoria").value = categoria || '';
        document.getElementById("editImagenUrl").value = imagenUrl || '';
    }

    function abrirModalEditar() {
        if (!prendaSeleccionada || !document.getElementById("editId").value) {
            alert("Por favor selecciona una prenda primero.");
            return;
        }
        document.getElementById("modalEditar").style.display = "block";
    }

    document.getElementById("formEliminar").addEventListener("submit", function(e) {
        if (!prendaSeleccionada || !document.getElementById("eliminarId").value) {
            e.preventDefault();
            alert("Por favor selecciona una prenda primero.");
            return;
        }
        e.preventDefault();
        document.getElementById("modalEliminarConfirmar").style.display = "block";
    });

    function validarCampos(form) {
        const campos = form.querySelectorAll("input[required]");
        for (let campo of campos) {
            if (!campo.value.trim()) {
                alert("Por favor completa todos los campos obligatorios.");
                return false;
            }
        }
        return true;
    }

    window.onclick = function(event) {
        const modales = ["modalAgregar", "modalEditar", "modalEliminarConfirmar"];
        for (let id of modales) {
            const modal = document.getElementById(id);
            if (event.target === modal) modal.style.display = "none";
        }
    }
</script>
<%
    String mensaje = (String) session.getAttribute("mensajePublicacion");
    if (mensaje != null) {
        session.removeAttribute("mensajePublicacion"); // limpiar después de mostrar
%>
<div id="modalMensaje" class="modal" style="display: block;">
    <div class="modal-content" style="width: 400px;">
        <span class="close" onclick="document.getElementById('modalMensaje').style.display='none'">&times;</span>
        <h3 style="color: #333;"><%= mensaje %></h3>
        <button class="btn btn-verde" onclick="document.getElementById('modalMensaje').style.display='none'">Aceptar</button>
    </div>
</div>
<% } %>
</body>
</html>


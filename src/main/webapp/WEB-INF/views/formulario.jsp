<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Nueva tarea</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <h1>Nueva tarea</h1>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/app">
        <input type="hidden" name="comando" value="guardar">
        <label for="titulo">Título:
            <input type="text" id="titulo" name="titulo" required>
        </label>
        <label for="categoria">Categoría:
            <input type="text" id="categoria" name="categoria">
        </label>
        <label for="prioridad">Prioridad:
            <select id="prioridad" name="prioridad">
                <option value="Alta">Alta</option>
                <option value="Media" selected>Media</option>
                <option value="Baja">Baja</option>
            </select>
        </label>
        <label for="fechaLimite">Fecha límite (yyyy-MM-dd):
            <input type="text" id="fechaLimite" name="fechaLimite" placeholder="2026-08-20" required>
        </label>
        <button type="submit">Guardar</button>
        <a href="${pageContext.request.contextPath}/app">Cancelar</a>
    </form>
</body>
</html>

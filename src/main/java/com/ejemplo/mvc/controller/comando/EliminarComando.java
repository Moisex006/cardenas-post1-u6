package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Usuario;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class EliminarComando implements Comando {
    private final TareaService service;

    public EliminarComando(TareaService service) { this.service = service; }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // El Front Controller ya garantiza la sesión, pero se comprueba igual
        // para no depender de ello y evitar un NullPointerException
        HttpSession session = req.getSession(false);
        Usuario actual = session == null
            ? null : (Usuario) session.getAttribute("usuarioActual");

        if (actual == null || !"ADMIN".equals(actual.getRol())) {
            req.setAttribute("error", "Solo un administrador puede eliminar tareas.");
            req.setAttribute("tareas", service.obtenerTodas());
            return "/WEB-INF/views/lista.jsp";
        }

        Comando.leerId(req).ifPresent(service::eliminar);
        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }
}

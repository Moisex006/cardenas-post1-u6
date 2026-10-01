package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FormularioComando implements Comando {
    private final TareaService service;

    public FormularioComando(TareaService service) { this.service = service; }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp) {
        Comando.leerId(req).ifPresent(id ->
            req.setAttribute("tarea", service.obtenerPorId(id)));
        return "/WEB-INF/views/formulario.jsp";
    }
}

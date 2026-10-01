package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Tarea;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GuardarComando implements Comando {
    private static final String VISTA_FORMULARIO = "/WEB-INF/views/formulario.jsp";
    private static final String ERROR_FECHA = "La fecha límite debe tener el formato yyyy-MM-dd.";

    private final TareaService service;

    public GuardarComando(TareaService service) { this.service = service; }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String titulo   = req.getParameter("titulo");
        String fechaStr = req.getParameter("fechaLimite");

        if (titulo == null || titulo.trim().isEmpty()) {
            req.setAttribute("error", "El título es obligatorio.");
            return VISTA_FORMULARIO;
        }
        if (fechaStr == null) {
            req.setAttribute("error", ERROR_FECHA);
            return VISTA_FORMULARIO;
        }

        Date fechaLimite;
        try {
            // Sin modo estricto, SimpleDateFormat acepta fechas como "01-12-2026"
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            formato.setLenient(false);
            fechaLimite = formato.parse(fechaStr);
        } catch (ParseException e) {
            req.setAttribute("error", ERROR_FECHA);
            return VISTA_FORMULARIO;
        }

        service.guardar(new Tarea(0, titulo.trim(),
            req.getParameter("categoria"),
            req.getParameter("prioridad"),
            fechaLimite));
        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }
}

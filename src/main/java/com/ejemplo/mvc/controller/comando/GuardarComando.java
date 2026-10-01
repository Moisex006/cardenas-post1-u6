package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Tarea;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class GuardarComando implements Comando {
    private static final String CAMPO_FECHA = "fechaLimite";

    private final TareaService service;

    public GuardarComando(TareaService service) { this.service = service; }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String titulo    = req.getParameter("titulo");
        String categoria = req.getParameter("categoria");
        String prioridad = req.getParameter("prioridad");
        String fechaStr  = req.getParameter(CAMPO_FECHA);

        int maxLongitud = (Integer)
            req.getServletContext().getAttribute("maxLongitudTitulo");
        Map<String, String> errores = new LinkedHashMap<>();

        if (titulo == null || titulo.trim().isEmpty()) {
            errores.put("titulo", "El título es obligatorio.");
        } else if (titulo.trim().length() > maxLongitud) {
            errores.put("titulo",
                "El título no debe superar " + maxLongitud + " caracteres.");
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            errores.put("categoria", "La categoría es obligatoria.");
        }

        if (!"Alta".equals(prioridad) && !"Media".equals(prioridad)
                && !"Baja".equals(prioridad)) {
            errores.put("prioridad", "Seleccione una prioridad válida.");
        }

        Date fechaLimite = parsearFecha(fechaStr);
        if (fechaLimite == null) {
            errores.put(CAMPO_FECHA, "Use el formato yyyy-MM-dd (ej: 2026-08-20).");
        } else if (fechaLimite.before(new Date())) {
            errores.put(CAMPO_FECHA, "La fecha límite no puede estar en el pasado.");
        }

        if (!errores.isEmpty()) {
            req.setAttribute("errores",     errores);
            req.setAttribute("titulo",      titulo);
            req.setAttribute("categoria",   categoria);
            req.setAttribute("prioridad",   prioridad);
            req.setAttribute(CAMPO_FECHA,   fechaStr);
            return "/WEB-INF/views/formulario.jsp";
        }

        service.guardar(new Tarea(0, titulo.trim(), categoria.trim(),
            prioridad, fechaLimite));
        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }

    /** Devuelve null si la fecha falta o no cumple estrictamente yyyy-MM-dd */
    private Date parsearFecha(String fechaStr) {
        if (fechaStr == null) {
            return null;
        }
        // Sin modo estricto, SimpleDateFormat acepta fechas como "01-12-2026"
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setLenient(false);
        try {
            return formato.parse(fechaStr);
        } catch (ParseException e) {
            return null;
        }
    }
}

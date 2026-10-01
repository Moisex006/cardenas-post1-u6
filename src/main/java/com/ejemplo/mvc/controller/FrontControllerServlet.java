package com.ejemplo.mvc.controller;

import com.ejemplo.mvc.controller.comando.*;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FrontControllerServlet", urlPatterns = {"/app"})
public class FrontControllerServlet extends HttpServlet {

    private final Map<String, Comando> comandos = new HashMap<>();

    @Override
    public void init() throws ServletException {
        TareaService tareaService = new TareaService();

        // Contexto de aplicación: configuración global leída una sola vez
        // desde web.xml y compartida por todos los usuarios (Guía, sección 6.1)
        ServletContext ctx = getServletContext();
        ctx.setAttribute("nombreApp", ctx.getInitParameter("app.nombre"));
        ctx.setAttribute("maxLongitudTitulo",
            Integer.parseInt(ctx.getInitParameter("app.maxLongitudTitulo")));

        comandos.put("listar",     new ListarComando(tareaService));
        comandos.put("formulario", new FormularioComando(tareaService));
        comandos.put("guardar",    new GuardarComando(tareaService));
        comandos.put("eliminar",   new EliminarComando(tareaService));
        comandos.put("completar",  new CompletarComando(tareaService));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            procesar(req, resp);
        } catch (ServletException | IOException e) {
            responderError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            procesar(req, resp);
        } catch (ServletException | IOException e) {
            responderError(resp, e);
        }
    }

    private void procesar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nombreComando = req.getParameter("comando");
        if (nombreComando == null) nombreComando = "listar";

        Comando comando = comandos.get(nombreComando);
        if (comando == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String vista = comando.ejecutar(req, resp);
        if (vista != null) {
            req.getRequestDispatcher(vista).forward(req, resp);
        }
    }

    /** Registra el error en el log del contenedor sin exponer el stack trace al navegador */
    private void responderError(HttpServletResponse resp, Exception e) {
        log("Error al procesar la petición en el Front Controller", e);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

package com.ejemplo.mvc.controller.comando;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.OptionalInt;

public interface Comando {
    /**
     * Ejecuta la acción y devuelve la ruta de la vista JSP a la que el
     * FrontControllerServlet debe hacer forward. Si el propio comando ya
     * resolvió la respuesta (por ejemplo con sendRedirect), devuelve null.
     */
    String ejecutar(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException;

    /**
     * Lee el parámetro "id". Un id ausente o no numérico devuelve vacío
     * en lugar de lanzar NumberFormatException (que terminaría en un 500).
     */
    static OptionalInt leerId(HttpServletRequest req) {
        try {
            return OptionalInt.of(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
}

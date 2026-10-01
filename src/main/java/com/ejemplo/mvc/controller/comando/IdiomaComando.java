package com.ejemplo.mvc.controller.comando;

import jakarta.servlet.http.*;
import java.io.IOException;

public class IdiomaComando implements Comando {
    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String lang = req.getParameter("lang");
        if ("es".equals(lang) || "en".equals(lang)) {
            Cookie cookie = new Cookie("idiomaPreferido", lang);
            cookie.setMaxAge(30 * 24 * 60 * 60);  // 30 dias: sobrevive al cierre de sesion
            cookie.setPath("/");
            // Las vistas leen la cookie en el servidor (EL implícito cookie), así
            // que JavaScript no necesita acceder a ella; Secure cuando se usa HTTPS
            cookie.setHttpOnly(true);
            cookie.setSecure(req.isSecure());
            resp.addCookie(cookie);
        }
        // Se redirige siempre a /app en vez de al encabezado Referer, que lo envía
        // el cliente y permitiría una redirección abierta hacia otro sitio. El
        // resultado es el mismo: con sesión /app muestra la lista y sin sesión el
        // Front Controller lleva al login, las dos vistas que tienen el selector
        resp.sendRedirect(req.getContextPath() + "/app");
        return null;
    }
}

package com.ejemplo.mvc.model;

import java.util.*;

public class UsuarioDAO {
    // Usuarios en memoria; en producción las claves se guardan con hash
    // (ej. bcrypt), nunca en texto plano. Se simplifica aquí para
    // enfocar el laboratorio en el uso de HttpSession y roles.
    private static final Map<String, Usuario> USUARIOS = new HashMap<>();

    static {
        USUARIOS.put("admin", new Usuario(
            "admin", "Admin123!", "Administrador General", "ADMIN"));
        USUARIOS.put("maria", new Usuario(
            "maria", "Maria2026!", "María Fernanda Rojas", "USER"));
    }

    public Usuario buscarPorUsername(String username) {
        return USUARIOS.get(username);
    }
}

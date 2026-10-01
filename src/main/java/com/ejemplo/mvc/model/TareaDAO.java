package com.ejemplo.mvc.model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TareaDAO {
    // Datos compartidos por todos los hilos del contenedor: se usan
    // estructuras seguras para concurrencia en lugar de ArrayList e int
    private static final List<Tarea> DATOS = new CopyOnWriteArrayList<>();
    private static final AtomicInteger CONTADOR = new AtomicInteger(3);

    static {
        Date hoy = new Date();
        DATOS.add(new Tarea(1, "Diseñar el diagrama de clases MVC",
            "Diseño", "Alta", sumarDias(hoy, 2)));
        DATOS.add(new Tarea(2, "Implementar el Front Controller",
            "Desarrollo", "Alta", sumarDias(hoy, 4)));
        DATOS.add(new Tarea(3, "Redactar el README con decisiones de diseño",
            "Documentación", "Media", sumarDias(hoy, 7)));
    }

    private static Date sumarDias(Date base, int dias) {
        long unDiaMs = 24L * 60 * 60 * 1000;
        return new Date(base.getTime() + dias * unDiaMs);
    }

    public List<Tarea> findAll() {
        return Collections.unmodifiableList(DATOS);
    }

    public Tarea findById(int id) {
        return DATOS.stream()
                    .filter(t -> t.getId() == id)
                    .findFirst().orElse(null);
    }

    public void save(Tarea t) {
        t.setId(CONTADOR.incrementAndGet());
        DATOS.add(t);
    }

    public void delete(int id) {
        DATOS.removeIf(t -> t.getId() == id);
    }
}

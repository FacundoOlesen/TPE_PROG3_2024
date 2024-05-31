package tpe;

import java.util.LinkedList;

public class TareasPorPrioridad {
    private Integer prioridad;
    private LinkedList<Tarea> tareas;

    public TareasPorPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
        this.tareas = new LinkedList<>();
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public LinkedList<Tarea> getTareas() {
        return new LinkedList<>(this.tareas);
    }

    public void addTarea(Tarea t) {
        this.tareas.add(t);
    }

    @Override
    public String toString() {
        return "TareasPorPrioridad{" +
                "prioridad=" + prioridad +
                ", tareas=" + tareas +
                '}';
    }
}

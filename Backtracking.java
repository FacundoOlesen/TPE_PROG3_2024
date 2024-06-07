import tpe.Procesador;
import tpe.Tarea;
import tpe.utils.CSVReader;

import java.util.*;

public class Backtracking {
    private HashMap<Procesador, LinkedList<Tarea>> solucion;
    private LinkedList<Procesador> procesadores;
    private LinkedList<Tarea> tareas;

    public Backtracking(String pathProcesadores, String pathTareas) {
        CSVReader reader = new CSVReader();
        this.tareas = reader.readTasks(pathTareas);
        this.procesadores = reader.readProcessors(pathProcesadores);


        this.solucion = new HashMap<Procesador, LinkedList<Tarea>>();
        for (Procesador p : procesadores)
            solucion.put(p, new LinkedList<Tarea>());

    }

    public HashMap<Procesador, LinkedList<Tarea>> asignacionTareas(Integer tiempoMaximoProcNoRefrigerado) {
        LinkedList<Tarea> colaTareas = new LinkedList<>(this.tareas);
        backtracking(null, colaTareas, tiempoMaximoProcNoRefrigerado);
        return solucion;
    }

    private void backtracking(Procesador procesadorActual, LinkedList<Tarea> colaTareas, Integer tiempoMaximoProcNoRefrigerado) {
        if (colaTareas.isEmpty()) {

            System.out.println("");
            System.out.println(solucion);
        } else {
            for (Procesador p : this.procesadores) {
                Tarea nextTarea = colaTareas.remove();
                agregarTareaAProc(p, nextTarea);
                backtracking(p, colaTareas, tiempoMaximoProcNoRefrigerado);
                sacarTareaAProc(p, nextTarea);
                colaTareas.add(0, nextTarea);
            }
        }


    }


    private Integer getTiempoEjecucionProcesador(Procesador procesador) {
        Integer tiempo = 0;
        LinkedList<Tarea> tareasProc = solucion.get(procesador);
        for (Tarea tarea : tareasProc)
            tiempo += tarea.getTiempoEjecucion();
        return tiempo;
    }

    private void agregarTareaAProc(Procesador procesador, Tarea tarea) {
        LinkedList<Tarea> listaTareas = solucion.get(procesador);
        listaTareas.add(tarea);
        if (tarea.getEsCritica())
            procesador.setCantTareasCriticas(procesador.getCantTareasCriticas() + 1); //MEJORABLE SEGURAMENTE
    }

    private void sacarTareaAProc(Procesador procesador, Tarea tarea) {
        solucion.get(procesador).remove(tarea);
    }

    private boolean puedeAsignarseTareaAProcesador(Procesador procesador, Tarea tarea, Integer tiempoMaximoProcNoRefrigerado) {
        if (procesador.getCantTareasCriticas() == 2 && tarea.getEsCritica())
            return false;
        if (!procesador.getRefrigerado() && getTiempoEjecucionProcesador(procesador) + tarea.getTiempoEjecucion() > tiempoMaximoProcNoRefrigerado)
            return false;

        return true;
    }





    public static void main(String[] args) {
        Backtracking b = new Backtracking("datasets/Procesadores.csv", "datasets/Tareas.csv");
        b.asignacionTareas(38);
    }


}
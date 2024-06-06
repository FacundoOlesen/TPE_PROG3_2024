import tpe.Procesador;
import tpe.Tarea;
import tpe.utils.CSVReader;

import java.util.*;

public class Backtracking {
    private HashMap<Procesador, LinkedList<Tarea>> solucion;
    private LinkedList<Procesador> procesadores;
    //private List<Tarea> tareas;


    public Backtracking(String pathProcesadores, String pathTareas) {
        CSVReader reader = new CSVReader();
        //this.tareas = reader.readTasks(pathTareas);
        this.procesadores = reader.readProcessors(pathProcesadores);


        this.solucion = new HashMap<Procesador, LinkedList<Tarea>>();

    }

    public HashMap<Procesador, LinkedList<Tarea>> asignacionTareas(Integer tiempoMaximoProcNoRefrigerado) {
        LinkedList<Procesador> solucionParcial = new LinkedList<>();

        backtracking(tareas.get(0), procesadores.get(0), solucionParcial, tiempoMaximoProcNoRefrigerado);
        return solucion;
    }

    private void backtracking(Tarea tareaActual, Procesador procesadorActual, LinkedList<Procesador> solucionParcial, Integer tiempoMaximoProcNoRefrigerado) {
        //SI ENCONTRE POSIBLE SOLUCION, DAME EL SIGUIENTE PROCESADOR
        //SI LA SUMA DE TODOS LOS TIEMPOS DE LOS PROCESADORES DE LA SOLUCION PARCIAL ES MENOR A LA SOLUCION ACTUAL
        //REEMPLAZO LAS SOLUCIONES
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


    private void reemplazarSolucion(Procesador procesador) {

    }


    public static void main(String[] args) {
        Backtracking b = new Backtracking("datasets/Procesadores.csv", "datasets/Tareas.csv");
        b.asignacionTareas(38);
    }


}
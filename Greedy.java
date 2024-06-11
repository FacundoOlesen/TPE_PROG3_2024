package tpe;

import tpe.utils.CSVReader;

import java.util.*;

public class Greedy {
    private HashMap<Procesador, LinkedList<Tarea>> solucion;
    private LinkedList<Procesador> procesadores;
    private LinkedList<Tarea> colaTareas;
    private Integer cantCandidatos;
    private LinkedList<Tarea> tareasCriticas;
    private LinkedList<Tarea> tareasNoCriticas;
    private int peorTiempoProcesador;


    public Greedy(String pathProcesadores, String pathTareas) {
        CSVReader reader = new CSVReader();
        this.colaTareas = reader.readTasks(pathTareas);
        this.tareasCriticas = new LinkedList<>();
        this.tareasNoCriticas = new LinkedList<>();

        this.procesadores = reader.readProcessors(pathProcesadores);
        this.solucion = new LinkedHashMap<>();
        for (Procesador p : procesadores) {
            solucion.put(p, new LinkedList<Tarea>());
        }
        for (Tarea tarea : colaTareas) {
            if (tarea.getEsCritica()) tareasCriticas.add(tarea);
            else tareasNoCriticas.add(tarea);
        }
        Collections.sort(tareasCriticas, Tarea.TiempoEjecucionComparator);
        Collections.sort(tareasNoCriticas, Tarea.TiempoEjecucionComparator);
        Collections.sort(procesadores, Procesador.procComparator);
        this.cantCandidatos = 0;
        this.peorTiempoProcesador = 0;
    }

    /*
    La ténica utilizida posee una cola de tareas con una lista de procesadores.
    Lo que se hace es ordenar las tareas de mayor a menor en 2 listas: una para las tareas críticas
    y otra para las no críticas.
    Luego, se agregan todas las tareas críticas de mayor a menor en los procesadores,
    y después las no críticas de mayor a menor también.
    Cada vez que se va a agregar una tarea se busca el procesador más adecuado para agregarle
    esa tarea teniendo en cuenta el tiempo de ejecución del procesador (se agarra el que tiene menos tiempo
    hasta el momento y al tener las tareas de mayor a menor se agarra la tarea de mayor tiempo, entonces
    se logra combinar el procesador de menor tiempo con la tarea de mayor tiempo manteniendo así equilibrado todos los tiempos
    de los procesadores) y las restricciones dadas (cant. de tareas críticas por procesador y tiempoMax para los p. no refrigerados).
    En caso de que se pueda agregar, se continúa con el algoritmo, y en caso de que no se encuentre ningún procesador disponible,
    se devuelve que no hay una solución encontrada. Si pudimos asignar todas las tareas a los procesadores obtendremos la solución.
     */
    public HashMap<Procesador, LinkedList<Tarea>> greedy(Integer tiempoMaximoProcNoRefrigerado) {
        if (tareasCriticas.size() > procesadores.size() * 2) return new HashMap<>();
        while (!tareasCriticas.isEmpty()) {
            if (!greedy(tareasCriticas, tiempoMaximoProcNoRefrigerado)) return new HashMap<>();
        }
        while (!tareasNoCriticas.isEmpty()) {
            if (!greedy(tareasNoCriticas, tiempoMaximoProcNoRefrigerado)) return new HashMap<>();
        }
        return solucion;
    }

    private boolean greedy(LinkedList<Tarea> tipoTareas, Integer tiempoMaximoProcNoRefrigerado) {
        Tarea tareaActual = tipoTareas.getFirst();
        Procesador procesadorParaAgregar = seleccionar(tareaActual, tiempoMaximoProcNoRefrigerado);
        if (factible(procesadorParaAgregar, tareaActual, tiempoMaximoProcNoRefrigerado)) {
            agregarTareaAProc(procesadorParaAgregar, tareaActual);
            tipoTareas.remove(tareaActual);
            return true;
        } else {
            System.out.println("No se encontró solución válida.");
            peorTiempoProcesador = -1;
            return false;
        }
    }


    private Procesador seleccionar(Tarea tarea, Integer tiempoMaximoProcNoRefrigerado) {
        Procesador mejorProcesador = procesadores.getFirst();
        for (Procesador procesador : procesadores) {
            if (factible(procesador, tarea, tiempoMaximoProcNoRefrigerado)) {
                if (procesador.getTiempoEjecucion() < mejorProcesador.getTiempoEjecucion())
                    mejorProcesador = procesador;
            }
            cantCandidatos++;
        }
        return mejorProcesador;
    }


    private void agregarTareaAProc(Procesador procesador, Tarea tarea) {
        LinkedList<Tarea> listaTareas = solucion.get(procesador);
        procesador.incrementarTiempoEjecucion(tarea.getTiempoEjecucion());
        listaTareas.add(tarea);
        if (tarea.getEsCritica()) procesador.incrementarTareasCriticas();
        if (procesador.getTiempoEjecucion() > peorTiempoProcesador)
            peorTiempoProcesador = procesador.getTiempoEjecucion();
    }


    private boolean factible(Procesador procesador, Tarea tarea, Integer tiempoMaximoProcNoRefrigerado) {
        if (procesador.getCantTareasCriticas() == 2 && tarea.getEsCritica()) return false;
        if (!procesador.getRefrigerado() && procesador.getTiempoEjecucion() + tarea.getTiempoEjecucion() > tiempoMaximoProcNoRefrigerado)
            return false;
        return true;
    }


    public Integer getCantCandidatos() {
        return cantCandidatos;
    }

    public int getPeorTiempoProcesador() {
        return peorTiempoProcesador;
    }
}

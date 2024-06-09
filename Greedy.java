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
        ordenarTareasPorCriticidad();
        Collections.sort(procesadores, Procesador.procComparator);
        this.cantCandidatos = 0;
        this.peorTiempoProcesador = 0;
    }

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
        Procesador procesadorParaAgregar = getMejorProcesadorParaAgregarle(tareaActual, tiempoMaximoProcNoRefrigerado);
        if (puedeAsignarseTareaAProcesador(procesadorParaAgregar, tareaActual, tiempoMaximoProcNoRefrigerado)) {
            agregarTareaAProc(procesadorParaAgregar, tareaActual);
            tipoTareas.remove(tareaActual);
            return true;
        } else {
            peorTiempoProcesador = -1;
            return false;
        }
    }


    private Procesador getMejorProcesadorParaAgregarle(Tarea tarea, Integer tiempoMaximoProcNoRefrigerado) {
        Procesador mejorProcesador = procesadores.getFirst();
        for (Procesador procesador : procesadores) {
            if (puedeAsignarseTareaAProcesador(procesador, tarea, tiempoMaximoProcNoRefrigerado)) {
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


    private boolean puedeAsignarseTareaAProcesador(Procesador procesador, Tarea tarea, Integer tiempoMaximoProcNoRefrigerado) {
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

    public void ordenarTareasPorCriticidad() {
        for (Tarea tarea : colaTareas) {
            if (tarea.getEsCritica()) tareasCriticas.add(tarea);
            else tareasNoCriticas.add(tarea);
        }
        mergesort(tareasCriticas, 0, tareasCriticas.size() - 1);
        mergesort(tareasNoCriticas, 0, tareasNoCriticas.size() - 1);
    }


    private void mergesort(LinkedList<Tarea> tareas, int low, int high) {
        int size = tareas.size();
        Tarea[] helper = new Tarea[size];
        if (low < high) {
            int middle = (low + high) / 2;
            mergesort(tareas, low, middle);
            mergesort(tareas, middle + 1, high);
            merge(tareas, helper, low, middle, high);
        }
    }

    private void merge(LinkedList<Tarea> tareas, Tarea[] helper, int low, int middle, int high) {
        for (int i = low; i <= high; i++) {
            helper[i] = tareas.get(i);
        }
        int i = low;
        int j = middle + 1;
        int k = low;
        while (i <= middle && j <= high) {
            if (helper[i].getTiempoEjecucion() > helper[j].getTiempoEjecucion()) {
                tareas.set(k, helper[i]);
                i++;
            } else {
                tareas.set(k, helper[j]);
                j++;
            }
            k++;
        }
        while (i <= middle) {
            tareas.set(k, helper[i]);
            k++;
            i++;
        }
        while (j <= high) {
            tareas.set(k, helper[i]);
            k++;
            j++;
        }
    }
}

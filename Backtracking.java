package tpe;

import tpe.utils.CSVReader;

import java.util.*;

public class Backtracking {
    private HashMap<Procesador, LinkedList<Tarea>> solucion;
    private LinkedList<Procesador> procesadores;
    private LinkedList<Tarea> colaTareas;
    private int cantEstadosGenerados;
    private int peorProcesadorSolucionActual;
    private boolean existeSolucion;

    public Backtracking(String pathProcesadores, String pathTareas) {
        CSVReader reader = new CSVReader();
        this.colaTareas = reader.readTasks(pathTareas);
        this.procesadores = reader.readProcessors(pathProcesadores);
        this.solucion = new LinkedHashMap<>();
        this.cantEstadosGenerados = 0;
        this.peorProcesadorSolucionActual = 0;
        this.existeSolucion = false;
    }

    //Método público del Backtracking donde se inicializan las estructuras a utilizar.
    public HashMap<Procesador, LinkedList<Tarea>> backtracking(Integer tiempoMaximoProcNoRefrigerado) {
        HashMap<Procesador, LinkedList<Tarea>> solucionParcial = new LinkedHashMap<Procesador, LinkedList<Tarea>>();
        for (Procesador p : procesadores) {
            solucion.put(p, new LinkedList<Tarea>());
            solucionParcial.put(p, new LinkedList<Tarea>());
        }
        backtracking(tiempoMaximoProcNoRefrigerado, solucionParcial);
        if (!existeSolucion) {
            return new HashMap<>();
        }
        return solucion;
    }

    /*
    El Backtracking implementado consta de una lista de procesadores y una cola de tareas.
    Se recorren los procesadores y se va obteniendo el primer elemento de la cola de tareas
    eliminandolo de la cola antes de la recursión. Luego, la tarea obtenida se agrega al primer
    procesador obtenido con el for y se accede a la recursión hasta que todas las tareas fueron
    asignadas (la cola de tareas está vácia). Una vez que asigne todas las tareas a procesadores
    (o sea que me quedé sin tareas en la cola de tareas) se vuelve al estado anterior luego de
    la recursión: se le quita la tarea al procesador y se vuelve a agregar la tarea a la cola
    volviendo al estado anterior para poder obtener el siguiente procesador y probar la tarea
    en ese nuevo procesador.
    De esta forma generamos todas las combinaciones  posibles de tareas y procesadores pudiendo
    encontrar la mejor siempre.
    */
    private void backtracking(Integer tiempoMaximoNoRefrigerado, HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        this.cantEstadosGenerados++;
        if (colaTareas.isEmpty()) {  //Si ya asigné todas las tareas:
            Integer peorProcSolParcial = getPeorTiempoDeProcesador(solucionParcial);
            if (solucionParcialEsMejorQueSolucion(peorProcSolParcial)) { //Si la solución parcial es mejor que la solución actual:
                reemplazarMejorSolucion(solucionParcial, peorProcSolParcial); //Reemplazo solución actual por la solución parcial.
            }
        } else {
            Tarea nextTarea = colaTareas.remove();
            for (Procesador procesador : this.procesadores) {
                if (puedeAsignarseTareaAProcesador(procesador, nextTarea, tiempoMaximoNoRefrigerado)) {
                    agregarTareaAProc(procesador, nextTarea, solucionParcial);
                    backtracking(tiempoMaximoNoRefrigerado, solucionParcial);
                    sacarTareaAProc(procesador, nextTarea, solucionParcial);
                }
            }
            colaTareas.add(0, nextTarea);
        }
    }

    //Agrega tarea a la posible solución.
    private void agregarTareaAProc(Procesador procesador, Tarea tarea, HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        LinkedList<Tarea> listaTareas = solucionParcial.get(procesador);
        procesador.incrementarTiempoEjecucion(tarea.getTiempoEjecucion());
        listaTareas.add(tarea);
        if (tarea.getEsCritica())
            procesador.incrementarTareasCriticas();
    }

    //Elimina tarea a la posible solución.
    private void sacarTareaAProc(Procesador procesador, Tarea tarea, HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        solucionParcial.get(procesador).remove(tarea);
        procesador.decrementarTiempoEjecucion(tarea.getTiempoEjecucion());
        if (tarea.getEsCritica())
            procesador.decrementarTareasCriticas();
    }

    //Chequeo de restricciones dadas en el enunciado.
    private boolean puedeAsignarseTareaAProcesador(Procesador procesador, Tarea
            tarea, Integer tiempoMaximoProcNoRefrigerado) {
        if (procesador.getCantTareasCriticas() == 2 && tarea.getEsCritica())
            return false;
        if (!procesador.getRefrigerado() && procesador.getTiempoEjecucion()
                + tarea.getTiempoEjecucion() > tiempoMaximoProcNoRefrigerado)
            return false;
        return true;
    }

    //Obtengo el tiempo máximo de ejecución de solución parcial.
    private Integer getPeorTiempoDeProcesador(HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        Iterator<Procesador> itProcesadores = solucionParcial.keySet().iterator();
        Integer peorTiempo = 0;
        while (itProcesadores.hasNext()) {
            Procesador nextProcesador = itProcesadores.next();
            Integer tiempoNextProcesador = nextProcesador.getTiempoEjecucion();
            if (tiempoNextProcesador > peorTiempo)
                peorTiempo = tiempoNextProcesador;
        }
        return peorTiempo;
    }

    //Chequeo si solución parcial es mejor que solución actual.
    private boolean solucionParcialEsMejorQueSolucion(Integer peorProcSolParcial) {
        return peorProcSolParcial < this.peorProcesadorSolucionActual || this.peorProcesadorSolucionActual == 0;
    }

    //Reemplaza solución parcial por solución actual (solo si la parcial es mejor)
    private void reemplazarMejorSolucion(HashMap<Procesador, LinkedList<Tarea>> solucionParcial, Integer peorProcSolParcial) {
        for (Procesador procesador : procesadores) {
            LinkedList<Tarea> tareasSolucionParcial = solucionParcial.get(procesador);
            solucion.get(procesador).clear();
            solucion.get(procesador).addAll(tareasSolucionParcial);
        }
        this.peorProcesadorSolucionActual = peorProcSolParcial;
        existeSolucion = true;
    }

    //Obtiene el tiempo de ejecución del peor procesador de la mejor solución encontrada.
    public Integer getTiempoMaximoEjecucionSolucion() {
        if (!existeSolucion)
            return -1;
        return peorProcesadorSolucionActual;
    }

    public int getCantEstadosGenerados() {
        return cantEstadosGenerados;
    }
}
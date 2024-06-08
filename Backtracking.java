package tpe;

import tpe.utils.CSVReader;

import java.util.*;

public class Backtracking {
    private HashMap<Procesador, LinkedList<Tarea>> solucion;
    private LinkedList<Procesador> procesadores;
    private LinkedList<Tarea> colaTareas;
    private int cantEstadosGenerados;
    private int peorProcesadorSolucionActual;

    public Backtracking(String pathProcesadores, String pathTareas) {
        this.cantEstadosGenerados = 0;
        CSVReader reader = new CSVReader();
        this.colaTareas = reader.readTasks(pathTareas);
        this.procesadores = reader.readProcessors(pathProcesadores);
        this.solucion = new LinkedHashMap<>();
        this.peorProcesadorSolucionActual = 0;
    }

    public HashMap<Procesador, LinkedList<Tarea>> backtracking(Integer tiempoMaximoProcNoRefrigerado) {
        HashMap<Procesador, LinkedList<Tarea>> solucionParcial = new LinkedHashMap<Procesador, LinkedList<Tarea>>();
        for (Procesador p : procesadores) {
            solucion.put(p, new LinkedList<Tarea>());
            solucionParcial.put(p, new LinkedList<Tarea>());
        }
        backtracking(tiempoMaximoProcNoRefrigerado, solucionParcial);
        if (solucionVacia()) {
            return new HashMap<>();
        }
        return solucion;
    }

    private void backtracking(Integer tiempoMaximoNoRefrigerado, HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        this.cantEstadosGenerados++;
        if (colaTareas.isEmpty()) {//SI ME QUEDE SIN TAREAS PARA ASIGNAR
            Integer peorProcSolParcial = getPeorTiempoDeProcesador(solucionParcial);
            if (solucionParcialEsMejorQueSolucion(solucionParcial, peorProcSolParcial)) { //SI LA SOLUCION PARCIAL ES MEJOR QUE LA SOLUCION ACTUAL
                reemplazarMejorSolucion(solucionParcial, peorProcSolParcial);          //REEMPLAZO SOLUCION ACTUAL POR SOLUCION PARCIAL
            }
        } else {
            Tarea nextTarea = colaTareas.remove();
            for (Procesador procesador : this.procesadores) {
                if (puedeAsignarseTareaAProcesador(procesador, nextTarea, solucionParcial, tiempoMaximoNoRefrigerado)) {
                    agregarTareaAProc(procesador, nextTarea, solucionParcial);
                    backtracking(tiempoMaximoNoRefrigerado, solucionParcial);
                    sacarTareaAProc(procesador, nextTarea, solucionParcial);
                }
            }
            colaTareas.add(0, nextTarea);
        }
    }


    //AGREGA TAREA A LA POSIBLE SOLUCIÓN
    private void agregarTareaAProc(Procesador procesador, Tarea tarea, HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        LinkedList<Tarea> listaTareas = solucionParcial.get(procesador);
        procesador.incrementarTiempoEjecucion(tarea.getTiempoEjecucion());
        listaTareas.add(tarea);
        if (tarea.getEsCritica())
            procesador.incrementarTareasCriticas();
    }

    //ELIMINA TAREA A LA POSIBLE SOLUCIÓN
    private void sacarTareaAProc(Procesador procesador, Tarea tarea, HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        solucionParcial.get(procesador).remove(tarea);
        procesador.decrementarTiempoEjecucion(tarea.getTiempoEjecucion());
        if (tarea.getEsCritica())
            procesador.decrementarTareasCriticas();
    }

    //CHEQUEO DE RESTRICCIONES DEL ENUNCIADO:
    //-NINGÚN PROCESADOR EJECUTA MAS DE 2 TAREAS CRITICAS
    //-PROCESADORES NO REFRIGERADOS NO PUEDEN DEDICAR MAS DE X TIEMPO DE EJECUCIÓN
    private boolean puedeAsignarseTareaAProcesador(Procesador procesador, Tarea
            tarea, HashMap<Procesador, LinkedList<Tarea>> solucionParcial, Integer tiempoMaximoProcNoRefrigerado) {
        if (procesador.getCantTareasCriticas() == 2 && tarea.getEsCritica())
            return false;
        if (!procesador.getRefrigerado() && getTiempoEjecucionProcesador(procesador, solucionParcial)
                + tarea.getTiempoEjecucion() > tiempoMaximoProcNoRefrigerado)
            return false;
        return true;
    }

    //OBTENGO TIEMPO DE EJECUCIÓN DE UN PROCESADOR
    //METER ESTO COMO ATRIBUTO DEL PROCESADOR PARA AHORRARME EL FOR
    private Integer getTiempoEjecucionProcesador(Procesador procesador, HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        return procesador.getTiempoEjecucion();
    }

    //OBTENGO EL TIEMPO MAXIMO DE EJECUCIÓN DE UNA POSIBLE SOLUCIÓN
    private Integer getPeorTiempoDeProcesador(HashMap<Procesador, LinkedList<Tarea>> solucionParcial) {
        Iterator<Procesador> itProcesadores = solucionParcial.keySet().iterator();
        Integer peorTiempo = 0;
        while (itProcesadores.hasNext()) {
            Procesador nextProcesador = itProcesadores.next();
            Integer tiempoProce = getTiempoEjecucionProcesador(nextProcesador, solucionParcial);
            if (tiempoProce > peorTiempo)
                peorTiempo = tiempoProce;
        }
        return peorTiempo;
    }

    //CHEQUEO SI POSIBLE SOLUCION ES MEJOR QUE SOLUCION
    private boolean solucionParcialEsMejorQueSolucion(HashMap<Procesador, LinkedList<Tarea>> solucionParcial, Integer peorProcSolParcial) {
        return peorProcSolParcial < this.peorProcesadorSolucionActual || this.peorProcesadorSolucionActual == 0;
    }

    //REEMPLAZA MEJOR SOLUCION POR POSIBLE SOLUCION (SOLO SI ES MEJOR)
    private void reemplazarMejorSolucion(HashMap<Procesador, LinkedList<Tarea>> solucionParcial, Integer peorProcSolParcial) {
        for (Procesador procesador : procesadores) {
            LinkedList<Tarea> p = solucionParcial.get(procesador);
            solucion.get(procesador).clear();
            solucion.get(procesador).addAll(p);
        }
        this.peorProcesadorSolucionActual = peorProcSolParcial;
    }

    public int getCantEstadosGenerados() {
        return cantEstadosGenerados;
    }

    public Integer getTiempoMaximoEjecucionSolucion() {
        if (solucionVacia())
            return -1;
        return peorProcesadorSolucionActual;
    }

    private boolean solucionVacia() {
        boolean vacio = true;
        Iterator<Procesador> itProcesadores = solucion.keySet().iterator();
        while (itProcesadores.hasNext()) {
            Procesador nextProcesador = itProcesadores.next();
            if (solucion.get(nextProcesador).size() > 0) {
                vacio = false;
                return vacio;
            }
        }
        return vacio;
    }

    public static void main(String[] args) {
        Backtracking solucionBacktracking = new Backtracking("datasets/Procesadores.csv", "datasets/Tareas.csv");
        System.out.println("Mejor solución encontrada: " + solucionBacktracking.backtracking(49));
        System.out.println("Tiempo máximo de ejecución de la solución: " + solucionBacktracking.getTiempoMaximoEjecucionSolucion());
        System.out.println("Cantidad de estados generados: " + solucionBacktracking.getCantEstadosGenerados());
    }
}
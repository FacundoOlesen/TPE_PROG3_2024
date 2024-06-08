package tpe;

import tpe.Procesador;
import tpe.Tarea;
import tpe.utils.CSVReader;

import java.util.*;

public class Backtracking {
    private HashMap<Procesador, LinkedList<Tarea>> solucion;
    private HashMap<Procesador, LinkedList<Tarea>> posibleSolucion;
    private LinkedList<Procesador> procesadores;
    private LinkedList<Tarea> colaTareas;
    private int cantEstadosGenerados;

    public Backtracking(String pathProcesadores, String pathTareas) {
        this.cantEstadosGenerados = 0;
        CSVReader reader = new CSVReader();
        this.colaTareas = reader.readTasks(pathTareas);
        this.procesadores = reader.readProcessors(pathProcesadores);

        this.solucion = new LinkedHashMap<>();
        this.posibleSolucion = new LinkedHashMap<Procesador, LinkedList<Tarea>>();

        for (Procesador p : procesadores) {
            solucion.put(p, new LinkedList<Tarea>());
            posibleSolucion.put(p, new LinkedList<Tarea>());
        }

    }

    public HashMap<Procesador, LinkedList<Tarea>> asignacionTareas(Integer tiempoMaximoProcNoRefrigerado) {
        backtracking(null, colaTareas, tiempoMaximoProcNoRefrigerado);
        return solucion;
    }

    private void backtracking(Procesador procesadorActual, LinkedList<Tarea> colaTareas, Integer tiempoMaximoNoRefrigerado) {
        this.cantEstadosGenerados++;
        if (colaTareas.isEmpty()) {  //SI ME QUEDE SIN TAREAS PARA ASIGNAR
            reemplazarMejorSolucion();
        } else {
            Tarea nextTarea = colaTareas.remove();
            for (Procesador procesador : this.procesadores) {
                if (puedeAsignarseTareaAProcesador(procesador, nextTarea, tiempoMaximoNoRefrigerado)) {
                    agregarTareaAProc(procesador, nextTarea);
                    procesador.decrementarTiempoEjecucion(nextTarea.getTiempoEjecucion());
                    backtracking(procesador, colaTareas, tiempoMaximoNoRefrigerado);
                    sacarTareaAProc(procesador, nextTarea);
                    procesador.decrementarTiempoEjecucion(nextTarea.getTiempoEjecucion());
                }
            }
            colaTareas.add(0, nextTarea);
        }
    }

    //AGREGA TAREA A LA POSIBLE SOLUCIÓN
    private void agregarTareaAProc(Procesador procesador, Tarea tarea) {
        LinkedList<Tarea> listaTareas = posibleSolucion.get(procesador);
        listaTareas.add(tarea);
        if (tarea.getEsCritica())
            procesador.incrementarTareasCriticas();
    }

    //ELIMINA TAREA A LA POSIBLE SOLUCIÓN
    private void sacarTareaAProc(Procesador procesador, Tarea tarea) {
        posibleSolucion.get(procesador).remove(tarea);
        if (tarea.getEsCritica())
            procesador.decrementarTareasCriticas();
    }

    //CHEQUEO DE RESTRICCIONES DEL ENUNCIADO:
    //-NINGÚN PROCESADOR EJECUTA MAS DE 2 TAREAS CRITICAS
    //-PROCESADORES NO REFRIGERADOS NO PUEDEN DEDICAR MAS DE X TIEMPO DE EJECUCIÓN
    private boolean puedeAsignarseTareaAProcesador(Procesador procesador, Tarea tarea, Integer
            tiempoMaximoProcNoRefrigerado) {
        if (procesador.getCantTareasCriticas() == 2 && tarea.getEsCritica())
            return false;
        if (!procesador.getRefrigerado() && getTiempoEjecucionProcesador(procesador, posibleSolucion)
                + tarea.getTiempoEjecucion() > tiempoMaximoProcNoRefrigerado)
            return false;
        return true;
    }

    //OBTENGO TIEMPO DE EJECUCIÓN DE UN PROCESADOR
    //METER ESTO COMO ATRIBUTO DEL PROCESADOR PARA AHORRARME EL FOR
    private Integer getTiempoEjecucionProcesador(Procesador procesador, HashMap<Procesador, LinkedList<Tarea>> posibleSolucion) {
        Integer tiempo = 0;
        LinkedList<Tarea> tareasProc = posibleSolucion.get(procesador);
        for (Tarea tarea : tareasProc)
            tiempo += tarea.getTiempoEjecucion();
        return tiempo;
    }

    //OBTENGO EL TIEMPO MAXIMO DE EJECUCIÓN DE UNA POSIBLE SOLUCIÓN
    private Integer getPeorTiempoDeProcesador(HashMap<Procesador, LinkedList<Tarea>> posibleSolucion) {
        Iterator<Procesador> itProcesadores = posibleSolucion.keySet().iterator();
        Integer peorTiempo = 0;
        while (itProcesadores.hasNext()) {
            Procesador nextProcesador = itProcesadores.next();
            Integer tiempoProce = getTiempoEjecucionProcesador(nextProcesador, posibleSolucion);
            if (tiempoProce > peorTiempo)
                peorTiempo = tiempoProce;
        }
        return peorTiempo;
    }

    //REEMPLAZA MEJOR SOLUCION POR POSIBLE SOLUCION (SOLO SI ES MEJOR)
    //PODRIA HACER SOLUCION = POSIBLE SOLUCION PERO ME COPIA TMB
    //LA DIRECCION DE MEMORIA
    private void reemplazarMejorSolucion() {
        if (posibleSolucionEsMejorQueSolucion()) {
            for (Procesador procesador : procesadores) {
                LinkedList<Tarea> p = posibleSolucion.get(procesador);
                solucion.get(procesador).clear();
                solucion.get(procesador).addAll(p);
            }
        }
    }

    //CHEQUEO SI POSIBLE SOLUCION ES MEJOR QUE SOLUCION
    private boolean posibleSolucionEsMejorQueSolucion() {
        return getPeorTiempoDeProcesador(posibleSolucion) <= getPeorTiempoDeProcesador(this.solucion)
                || getPeorTiempoDeProcesador(this.solucion) == 0;
    }

    public int getCantEstadosGenerados() {
        return cantEstadosGenerados;
    }

    public Integer getTiempoMaximoEjecucionSolucion() {
        return getPeorTiempoDeProcesador(this.solucion);
    }

    public static void main(String[] args) {
        Backtracking b = new Backtracking("datasets/Procesadores.csv", "datasets/Tareas.csv");
        System.out.println("Mejor solución encontrada: " + b.asignacionTareas(70));
        System.out.println("Tiempo máximo de ejecución de la solución: " + b.getTiempoMaximoEjecucionSolucion());
        System.out.println("Cantidad de estados generados: " + b.getCantEstadosGenerados());
    }
}
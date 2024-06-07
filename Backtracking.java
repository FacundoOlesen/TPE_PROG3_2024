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
        CSVReader reader = new CSVReader();
        this.colaTareas = reader.readTasks(pathTareas);
        this.procesadores = reader.readProcessors(pathProcesadores);
        this.cantEstadosGenerados = 0;

        this.solucion = new LinkedHashMap<>();
        this.posibleSolucion = new LinkedHashMap<Procesador, LinkedList<Tarea>>();

        for (Procesador p : procesadores) {
            solucion.put(p, new LinkedList<Tarea>());
            posibleSolucion.put(p, new LinkedList<Tarea>());
        }

    }

    public HashMap<Procesador, LinkedList<Tarea>> asignacionTareas(Integer tiempoMaximoProcNoRefrigerado) {
        /*if (colaTareas.size() > procesadores.size()) {
            System.out.println("No se encontró solución válida ya que hay mas tareas que procesadores.");
            return new HashMap<>();
        }*/
        backtracking(null, colaTareas, tiempoMaximoProcNoRefrigerado);
        return solucion;
    }

    private void backtracking(Procesador procesadorActual, LinkedList<Tarea> colaTareas, Integer tiempoMaximoNoRefrigerado) {
        this.cantEstadosGenerados++;
        if (colaTareas.isEmpty()) {  //SI ME QUEDE SIN TAREAS PARA ASIGNAR
            reemplazarMejorSolucion(posibleSolucion);
        } else {
            for (Procesador procesador : this.procesadores) {
                Tarea nextTarea = colaTareas.remove();
                if (puedeAsignarseTareaAProcesador(procesador, nextTarea, tiempoMaximoNoRefrigerado, posibleSolucion)) { //ESTO ME BAJA LA CANTIDAD DE ESTADOS GENERADOS
                    agregarTareaAProc(procesador, nextTarea, posibleSolucion, tiempoMaximoNoRefrigerado);                //ESTARIA MAL?
                    backtracking(procesador, colaTareas, tiempoMaximoNoRefrigerado);
                    sacarTareaAProc(procesador, nextTarea, posibleSolucion);
                }
                colaTareas.add(0, nextTarea);
            }
        }
    }

    //AGREGA TAREA A LA POSIBLE SOLUCIÓN
    private void agregarTareaAProc(Procesador procesador, Tarea tarea, HashMap<Procesador, LinkedList<Tarea>> posibleSolucion, Integer tiempoMaximoNoRefrigerado) {
        LinkedList<Tarea> listaTareas = posibleSolucion.get(procesador);
        listaTareas.add(tarea);
        if (tarea.getEsCritica())
            procesador.incrementarTareasCriticas(); //MEJORABLE SEGURAMENTE
    }

    //ELIMINA TAREA A LA POSIBLE SOLUCIÓN
    private void sacarTareaAProc(Procesador procesador, Tarea tarea, HashMap<Procesador, LinkedList<Tarea>> posibleSolucion) {
        posibleSolucion.get(procesador).remove(tarea);
        if (tarea.getEsCritica())
            procesador.decrementarTareasCriticas();
    }

    //CHEQUEO DE RESTRICCIONES DEL ENUNCIADO:
    //-NINGÚN PROCESADOR EJECUTA MAS DE 2 TAREAS CRITICAS
    //-PROCESADORES NO REFRIGERADOS NO PUEDEN DEDICAR MAS DE X TIEMPO DE EJECUCIÓN
    private boolean puedeAsignarseTareaAProcesador(Procesador procesador, Tarea tarea, Integer
            tiempoMaximoProcNoRefrigerado, HashMap<Procesador, LinkedList<Tarea>> posibleSolucion) {
        if (procesador.getCantTareasCriticas() == 2 && tarea.getEsCritica())
            return false;
        if (!procesador.getRefrigerado() && getTiempoEjecucionProcesador(procesador, posibleSolucion) + tarea.getTiempoEjecucion() > tiempoMaximoProcNoRefrigerado)
            return false;
        return true;
    }

    //OBTENGO TIEMPO DE EJECUCIÓN DE UN PROCESADOR
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
    private void reemplazarMejorSolucion(HashMap<Procesador, LinkedList<Tarea>> posibleSolucion) {
        if (posibleSolucionEsMejorQueSolucion(posibleSolucion)) {
            Iterator<LinkedList<Tarea>> itListaTareas = solucion.values().iterator();
            while (itListaTareas.hasNext()) {
                LinkedList<Tarea> listaTareaI = itListaTareas.next();
                listaTareaI.clear();
            }
            for (Procesador procesador : procesadores) {
                LinkedList<Tarea> p = posibleSolucion.get(procesador);
                solucion.get(procesador).addAll(p);
            }
        }
    }

    //CHEQUEO SI POSIBLE SOLUCION ES MEJOR QUE SOLUCION
    private boolean posibleSolucionEsMejorQueSolucion(HashMap<Procesador, LinkedList<Tarea>> posibleSolucion) {
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
        System.out.println("Mejor solución encontrada: " + b.asignacionTareas(51));
        System.out.println("Tiempo máximo de ejecución de la solución: " + b.getTiempoMaximoEjecucionSolucion());
        System.out.println("Cantidad de estados generados: " + b.getCantEstadosGenerados());
    }


}
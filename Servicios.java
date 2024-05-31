package tpe;

import tpe.utils.CSVReader;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
    private HashMap<String, Tarea> tareas;
    private LinkedList<Tarea> tareasCriticas;
    private LinkedList<Tarea> tareasNoCriticas;
    private TareasPorPrioridad[] tareasPorPrioridad;

    /*
     * COMPLEJIDAD O(N) DONDE N ES LA CANTIDAD DE TAREAS
     */
    public Servicios(String pathProcesadores, String pathTareas) {
        CSVReader reader = new CSVReader();
        reader.readProcessors(pathProcesadores);
        reader.readTasks(pathTareas);

        List<Tarea> listaTareas = reader.readTasks(pathTareas);

        this.tareas = new HashMap<>();
        this.tareasCriticas = new LinkedList<>();
        this.tareasNoCriticas = new LinkedList<>();
        this.tareasPorPrioridad = new TareasPorPrioridad[101];

        for (Tarea tarea : listaTareas) {
            tareas.put(tarea.getIdTarea(), tarea); //MAPEO SERVICIO 1 O(1)
            this.addTareaPorCriticidad(tarea.getEsCritica(), tarea); //MAPEO SERVICIO 2 O(1)
            this.addTareaAPrioridad(tarea.getNivelPrioridad(), tarea); //MAPEO SERVICIO 3 O(1)
        }
    }


    /*
     * O(1)
     */
    public Tarea servicio1(String ID) {
        return tareas.get(ID);
    }


    /*
     * Expresar la complejidad temporal del servicio 2.
     */
    //OBTENER TAREAS POR CRITICIDAD O(1)
    public List<Tarea> servicio2(boolean esCritica) {
        if (esCritica)
            return tareasCriticas;
        return tareasNoCriticas;
    }

    //AGREGAR TAREAS POR CRITICIDAD O(1)
    public void addTareaPorCriticidad(boolean esCritica, Tarea tarea) {
        if (esCritica)
            tareasCriticas.add(tarea);
        else
            tareasCriticas.add(tarea);
    }


    /*
     * O(N) donde N es la cantidad de tareas.
     */
    //OBTENER LAS TAREAS ENTRE LAS PRIORIDADES O(prioridadSuperior - prioridadInferior) = O(n) EN EL PEOR CASO
    public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
        if (prioridadInferior < 0 || prioridadSuperior > 100) {
            return null;
        } else {
            List<Tarea> resultado = new LinkedList<>();
            while (prioridadInferior <= prioridadSuperior) {
                TareasPorPrioridad tareaHija = tareasPorPrioridad[prioridadInferior];
                if (tareaHija != null) {
                    resultado.addAll(tareaHija.getTareas());
                }
                prioridadInferior++;
            }
            return resultado;
        }
    }

    public void addTareaAPrioridad(Integer prioridad, Tarea tarea) {
        if (prioridad >= 0 && prioridad <= 100) {
            if (tareasPorPrioridad[prioridad] == null)
                tareasPorPrioridad[prioridad] = new TareasPorPrioridad(prioridad);
            tareasPorPrioridad[prioridad].addTarea(tarea);
        }
    }
}
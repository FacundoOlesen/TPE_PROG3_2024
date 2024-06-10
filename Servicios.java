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


    /*La operación más costosa del Constructor es la de agregar todas las tareas a las
    estructuras utilizadas para los servicios teniendo que recorrer TODAS las tareas.
    Esto nos genera una complejidad de O(N) donde N es la cantidad de tareas.*/
    public Servicios(String pathProcesadores, String pathTareas) {
        CSVReader reader = new CSVReader();
        List<Tarea> listaTareas = reader.readTasks(pathTareas);

        this.tareas = new HashMap<>();
        this.tareasCriticas = new LinkedList<>();
        this.tareasNoCriticas = new LinkedList<>();
        this.tareasPorPrioridad = new TareasPorPrioridad[101];

        for (Tarea tarea : listaTareas) {
            tareas.put(tarea.getIdTarea(), tarea);
            this.addTareaPorCriticidad(tarea.getEsCritica(), tarea);
            this.addTareaAPrioridad(tarea.getNivelPrioridad(), tarea);
        }
    }

    /*La Complejidad Temporal del Servicio 1 es O(1) ya que el HashMap hace 1 solo acceso
    con la funcion mod para obtener un elemento de su estructura.*/
    public Tarea servicio1(String ID) {
        return tareas.get(ID);
    }


    /*La Complejidad Temporal del Servicio 2 es O(1) ya que al dividir las tareas por criticidad en
      2 listas, si queremos ver las tareas criticas obtenemos la lista de tareas criticas O(1) y si
      queremos ver las no criticas obtenemos la lista de tareas no criticas O(1).*/
    public List<Tarea> servicio2(boolean esCritica) {
        if (esCritica)
            return tareasCriticas;
        return tareasNoCriticas;
    }

    /*El método utilizado para agregar tareas a las listas de criticidad dependiendo si es crítica o no
     es O(1) también ya que LinkedList tiene una complejidad de O(1) para insertar elementos.*/
    public void addTareaPorCriticidad(boolean esCritica, Tarea tarea) {
        if (esCritica)
            tareasCriticas.add(tarea);
        else
            tareasNoCriticas.add(tarea);
    }


    /*
     Obtener las tareas entre 2 niveles de prioridad dados tiene una Complejidad de O(N) donde N
     es la cantidad de tareas ya que en el peor de los casos nos pasaran la prioridad mínima que
     pueden tener las tareas y la prioridad máxima, obligandonos de esta forma a recorrer toda
     la lista de tareas.
     */
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

    /*
    La complejidad temporal de insertar un elemento en la lista de solución
    es de O(1) ya que previamente asignamos el arreglo con los espacios
    necesarios para evitar los corrimientos que este realiza al querer
    insertar un elemento cuando ya no hay más espacio.
    */
    public void addTareaAPrioridad(Integer prioridad, Tarea tarea) {
        if (prioridad >= 0 && prioridad <= 100) {
            if (tareasPorPrioridad[prioridad] == null)
                tareasPorPrioridad[prioridad] = new TareasPorPrioridad(prioridad);
            tareasPorPrioridad[prioridad].addTarea(tarea);
        }
    }
}
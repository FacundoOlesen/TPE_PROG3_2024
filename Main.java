package tpe;

import java.util.LinkedList;

public class Main {

    public static void main(String args[]) {
        Servicios servicios = new Servicios("datasets/Procesadores.csv", "datasets/Tareas.csv");
        //System.out.println(servicios.servicio3(0, 100));


        Backtracking b = new Backtracking("datasets/Procesadores.csv", "datasets/Tareas.csv");
        System.out.println("Mejor solución encontrada: " + b.asignacionTareas(69));
        System.out.println("Tiempo máximo de ejecución de la solución: " + b.getTiempoMaximoEjecucionSolucion());
        System.out.println("Cantidad de estados generados: " + b.getCantEstadosGenerados());
    }
}

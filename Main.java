package tpe;

import java.util.LinkedList;

public class Main {

    public static void main(String args[]) {
        System.out.println("TPE PARTE 1");
        System.out.println("");

        Servicios servicios = new Servicios("datasets/Procesadores.csv", "datasets/Tareas.csv");

        System.out.println("SERVICIO 1: (Dado un identificador de tarea obtener toda la información de la tarea asociada).");
        System.out.println(servicios.servicio1("T1"));

        System.out.println("");

        System.out.println("SERVICIO 2: (Ver todas las tareas críticas o no críticas).");
        System.out.println(servicios.servicio2(false));

        System.out.println("");

        System.out.println("SERVICIO 3: (Obtener todas las tareas entre 2 niveles de prioridad indicados).");
        System.out.println(servicios.servicio3(0, 35));


        System.out.println("");
        System.out.println("");

        System.out.println("TPE PARTE 2");
        System.out.println("SOLUCIÓN BACKTRACKING");
        Backtracking solucionBacktracking = new Backtracking("datasets/Procesadores.csv", "datasets/Tareas.csv");
         System.out.println("Mejor solución encontrada: " + solucionBacktracking.backtracking(69));
        System.out.println("Tiempo máximo de ejecución de la solución: " + solucionBacktracking.getTiempoMaximoEjecucionSolucion());
        System.out.println("Cantidad de estados generados: " + solucionBacktracking.getCantEstadosGenerados());

        System.out.println("");
        System.out.println("");


        System.out.println("SOLUCIÓN GREEDY");

        Greedy greedy = new Greedy("datasets/Procesadores.csv", "datasets/Tareas.csv");
        System.out.println(greedy.greedy(69));
        System.out.println("Tiempo máximo de ejecución de la solución: " + greedy.getPeorTiempoProcesador());
        System.out.println("Cant candidatos: " + greedy.getCantCandidatos());
    }
}

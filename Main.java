package tpe;

import java.util.LinkedList;

public class Main {

    public static void main(String args[]) {
        //Servicios servicios = new Servicios("datasets/Procesadores.csv", "datasets/Tareas.csv");
        //System.out.println(servicios.servicio3(0, 100));

        System.out.println("SOLUCIÓN BACKTRACKING");
        Backtracking solucionBacktracking = new Backtracking("datasets/Procesadores.csv", "datasets/Tareas.csv");
        System.out.println("Mejor solución encontrada: " + solucionBacktracking.backtracking(70));
        System.out.println("Tiempo máximo de ejecución de la solución: " + solucionBacktracking.getTiempoMaximoEjecucionSolucion());
        System.out.println("Cantidad de estados generados: " + solucionBacktracking.getCantEstadosGenerados());

        System.out.println("");
        System.out.println("");


        System.out.println("SOLUCIÓN GREEDY");

        Greedy greedy = new Greedy("datasets/Procesadores.csv", "datasets/Tareas.csv");
        System.out.println(greedy.greedy(70));
        System.out.println("Cant candidatos: " + greedy.getCantCandidatos());
    }
}

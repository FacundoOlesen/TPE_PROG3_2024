package tpe;

import java.util.LinkedList;

public class Main {

    public static void main(String args[]) {
        Servicios servicios = new Servicios("datasets/Procesadores.csv", "datasets/Tareas.csv");
        System.out.println(servicios.servicio3(0, 100));
    }
}

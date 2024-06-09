package tpe;

import java.util.Comparator;
import java.util.LinkedList;

public class Procesador {
    private String id;
    private String codigo;
    private Boolean refrigerado;
    private Integer anio;
    private int cantTareasCriticas;
    private int tiempoEjecucion;

    public Procesador(String id, String codigo, Boolean refrigerado, Integer anio) {
        this.id = id;
        this.codigo = codigo;
        this.refrigerado = refrigerado;
        this.anio = anio;
        this.cantTareasCriticas = 0;
        this.tiempoEjecucion = 0;
    }

    public String getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public Boolean getRefrigerado() {
        return refrigerado;
    }

    public Integer getAnio() {
        return anio;
    }


    public int getCantTareasCriticas() {
        return cantTareasCriticas;
    }

    public void incrementarTareasCriticas() {
        this.cantTareasCriticas++;
    }

    public void decrementarTareasCriticas() {
        this.cantTareasCriticas--;
    }

    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void incrementarTiempoEjecucion(int tiempoEjecucion) {
        this.tiempoEjecucion = this.getTiempoEjecucion() + tiempoEjecucion;
    }

    public void decrementarTiempoEjecucion(int tiempoEjecucion) {
        this.tiempoEjecucion = this.getTiempoEjecucion() - tiempoEjecucion;
    }

    public static Comparator<Procesador> procComparator = new Comparator<Procesador>() {
        @Override
        public int compare(Procesador p1, Procesador p2) {
            return Boolean.compare(p2.getRefrigerado(), p1.getRefrigerado());
        }
    };

    @Override
    public String toString() {
        return id;
    }
}

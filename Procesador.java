package tpe;

import java.util.LinkedList;

public class Procesador {
    private String id;
    private String codigo;
    private Boolean refrigerado;
    private Integer anio;
    private int cantTareasCriticas;

    public Procesador(String id, String codigo, Boolean refrigerado, Integer anio) {
        this.id = id;
        this.codigo = codigo;
        this.refrigerado = refrigerado;
        this.anio = anio;
        this.cantTareasCriticas = 0;
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

    public void setCantTareasCriticas(int cantTareasCriticas) {
        this.cantTareasCriticas = cantTareasCriticas;
    }

    @Override
    public String toString() {
        return id;
    }
}
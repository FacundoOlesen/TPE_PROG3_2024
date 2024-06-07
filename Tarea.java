package tpe;

public class Tarea {
    private String idTarea;
    private String nombreTarea;
    public Integer tiempoEjecucion;
    private Boolean esCritica;
    private Integer nivelPrioridad;

    public Tarea(String idTarea, String nombreTarea, Integer tiempoEjecucion, Boolean esCritica, Integer nivelPrioridad) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.tiempoEjecucion = tiempoEjecucion;
        this.esCritica = esCritica;
        this.nivelPrioridad = nivelPrioridad;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public Integer getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(Integer tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public Boolean getEsCritica() {
        return esCritica;
    }

    public void setEsCritica(Boolean esCritica) {
        this.esCritica = esCritica;
    }

    public Integer getNivelPrioridad() {
        return nivelPrioridad;
    }

    public void setNivelPrioridad(Integer nivelPrioridad) {
        this.nivelPrioridad = nivelPrioridad;
    }

    @Override
    public String toString() {
        return idTarea;
    }
}

package EstacionEspacial;

public class Robot {
    private String nombre;
    private String numeroSerie;
    private String falla;

    public Robot(String nombre, String numeroSerie, String falla){
        this.nombre = nombre;
        this.numeroSerie = numeroSerie;
        this.falla = falla;
    }

    @Override
    public String toString(){
        return "El robot :" + nombre + ",  Serie " + numeroSerie +", tiene una falla de "+  falla ;
    }
    public String getNombre() { return nombre;}
}

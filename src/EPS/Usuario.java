package EPS;

public class Usuario {
    private String nombre;
    private String cedula;
    private boolean atendido;
    private Vacuna vacPreferencia;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }

    public Vacuna getVacPreferencia() {
        return vacPreferencia;
    }

    public void setVacPreferencia(Vacuna vacPreferencia) {
        this.vacPreferencia = vacPreferencia;
    }

    @Override
    public String toString() {
        if(this.isAtendido())
        {
            return "Usuario Atendido {" + nombre + ", " + cedula + ", " + vacPreferencia + '}';
        }else{
            return "Usuario No Atendido {" + nombre + ", " + cedula + ", " + vacPreferencia + '}';
        }
    }

    public Usuario(String nombre, String cedula, boolean atendido, Vacuna vacPreferencia) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.atendido = atendido;
        this.vacPreferencia = vacPreferencia;
    }
}

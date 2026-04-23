package modelo;

public class Objeto {
    private int id_objeto;
    private String nombre;
    private int ataque;
    private int defensa;
    private int velocidad;
    private int ata_especial;
    private int def_especial;
    private int estamina;
    private int recuperacion_estamina;
    private int cantidad;
    public Objeto() {
    }

    // Constructor completo
    public Objeto(int id_objeto, String nombre, int ataque, int defensa, int velocidad, 
                  int ata_especial, int def_especial, int estamina, int recuperacion_estamina, int cantidad) {
        this.id_objeto = id_objeto;
        this.nombre = nombre;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.ata_especial = ata_especial;
        this.def_especial = def_especial;
        this.estamina = estamina;
        this.recuperacion_estamina = recuperacion_estamina;
        this.cantidad = cantidad;
    }


    public int getId_objeto() {
        return id_objeto;
    }

    public void setId_objeto(int id_objeto) {
        this.id_objeto = id_objeto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getAta_especial() {
        return ata_especial;
    }

    public void setAta_especial(int ata_especial) {
        this.ata_especial = ata_especial;
    }

    public int getDef_especial() {
        return def_especial;
    }

    public void setDef_especial(int def_especial) {
        this.def_especial = def_especial;
    }

    public int getEstamina() {
        return estamina;
    }

    public void setEstamina(int estamina) {
        this.estamina = estamina;
    }

    public int getRecuperacion_estamina() {
        return recuperacion_estamina;
    }

    public void setRecuperacion_estamina(int recuperacion_estamina) {
        this.recuperacion_estamina = recuperacion_estamina;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

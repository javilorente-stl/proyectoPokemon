package modelo;

public enum Tipo {
	AGUA("Agua"), 
	FUEGO("Fuego"), 
	PLANTA("Planta"), 
	BICHO("Bicho"), 
	VOLADOR("Volador"), 
	ELECTRICO("Electrico"), 
	TIERRA("Tierra"), 
	NORMAL("Normal"), 
	VENENO("Veneno"), 
	PSIQUICO("Psiquico"), 
	ROCA("Roca"), 
	HIELO("Hielo"), 
	FANTASMA("Fantasma"), 
	DRAGON("Dragon"), 
	LUCHA("Lucha");
    
    private final String nombreBD;

    Tipo(String nombreBD) {
        this.nombreBD = nombreBD;
    }

    public String getNombreBD() {
        return nombreBD;
    }
    
    public static Tipo convertirTpoDesdeString(String tipoString) {
    	for(Tipo tipo:Tipo.values()) {
    		if(tipo.getNombreBD().toUpperCase().equals(tipoString)) {
    			return tipo;
    		}
    	}
    	return null;
    }
}

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
    	if (tipoString == null) return null;
        
        String busqueda = tipoString.trim().toUpperCase();

        for (Tipo tipo : Tipo.values()) {
            if (tipo.name().toUpperCase().equals(busqueda) || 
                tipo.getNombreBD().toUpperCase().equals(busqueda)) {
                return tipo;
            }
        }
        
        // Si llegamos aquí y no se encontró, devolvemos null o un tipo por defecto
        // para que la aplicación no se cierre.
        System.out.println("ADVERTENCIA: No se encontró el tipo para: " + tipoString);
        return null;
    }
}

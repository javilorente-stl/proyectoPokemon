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
        
        // Normalizamos la entrada: quitamos espacios, pasamos a mayúsculas
        // y podrías añadir .replace("É", "E") aquí si quieres ser específico
        String busqueda = tipoString.trim().toUpperCase()
                                    .replace("É", "E")
                                    .replace("Í", "I")
                                    .replace("Á", "A")
                                    .replace("Ó", "O")
                                    .replace("Ú", "U");

        for (Tipo tipo : Tipo.values()) {
            // Hacemos lo mismo con el nombre del Enum y el nombre de la BD
            String nombreEnum = tipo.name().toUpperCase();
            String nombreBD = tipo.getNombreBD().toUpperCase()
                                                .replace("É", "E")
                                                .replace("Í", "I")
                                                .replace("Á", "A")
                                                .replace("Ó", "O")
                                                .replace("Ú", "U");

            if (nombreEnum.equals(busqueda) || nombreBD.equals(busqueda)) {
                return tipo;
            }
        }
        
        System.out.println("ADVERTENCIA: No se encontró el tipo para: " + tipoString);
        return null;
    }
}

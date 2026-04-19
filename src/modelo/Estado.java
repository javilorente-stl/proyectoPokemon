package modelo;

public enum Estado {
	VIVO,
	PARALIZADO,
	QUEMADO,
	ENVENENADO,
	GRAVEMENTE_ENVENENADO,
	DORMIDO,
	CONGELADO,
	HELADO,
	SOMNOLIENTO,
	POKERUS,
	DEBILITADO,
	CONFUSO,
	ENAMORADO,
	ATRAPADO,
	MALDITO,
	DRENADORAS,
	CANTO_MORTAL,
	CENTRO_DE_ATENCION,
	RETROCEDIDO;


public static Estado convertirEstadoDesdeString(String estadoStr) {
    if (estadoStr == null || estadoStr.isEmpty()) {
        return VIVO; // Valor por defecto seguro
    }

    try {
        // Pasamos a mayúsculas y quitamos espacios por seguridad
        // Por si en la DB se guardó "Canto Mortal" en lugar de "CANTO_MORTAL"
        String formateado = estadoStr.trim().toUpperCase().replace(" ", "_");
        return Estado.valueOf(formateado);
    } catch (IllegalArgumentException e) {
        // Si el texto no coincide con ningún Enum (ej: "QUEMADOO")
        System.err.println("Advertencia: El estado '" + estadoStr + "' no existe. Se asigna VIVO.");
        return VIVO;
    }
}
}
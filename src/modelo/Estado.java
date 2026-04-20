package modelo;

public enum Estado {
    VIVO("Vivo"),
    PARALIZADO("Paralizado"),
    QUEMADO("Quemado"),
    ENVENENADO("Envenenado"),
    GRAVEMENTE_ENVENENADO("Gravemente_Envenenado"),
    DORMIDO("Dormido"),
    CONGELADO("Congelado"),
    HELADO("Helado"),
    SOMNOLIENTO("Somnoliento"),
    POKERUS("Pokérus"),
    DEBILITADO("Debilitado"),
    CONFUSO("Confuso"),
    ENAMORADO("Enamorado"),
    ATRAPADO("Atrapado"),
    MALDITO("Maldito"),
    DRENADORAS("Drenadoras"),
    CANTO_MORTAL("Canto Mortal"),
    CENTRO_DE_ATENCION("Centro de Atención"),
    RETROCEDIDO("Retrocedido");

    private final String nombreMostrar;

    Estado(String nombreMostrar) {
        this.nombreMostrar = nombreMostrar;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    public static Estado convertirEstadoDesdeString(String estadoStr) {
        if (estadoStr == null || estadoStr.isEmpty()) return VIVO;

        // Normalizamos la entrada: quitamos tildes, espacios y pasamos a mayúsculas
        String busqueda = normalizar(estadoStr);

        for (Estado estado : Estado.values()) {
            // Comparamos contra el nombre del Enum (ej: GRAVEMENTE_ENVENENADO)
            String nombreEnum = estado.name(); 
            // Comparamos contra el nombre legible (ej: Gravemente Envenenado)
            String nombreBonito = normalizar(estado.getNombreMostrar());

            if (nombreEnum.equals(busqueda) || nombreBonito.equals(busqueda)) {
                return estado;
            }
        }

        System.err.println("ADVERTENCIA: No se encontró el estado para: " + estadoStr + ". Se asigna VIVO.");
        return VIVO;
    }

    // Método privado para limpiar Strings (tildes, espacios y guiones)
    private static String normalizar(String texto) {
        return texto.trim().toUpperCase()
                .replace(" ", "_")
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U");
    }
}
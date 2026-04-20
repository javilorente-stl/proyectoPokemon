package modelo;

public enum Sexo {
    H("Hembra"),
    M("Macho"),
    X("Desconocido");

    private final String nombreMostrar;

    Sexo(String nombreMostrar) {
        this.nombreMostrar = nombreMostrar;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    public static Sexo convertirSexoDesdeString(String sexoStr) {
        if (sexoStr == null || sexoStr.isEmpty()) return X;

        // Normalizamos la entrada (mayúsculas y sin espacios)
        String busqueda = sexoStr.trim().toUpperCase();

        for (Sexo sexo : Sexo.values()) {
            // Comparamos contra el nombre del Enum (H, M, X)
            String nombreEnum = sexo.name();
            // Comparamos contra el nombre descriptivo (Hembra, Macho...)
            String nombreBonito = sexo.getNombreMostrar().toUpperCase();

            if (nombreEnum.equals(busqueda) || nombreBonito.equals(busqueda)) {
                return sexo;
            }
        }

        System.err.println("ADVERTENCIA: No se encontró el sexo para: " + sexoStr + ". Se asigna X.");
        return X;
    }
}
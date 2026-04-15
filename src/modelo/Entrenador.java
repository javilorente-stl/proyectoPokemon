package modelo;

import java.util.ArrayList;

public class Entrenador {
	private int idEntrenador;
	private String nombre;
	private String password;
	private int pokedollars;
	private String imagenEntrenador;
	private int claseEntrenador;
	private ArrayList<Pokemon> equipo1;
	private ArrayList<Pokemon> equipo2;
	private ArrayList<Objeto> mochila;

	
	
	
	public Entrenador(String nombre, String password, ArrayList<Pokemon> equipo1, ArrayList<Pokemon> equipo2,
			int pokedollars, ArrayList<Objeto> mochila) {
		super();
		this.nombre = nombre;
		this.password = password;
		this.equipo1 = equipo1;
		this.equipo2 = equipo2;
		this.pokedollars = pokedollars;
		this.mochila = mochila;
	}

	public Entrenador(String usuario, String password) {
		super();
		this.idEntrenador=0;
		this.nombre = usuario;
		this.password = password;
		this.pokedollars = 0;
		this.equipo1 = null;
		this.equipo2 = null;
		this.mochila = null;
	}




	public int getIdEntrenador() {
		return idEntrenador;
	}

	public void setIdEntrenador(int idEntrenador) {
		this.idEntrenador = idEntrenador;
	}

	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public String getImagenEntrenador() {
		return imagenEntrenador;
	}

	public void setImagenEntrenador(String imagenEntrenador) {
		this.imagenEntrenador = imagenEntrenador;
	}

	public int getClaseEntrenador() {
		return claseEntrenador;
	}

	public void setClaseEntrenador(int claseEntrenador) {
		this.claseEntrenador = claseEntrenador;
	}

	public ArrayList<Pokemon> getEquipo1() {
		return equipo1;
	}




	public void setEquipo1(ArrayList<Pokemon> equipo1) {
		this.equipo1 = equipo1;
	}




	public ArrayList<Pokemon> getEquipo2() {
		return equipo2;
	}




	public void setEquipo2(ArrayList<Pokemon> equipo2) {
		this.equipo2 = equipo2;
	}




	public int getPokedollars() {
		return pokedollars;
	}




	public void setPokedollars(int pokedollars) {
		this.pokedollars = pokedollars;
	}




	public ArrayList<Objeto> getMochila() {
		return mochila;
	}




	public void setMochila(ArrayList<Objeto> mochila) {
		this.mochila = mochila;
	}




	public void moverPokemonACaja(int indice) {
	    //Validamos que el equipo principal tenga más de un Pokémon
	    if (equipo1.size() <= 1) {
	        System.out.println("Error: No puedes quedarte sin Pokémon en tu equipo principal.");
	        return; // Salimos del método para que no borre nada
	    }

	    //Validamos que el índice que nos pasan sea correcto (0 a size-1)
	    if (indice >= 0 && indice < equipo1.size()) {
	        
	        //Obtenemos el Pokémon del equipo 1
	        Pokemon p = equipo1.get(indice);
	        
	        //Lo añadimos a la caja (equipo 2)
	        equipo2.add(p);
	        
	        //Lo borramos del equipo principal
	        equipo1.remove(indice);
	        
	        System.out.println(p.getNombre() + " ha sido movido a la caja.");
	        
	    } else {
	        System.out.println("Error: El índice seleccionado no es válido.");
	    }
	}
	
	public void sacarDeCajaAEquipo(int indiceCaja) {
	    //Validar si el equipo principal ya está lleno (máximo 4)
	    if (equipo1.size() >= 4) {
	        System.out.println("Error: El equipo principal ya tiene 4 Pokémon. Debes mover uno a la caja primero.");
	        return;
	    }

	    //Validar que el índice de la caja sea correcto
	    if (indiceCaja >= 0 && indiceCaja < equipo2.size()) {
	        
	        //Obtener el Pokémon de la caja
	        Pokemon p = equipo2.get(indiceCaja);
	        
	        //Añadirlo al equipo principal
	        equipo1.add(p);
	        
	        //Eliminarlo de la caja
	        equipo2.remove(indiceCaja);
	        
	        System.out.println(p.getNombre() + " se ha unido al equipo principal.");
	        
	    } else {
	        System.out.println("Error: No hay ningún Pokémon en esa posición de la caja.");
	    }
	}
	
	public void entrenarPokemon(Pokemon p, String tipo) {
	    int baseCoste = 0;
	    int nivel = p.getNivel();
	    int mejora = 5; // Los "Y" puntos que mencionas

	    //Determinamos el multiplicador de coste según el tipo
	    switch (tipo.toLowerCase()) {
	        case "pesado": baseCoste = 20; break;
	        case "furioso": baseCoste = 30; break;
	        case "funcional": baseCoste = 40; break;
	        case "onirico": baseCoste = 40; break;
	        default:
	            System.out.println("Tipo de entrenamiento no reconocido.");
	            return;
	    }

	    int costeTotal = baseCoste * nivel;

	    //Verificamos si el entrenador tiene dinero suficiente
	    if (this.pokedollars >= costeTotal) {
	        this.pokedollars -= costeTotal; // Cobramos

	        //Aplicamos las mejoras según el tipo de entrenamiento
	        switch (tipo.toLowerCase()) {
	            case "pesado":
	                p.setDefensa(p.getDefensa() + mejora);
	                p.setDefensaEspecial(p.getDefensaEspecial() + mejora);
	                p.setVitalidad(p.getVitalidad() + mejora);
	                break;
	            case "furioso":
	                p.setAtaque(p.getAtaque() + mejora);
	                p.setAtaqueEspecial(p.getAtaqueEspecial() + mejora);
	                p.setVelocidad(p.getVelocidad() + mejora);
	                break;
	            case "funcional":
	                p.setVelocidad(p.getVelocidad() + mejora);
	                p.setAtaque(p.getAtaque() + mejora);
	                p.setDefensa(p.getDefensa() + mejora);
	                p.setVitalidad(p.getVitalidad() + mejora);
	                break;
	            case "onirico":
	                p.setVelocidad(p.getVelocidad() + mejora);
	                p.setAtaqueEspecial(p.getAtaqueEspecial() + mejora);
	                p.setDefensaEspecial(p.getDefensaEspecial() + mejora);
	                p.setVitalidad(p.getVitalidad() + mejora);
	                break;
	        }
	        System.out.println("¡Entrenamiento " + tipo + " completado! Has gastado " + costeTotal + " pokédollars.");
	    } else {
	        System.out.println("No tienes suficiente dinero. Coste necesario: " + costeTotal);
	    }
	}
	
	public Pokemon criar(Pokemon padre, Pokemon madre) {
	    //Verificación de seguridad (Fertilidad)
	    if (padre.getFertilidad() <= 0 || madre.getFertilidad() <= 0) {
	        System.out.println("La crianza no es posible: falta fertilidad.");
	        return null;
	    }

	    //Mote: Mitad y mitad en orden aleatorio (Se mantiene igual)
	    String partePadre = padre.getMote().substring(0, padre.getMote().length() / 2);
	    String parteMadre = madre.getMote().substring(0, madre.getMote().length() / 2);
	    String nuevoMote = (Math.random() < 0.5) ? (partePadre + parteMadre) : (parteMadre + partePadre);

	    //Estadísticas: Las mejores de cada progenitor (Se mantiene igual)
	    int vitalidadHijo = Math.max(padre.getVitalidad(), madre.getVitalidad());
	    int ataqueHijo = Math.max(padre.getAtaque(), madre.getAtaque());
	    int defensaHijo = Math.max(padre.getDefensa(), madre.getDefensa());
	    int atkEspHijo = Math.max(padre.getAtaqueEspecial(), madre.getAtaqueEspecial());
	    int defEspHijo = Math.max(padre.getDefensaEspecial(), madre.getDefensaEspecial());
	    int velocidadHijo = Math.max(padre.getVelocidad(), madre.getVelocidad());

	    //Ataques: Se mantiene ArrayList si tu clase Pokemon aún usa listas para ataques
	    // (Si también quieres cambiar ataques a variables fijas, avísame).
	    ArrayList<Movimiento> ataquesHijo = new ArrayList<>();
	    for (int i = 0; i < 2; i++) {
	        ataquesHijo.add(padre.getMovimientos().get((int)(Math.random() * padre.getMovimientos().size())));
	        ataquesHijo.add(madre.getMovimientos().get((int)(Math.random() * madre.getMovimientos().size())));
	    }

	    //Tipos: Lógica adaptada para tipo1 y tipo2
	    // Creamos un array con todos los tipos posibles de los padres (ignorando nulos)
	    Tipo[] poolTiposAux = {padre.getTipo1(), padre.getTipo2(), madre.getTipo1(), madre.getTipo2()};
	    
	    // Contamos cuántos tipos reales (no nulos) hay para evitar errores
	    int tiposValidos = 0;
	    for (Tipo t : poolTiposAux) if (t != null) tiposValidos++;
	    
	    // Pasamos los tipos no nulos a un array limpio para elegir al azar
	    Tipo[] poolLimpio = new Tipo[tiposValidos];
	    int j = 0;
	    for (Tipo t : poolTiposAux) {
	        if (t != null) poolLimpio[j++] = t;
	    }

	    // Elegimos el tipo 1 del hijo
	    Tipo tipo1Hijo = poolLimpio[(int)(Math.random() * poolLimpio.length)];
	    
	    // Elegimos el tipo 2 del hijo (puede ser nulo si queremos que haya puros, o elegir otro)
	    Tipo tipo2Hijo = poolLimpio[(int)(Math.random() * poolLimpio.length)];
	    
	    // Si el tipo2 es igual al tipo1, lo ponemos como null para que sea tipo único
	    if (tipo1Hijo == tipo2Hijo) {
	        tipo2Hijo = null;
	    }

	    //Actualización de padres
	    padre.setFertilidad(padre.getFertilidad() - 1);
	    madre.setFertilidad(madre.getFertilidad() - 1);

	    //Creamos el nuevo Pokémon con los nuevos parámetros
	    return new Pokemon(nuevoMote, tipo1Hijo, tipo2Hijo, ataquesHijo, vitalidadHijo, 
	                       ataqueHijo, defensaHijo, atkEspHijo, defEspHijo, velocidadHijo);
	}
	
}

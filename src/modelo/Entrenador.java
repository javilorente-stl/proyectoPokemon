package modelo;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Representa al usuario o entidad que posee y gestiona Pokémon.
 * Contiene la información de perfil, el saldo de Pokédollars y los contenedores
 * para el equipo activo, el almacenamiento (caja) y los objetos de la mochila, que al final no he implementado.
 * @author Javier Lorente Rodríguez
 * @version 1.2
 */
public class Entrenador {
	private int idEntrenador;
	private String nombre;
	private String password;
	private int pokedollars;
	private String imagenEntrenador;
	private int claseEntrenador;
	// Contenedores para los datos
	private LinkedList<Pokemon> equipo1 = new LinkedList<>();
	private LinkedList<Pokemon> equipo2 = new LinkedList<>();
	private LinkedList<Objeto> mochila = new LinkedList<>();

	
	/**
	 * Constructor principal para la carga completa de un perfil de entrenador
	 * desde la persistencia.
	 */
	public Entrenador(String nombre, String password, LinkedList<Pokemon> equipo1, LinkedList<Pokemon> equipo2,
			int pokedollars, LinkedList<Objeto> mochila) {
		super();
		this.nombre = nombre;
		this.password = password;
		this.equipo1 = equipo1;
		this.equipo2 = equipo2;
		this.pokedollars = pokedollars;
		this.mochila = mochila;
	}

	/**
	 * Constructor simplificado para inicializar un nuevo entrenador con 
	 * valores por defecto y sin fondos iniciales. Creo que este no hemos llegado
	 * a usarlo específicamente
	 */
	public Entrenador(String nombre, String password, LinkedList<Pokemon> equipo1) {
		super();
		this.nombre = nombre;
		this.password = password;
		this.equipo1 = equipo1;

	}
	
	/**
	 * Este es el constructor que hemos usado en el login para crear a nuevos
	 * usuarios, solo con el nombre y la contraseña
	 * y el resto de valores por defecto
	 * @param usuario
	 * @param password
	 */
	public Entrenador(String usuario, String password) {
		super();
		this.idEntrenador = 0;
		this.nombre = usuario;
		this.password = password;
		this.pokedollars = 0;
		this.equipo1 = new LinkedList<>();
		this.equipo2 = new LinkedList<>();
		this.mochila = new LinkedList<>();
	}

	// Un constructor vacío por si acaso
	public Entrenador() {
		super();

	}

	// Métodos Getter y Setter
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

	public LinkedList<Pokemon> getEquipo1() {
		return equipo1;
	}

	public void setEquipo1(LinkedList<Pokemon> equipo1) {
		this.equipo1 = equipo1;
	}

	public LinkedList<Pokemon> getEquipo2() {
		return equipo2;
	}

	public void setEquipo2(LinkedList<Pokemon> equipo2) {
		this.equipo2 = equipo2;
	}

	public int getPokedollars() {
		return pokedollars;
	}

	public void setPokedollars(int pokedollars) {
		this.pokedollars = pokedollars;
	}

	public LinkedList<Objeto> getMochila() {
		return mochila;
	}

	public void setMochila(LinkedList<Objeto> mochila) {
		this.mochila = mochila;
	}

	/**
	 * Método inicialmente creado para mover los pokemon, finalmente este método está 
	 * obsoleto, no lo hemos usado
	 * @param indice a la posición que movemos el pokemon
	 */
	public void moverPokemonACaja(int indice) {
		// Validamos que el equipo principal tenga más de un Pokémon
		if (equipo1.size() <= 1) {
			System.out.println("Error: No puedes quedarte sin Pokémon en tu equipo principal.");
			return; // Salimos del método para que no borre nada
		}

		// Validamos que el índice que nos pasan sea correcto (0 a size-1)
		if (indice >= 0 && indice < equipo1.size()) {

			// Obtenemos el Pokémon del equipo 1
			Pokemon p = equipo1.get(indice);

			// Lo añadimos a la caja (equipo 2)
			equipo2.add(p);

			// Lo borramos del equipo principal
			equipo1.remove(indice);

			System.out.println(p.getNombre() + " ha sido movido a la caja.");

		} else {
			System.out.println("Error: El índice seleccionado no es válido.");
		}
	}

	/**
	 * El método contrario al anterior que saca al pokemon de la caja al equipo, igualmente
	 * obsoleto y que no estamos finalmente usando en la vista de la caja
	 * @param indiceCaja de donde sale
	 */
	public void sacarDeCajaAEquipo(int indiceCaja) {
		// Validar si el equipo principal ya está lleno (máximo 6)
		if (equipo1.size() >= 4) {
			System.out.println("Error: El equipo principal ya tiene 4 Pokémon. Debes mover uno a la caja primero.");
			return;
		}

		// Validar que el índice de la caja sea correcto
		if (indiceCaja >= 0 && indiceCaja < equipo2.size()) {

			// Obtener el Pokémon de la caja
			Pokemon p = equipo2.get(indiceCaja);

			// Añadirlo al equipo principal
			equipo1.add(p);

			// Eliminarlo de la caja
			equipo2.remove(indiceCaja);

			System.out.println(p.getNombre() + " se ha unido al equipo principal.");

		} else {
			System.out.println("Error: No hay ningún Pokémon en esa posición de la caja.");
		}
	}

}

package modelo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Pokemon {

	Scanner sc = new Scanner(System.in);

	private int id_pokemon;
	private int num_pokedex;
	private String nombre;
	private String mote;
	private int nivel;
	private int fertilidad;
	private Sexo sexo;
	private Estado estado;
	private int vitalidad;
	private int vitalidadMax;
	private int ataque;
	private int defensa;
	private int ataqueEspecial;
	private int defensaEspecial;
	private int velocidad;
	private int estamina;
	private int estaminaMax;
	private int experiencia;
	private Tipo tipo1;
	private Tipo tipo2;
	private LinkedList<Movimiento> movimientos;
	private LinkedList<Movimiento> movimientosPosibles;
	private Objeto objeto;
	private int caja;

	public Pokemon(int num_pokedex, String mote, int nivel, int fertilidad, Sexo sexo, Estado estado, int vitalidad,
			int vitalidadMax ,int ataque, int defensa, int ataqueEspecial, int defensaEspecial, int velocidad, int estamina,
			int experiencia, Tipo tipo1, Tipo tipo2, LinkedList<Movimiento> movimientos, Objeto objeto, int caja) {
		super();
		this.num_pokedex = num_pokedex;
		this.mote = mote;
		this.nivel = nivel;
		this.fertilidad = fertilidad;
		this.sexo = sexo;
		this.estado = estado;
		this.vitalidad = vitalidad;
		this.vitalidadMax = vitalidadMax;
		this.ataque = ataque;
		this.defensa = defensa;
		this.ataqueEspecial = ataqueEspecial;
		this.defensaEspecial = defensaEspecial;
		this.velocidad = velocidad;
		this.estamina = estamina;
		this.experiencia = experiencia;
		this.tipo1 = tipo1;
		this.tipo1 = tipo2;
		this.movimientos = movimientos;
		this.objeto = objeto;
		this.caja=caja;
	}
	
	
	

	public Pokemon(String mote, Tipo tipo1, Tipo tipo2, LinkedList<Movimiento> ataquesHijo, int vitalidad, int vitalidadMax, int ataque,
			int defensa, int ataqueEspecial, int defensaEspecial, int velocidad) {

		this.mote = mote;
		this.tipo1 = tipo1;
		this.tipo1 = tipo2;
		this.movimientos = ataquesHijo;
		this.vitalidad = vitalidad;
		this.vitalidadMax = vitalidadMax;
		this.ataque = ataque;
		this.defensa = defensa;
		this.ataqueEspecial = ataqueEspecial;
		this.defensaEspecial = defensaEspecial;
		this.velocidad = velocidad;
		this.nombre = mote;
		this.nivel = 1;
		this.experiencia = 0;
		this.fertilidad = 5; 
		this.objeto = null; 
		this.estado = Estado.VIVO; 
		this.sexo = (Math.random() < 0.5) ? Sexo.M : Sexo.H;
		this.estaminaMax = 100;
		this.estamina = this.estaminaMax;
		this.movimientosPosibles = new LinkedList<>();
		this.num_pokedex = 0;
		//La caja por defecto es la 2
		this.caja=2;
	}
	
	public Pokemon() {

		this.vitalidadMax = (int)(Math.random() * 5) + 1;
		this.vitalidad=vitalidadMax;
	    this.ataque = (int)(Math.random() * 5) + 1;
	    this.defensa = (int)(Math.random() * 5) + 1;
	    this.ataqueEspecial = (int)(Math.random() * 5) + 1;
	    this.defensaEspecial = (int)(Math.random() * 5) + 1;
	    this.velocidad = (int)(Math.random() * 5) + 1;
		this.nivel = 1;
		this.experiencia = 0;
		this.fertilidad = 5; 
		this.objeto = null; 
		this.estado = Estado.VIVO; 
		this.sexo = (Math.random() < 0.5) ? Sexo.M : Sexo.H;
		this.estaminaMax = 100;
		this.estamina = this.estaminaMax;
		this.movimientosPosibles = new LinkedList<>();
		this.num_pokedex = 0;
		this.caja=2;
	}
	
	
	public Pokemon(String mote) {

		// 1. Asignamos los valores heredados por crianza
		this.mote = mote;
		this.tipo1 = tipo1;
		this.tipo2 = tipo2;
		this.vitalidad = (int)(Math.random() * 5) + 1;
	    this.ataque = (int)(Math.random() * 5) + 1;
	    this.defensa = (int)(Math.random() * 5) + 1;
	    this.ataqueEspecial = (int)(Math.random() * 5) + 1;
	    this.defensaEspecial = (int)(Math.random() * 5) + 1;
	    this.velocidad = (int)(Math.random() * 5) + 1;

		
		this.nombre = mote;
		this.nivel = 1;
		this.experiencia = 0;
		this.fertilidad = 5;
		this.objeto = null;
		this.estado = Estado.VIVO;
		this.sexo = (Math.random() < 0.5) ? Sexo.M : Sexo.H;
		this.estaminaMax = 100;
		this.estamina = this.estaminaMax;
		this.movimientosPosibles = new LinkedList<>();
		// esto hay que arreglarlo
		this.num_pokedex = 0;
	}
	
	

	public int getId_pokemon() {
		return id_pokemon;
	}




	public void setId_pokemon(int id_pokemon) {
		this.id_pokemon = id_pokemon;
	}




	public int getNum_pokedex() {
		return num_pokedex;
	}

	public void setNum_pokedex(int num_pokedex) {
		this.num_pokedex = num_pokedex;
	}

	public String getNombre() {
		return nombre;
	}

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEstaminaMax() {
		return estaminaMax;
	}

	public void setEstaminaMax(int estaminaMax) {
		this.estaminaMax = estaminaMax;
	}

	public String getMote() {
		return mote;
	}

	public void setMote(String mote) {
		this.mote = mote;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getFertilidad() {
		return fertilidad;
	}

	public void setFertilidad(int fertilidad) {
		this.fertilidad = fertilidad;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getVitalidad() {
		return vitalidad;
	}

	public void setVitalidad(int vitalidad) {
		this.vitalidad = vitalidad;
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

	public int getAtaqueEspecial() {
		return ataqueEspecial;
	}

	public void setAtaqueEspecial(int ataqueEspecial) {
		this.ataqueEspecial = ataqueEspecial;
	}

	public int getDefensaEspecial() {
		return defensaEspecial;
	}

	public void setDefensaEspecial(int defensaEspecial) {
		this.defensaEspecial = defensaEspecial;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public int getEstamina() {
		return estamina;
	}

	public void setEstamina(int estamina) {
		this.estamina = estamina;
	}

	public int getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(int experiencia) {
		this.experiencia = experiencia;
	}

	public Tipo getTipo1() {
		return tipo1;
	}

	public void setTipo1(Tipo tipo1) {
		this.tipo1 = tipo1;
	}

	public Tipo getTipo2() {
		return tipo2;
	}

	public void setTipo2(Tipo tipo2) {
		this.tipo2 = tipo2;
	}

	public LinkedList<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(LinkedList<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public Objeto getObjeto() {
		return objeto;
	}

	public void setObjeto(Objeto objeto) {
		this.objeto = objeto;
	}

	public LinkedList<Movimiento> getMovimientosPosibles() {
		return movimientosPosibles;
	}

	public void setMovimientosPosibles(LinkedList<Movimiento> movimientosPosibles) {
		this.movimientosPosibles = movimientosPosibles;
	}
	
	

	public int getCaja() {
		return caja;
	}




	public void setCaja(int caja) {
		this.caja = caja;
	}




	public void subirNivel(Pokemon pokemon, Scanner sc) {
		if ((pokemon.getExperiencia() * 10) >= pokemon.getNivel()) {
			System.out.println("Ha subido un nivel");
			// Subimos un nivel del pokemon
			pokemon.setNivel(pokemon.getNivel() + 1);
			// Subimos todas las stats por la subida de nivel
			pokemon.setVitalidad(pokemon.getVitalidad() + (int) (Math.random() * 5) + 1);
			pokemon.setAtaque(pokemon.getAtaque() + (int) (Math.random() * 5) + 1);
			pokemon.setDefensa(pokemon.getDefensa() + (int) (Math.random() * 5) + 1);
			pokemon.setAtaqueEspecial(pokemon.getAtaqueEspecial() + (int) (Math.random() * 5) + 1);
			pokemon.setDefensaEspecial(pokemon.getDefensaEspecial() + (int) (Math.random() * 5) + 1);
			pokemon.setVelocidad(pokemon.getVelocidad() + (int) (Math.random() * 5) + 1);
		} else if (pokemon.getNivel() == 100) {
			System.out.println("Nivel 100 alcanzado, no puedes subir de nivel");
		} else {
			System.out.println("No tienes suficiente experiencia para subir de nivel");
		}

		if ((pokemon.getNivel() % 3) == 0) {
			pokemon.aprenderMovimiento(pokemon, sc);
		}
	}

	public void aprenderMovimiento(Pokemon pokemon, Scanner sc) {
		pokemon.getMovimientosPosibles().get((int) (Math.random() * (pokemon.getMovimientosPosibles().size())) + 1);
		if (pokemon.getMovimientos().size() == 4) {
			System.out.println("Ya tienes cuatro movimientos, selecciona cual quieres olvidar: ");
			int eleccion = sc.nextInt();
			// pokemon.getMovimientosPosibles().add(eleccion-1,
			// pokemon.getMovimientos().get(eleccion-1));;
			Movimiento olvidado = pokemon.getMovimientos().get(eleccion - 1);
			pokemon.getMovimientos().remove(eleccion - 1);

			// int indiceAleatorio = (int)(Math.random() *
			// pokemon.getMovimientosPosibles().size()-1);
			// pokemon.getMovimientos().add(pokemon.getMovimientosPosibles().get(indiceAleatorio));
			// pokemon.getMovimientos().add(pokemon.getMovimientosPosibles().get((int)(Math.random()
			// * (pokemon.getMovimientosPosibles().size())) + 1));

			// otra forma de tratar que no repita el mismo movimiento que acaba de quitar

			Movimiento elegido;

			do {
				int r = (int) (Math.random() * pokemon.getMovimientosPosibles().size());
				elegido = pokemon.getMovimientosPosibles().get(r);
			} while ((elegido.equals(olvidado)) && (pokemon.getMovimientosPosibles().size() > 1)); // Repite si elige el
																									// mismo que
																									// olvidaste
			// tambien tratamos si los posibles solo tienen un movimiento en la lista
			pokemon.getMovimientos().add(elegido);
			pokemon.getMovimientosPosibles().remove(elegido);

			// hay que completar esto con la info de movimientos, no se como se hace ahora
			System.out.println("Ha olvidad: " + " y ha aprendido: ");
		}
	}

	public double calcularMultiplicadorFinal(Movimiento movElegido, Pokemon defensor) {
		// Empezamos con un multiplicador de 1.0 (daño neutro)
		double multiplicadorTotal = 1.0;

		// Obtenemos el tipo del movimiento que se va a usar
		Tipo tipoAtaque = movElegido.getTipo();

		// Evaluamos el primer tipo del defensor
		if (defensor.getTipo1() != null) {
			multiplicadorTotal *= obtenerEfectividad(tipoAtaque, defensor.getTipo1());
		}

		// Evaluamos el segundo tipo del defensor (si lo tiene)
		if (defensor.getTipo2() != null) {
			multiplicadorTotal *= obtenerEfectividad(tipoAtaque, defensor.getTipo2());
		}

		// Lógica de mensajes por consola
		if (multiplicadorTotal == 0) {
			System.out.println("El Pokémon es inmune al movimiento.");
		} else if (multiplicadorTotal == 0.25) {
			System.out.println("El movimiento es extremadamente poco eficaz (x0.25).");
		} else if (multiplicadorTotal == 0.5) {
			System.out.println("El movimiento es poco eficaz (x0.5).");
		} else if (multiplicadorTotal == 1.0) {
			System.out.println("El movimiento es eficaz (x1).");
		} else if (multiplicadorTotal == 2.0) {
			System.out.println("El movimiento es muy eficaz (x2).");
		} else if (multiplicadorTotal == 4.0) {
			System.out.println("El movimiento es súper eficaz (x4)!");
		} else {
			System.out.println("Resultado inusual: x" + multiplicadorTotal);
		}

		return multiplicadorTotal;
	}

	public void comprobarVentajaPorTipo(Pokemon defensor) {
		boolean tieneVentaja = false;
		boolean tieneDesventaja = false;

		// Guardamos mis tipos en un array temporal para iterar fácilmente sin ArrayList
		// o simplemente hacemos las llamadas directas.
		Tipo[] misTipos = { this.tipo1, this.tipo2 };
		Tipo[] tiposRival = { defensor.getTipo1(), defensor.getTipo2() };

		for (Tipo miTipo : misTipos) {
			if (miTipo == null)
				continue; // Saltamos si el segundo tipo no existe

			for (Tipo tRival : tiposRival) {
				if (tRival == null)
					continue; // Saltamos si el rival solo tiene un tipo

				double efectividad = obtenerEfectividad(miTipo, tRival);

				if (efectividad > 1.0) {
					tieneVentaja = true;
				}
				if (efectividad < 1.0) {
					tieneDesventaja = true;
				}
			}
		}

		// La lógica de impresión se mantiene igual
		if (tieneVentaja && !tieneDesventaja) {
			System.out.println("Tienes una ventaja clara sobre el rival.");
		} else if (tieneVentaja && tieneDesventaja) {
			System.out.println("El combate está reñido. Ambos tenéis tipos efectivos contra el otro.");
		} else if (!tieneVentaja && tieneDesventaja) {
			System.out.println("Estás en desventaja. Tus tipos naturales no son efectivos aquí.");
		} else {
			System.out.println("El combate es neutral por tipos.");
		}
	}

	private double obtenerEfectividad(Tipo ataque, Tipo defensa) {
		// Si el ataque o la defensa son nulos, por seguridad devolvemos daño neutral
		if (ataque == null || defensa == null)
			return 1.0;

		switch (ataque) {
		case AGUA:
			if (defensa == Tipo.FUEGO || defensa == Tipo.TIERRA || defensa == Tipo.ROCA)
				return 2.0;
			if (defensa == Tipo.AGUA || defensa == Tipo.PLANTA || defensa == Tipo.DRAGON)
				return 0.5;
			break;

		case FUEGO:
			if (defensa == Tipo.PLANTA || defensa == Tipo.HIELO || defensa == Tipo.BICHO)
				return 2.0;
			if (defensa == Tipo.FUEGO || defensa == Tipo.AGUA || defensa == Tipo.ROCA || defensa == Tipo.DRAGON)
				return 0.5;
			break;

		case PLANTA:
			if (defensa == Tipo.AGUA || defensa == Tipo.TIERRA || defensa == Tipo.ROCA)
				return 2.0;
			if (defensa == Tipo.FUEGO || defensa == Tipo.PLANTA || defensa == Tipo.VENENO || defensa == Tipo.VOLADOR
					|| defensa == Tipo.BICHO || defensa == Tipo.DRAGON)
				return 0.5;
			break;

		case ELECTRICO:
			if (defensa == Tipo.AGUA || defensa == Tipo.VOLADOR)
				return 2.0;
			if (defensa == Tipo.ELECTRICO || defensa == Tipo.PLANTA || defensa == Tipo.DRAGON)
				return 0.5;
			if (defensa == Tipo.TIERRA)
				return 0.0; // ¡Inmunidad!
			break;

		case HIELO:
			if (defensa == Tipo.PLANTA || defensa == Tipo.TIERRA || defensa == Tipo.VOLADOR || defensa == Tipo.DRAGON)
				return 2.0;
			if (defensa == Tipo.FUEGO || defensa == Tipo.AGUA || defensa == Tipo.HIELO)
				return 0.5;
			break;

		case TIERRA:
			if (defensa == Tipo.FUEGO || defensa == Tipo.ELECTRICO || defensa == Tipo.VENENO || defensa == Tipo.ROCA)
				return 2.0;
			if (defensa == Tipo.PLANTA || defensa == Tipo.BICHO)
				return 0.5;
			if (defensa == Tipo.VOLADOR)
				return 0.0; // ¡Inmunidad!
			break;

		case VOLADOR:
			if (defensa == Tipo.PLANTA || defensa == Tipo.LUCHA || defensa == Tipo.BICHO)
				return 2.0;
			if (defensa == Tipo.ELECTRICO || defensa == Tipo.ROCA)
				return 0.5;
			break;

		case NORMAL:
			if (defensa == Tipo.ROCA)
				return 0.5;
			if (defensa == Tipo.FANTASMA)
				return 0.0; // ¡Inmunidad!
			break;

		case LUCHA:
			if (defensa == Tipo.NORMAL || defensa == Tipo.HIELO || defensa == Tipo.ROCA)
				return 2.0;
			if (defensa == Tipo.VENENO || defensa == Tipo.VOLADOR || defensa == Tipo.PSIQUICO || defensa == Tipo.BICHO)
				return 0.5;
			if (defensa == Tipo.FANTASMA)
				return 0.0; // Inmunidad
			break;

		case VENENO:
			if (defensa == Tipo.PLANTA)
				return 2.0;
			if (defensa == Tipo.VENENO || defensa == Tipo.TIERRA || defensa == Tipo.ROCA || defensa == Tipo.FANTASMA)
				return 0.5;
			break;

		case PSIQUICO:
			if (defensa == Tipo.LUCHA || defensa == Tipo.VENENO)
				return 2.0;
			if (defensa == Tipo.PSIQUICO)
				return 0.5;
			break;

		case BICHO:
			if (defensa == Tipo.PLANTA || defensa == Tipo.PSIQUICO)
				return 2.0;
			if (defensa == Tipo.FUEGO || defensa == Tipo.LUCHA || defensa == Tipo.VENENO || defensa == Tipo.VOLADOR
					|| defensa == Tipo.FANTASMA)
				return 0.5;
			break;

		case ROCA:
			if (defensa == Tipo.FUEGO || defensa == Tipo.HIELO || defensa == Tipo.VOLADOR || defensa == Tipo.BICHO)
				return 2.0;
			if (defensa == Tipo.LUCHA || defensa == Tipo.TIERRA)
				return 0.5;
			break;

		case FANTASMA:
			if (defensa == Tipo.PSIQUICO || defensa == Tipo.FANTASMA)
				return 2.0;
			if (defensa == Tipo.NORMAL)
				return 0.0; // Inmunidad
			break;

		case DRAGON:
			if (defensa == Tipo.DRAGON)
				return 2.0;
			break;

		default:
			return 1.0; // Si el tipo de ataque no tiene reglas especiales, daño neutro
		}

		return 1.0; // Caso por defecto si no entra en ningún 'if' dentro del switch
	}

	public void descansar() {
		// Igualamos la estamina actual a la máxima directamente
		this.estamina = this.estaminaMax;

		// Mostramos un mensaje por consola para que el jugador sepa qué ha pasado
		System.out.println("------------------------------------------");
		System.out.println(this.nombre + " ha descansado profundamente.");
		System.out.println("¡Estamina totalmente recuperada! (" + this.estamina + "/" + this.estaminaMax + ")");
		System.out.println("------------------------------------------");
	}

}

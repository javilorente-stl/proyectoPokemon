package modelo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Random;


/**
 * Gestiona la lógica de un enfrentamiento entre dos Pokémon en los combates y la liga
 * Controla el flujo de turnos, el cálculo de daño, la aplicación de estados, las mejoras, limpiar estados
 * y el registro de eventos en un historial que usaremos en el log
 * @author Javier Lorente Rodríguez
 * @version 2.0
 */
public class Combate {
	private Pokemon pJugador;
	private Pokemon pRival;
	private int turno;
	private boolean esTurnoJugador;
	private LinkedList<String> historialLog;
	private LocalDateTime fechaInicio;
	private int turnosEstadoJugador = 0;
	private int turnosEstadoRival = 0;
	private boolean combateFinalizado = false;

	
	/**
	 * Inicia un nuevo combate determinando quién comienza según la velocidad.
	 * @param pJugador Pokémon controlado por el usuario.
	 * @param pRival Pokémon oponente.
	 */
	public Combate(Pokemon pJugador, Pokemon pRival) {
		this.pJugador = pJugador;
		this.pRival = pRival;
		this.turno = 1;
		this.historialLog = new LinkedList<>();
		this.fechaInicio = LocalDateTime.now();

		// Inicializamos los contadores de turnos de estado en 0
		this.turnosEstadoJugador = 0;
		this.turnosEstadoRival = 0;

		// He tenido que cambiar esto porque ponía a los pokemon en vida con 0 de vitalidad, esta mal
		// Aseguramos que los Pokémon inicien en estado VIVO al comenzar la pelea
		// this.pJugador.setEstado(Estado.VIVO);
		// this.pRival.setEstado(Estado.VIVO);

		// Decidimos quién empieza por velocidad
		this.esTurnoJugador = pJugador.getVelocidad() >= pRival.getVelocidad();

		// Primer registro del log
		registrarSuceso("Inicio del combate: " + pJugador.getNombre() + " vs " + pRival.getNombre());
		registrarSuceso("El primer turno es para: " + (esTurnoJugador ? pJugador.getNombre() : pRival.getNombre()));
	}

	
	// Metodos Getter and Setter
	public Pokemon getpJugador() {
		return pJugador;
	}

	public void setpJugador(Pokemon pJugador) {
		this.pJugador = pJugador;
	}

	public Pokemon getpRival() {
		return pRival;
	}

	public void setpRival(Pokemon pRival) {
		this.pRival = pRival;
	}

	public int getTurno() {
		return turno;
	}

	public void setTurno(int turno) {
		this.turno = turno;
	}

	public boolean isEsTurnoJugador() {
		return esTurnoJugador;
	}

	public void setEsTurnoJugador(boolean esTurnoJugador) {
		this.esTurnoJugador = esTurnoJugador;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public int getTurnosEstadoJugador() {
		return turnosEstadoJugador;
	}

	public void setTurnosEstadoJugador(int turnosEstadoJugador) {
		this.turnosEstadoJugador = turnosEstadoJugador;
	}

	public int getTurnosEstadoRival() {
		return turnosEstadoRival;
	}

	public void setTurnosEstadoRival(int turnosEstadoRival) {
		this.turnosEstadoRival = turnosEstadoRival;
	}

	public boolean isCombateFinalizado() {
		return combateFinalizado;
	}

	public void setCombateFinalizado(boolean combateFinalizado) {
		this.combateFinalizado = combateFinalizado;
	}

	public void setHistorialLog(LinkedList<String> historialLog) {
		this.historialLog = historialLog;
	}

	
	/**
	 * Añade un evento al log del combate con el número de turno actual.
	 * Usamos este metodo para centralizar la escritura
	 * @param frase Descripción del suceso ocurrido.
	 */
	public void registrarSuceso(String frase) {
		String registro = "[Turno " + turno + "] " + frase;
		historialLog.add(registro);
		// Imprimir por consola mientras programas para debuguear y ver que pasa
		System.out.println(registro);
	}

	/**
	 * cálculo de daño y verificación de debilitación, este metodo es de prueba antes del real
	 * @param mov Movimiento seleccionado.
	 * @param atacante Pokémon que ejecuta la acción.
	 * @param defensor Pokémon que recibe el impacto.
	 */
	public void procesarAtaque(Movimiento mov, Pokemon atacante, Pokemon defensor) {
		registrarSuceso(atacante.getNombre() + " usa " + mov.getNombre());

		// ... lógica de daño ...
		int danio = 20; // Supongamos
		defensor.setVitalidad(defensor.getVitalidad() - danio);

		registrarSuceso(defensor.getNombre() + " pierde " + danio + " puntos de vida.");

		if (defensor.getVitalidad() <= 0) {
			// para que no repita textos esto esta comentado
			// registrarSuceso(defensor.getNombre() + " se ha debilitado.");

		}
	}

	// Getter para que el Controller o un "LogWriter" pueda leer la lista al final
	public LinkedList<String> getHistorialLog() {
		return historialLog;
	}
	
	
	/**
	 * Ejecuta la acción de ataque completa, gestionando precisión, tipo de movimiento,
	 * cálculo de daño y verificación de debilitación.
	 * Este es el metodo que se ha usado en todo el combate real
	 * @param mov Movimiento seleccionado.
	 * @param atacante Pokémon que ejecuta la acción.
	 * @param defensor Pokémon que recibe el impacto.
	 */
	public void ejecutarTurno(Movimiento mov, Pokemon atacante, Pokemon defensor) {
		registrarSuceso("--- Turno " + turno + ": " + atacante.getNombre() + " ---");

		if (!puedeAtacar(atacante))
			return;

		// Chequeo de Precisión
		if (mov.getPrecision() > 0) {
			int precisionFinal = (atacante.getPrecision() * mov.getPrecision()) / 100;

			Random rand = new Random();
			int dado = rand.nextInt(100) + 1; // Genera 1 a 100

			if (dado > precisionFinal) {
				registrarSuceso("¡" + atacante.getNombre() + " ha fallado!");
				// Gastamos PP igualmente aunque falle
				mov.setNumPP(Math.max(0, mov.getNumPP() - 1));
				return;
			}
		} else {
			// Debug opcional para confirmar que el ataque es infalible
			System.out.println(">>> [INFO] " + mov.getNombre() + " es un movimiento infalible (Precisión 0).");
		}

		// Ejecución por Clase de Movimiento
		int clase = mov.getClaseMov();

		if (clase == 1) { // ATAQUE CON DAÑO
			if (mov.getMejora() == null || mov.getMejora().isEmpty()) {
				// --- DAÑO DIRECTO ---
				int danio = calcularDanio(mov, atacante, defensor, 1.0);
				defensor.setVitalidad(Math.max(0, defensor.getVitalidad() - danio));
				registrarSuceso(atacante.getNombre() + " causa " + danio + " de daño.");

				// LLAMADA NUEVA: Intentar aplicar estado tras el daño directo
				aplicarEstadoAtaque(mov, defensor);
			} else {
				// --- DAÑO CON MEJORAS (Repetir, Drenado, etc.) ---
				mejorasAtaque(mov, atacante, defensor);

				aplicarEstadoAtaque(mov, defensor);
			}
		} else if (clase == 2) { // ESTADO RIVAL (100% Probabilidad)
			aplicarEstadoClase2(mov, defensor);
			aplicarDebuffs(mov, defensor);
		} else if (clase == 3) { // BUFFS PROPIOS
			aplicarBuffs(mov, atacante);
		}

		// Descuento de recursos
		mov.setNumPP(Math.max(0, mov.getNumPP() - 1));

		// Verificación de derrota
		if (defensor.getVitalidad() <= 0) {
			defensor.setVitalidad(0);
			defensor.setEstado(Estado.DEBILITADO);
			registrarSuceso("¡" + defensor.getNombre() + " se ha debilitado!");
		}

		if (defensor.getVitalidad() <= 0) {
			this.combateFinalizado = true; // ¡Importante marcar que terminó!
		}

	}

	/**
	 * Verifica si el Pokémon está en condiciones de realizar un movimiento
	 * basándose en su estado actual (Parálisis, Sueño, Confusión, etc.).
	 * @param p Pokémon a evaluar.
	 * @return true si puede realizar la acción, false si el estado se lo impide.
	 */
	public boolean puedeAtacar(Pokemon p) {
		// Al ser Enum, usamos el operador == o el switch directo
		Estado estadoActual = p.getEstado();

		// Si está debilitado, nunca puede atacar, aunque no debería ni estar en el combate, pero por si acaso
		if (estadoActual == Estado.DEBILITADO) {
			return false;
		}

		switch (estadoActual) {
		case PARALIZADO:
			if (Math.random() < 0.25) {
				registrarSuceso(p.getNombre() + " está paralizado. ¡No puede moverse!");
				return false;
			}
			break;

		case DORMIDO:
			registrarSuceso(p.getNombre() + " está profundamente dormido...");
			// probabilidad de despertar como en el juego real
			if (Math.random() < 0.33) {
				p.setEstado(Estado.VIVO);
				registrarSuceso("¡" + p.getNombre() + " se ha despertado!");
				return true;
			}
			return false;

		case CONGELADO:
		case HELADO:
			registrarSuceso(p.getNombre() + " está congelado. ¡No puede atacar!");
			return false;

		case CONFUSO:
			registrarSuceso(p.getNombre() + " está confuso...");
			if (Math.random() < 0.33) {
				int danioAuto = (int) (p.getAtaque() * 0.1); // Se hiere un poco si se cumple la condición
				p.setVitalidad(p.getVitalidad() - danioAuto);
				registrarSuceso("¡Tan confuso está que se ha herido a sí mismo!");
				return false;
			}
			break;

		case RETROCEDIDO:
			registrarSuceso(p.getNombre() + " ha retrocedido.");
			p.setEstado(Estado.VIVO); // El retroceso se limpia tras el chequeo
			return false;

		case ENAMORADO:
			if (Math.random() < 0.50) {
				registrarSuceso(p.getNombre() + " está enamorado de su rival y no puede atacar.");
				return false;
			}
			break;

		default:

			break;
		}
		return true;
	}

	/**
	 * Aplica el daño residual al final del turno causado por estados persistentes
	 * como Quemaduras, Veneno o Drenadoras. También gestiona la curación derivada de las drenadoras
	 * @param p Pokémon que recibe el efecto final.
	 */
	public void aplicarEfectosFinales(Pokemon p) {
		if (p.getVitalidad() <= 0 || p.getEstado() == Estado.VIVO)
			return;

		Estado estado = p.getEstado();

		boolean esJugador = (p == this.pJugador);
		int turnosRestantes = esJugador ? this.turnosEstadoJugador : this.turnosEstadoRival;

		// Si el estado es temporal, restamos un turno
		if (estado == Estado.ATRAPADO || estado == Estado.CONFUSO || estado == Estado.DRENADORAS) {
			turnosRestantes--;

			// Actualizamos el contador en la clase Combate
			if (esJugador)
				this.turnosEstadoJugador = turnosRestantes;
			else
				this.turnosEstadoRival = turnosRestantes;

			// Si se acaban los turnos, el Pokémon se libera
			if (turnosRestantes <= 0) {
				p.setEstado(Estado.VIVO);
				registrarSuceso(p.getNombre() + " se ha liberado de su estado.");
				return; // Salimos para no aplicar daño residual este turno si ya se liberó
			}
		}
		int danioResidual = 0;
		// con los datos reales del juego
		switch (estado) {
		case QUEMADO:
			danioResidual = (int) (p.getVitalidadMax() * 0.06);
			registrarSuceso(p.getNombre() + " sufre por la quemadura.");
			break;

		case ENVENENADO:
			danioResidual = (int) (p.getVitalidadMax() * 0.12);
			registrarSuceso(p.getNombre() + " sufre por el veneno.");
			break;

		case GRAVEMENTE_ENVENENADO:
			danioResidual = (int) (p.getVitalidadMax() * 0.18);
			registrarSuceso(p.getNombre() + " sufre mucho por el veneno grave.");
			break;
			// este ni lo uso porque ningún movimiento lo puede aplicar
		case MALDITO:
			danioResidual = (int) (p.getVitalidadMax() * 0.25);
			registrarSuceso("La maldición consume a " + p.getNombre());
			break;

		case DRENADORAS:
			danioResidual = (int) (p.getVitalidadMax() * 0.12);
			registrarSuceso("Las drenadoras absorben energía de " + p.getNombre());

			// Con esto ponemos la curación en el pokemon que lanzó la habilidad
			Pokemon beneficiario = (p == this.pJugador) ? this.pRival : this.pJugador;

			if (beneficiario != null && beneficiario.getVitalidad() > 0) {
				int saludACurar = danioResidual;
				int vidaAntes = beneficiario.getVitalidad();

				// Curamos sin sobrepasar el máximo por si acaso
				beneficiario.setVitalidad(
						Math.min(beneficiario.getVitalidadMax(), beneficiario.getVitalidad() + saludACurar));

				int curadoReal = beneficiario.getVitalidad() - vidaAntes;
				if (curadoReal > 0) {
					registrarSuceso("¡" + beneficiario.getNombre() + " recupera salud!");
				}
			}

		case ATRAPADO:
			danioResidual = (int) (p.getVitalidadMax() * 0.06);
			registrarSuceso(p.getNombre() + " sufre por estar atrapado.");
			break;

		default:
			break;
		}

		// Aseguramos que si hay daño, al menos sea 1 PS
		if (danioResidual <= 0 && estado != Estado.VIVO) {
			// Solo si el estado es uno de los que hace daño
			if (estado == Estado.QUEMADO || estado == Estado.ENVENENADO || estado == Estado.MALDITO
					|| estado == Estado.DRENADORAS) {
				danioResidual = 1;
			}
		}
		// Aplicamos el daño
		if (danioResidual > 0) {
			p.setVitalidad(Math.max(0, p.getVitalidad() - danioResidual));
			// Actualizamos estado por si muere por el efecto
			if (p.getVitalidad() <= 0) {
				p.setVitalidad(0);
				p.setEstado(Estado.DEBILITADO);
				registrarSuceso("¡" + p.getNombre() + " ha sucumbido a su estado!");
			}
		}
	}

	
	/**
	 * Con este metodo aplicamos las mejoras de los movimientos del tipo 1 que son de ataque
	 * cosas como que se repita el movimiento en el mismo turno un numero aleatorio de veces, o 
	 * que aplique daño fijo según nivel...
	 * @param mov
	 * @param atacante
	 * @param defensor
	 */
	private void mejorasAtaque(Movimiento mov, Pokemon atacante, Pokemon defensor) {
		String mejora = mov.getMejora();
		Random rand = new Random();
		int repeticiones = 1;
		int danioTotalTurno = 0;

		// DETERMINAR REPETICIONES
		if ("Repetir2".equalsIgnoreCase(mejora)) {
			repeticiones = 2;
		} else if ("Repetir".equalsIgnoreCase(mejora)) {
			repeticiones = rand.nextInt(4) + 2; // De 2 a 5 veces
		}

		// BUCLE DE IMPACTOS
		for (int i = 1; i <= repeticiones; i++) {
			int danioImpacto = 0;

			// Caso Mejora: FIJO
			if ("Fijo".equalsIgnoreCase(mejora)) {
				danioImpacto = mov.getPotencia();
			} else {
				// Cálculo de Crítico
				double multCritico = ("Critico".equalsIgnoreCase(mejora) && rand.nextDouble() < 0.25) ? 1.5 : 1.0;
				if (multCritico > 1.0)
					registrarSuceso("¡Un golpe crítico!");

				// LLAMADA AL NUEVO MÉTODO EXTERNO
				// Este método ya gestiona internamente Tipos (Gen 1), Efectividad y Stats
				danioImpacto = calcularDanio(mov, atacante, defensor, multCritico);
			}

			// Aplicar daño a la vitalidad
			defensor.setVitalidad(Math.max(0, defensor.getVitalidad() - danioImpacto));
			danioTotalTurno += danioImpacto;

			registrarSuceso(atacante.getNombre() + " causa " + danioImpacto + " de daño.");

			// Si el defensor cae, se detienen las repeticiones inmediatamente
			if (defensor.getVitalidad() <= 0)
				break;
		}

		if (repeticiones > 1) {
			registrarSuceso("¡Ha golpeado " + repeticiones + " veces!");
		}

		// EFECTOS SECUNDARIOS (Post-daño acumulado)
		if (mejora != null) {
			switch (mejora.toLowerCase()) {
			case "drenado":
				int cura = danioTotalTurno / 2;
				atacante.setVitalidad(Math.min(atacante.getVitalidadMax(), atacante.getVitalidad() + cura));
				registrarSuceso(atacante.getNombre() + " recupera " + cura + " PS por drenado.");
				break;

			case "retroceso":
				int recule = danioTotalTurno / 4;
				atacante.setVitalidad(Math.max(0, atacante.getVitalidad() - recule));
				registrarSuceso(atacante.getNombre() + " sufre " + recule + " de daño por retroceso.");
				if (atacante.getVitalidad() <= 0) {
					atacante.setEstado(Estado.DEBILITADO);
					registrarSuceso(atacante.getNombre() + " ha caído por el daño de retroceso.");
				}
				break;

			case "defensa-1":
				if (rand.nextDouble() < 0.30) {
					defensor.setDefensa(Math.max(1, defensor.getDefensa() - 5));
					registrarSuceso("¡La defensa de " + defensor.getNombre() + " ha bajado!");
				}
				break;

			case "ataque-1":
				if (rand.nextDouble() < 0.30) {
					defensor.setAtaque(Math.max(1, defensor.getAtaque() - 5));
					registrarSuceso("¡El ataque de " + defensor.getNombre() + " ha bajado!");
				}
				break;
			}
		}
	}

	/**
	 * Este aplica los estados de los ataques que puedan provocarlos, con un 15%
	 * de probabilidad fija porque así lo he decidido
	 * @param mov de ataque
	 * @param defensor o quien lo recibe
	 */
	private void aplicarEstadoAtaque(Movimiento mov, Pokemon defensor) {
		Random rand = new Random();

		// Probabilidad fija del 15% para aplicar el estado
		if (rand.nextDouble() < 0.15) {

			// Obtenemos el estado que intenta aplicar el movimiento
			Estado estadoAAplicar = mov.getEstado();

			// Si el movimiento no tiene estado asociado o el rival ya está debilitado,
			// salimos
			if (estadoAAplicar == null || defensor.getVitalidad() <= 0) {
				return;
			}

			// Lógica específica para el estado ATRAPADO
			if (estadoAAplicar == Estado.ATRAPADO) {
				int duracion = Math.min(mov.getNumTurnos(), 5);
				if (duracion <= 0)
					duracion = 2;

				// ASIGNAR LA DURACIÓN A LA CLASE COMBATE
				if (defensor == this.pJugador)
					this.turnosEstadoJugador = duracion;
				else
					this.turnosEstadoRival = duracion;

				defensor.setEstado(Estado.ATRAPADO);
				registrarSuceso(defensor.getNombre() + " ha sido atrapado durante " + duracion + " turnos.");
			}

			// Lógica para el resto de estados persistentes
			else {
				// Solo aplicamos el estado si el Pokémon está VIVO (no sobreescribimos otros estados)
				if (defensor.getEstado() == Estado.VIVO) {
					defensor.setEstado(estadoAAplicar);
					registrarSuceso(
							defensor.getNombre() + " ahora está " + estadoAAplicar.toString().toLowerCase() + ".");
				} else {
					registrarSuceso("El ataque no ha podido cambiar el estado de " + defensor.getNombre());
				}
			}

		} else {
			// El 85% de las veces el estado no se aplica, pero el log no necesita registrar
			// el fallo para no llenar el documento de mensajes irrelevantes.
		}
	}

	/**
	 * Para los movimientos de clase 2, que aplican cambios de estadisticas
	 * y que estas estadísticas solo persistan durante el combate, no se actualizan en la base de datos
	 * @param mov que lo aplica
	 * @param defensor que recibe el cambio de estadísticas
	 */
	private void aplicarDebuffs(Movimiento mov, Pokemon defensor) {
		String mejora = mov.getMejora();

		// Si no hay mejora definida en el movimiento, salimos
		if (mejora == null || mejora.isEmpty()) {
			return;
		}

		switch (mejora.toLowerCase()) {
		case "ataque-1":
			// Reducimos el ataque un 10% del valor base o un valor fijo (ej: 5)
			defensor.setAtaque(Math.max(1, defensor.getAtaque() - 5));
			registrarSuceso("¡El ataque de " + defensor.getNombre() + " ha bajado!");
			break;

		case "precision-1":
			// Como la precisión inicial es 100, bajamos 10 puntos
			defensor.setPrecision(Math.max(10, defensor.getPrecision() - 10));
			registrarSuceso("¡La precisión de " + defensor.getNombre() + " ha disminuido!");
			break;

		case "defensa-2":
			// Reducción fuerte de defensa (el doble que una mejora normal)
			defensor.setDefensa(Math.max(1, defensor.getDefensa() - 10));
			registrarSuceso("¡La defensa de " + defensor.getNombre() + " ha bajado drásticamente!");
			break;

		case "velocidad-1":
			defensor.setVelocidad(Math.max(1, defensor.getVelocidad() - 5));
			registrarSuceso("¡La velocidad de " + defensor.getNombre() + " ha bajado!");
			break;

		default:

			break;
		}
	}

	/**
	 * Igual que tenemos el metodo de los movimientos de ataque que aplican estado
	 * pues este es para los movimientos de tipo 2, con la misma probabilidad
	 * ademas se tiene en cuenta la duración de los estados durante los turnos
	 * @param mov que lo aplica
	 * @param defensor que lo recibe
	 */
	private void aplicarEstadoClase2(Movimiento mov, Pokemon defensor) {
		Estado estadoAAplicar = mov.getEstado();

		// Validaciones básicas
		if (estadoAAplicar == null || defensor.getVitalidad() <= 0)
			return;

		if (defensor.getEstado() != Estado.VIVO) {
			registrarSuceso("¡Pero " + defensor.getNombre() + " ya sufre un estado!");
			return;
		}

		int duracion = 0;
		// Definir duraciones específicas
		if (estadoAAplicar == Estado.CONFUSO || estadoAAplicar == Estado.DRENADORAS) {
			duracion = 3;
		} else if (estadoAAplicar == Estado.ATRAPADO) {
			// Aseguramos un mínimo de 2 turnos si la BD devuelve 0
			duracion = Math.max(2, Math.min(mov.getNumTurnos(), 5));
		}

		// Aplicación de estados temporales (con contador)
		if (duracion > 0) {
			if (defensor == this.pJugador) {
				this.turnosEstadoJugador = duracion;
			} else {
				this.turnosEstadoRival = duracion;
			}
			defensor.setEstado(estadoAAplicar);
			registrarSuceso(defensor.getNombre() + " sufre " + estadoAAplicar.name().toLowerCase() + " por " + duracion
					+ " turnos.");
		}
		// Aplicación de estados persistentes (sin contador)
		else {
			// Aquí entran Veneno, Parálisis, Quemado...
			defensor.setEstado(estadoAAplicar);
			registrarSuceso(defensor.getNombre() + " ahora está " + estadoAAplicar.name().toLowerCase() + ".");
		}
	}

	/**
	 * Este es el último de los movimientos, que son de tipo 3
	 * aplica las mejoras en el pokemon que lo usa
	 * @param mov que aplica
	 * @param atacante o el que recibe la mejora
	 */
	private void aplicarBuffs(Movimiento mov, Pokemon atacante) {
		String mejora = mov.getMejora();

		if (mejora == null || mejora.isEmpty()) {
			return;
		}

		switch (mejora.toLowerCase()) {
		case "ataque+1":
			atacante.setAtaque(atacante.getAtaque() + 5);
			registrarSuceso("¡El ataque de " + atacante.getNombre() + " ha subido!");
			break;

		case "ataque+2":
			atacante.setAtaque(atacante.getAtaque() + 10);
			registrarSuceso("¡El ataque de " + atacante.getNombre() + " ha subido drásticamente!");
			break;

		case "defensa+1":
			atacante.setDefensa(atacante.getDefensa() + 5);
			registrarSuceso("¡La defensa de " + atacante.getNombre() + " ha subido!");
			break;

		case "defensa+2":
			atacante.setDefensa(atacante.getDefensa() + 10);
			registrarSuceso("¡La defensa de " + atacante.getNombre() + " ha subido drásticamente!");
			break;

		case "especial+1":
			atacante.setAtaqueEspecial(atacante.getAtaqueEspecial() + 5);
			registrarSuceso("¡El ataque especial de " + atacante.getNombre() + " ha subido!");
			break;

		case "curacion":
			// Cura el 50% de la vitalidad máxima
			int cantidadCura = atacante.getVitalidadMax() / 2;
			int vidaAnterior = atacante.getVitalidad();

			// Sumamos la cura sin sobrepasar el máximo porque sino se pasa
			atacante.setVitalidad(Math.min(atacante.getVitalidadMax(), atacante.getVitalidad() + cantidadCura));

			int curadoReal = atacante.getVitalidad() - vidaAnterior;
			registrarSuceso(atacante.getNombre() + " se ha curado " + curadoReal + " PS.");
			break;

		default:
			registrarSuceso("El movimiento de " + atacante.getNombre() + " no tuvo efecto.");
			break;
		}
	}

	/**
	 * Método usado para actualizar los contadores de los estados y limpiar el estado
	 */
	public void actualizarContadores() {
		// Gestionar turnos del Jugador
		if (turnosEstadoJugador > 0) {
			turnosEstadoJugador--;
			if (turnosEstadoJugador == 0) {
				registrarSuceso("¡" + pJugador.getNombre() + " se ha recuperado de su estado!");
				pJugador.setEstado(Estado.VIVO);
			}
		}

		// Gestionar turnos del Rival
		if (turnosEstadoRival > 0) {
			turnosEstadoRival--;
			if (turnosEstadoRival == 0) {
				registrarSuceso("¡" + pRival.getNombre() + " se ha recuperado de su estado!");
				pRival.setEstado(Estado.VIVO);
			}
		}
	}

	
	/**
	 * Determina si un tipo elemental debe atacar usando la estadística de 
	 * Ataque (Físico) o Ataque Especial.
	 * @param tipo El tipo elemental del movimiento.
	 * @return "ESPECIAL" para tipos de la lista, "FISICO" para el resto.
	 */
	public String determinarClasePorTipo(Tipo tipo) {
		switch (tipo) {
		case FUEGO:
		case AGUA:
		case PLANTA:
		case ELECTRICO:
		case PSIQUICO:
		case HIELO:
		case DRAGON:
			return "ESPECIAL";
		default:
			// Todos los demás (Normal, Lucha, Veneno, Tierra, etc.)
			return "FISICO";
		}
	}

	/**
	 * Calcula el daño final basándose en la fórmula del juego real
	 * Daño = (((2*Nivel/5 + 2) * Potencia * (Atk/Def) / 50) + 2) * Multiplicadores
	 * @param m Movimiento ejecutado
	 * @param atacante Pokémon que ataca
	 * @param defensor Pokémon que defiende
	 * @param multCritico Multiplicador de golpe crítico (1.5 o 1.0)
	 * @return Valor entero del daño causado
	 */
	public int calcularDanio(Movimiento m, Pokemon atacante, Pokemon defensor, double multCritico) {
		int atkUso;
		int defUso;

		// Clasificación por Tipo (Físico/Especial)
		String clase = determinarClasePorTipo(m.getTipo());
		if ("ESPECIAL".equals(clase)) {
			atkUso = atacante.getAtaqueEspecial();
			defUso = defensor.getDefensaEspecial();
		} else {
			atkUso = atacante.getAtaque();
			defUso = defensor.getDefensa();
		}

		// Cálculo de Efectividad con los métodos de pokemon

		double efec = atacante.calcularMultiplicadorFinal(m, defensor);

		// FÓRMULA DE DAÑO
		double parteNivel = ((2.0 * atacante.getNivel()) / 5.0) + 2.0;
		double baseDanio = (parteNivel * m.getPotencia() * ((double) atkUso / defUso)) / 50.0;

		double variacion = 0.85 + (Math.random() * 0.15);

		// Cálculo inicial
		int danioFinal = (int) ((baseDanio + 2) * efec * multCritico * variacion);

		if (efec == 0) {
			danioFinal = 0;
		} else {
			danioFinal = Math.max(1, danioFinal);
		}

		// Debug
		System.out.println(">>> [CALC] " + m.getNombre() + " | Efec: x" + efec + " | Daño Final: " + danioFinal);

		return danioFinal;
	}

	/**
	 * Este método devuelve el pokemon que ha ganado
	 * @return Pokémon que ha ganado el combate
	 */
	public Pokemon getGanador() {
		if (pJugador.getVitalidad() > 0 && pRival.getVitalidad() <= 0)
			return pJugador;
		if (pRival.getVitalidad() > 0 && pJugador.getVitalidad() <= 0)
			return pRival;
		return null; // Empate o combate no terminado
	}

	/**
	 * Genera un reporte textual completo de todo el enfrentamiento, compila la fecha de inicio y 
	 * cada uno de los sucesos registrados en el historial (turnos, daños, estados y resultados).
	 * @return Un String con el registro cronológico del combate.
	 */
	public String getTextoLogCompleto() {
		StringBuilder sb = new StringBuilder();
		sb.append("LOG DE COMBATE - ").append(fechaInicio).append("\n");
		// Guarda múltiples líneas
		for (String linea : historialLog) {
			sb.append(linea).append("\n");
		}
		return sb.toString();
	}
}
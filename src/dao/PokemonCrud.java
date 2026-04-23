package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import modelo.Entrenador;
import modelo.Estado;
import modelo.Movimiento;
import modelo.Pokemon;
import modelo.Sexo;
import modelo.Tipo;

public class PokemonCrud {

	
	
	/**
	 * Este metodo guarda un pokemon, se usa en la vista de la captura
	 * genera el insert dentro del entrenador que esta realizando la captura
	 * Se le dan los datos aleatorios adecuados y se tratan valores como el sexo
	 * También se le hace una llamada al método para asignarle un movimiento
	 * @param con
	 * @param numPokedex
	 * @param ent
	 * @param mote
	 * @throws SQLException
	 */
	public static void guardarPokemon(Connection con, int numPokedex, Entrenador ent, String mote) throws SQLException {
	    
	    String sqlInsert = "INSERT INTO POKEMON ("
	            + "    ID_POKEMON, "
	            + "    NUM_POKEDEX, "
	            + "    ID_ENTRENADOR, "
	            + "    MOTE, "
	            + "    VITALIDAD, "
	            + "    VITALIDAD_MAX, " 
	            + "    ATAQUE, "
	            + "    ATA_ESPECIAL, "
	            + "    DEFENSA, "
	            + "    DEF_ESPECIAL, "
	            + "    VELOCIDAD, "
	            + "    NIVEL, "
	            + "    EXPERIENCIA, "
	            + "    FERTILIDAD, "
	            + "    SEXO, "
	            + "    ESTADO, "
	            + "    CAJA, "
	            + "    ID_OBJETO"
	            + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	    String sqlSelect = "SELECT NOM_POKEMON, TIPO1, TIPO2 FROM POKEDEX WHERE NUM_POKEDEX=?";

	 // Obtener el nuevo ID_POKEMON
	    int nuevoId = 1;
	    try (PreparedStatement psMax = con.prepareStatement("SELECT MAX(ID_POKEMON) FROM POKEMON");
	         ResultSet rsMax = psMax.executeQuery()) {
	        if (rsMax.next()) {
	            nuevoId = rsMax.getInt(1) + 1;
	        }
	    }

	    
	    // Obtenemos todas las posiciones ocupadas por este entrenador, ordenadas
	    int proximaPosicion = 1;
	    String sqlHuecos = "SELECT CAJA FROM POKEMON WHERE ID_ENTRENADOR = ? ORDER BY CAJA ASC";
	    try (PreparedStatement psHuecos = con.prepareStatement(sqlHuecos)) {
	        psHuecos.setInt(1, ent.getIdEntrenador());
	        try (ResultSet rsHuecos = psHuecos.executeQuery()) {
	            // Lógica de relleno:
	            // Si tenemos ocupados 1, 2, 4... el bucle detectará que falta el 3.
	            while (rsHuecos.next()) {
	                int ocupado = rsHuecos.getInt("CAJA");
	                if (ocupado == proximaPosicion) {
	                    proximaPosicion++; // Si el hueco está ocupado, probamos el siguiente
	                } else if (ocupado > proximaPosicion) {
	                    break; 
	                }
	            }
	        }
	    }

	    //Obtener datos de la Pokedex e insertar
	    try (PreparedStatement psSel = con.prepareStatement(sqlSelect)) {
	        psSel.setInt(1, numPokedex);
	        try (ResultSet rs = psSel.executeQuery()) {
	            if (rs.next()) {
	            	int vitalidadAleatoria = (int) (Math.random() * 6) + 16;
	            	Sexo sexoAleatorio = (Math.random() < 0.5) ? Sexo.M : Sexo.H;
	            	if (numPokedex >= 29 && numPokedex <= 31) { // Nidoran♀
	            	    sexoAleatorio = Sexo.H;
	            	} else if (numPokedex >= 32 && numPokedex <= 34) { // Nidoran♂
	            	    sexoAleatorio = Sexo.M;
	            	} else if (numPokedex == 81 || numPokedex == 82 || // Magnemite, Magneton
	            	           numPokedex == 100 || numPokedex == 101 || // Voltorb, Electrode
	            	           numPokedex == 120 || numPokedex == 121 || // Staryu, Starmie
	            	           numPokedex == 132 || // Ditto
	            	           numPokedex == 137 || // Porygon
	            	           numPokedex == 144 || numPokedex == 145 || numPokedex == 146 || // Aves Legendarias
	            	           numPokedex == 150 || numPokedex == 151) { // Mewtwo, Mew
	            	    sexoAleatorio = Sexo.X;
	            	}
	            	Estado estadoInicial = Estado.VIVO;
	            	
	                
	                try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
	                    psIns.setInt(1, nuevoId);
	                    psIns.setInt(2, numPokedex);
	                    psIns.setInt(3, ent.getIdEntrenador());
	                    psIns.setString(4, mote);
	                    psIns.setInt(5, vitalidadAleatoria);
	                    psIns.setInt(6, vitalidadAleatoria);
	                    psIns.setInt(7, (int) (Math.random() * 5) + 1);
	                    psIns.setInt(8, (int) (Math.random() * 5) + 1);
	                    psIns.setInt(9, (int) (Math.random() * 5) + 1);
	                    psIns.setInt(10, (int) (Math.random() * 5) + 1);
	                    psIns.setInt(11, (int) (Math.random() * 5) + 1);
	                    psIns.setInt(12, 1);
	                    psIns.setInt(13, 0);
	                    psIns.setInt(14, 5);
	                    psIns.setString(15, sexoAleatorio.name()); 
	                    psIns.setString(16, estadoInicial.name());
	                    psIns.setInt(17, proximaPosicion); // El hueco encontrado
	                    psIns.setNull(18, java.sql.Types.INTEGER);

	                    psIns.executeUpdate();
	                    
	                 // Obtenemos el tipo principal directamente del ResultSet de la Pokedex
	                    Tipo tipoPrincipal = Tipo.convertirTpoDesdeString(rs.getString("TIPO1").toUpperCase());

	                    // Llamamos al método que busca el ataque más débil (Potencia > 0) de ese tipo
	                    Movimiento inicial = MovimientoCrud.obtenerMovimientoInicial(con, tipoPrincipal);

	                    // Verificamos que se haya encontrado algo antes de insertar
	                    if (inicial != null) {
	                        // Depuración: Verifica en consola que el ID ya no sea 0
	                        System.out.println("Asignando Movimiento: " + inicial.getNombre() + " (ID: " + inicial.getIdMovimiento() + ") al Pokemon ID: " + nuevoId);

	                        String sqlInsMov = "INSERT INTO POKEMON_MOVIMIENTO (ID_POKEMON, ID_MOVIMIENTO, NUM_PP, ACTIVO) VALUES (?, ?, ?, ?)";
	                        
	                        try (PreparedStatement psMov = con.prepareStatement(sqlInsMov)) {
	                            psMov.setInt(1, nuevoId);
	                            psMov.setInt(2, inicial.getIdMovimiento()); 
	                            psMov.setInt(3, inicial.getNumPP());        // PP oficiales de la tabla MOVIMIENTO
	                            psMov.setInt(4, 1);                         // 1 = Movimiento activo
	                            
	                            psMov.executeUpdate();
	                        }
	                    } else {
	                        System.out.println("Aviso: No se encontró ningún movimiento de ataque para el tipo " + tipoPrincipal);
	                    }
	                    
	                }
	            }
	        }
	    }
	}
	
	/**
	 * Este metodo es para obtener los pokemon que están en el equipo principal
	 * todos sus datos
	 * @param conexion
	 * @param e
	 * @throws SQLException
	 */
	public static void obtenerPokemon1(Connection conexion, Entrenador e) throws SQLException {
		String sql = "SELECT P.ID_POKEMON, PX.NUM_POKEDEX, PX.NOM_POKEMON, P.MOTE, P.VITALIDAD, "
	            + "P.VITALIDAD_MAX, "
	            + "P.ATAQUE, P.ATA_ESPECIAL, P.DEFENSA, P.DEF_ESPECIAL, P.VELOCIDAD, "
	            + "P.NIVEL, P.FERTILIDAD, P.SEXO, P.ESTADO, P.CAJA, P.ID_OBJETO, "
	            + "PX.TIPO1, PX.TIPO2 "
	            + "FROM POKEMON P "
	            + "INNER JOIN POKEDEX PX ON PX.NUM_POKEDEX = P.NUM_POKEDEX "
	            + "WHERE P.ID_ENTRENADOR = ? AND P.CAJA BETWEEN 1 AND 6 "
	            + "ORDER BY P.CAJA ASC";

	    PreparedStatement ps = conexion.prepareStatement(sql);
	    ps.setInt(1, e.getIdEntrenador()); 
	    
	    
	    ResultSet rs = ps.executeQuery();
	    LinkedList<Pokemon> listadoPokemon = new LinkedList<Pokemon>();

	    while (rs.next()) {
	        Pokemon p = new Pokemon();
	        int idPkmn = rs.getInt("ID_POKEMON");

	        // Seteo de datos básicos
	        p.setId_pokemon(idPkmn);
	        p.setNombre(rs.getString("NOM_POKEMON"));
	        p.setNum_pokedex(rs.getInt("NUM_POKEDEX"));
	        p.setMote(rs.getString("MOTE"));
	        p.setVitalidad(rs.getInt("VITALIDAD"));
	        p.setVitalidadMax(rs.getInt("VITALIDAD_MAX"));
	        p.setAtaque(rs.getInt("ATAQUE"));
	        p.setAtaqueEspecial(rs.getInt("ATA_ESPECIAL"));
	        p.setDefensa(rs.getInt("DEFENSA"));
	        p.setDefensaEspecial(rs.getInt("DEF_ESPECIAL"));
	        p.setVelocidad(rs.getInt("VELOCIDAD"));
	        p.setNivel(rs.getInt("NIVEL"));
	        p.setFertilidad(rs.getInt("FERTILIDAD"));
	        if (rs.getString("TIPO1") != null) {
	            p.setTipo1(Tipo.convertirTpoDesdeString(rs.getString("TIPO1").toUpperCase()));
	        }
	        if (rs.getString("SEXO") != null) {
	            p.setSexo(Sexo.convertirSexoDesdeString(rs.getString("SEXO").toUpperCase()));
	        }

	        if (rs.getString("TIPO2") != null) {
	            // Usamos el mismo método que en TIPO1 para evitar errores de tildes o formatos
	            p.setTipo2(Tipo.convertirTpoDesdeString(rs.getString("TIPO2").toUpperCase()));
	        }
	        
	        if (rs.getString("ESTADO") != null) {
	            p.setEstado(Estado.convertirEstadoDesdeString(rs.getString("ESTADO").toUpperCase()));
	        }

	        cargarMovimientosPokemon(conexion, p);
	        listadoPokemon.add(p);
	    }

	    // Guardar la lista en el entrenador (ajusta según el nombre de tu setter)
	    e.setEquipo1(listadoPokemon); 

	    rs.close();
	    ps.close();
	}
	
	public static void obtenerTodosLosPokemon(Connection conexion, Entrenador e) throws SQLException {
	    // Eliminamos el filtro de CAJA para traer a todos
	    String sql = "SELECT P.ID_POKEMON, PX.NUM_POKEDEX, PX.NOM_POKEMON, P.MOTE, P.VITALIDAD, "
	                + "P.VITALIDAD_MAX, "
	                + "P.ATAQUE, P.ATA_ESPECIAL, P.DEFENSA, P.DEF_ESPECIAL, P.VELOCIDAD, "
	                + "P.NIVEL, P.FERTILIDAD, P.SEXO, P.ESTADO, P.CAJA, P.ID_OBJETO, "
	                + "PX.TIPO1, PX.TIPO2 "
	                + "FROM POKEMON P "
	                + "INNER JOIN POKEDEX PX ON PX.NUM_POKEDEX = P.NUM_POKEDEX "
	                + "WHERE P.ID_ENTRENADOR = ? " // Filtramos solo por el dueño
	                + "ORDER BY P.CAJA ASC";

	    PreparedStatement ps = conexion.prepareStatement(sql);
	    ps.setInt(1, e.getIdEntrenador()); 
	    
	    ResultSet rs = ps.executeQuery();
	    LinkedList<Pokemon> listadoCompleto = new LinkedList<Pokemon>();

	    while (rs.next()) {
	        Pokemon p = new Pokemon();
	        p.setId_pokemon(rs.getInt("ID_POKEMON"));
	        p.setNombre(rs.getString("NOM_POKEMON"));
	        p.setNum_pokedex(rs.getInt("NUM_POKEDEX"));
	        p.setMote(rs.getString("MOTE"));
	        p.setVitalidad(rs.getInt("VITALIDAD"));
	        p.setVitalidadMax(rs.getInt("VITALIDAD_MAX"));
	        p.setAtaque(rs.getInt("ATAQUE"));
	        p.setAtaqueEspecial(rs.getInt("ATA_ESPECIAL"));
	        p.setDefensa(rs.getInt("DEFENSA"));
	        p.setDefensaEspecial(rs.getInt("DEF_ESPECIAL"));
	        p.setVelocidad(rs.getInt("VELOCIDAD"));
	        p.setNivel(rs.getInt("NIVEL"));
	        p.setFertilidad(rs.getInt("FERTILIDAD"));
	        p.setCaja(rs.getInt("CAJA")); // Importante setear esto para saber dónde están

	        // Manejo de Enums y Tipos
	        if (rs.getString("TIPO1") != null) {
	            p.setTipo1(Tipo.convertirTpoDesdeString(rs.getString("TIPO1").toUpperCase()));
	        }
	        if (rs.getString("TIPO2") != null) {
	            p.setTipo2(Tipo.convertirTpoDesdeString(rs.getString("TIPO2").toUpperCase()));
	        }
	        if (rs.getString("SEXO") != null) {
	            p.setSexo(Sexo.convertirSexoDesdeString(rs.getString("SEXO").toUpperCase()));
	        }
	        if (rs.getString("ESTADO") != null) {
	            p.setEstado(Estado.convertirEstadoDesdeString(rs.getString("ESTADO").toUpperCase()));
	        }

	        // Cargamos sus movimientos (reutilizando tu método existente)
	        cargarMovimientosPokemon(conexion, p);
	        
	        listadoCompleto.add(p);
	    }

	    // OPCIÓN A: Guardarlos en una lista general del entrenador (si la tienes)
	    // e.setTodosLosPokemon(listadoCompleto); 
	    
	    // OPCIÓN B: Si quieres que sustituya al equipo actual
	    e.setEquipo1(listadoCompleto); 

	    rs.close();
	    ps.close();
	}
	
	/**
	 * Este es el mismo metodo de obtener pokemon, pero se usa para los pokemon de la caja
	 * También se cargan los movimientos
	 * @param conexion
	 * @param e
	 * @throws SQLException
	 */
	public static void obtenerPokemon2(Connection conexion, Entrenador e) throws SQLException {
	    String sql = "SELECT P.ID_POKEMON, PX.NUM_POKEDEX, PX.NOM_POKEMON, P.MOTE, P.VITALIDAD, "
	                + "P.VITALIDAD_MAX, P.ATAQUE, P.ATA_ESPECIAL, P.DEFENSA, P.DEF_ESPECIAL, P.VELOCIDAD, "
	                + "P.NIVEL, P.FERTILIDAD, P.SEXO, P.ESTADO, P.CAJA, P.ID_OBJETO, "
	                + "PX.TIPO1, PX.TIPO2 "
	                + "FROM POKEMON P "
	                + "INNER JOIN POKEDEX PX ON PX.NUM_POKEDEX = P.NUM_POKEDEX "
	                + "WHERE P.ID_ENTRENADOR = ? AND P.CAJA > 6 "
	                + "ORDER BY P.CAJA ASC";

	    //Usamos try-with-resources para manejar el cierre de recursos automáticamente
	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setInt(1, e.getIdEntrenador()); 
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            LinkedList<Pokemon> listadoCaja = new LinkedList<Pokemon>();

	            while (rs.next()) {
	                Pokemon p = new Pokemon();
	                
	                //Seteo de datos básicos
	                p.setId_pokemon(rs.getInt("ID_POKEMON"));
	                p.setNombre(rs.getString("NOM_POKEMON"));
	                p.setNum_pokedex(rs.getInt("NUM_POKEDEX"));
	                p.setMote(rs.getString("MOTE"));
	                p.setVitalidad(rs.getInt("VITALIDAD"));
	                p.setVitalidadMax(rs.getInt("VITALIDAD_MAX"));
	                p.setAtaque(rs.getInt("ATAQUE"));
	                p.setAtaqueEspecial(rs.getInt("ATA_ESPECIAL"));
	                p.setDefensa(rs.getInt("DEFENSA"));
	                p.setDefensaEspecial(rs.getInt("DEF_ESPECIAL"));
	                p.setVelocidad(rs.getInt("VELOCIDAD"));
	                p.setNivel(rs.getInt("NIVEL"));
	                
	                //Guardamos la posición exacta en la caja (7, 8, 9...)
	                p.setCaja(rs.getInt("CAJA"));

	                //Manejo de Tipos con tu método conversor
	                if (rs.getString("TIPO1") != null) {
	                    p.setTipo1(Tipo.convertirTpoDesdeString(rs.getString("TIPO1").toUpperCase()));
	                }
	                if (rs.getString("TIPO2") != null) {
	                    p.setTipo2(Tipo.convertirTpoDesdeString(rs.getString("TIPO2").toUpperCase()));
	                }

	                cargarMovimientosPokemon(conexion, p);	              
	                listadoCaja.add(p);
	            }
	            e.setEquipo2(listadoCaja); 
	        }
	    }
	}
	
	/**
	 * Carga los datos de los movimientos que estan asociados a un pokemon
	 * devuelve la lista de los movimientos
	 * @param conexion
	 * @param p
	 */
	public static void cargarMovimientosPokemon(Connection conexion, Pokemon p) {
	    // Definimos la consulta (Asegúrate de que el JOIN o la subconsulta sea la correcta para tu DB)
	    // Ejemplo si usas una tabla intermedia:
	    String sql = "SELECT M.* FROM MOVIMIENTO M " +
	                 "INNER JOIN POKEMON_MOVIMIENTO PM ON M.ID_MOVIMIENTO = PM.ID_MOVIMIENTO " +
	                 "WHERE PM.ID_POKEMON = ?";

	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setInt(1, p.getId_pokemon()); // O el método que uses para obtener su ID
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            LinkedList<Movimiento> listaMovs = new LinkedList<>();
	            
	            while (rs.next()) {
	                Movimiento m = new Movimiento();
	                m.setIdMovimiento(rs.getInt("ID_MOVIMIENTO"));
	                m.setNombre(rs.getString("NOM_MOVIMIENTO"));
	                m.setNumPP(rs.getInt("PP"));
	                
	                // --- Cargamos el Tipo para las imágenes ---
	                String tipoBD = rs.getString("TIPO");
	                if (tipoBD != null) {
	                    m.setTipo(Tipo.valueOf(tipoBD.toUpperCase()));
	                }
	                
	                listaMovs.add(m);
	            }
	            
	            // Le asignamos la lista al Pokémon
	            p.setMovimientos(listaMovs);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al cargar movimientos del pokemon: " + e.getMessage());
	    }
	}
	
	/**
	 * Dejando el primero en un lugar inaccesible y luego actualizadolo
	 * ya que los valores de CAJA en la base de datos es UNIQUE y puede
	 * dar conflicto.
	 * @param con
	 * @param idEntrenador
	 * @param posA
	 * @param posB
	 * @throws SQLException
	 */
	
	public static void intercambiarPosicion(Connection con, int idEntrenador, int posA, int posB) throws SQLException {
	    String sql1 = "UPDATE POKEMON SET CAJA = -1 WHERE ID_ENTRENADOR = ? AND CAJA = ?";
	    String sql2 = "UPDATE POKEMON SET CAJA = ? WHERE ID_ENTRENADOR = ? AND CAJA = ?";
	    String sql3 = "UPDATE POKEMON SET CAJA = ? WHERE ID_ENTRENADOR = ? AND CAJA = -1";


	    try {
	        con.setAutoCommit(false);
	        //Quitar el pokemon 1
	        PreparedStatement ps1 = con.prepareStatement(sql1);
	        ps1.setInt(1, idEntrenador);
	        ps1.setInt(2, posA);
	        ps1.executeUpdate();

	        //Mover el pokemon 2 a la posicion 1
	        PreparedStatement ps2 = con.prepareStatement(sql2);
	        ps2.setInt(1, posA);
	        ps2.setInt(2, idEntrenador);
	        ps2.setInt(3, posB);
	        ps2.executeUpdate();

	        //Saca el pokemon del sitio perdido
	        PreparedStatement ps3 = con.prepareStatement(sql3);
	        ps3.setInt(1, posB);
	        ps3.setInt(2, idEntrenador);
	        ps3.executeUpdate();

	        con.commit(); 
	    } catch (SQLException e) {
	        con.rollback(); 
	        throw e;
	    }
	}
	
	public static void moverPokemon(Connection con, int idEntrenador, int posOrigen, int posDestino) throws SQLException {
	    
	    // Comprobar si hay alguien en la posición de destino
	    String sqlCheck = "SELECT ID_POKEMON FROM POKEMON WHERE ID_ENTRENADOR = ? AND CAJA = ?";
	    
	    PreparedStatement psCheck = con.prepareStatement(sqlCheck);
	    psCheck.setInt(1, idEntrenador);
	    psCheck.setInt(2, posDestino);
	    ResultSet rs = psCheck.executeQuery();

	    if (rs.next()) {
	        // Usar metodo de intercambio por UNIQUE
	        System.out.println("Posición ocupada. Intercambiando...");
	        intercambiarPosicion(con, idEntrenador, posOrigen, posDestino);
	    } else {
	        // Si no hay nadie
	        String sqlUpdate = "UPDATE POKEMON SET CAJA = ? WHERE ID_ENTRENADOR = ? AND CAJA = ?";
	        PreparedStatement psUpd = con.prepareStatement(sqlUpdate);
	        psUpd.setInt(1, posDestino);
	        psUpd.setInt(2, idEntrenador);
	        psUpd.setInt(3, posOrigen);
	        psUpd.executeUpdate();
	        System.out.println("Movimiento realizado con éxito.");
	    }
	}
	public static void compactarEquipo(Connection con, int idEntrenador) throws SQLException {
	   
	    String sqlSelect = "SELECT ID_POKEMON FROM POKEMON WHERE ID_ENTRENADOR = ? AND CAJA BETWEEN 1 AND 6 ORDER BY CAJA ASC";
	    String sqlUpdate = "UPDATE POKEMON SET CAJA = ? WHERE ID_POKEMON = ?";

	    LinkedList<Integer> ids = new LinkedList<>();

	    try {
	        con.setAutoCommit(false);
	        PreparedStatement psSel = con.prepareStatement(sqlSelect);
	        psSel.setInt(1, idEntrenador);
	        ResultSet rs = psSel.executeQuery();
	        while (rs.next()) {
	            ids.add(rs.getInt("ID_POKEMON"));
	        }

	     
	        PreparedStatement psUpd = con.prepareStatement(sqlUpdate);
	        int nuevaPosicion = 1;

	        for (int id : ids) {
	            psUpd.setInt(1, nuevaPosicion);
	            psUpd.setInt(2, id);
	            psUpd.executeUpdate();
	            nuevaPosicion++;
	        }

	        con.commit();
	        System.out.println("Equipo compactado: se han reordenado " + ids.size() + " pokémon.");
	        
	    } catch (SQLException e) {
	        con.rollback();
	        throw e;
	    } finally {
	        con.setAutoCommit(true);
	    }
	}
	
	public static void compactarCaja(Connection con, int idEntrenador) throws SQLException {
	    //Buscamos solo los que tienen caja mayor a 6
	    String sqlSelect = "SELECT ID_POKEMON FROM POKEMON WHERE ID_ENTRENADOR = ? AND CAJA > 6 ORDER BY CAJA ASC";
	    String sqlUpdate = "UPDATE POKEMON SET CAJA = ? WHERE ID_POKEMON = ?";

	    LinkedList<Integer> ids = new LinkedList<>();

	    try {
	        con.setAutoCommit(false);
	        
	        //Obtenemos los IDs de los pokémon que están en la caja
	        PreparedStatement psSel = con.prepareStatement(sqlSelect);
	        psSel.setInt(1, idEntrenador);
	        ResultSet rs = psSel.executeQuery();
	        while (rs.next()) {
	            ids.add(rs.getInt("ID_POKEMON"));
	        }

	        PreparedStatement psUpd = con.prepareStatement(sqlUpdate);
	        
	        //La primera posición de la caja es la 7 (1-6 son el equipo)
	        int nuevaPosicionCaja = 7;

	        for (int id : ids) {
	            psUpd.setInt(1, nuevaPosicionCaja);
	            psUpd.setInt(2, id);
	            psUpd.executeUpdate();
	            nuevaPosicionCaja++;
	        }

	        con.commit();
	        System.out.println("Caja compactada: se han reordenado " + ids.size() + " pokémon a partir de la posición 7.");
	        
	    } catch (SQLException e) {
	        if (con != null) con.rollback();
	        throw e;
	    } finally {
	        con.setAutoCommit(true);
	    }
	}
	
	public static int obtenerSiguienteHuecoCaja(Connection conexion, int idEntrenador) throws SQLException {
	    // Buscamos el valor máximo de la columna CAJA para este entrenador
	    String sql = "SELECT MAX(CAJA) AS ultimo_hueco FROM POKEMON WHERE ID_ENTRENADOR = ?";
	    
	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setInt(1, idEntrenador);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                int ultimoHueco = rs.getInt("ultimo_hueco");
	                // Si la consulta devuelve 0 o un valor menor a 6 (porque no hay pokémon en la caja),
	                // el siguiente hueco disponible debe ser el 7.
	                if (ultimoHueco < 6) {
	                    return 7;
	                }
	                return ultimoHueco + 1;
	            }
	        }
	    }
	    // Por si acaso falla algo
	    return 7;
	}
	
	public static void actualizarPosicionPokemon(Connection con, int idPokemon, int nuevaCaja) throws SQLException {
	    String sql = "UPDATE POKEMON SET CAJA = ? WHERE ID_POKEMON = ?";
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, nuevaCaja);
	        ps.setInt(2, idPokemon);
	        ps.executeUpdate();
	    }
	}
	
	public static void asignarMovimiento(Connection con, int idPokemon, int idMovimiento, int ppMax) throws SQLException {
	    // 1. Contar cuántos movimientos tiene ya el pokemon
	    String sqlContar = "SELECT COUNT(*) FROM POKEMON_MOVIMIENTO WHERE ID_POKEMON = ?";
	    
	    int totalMovimientos = 0;
	    try (PreparedStatement psCount = con.prepareStatement(sqlContar)) {
	        psCount.setInt(1, idPokemon);
	        ResultSet rs = psCount.executeQuery();
	        if (rs.next()) {
	            totalMovimientos = rs.getInt(1);
	        }
	    }

	    // 2. Comprobar el límite
	    if (totalMovimientos >= 4) {
	        System.out.println("Error: El Pokémon ya tiene 4 movimientos. Debes olvidar uno primero.");
	        return; // Salimos sin insertar
	    }

	    // 3. Si tiene menos de 4, procedemos al INSERT
	    String sqlInsert = "INSERT INTO POKEMON_MOVIMIENTO (ID_POKEMON, ID_MOVIMIENTO, ACTIVO, NUM_PP) VALUES (?, ?, 1, ?)";
	    try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
	        psInsert.setInt(1, idPokemon);
	        psInsert.setInt(2, idMovimiento);
	        psInsert.setInt(3, ppMax);
	        psInsert.executeUpdate();
	        System.out.println("Movimiento asignado con éxito.");
	    }
	    
	}
	
	public static boolean eliminarPokemon(Connection con, int idPokemon, int idEntrenador) throws SQLException {
	    String sqlDelete = "DELETE FROM POKEMON WHERE ID_POKEMON = ?";
	    
	    // FASE 1: Borrado atómico
	    try (PreparedStatement ps = con.prepareStatement(sqlDelete)) {
	        ps.setInt(1, idPokemon);
	        int filasAfec = ps.executeUpdate();
	        
	        if (filasAfec > 0) {
	            // Confirmamos el borrado inmediatamente si la conexión lo requiere
	            if (!con.getAutoCommit()) con.commit(); 

	            // FASE 2: Llamadas independientes
	            // Como no queremos tocar estos métodos, los llamamos por separado.
	            // Cada uno gestionará su propio setAutoCommit y Commit internamente.
	            compactarEquipo(con, idEntrenador);
	            compactarCaja(con, idEntrenador);
	            
	            return true;
	        }
	    } catch (SQLException e) {
	        // Si falla el borrado, aquí no hay riesgo de rollback cruzado
	        throw e;
	    }
	    return false;
	}
	/**
	 * Este metodo busca la pareja del pokemon del hueco uno, si no hay pareja pues me salta un mensaje
	 * @param con
	 * @param idEntrenador
	 * @param p
	 * @throws SQLException
	 */
	public static boolean sonCompatibles(Pokemon p1, Pokemon p2) {
	    // 1. No pueden ser el mismo Pokémon físicamente
	    if (p1.getId_pokemon() == p2.getId_pokemon()) {
	        return false;
	    }

	    // 2. Deben ser de la misma especie (mismo número de Pokédex)
	    if (p1.getNum_pokedex() != p2.getNum_pokedex()) {
	        return false;
	    }

	    // 3. Deben ser de distinto sexo
	    // Asumiendo que usas un Enum o String para el sexo
	    if (p1.getSexo() == p2.getSexo()) {
	        return false;
	    }

	    // 4. Deben tener fertilidad mayor a 0
	    if (p1.getFertilidad() <= 0 || p2.getFertilidad() <= 0) {
	        return false;
	    }

	    // Si pasa todos los filtros, son compatibles
	    return true;
	}
	
	
	
	public static Pokemon pokemonCriado(Connection con, Entrenador entrenador, Pokemon p1, Pokemon p2) {
		Pokemon hijo = null;
		try {
	        // Desactivamos el auto-commit para manejar la transacción manualmente
	        con.setAutoCommit(false);

	        // 1. GENERAR MOTE MEZCLADO
	        String moteHijo;
	        int mitad1 = p1.getMote().length() / 2;
	        int mitad2 = p2.getMote().length() / 2;
	        
	        if (Math.random() > 0.5) {
	            moteHijo = p1.getMote().substring(0, mitad1) + p2.getMote().substring(mitad2);
	        } else {
	            moteHijo = p2.getMote().substring(0, mitad2) + p1.getMote().substring(mitad1);
	        }

	        // 2. OBTENER EL NUEVO ID_POKEMON (Manual)
	        int nuevoId = 1;
	        try (PreparedStatement psMax = con.prepareStatement("SELECT MAX(ID_POKEMON) FROM POKEMON");
	             ResultSet rsMax = psMax.executeQuery()) {
	            if (rsMax.next()) {
	                nuevoId = rsMax.getInt(1) + 1;
	            }
	        }

	        // 3. OBTENER EL PRIMER HUECO LIBRE EN LA CAJA PARA ESTE ENTRENADOR
	        int proximaPosicion = 1;
	        String sqlHuecos = "SELECT CAJA FROM POKEMON WHERE ID_ENTRENADOR = ? ORDER BY CAJA ASC";
	        try (PreparedStatement psHuecos = con.prepareStatement(sqlHuecos)) {
	            psHuecos.setInt(1, entrenador.getIdEntrenador()); // Usamos el ID del objeto Entrenador
	            try (ResultSet rsHuecos = psHuecos.executeQuery()) {
	                while (rsHuecos.next()) {
	                    int ocupado = rsHuecos.getInt("CAJA");
	                    if (ocupado == proximaPosicion) {
	                        proximaPosicion++;
	                    } else if (ocupado > proximaPosicion) {
	                        break; 
	                    }
	                }
	            }
	        }

	        // 4. MEJORES CARACTERÍSTICAS
	        int hp = Math.max(p1.getVitalidad(), p2.getVitalidad());
	        int ataque = Math.max(p1.getAtaque(), p2.getAtaque());
	        int ataqueEsp = Math.max(p1.getAtaqueEspecial(), p2.getAtaqueEspecial());
	        int defensa = Math.max(p1.getDefensa(), p2.getDefensa());
	        int defensaEsp = Math.max(p1.getDefensaEspecial(), p2.getDefensaEspecial());
	        int velocidad = Math.max(p1.getVelocidad(), p2.getVelocidad());

	        // 5. INSERTAR EL POKÉMON EN LA TABLA POKEMON
	        String sqlPoke = "INSERT INTO POKEMON (ID_POKEMON, NUM_POKEDEX, ID_ENTRENADOR, MOTE, VITALIDAD, VITALIDAD_MAX, " +
	                         "ATAQUE, ATA_ESPECIAL, DEFENSA, DEF_ESPECIAL, VELOCIDAD, NIVEL, EXPERIENCIA, " +
	                         "FERTILIDAD, SEXO, ESTADO, CAJA) " +
	                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, 0, 5, ?, 'VIVO', ?)";

	        Sexo sexoEnumHijo = (Math.random() > 0.5) ? Sexo.M : Sexo.H;
	        
	        try (PreparedStatement ps = con.prepareStatement(sqlPoke)) {
	        	hijo = new Pokemon();
	        	ps.setInt(1, nuevoId);
	            ps.setInt(2, p1.getNum_pokedex());
	            ps.setInt(3, entrenador.getIdEntrenador()); // Usamos el ID del objeto Entrenador
	            ps.setString(4, moteHijo);
	            ps.setInt(5, hp);
	            ps.setInt(6, hp); // VITALIDAD_MAX
	            ps.setInt(7, ataque);
	            ps.setInt(8, ataqueEsp);
	            ps.setInt(9, defensa);
	            ps.setInt(10, defensaEsp);
	            ps.setInt(11, velocidad);
	            ps.setString(12, Math.random() > 0.5 ? "M" : "H");
	            ps.setInt(13, proximaPosicion);
	            
	            ps.executeUpdate();
	            
	            hijo = new Pokemon();
	            hijo.setId_pokemon(nuevoId);
	            hijo.setNum_pokedex(p1.getNum_pokedex());
	            hijo.setNombre(p1.getNombre());
	            hijo.setMote(moteHijo);
	            hijo.setVitalidad(hp);
	            hijo.setVitalidadMax(hp);
	            hijo.setAtaque(ataque);
	            hijo.setAtaqueEspecial(ataqueEsp);
	            hijo.setDefensa(defensa);
	            hijo.setDefensaEspecial(defensaEsp);
	            hijo.setVelocidad(velocidad);
	            hijo.setSexo(sexoEnumHijo); // Guardamos el Enum
	            hijo.setNivel(1);
	            hijo.setCaja(proximaPosicion);
	            hijo.setTipo1(p1.getTipo1());
	            hijo.setTipo2(p1.getTipo2());
	        }

	        // 6. LÓGICA DE HERENCIA DE MOVIMIENTOS (De los padres al hijo)
	        List<Integer> movimientosHijo = new ArrayList<>();
	        String sqlGetMovs = "SELECT ID_MOVIMIENTO FROM pokemon_movimiento WHERE ID_POKEMON = ? AND ACTIVO = 1";
	        
	        try (PreparedStatement psGet = con.prepareStatement(sqlGetMovs)) {
	            // Movimientos del Padre 1
	            psGet.setInt(1, p1.getId_pokemon());
	            try (ResultSet rs = psGet.executeQuery()) {
	                List<Integer> movsP1 = new ArrayList<>();
	                while (rs.next()) movsP1.add(rs.getInt("ID_MOVIMIENTO"));
	                Collections.shuffle(movsP1);
	                for (int i = 0; i < Math.min(2, movsP1.size()); i++) {
	                    movimientosHijo.add(movsP1.get(i));
	                }
	            }
	            // Movimientos del Padre 2
	            psGet.setInt(1, p2.getId_pokemon());
	            try (ResultSet rs = psGet.executeQuery()) {
	                List<Integer> movsP2 = new ArrayList<>();
	                while (rs.next()) movsP2.add(rs.getInt("ID_MOVIMIENTO"));
	                Collections.shuffle(movsP2);
	                for (int i = 0; i < Math.min(2, movsP2.size()); i++) {
	                    if (!movimientosHijo.contains(movsP2.get(i))) {
	                        movimientosHijo.add(movsP2.get(i));
	                    }
	                }
	            }
	        }

	        // 7. INSERTAR MOVIMIENTOS AL HIJO
	        String sqlInsMov = "INSERT INTO pokemon_movimiento (ID_POKEMON, ID_MOVIMIENTO, ACTIVO, NUM_PP) VALUES (?, ?, 1, 15)";
	        try (PreparedStatement psInsMov = con.prepareStatement(sqlInsMov)) {
	            for (Integer idMov : movimientosHijo) {
	                psInsMov.setInt(1, nuevoId);
	                psInsMov.setInt(2, idMov);
	                psInsMov.addBatch();
	            }
	            psInsMov.executeBatch();
	        }

	        // 8. RESTAR FERTILIDAD A LOS PADRES
	        String sqlUpdate = "UPDATE POKEMON SET FERTILIDAD = FERTILIDAD - 1 WHERE ID_POKEMON = ?";
	        try (PreparedStatement psUp = con.prepareStatement(sqlUpdate)) {
	            psUp.setInt(1, p1.getId_pokemon());
	            psUp.executeUpdate();
	            psUp.setInt(1, p2.getId_pokemon());
	            psUp.executeUpdate();
	        }

	        // --- FINALIZAR TRANSACCIÓN ---
	        con.commit();
	        System.out.println("[LOG] Crianza finalizada. El entrenador " + entrenador.getNombre() + " tiene un nuevo Pokemon: " + moteHijo);

	    } catch (SQLException e) {
	        try {
	            if (con != null) con.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        System.err.println("[ERROR] Error al procesar la crianza en BD");
	        e.printStackTrace();
	    } finally {
	        try {
	            if (con != null) con.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return hijo;
	}
	
	public static boolean entrenamientoPesado(Connection con, Entrenador entrenador, Pokemon p) {
	    // 0. COMPROBACIÓN DE NIVEL MÁXIMO
	    if (p.getNivel() >= 100) {
	        System.out.println("El Pokémon ya está al nivel máximo (100).");
	        return false;
	    }

	    // Calculamos el coste: 20 * Nivel
	    int coste = 20 * p.getNivel();

	    // Verificamos si el entrenador tiene dinero suficiente
	    if (entrenador.getPokedollars() < coste) {
	        System.out.println("Dinero insuficiente para el entrenamiento pesado.");
	        return false;
	    }

	    try {
	        con.setAutoCommit(false);

	        // 1. Descontar dinero al entrenador
	        String sqlDinero = "UPDATE ENTRENADOR SET POKEDOLLARS = POKEDOLLARS - ? WHERE ID_ENTRENADOR = ?";
	        try (PreparedStatement psDinero = con.prepareStatement(sqlDinero)) {
	            psDinero.setInt(1, coste);
	            psDinero.setInt(2, entrenador.getIdEntrenador());
	            psDinero.executeUpdate();
	        }

	        // 2. Aumentar estadísticas (+5) y subir NIVEL (+1)
	        // Usamos LEAST(NIVEL + 1, 100) en SQL por seguridad, aunque ya lo controlamos en Java
	        String sqlStats = "UPDATE POKEMON SET DEFENSA = DEFENSA + 5, DEF_ESPECIAL = DEF_ESPECIAL + 5, " +
	                          "VITALIDAD = VITALIDAD + 5, VITALIDAD_MAX = VITALIDAD_MAX + 5, " +
	                          "NIVEL = NIVEL + 1 " +
	                          "WHERE ID_POKEMON = ?";
	        try (PreparedStatement psStats = con.prepareStatement(sqlStats)) {
	            psStats.setInt(1, p.getId_pokemon());
	            psStats.executeUpdate();
	        }

	        con.commit();

	        // 3. Actualizamos los objetos en memoria
	        entrenador.setPokedollars(entrenador.getPokedollars() - coste);
	        p.setDefensa(p.getDefensa() + 5);
	        p.setDefensaEspecial(p.getDefensaEspecial() + 5);
	        p.setVitalidad(p.getVitalidad() + 5);
	        p.setVitalidadMax(p.getVitalidadMax() + 5);
	        p.setNivel(p.getNivel() + 1); // Subimos el nivel en el objeto

	        System.out.println("[LOG] Entrenamiento pesado completado. " + p.getMote() + " ahora es nivel " + p.getNivel());
	        return true;

	    } catch (SQLException e) {
	        try {
	            if (con != null) con.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            con.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	public static boolean entrenamientoFurioso(Connection con, Entrenador entrenador, Pokemon p) {
	    // 0. COMPROBACIÓN DE NIVEL MÁXIMO
	    if (p.getNivel() >= 100) {
	        System.out.println("El Pokémon ya está al nivel máximo (100).");
	        return false;
	    }

	    // Calculamos el coste: 30 * Nivel
	    int coste = 30 * p.getNivel();

	    // Verificamos si el entrenador tiene dinero suficiente
	    if (entrenador.getPokedollars() < coste) {
	        System.out.println("Dinero insuficiente para el entrenamiento furioso.");
	        return false;
	    }

	    try {
	        // Desactivamos auto-commit para manejar la transacción
	        con.setAutoCommit(false);

	        // 1. Descontar dinero al entrenador
	        String sqlDinero = "UPDATE ENTRENADOR SET POKEDOLLARS = POKEDOLLARS - ? WHERE ID_ENTRENADOR = ?";
	        try (PreparedStatement psDinero = con.prepareStatement(sqlDinero)) {
	            psDinero.setInt(1, coste);
	            psDinero.setInt(2, entrenador.getIdEntrenador());
	            psDinero.executeUpdate();
	        }

	        // 2. Aumentar estadísticas (+5) y subir NIVEL (+1)
	        // Estadísticas afectadas: ATAQUE, ATA_ESPECIAL, VELOCIDAD
	        String sqlStats = "UPDATE POKEMON SET ATAQUE = ATAQUE + 5, ATA_ESPECIAL = ATA_ESPECIAL + 5, " +
	                          "VELOCIDAD = VELOCIDAD + 5, NIVEL = NIVEL + 1 " +
	                          "WHERE ID_POKEMON = ?";
	        try (PreparedStatement psStats = con.prepareStatement(sqlStats)) {
	            psStats.setInt(1, p.getId_pokemon());
	            psStats.executeUpdate();
	        }

	        // Confirmar cambios
	        con.commit();

	        // 3. Actualizamos los objetos en memoria para que la interfaz se vea al día
	        entrenador.setPokedollars(entrenador.getPokedollars() - coste);
	        p.setAtaque(p.getAtaque() + 5);
	        p.setAtaqueEspecial(p.getAtaqueEspecial() + 5);
	        p.setVelocidad(p.getVelocidad() + 5);
	        p.setNivel(p.getNivel() + 1);

	        System.out.println("[LOG] Entrenamiento furioso completado. " + p.getMote() + " ahora es nivel " + p.getNivel());
	        return true;

	    } catch (SQLException e) {
	        // En caso de error, deshacer cambios
	        try {
	            if (con != null) con.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        // Restaurar estado del auto-commit
	        try {
	            if (con != null) con.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static boolean entrenamientoFuncional(Connection con, Entrenador entrenador, Pokemon p) {
	    // 0. COMPROBACIÓN DE NIVEL MÁXIMO
	    if (p.getNivel() >= 100) {
	        System.out.println("El Pokémon ya está al nivel máximo (100).");
	        return false;
	    }

	    // Calculamos el coste: 40 * Nivel
	    int coste = 40 * p.getNivel();

	    // Verificamos si el entrenador tiene dinero suficiente
	    if (entrenador.getPokedollars() < coste) {
	        System.out.println("Dinero insuficiente para el entrenamiento funcional.");
	        return false;
	    }

	    try {
	        con.setAutoCommit(false);

	        // 1. Descontar dinero al entrenador
	        String sqlDinero = "UPDATE ENTRENADOR SET POKEDOLLARS = POKEDOLLARS - ? WHERE ID_ENTRENADOR = ?";
	        try (PreparedStatement psDinero = con.prepareStatement(sqlDinero)) {
	            psDinero.setInt(1, coste);
	            psDinero.setInt(2, entrenador.getIdEntrenador());
	            psDinero.executeUpdate();
	        }

	        // 2. Aumentar estadísticas (+5) y subir NIVEL (+1)
	        // Afecta a: VELOCIDAD, ATAQUE, DEFENSA, VITALIDAD y VITALIDAD_MAX
	        String sqlStats = "UPDATE POKEMON SET VELOCIDAD = VELOCIDAD + 5, ATAQUE = ATAQUE + 5, " +
	                          "DEFENSA = DEFENSA + 5, VITALIDAD = VITALIDAD + 5, " +
	                          "VITALIDAD_MAX = VITALIDAD_MAX + 5, NIVEL = NIVEL + 1 " +
	                          "WHERE ID_POKEMON = ?";
	        try (PreparedStatement psStats = con.prepareStatement(sqlStats)) {
	            psStats.setInt(1, p.getId_pokemon());
	            psStats.executeUpdate();
	        }

	        con.commit();

	        // 3. Actualizamos los objetos en memoria
	        entrenador.setPokedollars(entrenador.getPokedollars() - coste);
	        p.setVelocidad(p.getVelocidad() + 5);
	        p.setAtaque(p.getAtaque() + 5);
	        p.setDefensa(p.getDefensa() + 5);
	        p.setVitalidad(p.getVitalidad() + 5);
	        p.setVitalidadMax(p.getVitalidadMax() + 5);
	        p.setNivel(p.getNivel() + 1);

	        System.out.println("[LOG] Entrenamiento funcional completado para " + p.getMote());
	        return true;

	    } catch (SQLException e) {
	        try {
	            if (con != null) con.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if (con != null) con.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static boolean entrenamientoOnirico(Connection con, Entrenador entrenador, Pokemon p) {
	    // 0. COMPROBACIÓN DE NIVEL MÁXIMO
	    if (p.getNivel() >= 100) {
	        System.out.println("El Pokémon ya está al nivel máximo (100).");
	        return false;
	    }

	    // Calculamos el coste: 40 * Nivel
	    int coste = 40 * p.getNivel();

	    // Verificamos si el entrenador tiene dinero suficiente
	    if (entrenador.getPokedollars() < coste) {
	        System.out.println("Dinero insuficiente para el entrenamiento onírico.");
	        return false;
	    }

	    try {
	        con.setAutoCommit(false);

	        // 1. Descontar dinero al entrenador
	        String sqlDinero = "UPDATE ENTRENADOR SET POKEDOLLARS = POKEDOLLARS - ? WHERE ID_ENTRENADOR = ?";
	        try (PreparedStatement psDinero = con.prepareStatement(sqlDinero)) {
	            psDinero.setInt(1, coste);
	            psDinero.setInt(2, entrenador.getIdEntrenador());
	            psDinero.executeUpdate();
	        }

	        // 2. Aumentar estadísticas (+5) y subir NIVEL (+1)
	        // Afecta a: VELOCIDAD, ATA_ESPECIAL, DEF_ESPECIAL, VITALIDAD y VITALIDAD_MAX
	        String sqlStats = "UPDATE POKEMON SET VELOCIDAD = VELOCIDAD + 5, ATA_ESPECIAL = ATA_ESPECIAL + 5, " +
	                          "DEF_ESPECIAL = DEF_ESPECIAL + 5, VITALIDAD = VITALIDAD + 5, " +
	                          "VITALIDAD_MAX = VITALIDAD_MAX + 5, NIVEL = NIVEL + 1 " +
	                          "WHERE ID_POKEMON = ?";
	        try (PreparedStatement psStats = con.prepareStatement(sqlStats)) {
	            psStats.setInt(1, p.getId_pokemon());
	            psStats.executeUpdate();
	        }

	        con.commit();

	        // 3. Actualizamos los objetos en memoria
	        entrenador.setPokedollars(entrenador.getPokedollars() - coste);
	        p.setVelocidad(p.getVelocidad() + 5);
	        p.setAtaqueEspecial(p.getAtaqueEspecial() + 5);
	        p.setDefensaEspecial(p.getDefensaEspecial() + 5);
	        p.setVitalidad(p.getVitalidad() + 5);
	        p.setVitalidadMax(p.getVitalidadMax() + 5);
	        p.setNivel(p.getNivel() + 1);

	        System.out.println("[LOG] Entrenamiento onírico completado para " + p.getMote());
	        return true;

	    } catch (SQLException e) {
	        try {
	            if (con != null) con.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if (con != null) con.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static void actualizarNivelStats(Connection con, Pokemon p) throws SQLException {
	    String sql = "UPDATE POKEMON SET NIVEL = ?, VITALIDAD = ?, ATAQUE = ?, DEFENSA = ?, "
	               + "ATAQUE_ESPECIAL = ?, DEFENSA_ESPECIAL = ?, VELOCIDAD = ? "
	               + "WHERE ID_POKEMON = ?";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, p.getNivel());
	        ps.setInt(2, p.getVitalidad());
	        ps.setInt(3, p.getAtaque());
	        ps.setInt(4, p.getDefensa());
	        ps.setInt(5, p.getAtaqueEspecial());
	        ps.setInt(6, p.getDefensaEspecial());
	        ps.setInt(7, p.getVelocidad());
	        ps.setInt(8, p.getId_pokemon());

	        ps.executeUpdate();
	    }
	}
	
}

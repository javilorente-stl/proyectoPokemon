package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.LinkedList;

import modelo.Entrenador;
import modelo.Estado;
import modelo.Movimiento;
import modelo.Pokemon;
import modelo.Sexo;
import modelo.Tipo;

public class PokemonCrud {

	
	
	
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

	 // 1. Obtener el nuevo ID_POKEMON (Global)
	    int nuevoId = 1;
	    try (PreparedStatement psMax = con.prepareStatement("SELECT MAX(ID_POKEMON) FROM POKEMON");
	         ResultSet rsMax = psMax.executeQuery()) {
	        if (rsMax.next()) {
	            nuevoId = rsMax.getInt(1) + 1;
	        }
	    }

	    // 2. BUSCAR EL PRIMER HUECO LIBRE EN LA CAJA (Local para el entrenador)
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
	                    break; // ¡Encontramos un salto! proximaPosicion es el hueco libre
	                }
	            }
	        }
	    }

	    // 3. Obtener datos de la Pokedex e insertar
	    try (PreparedStatement psSel = con.prepareStatement(sqlSelect)) {
	        psSel.setInt(1, numPokedex);
	        try (ResultSet rs = psSel.executeQuery()) {
	            if (rs.next()) {
	            	int vitalidadAleatoria = (int) (Math.random() * 6) + 15;
	                
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
	                    psIns.setString(15, (Math.random() < 0.5) ? "M" : "H");
	                    psIns.setString(16, "VIVO");
	                    psIns.setInt(17, proximaPosicion); // El hueco encontrado
	                    psIns.setNull(18, java.sql.Types.INTEGER);

	                    psIns.executeUpdate();
	                    
	                 // 1. Obtenemos el tipo principal directamente del ResultSet de la Pokedex
	                    Tipo tipoPrincipal = Tipo.convertirTpoDesdeString(rs.getString("TIPO1").toUpperCase());

	                    // 2. Llamamos al método que busca el ataque más débil (Potencia > 0) de ese tipo
	                    Movimiento inicial = MovimientoCrud.obtenerMovimientoInicial(con, tipoPrincipal);

	                    // 3. Verificamos que se haya encontrado algo antes de insertar
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
	        if (rs.getString("TIPO1") != null) {
	            p.setTipo1(Tipo.convertirTpoDesdeString(rs.getString("TIPO1").toUpperCase()));
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
	
	//Dejando el primero en un lugar inaccesible y luego actualizadolo
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
	    String sqlContar = "SELECT COUNT(*) FROM POKEMON_MOVIMIENTOS WHERE ID_POKEMON = ?";
	    
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
	    String sqlInsert = "INSERT INTO POKEMON_MOVIMIENTOS (ID_POKEMON, ID_MOVIMIENTO, ACTIVO, NUM_PP) VALUES (?, ?, 1, ?)";
	    try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
	        psInsert.setInt(1, idPokemon);
	        psInsert.setInt(2, idMovimiento);
	        psInsert.setInt(3, ppMax);
	        psInsert.executeUpdate();
	        System.out.println("Movimiento asignado con éxito.");
	    }
	}
}

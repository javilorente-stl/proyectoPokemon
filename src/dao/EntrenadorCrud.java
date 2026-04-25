package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import modelo.Entrenador;
import modelo.Objeto;

public class EntrenadorCrud {

	public static void crearEntrenador(Connection con, Entrenador e) throws SQLException{
		e.setIdEntrenador(obtenerIdEntrenador(con));
		e.setPokedollars(generarPokedollares());
		e.setImagenEntrenador("Prueba");
		e.setClaseEntrenador(1);
		
		String sql = "INSERT INTO ENTRENADOR VALUES (?, ?, ?, ?, ?, ?)";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, e.getIdEntrenador());
		ps.setString(2, e.getNombre());
		ps.setString(3, e.getPassword());
		ps.setInt(4, e.getPokedollars());
		ps.setString(5, e.getImagenEntrenador());
		ps.setInt(6, e.getClaseEntrenador());
		
		ps.executeUpdate();
		
	}
	
	private static int obtenerIdEntrenador(Connection con) throws SQLException {
		int idEntrenador = 0;
		String sql = "SELECT MAX(ID_ENTRENADOR) FROM ENTRENADOR";
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		while(rs.next()) {
			idEntrenador = rs.getInt(1);
		}
		
		return idEntrenador+1;
	}
	
	private static int generarPokedollares() {
		Random rd = new Random();
		return rd.nextInt(201)+800;
	}
	
	public static void obtenerIDPokedollares(Connection con, Entrenador e) throws SQLException {
		String sql = "SELECT ID_ENTRENADOR, POKEDOLLARS\r\n"
				+ "FROM ENTRENADOR \r\n"
				+ "WHERE NOM_ENTRENADOR=?\r\n"
				+ "AND PASSWORD=?";
		
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, e.getNombre());
		ps.setString(2, e.getPassword());
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			e.setIdEntrenador(rs.getInt("ID_ENTRENADOR"));
			e.setPokedollars(rs.getInt("POKEDOLLARS"));
		}
	}
	
	
	  
	public static void obtenerMochila(Connection con, Entrenador e) throws SQLException {
		    // Nos aseguramos de que el entrenador tenga una mochila instanciada
		    if (e.getMochila() == null) {
		        e.setMochila(new LinkedList<Objeto>());
		    }
		    
		    //  Inicializamos o limpiamos la LinkedList de objetos
		    // (Asegúrate de que en la clase Mochila, el atributo sea LinkedList<Objeto>)
		    LinkedList<Objeto> listaObjetos = new LinkedList<>();

		    // Consulta SQL con JOIN para obtener los datos base y la cantidad actual
		    String sql = "SELECT O.ID_OBJETO, O.NOM_OBJETO, O.ATAQUE, O.DEFENSA, O.VELOCIDAD, " +
		                 "O.ATA_ESPECIAL, O.DEF_ESPECIAL, O.ESTAMINA, O.RECUPERACION_ESTAMINA, M.CANTIDAD " +
		                 "FROM OBJETO O " +
		                 "JOIN MOCHILA M ON O.ID_OBJETO = M.ID_OBJETO " +
		                 "WHERE M.ID_ENTRENADOR = ?";

		    try (PreparedStatement ps = con.prepareStatement(sql)) {
		        ps.setInt(1, e.getIdEntrenador());
		        ResultSet rs = ps.executeQuery();

		        while (rs.next()) {
		            // Creamos el objeto y rellenamos sus atributos
		            Objeto obj = new Objeto();
		            obj.setId_objeto(rs.getInt("ID_OBJETO"));
		            obj.setNombre(rs.getString("NOM_OBJETO"));
		            obj.setAtaque(rs.getInt("ATAQUE"));
		            obj.setDefensa(rs.getInt("DEFENSA"));
		            obj.setVelocidad(rs.getInt("VELOCIDAD"));
		            obj.setAta_especial(rs.getInt("ATA_ESPECIAL"));
		            obj.setDef_especial(rs.getInt("DEF_ESPECIAL"));
		            obj.setEstamina(rs.getInt("ESTAMINA"));
		            obj.setRecuperacion_estamina(rs.getInt("RECUPERACION_ESTAMINA"));
		            
		            // La CANTIDAD viene de la tabla de relación (INVENTARIO)
		            obj.setCantidad(rs.getInt("CANTIDAD"));

		            // Añadimos al final de la LinkedList
		            listaObjetos.add(obj);
		        }
		    }
		    e.setMochila(listaObjetos);
	}
	
	public static void usarObjeto(Connection con, int idEntrenador, int idObjeto) throws SQLException {
	    //Usamos un UPDATE para restar 1 a la cantidad
		String sql = "UPDATE MOCHILA SET CANTIDAD = CANTIDAD - 1 " +
                "WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ? AND CANTIDAD > 0";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, idEntrenador);
	        ps.setInt(2, idObjeto);
	        
	        int filasAfectadas = ps.executeUpdate();
	        if (filasAfectadas == 0) {
	            throw new SQLException("No se pudo restar el objeto (¿Cantidad 0?)");
	        }
	    }
	}
	
	public static Entrenador obtenerEntrenadorFijo(Connection conexion, int idRival) throws SQLException {
	    Entrenador rival = new Entrenador();
	    String sql = "SELECT ID_ENTRENADOR, NOM_ENTRENADOR, POKEDOLLARS FROM ENTRENADOR WHERE ID_ENTRENADOR = ?";

	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setInt(1, idRival);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                rival.setIdEntrenador(rs.getInt("ID_ENTRENADOR"));
	                rival.setNombre(rs.getString("NOM_ENTRENADOR"));
	                

	                // Aprovechamos el método que ya tienes para cargar sus Pokémon
	                PokemonCrud.obtenerPokemon1(conexion, rival);
	                
	                System.out.println("Rival cargado: " + rival.getNombre() + " con " + rival.getEquipo1().size() + " Pokémon.");
	            } else {
	                System.err.println("No se encontró ningún entrenador con ID: " + idRival);
	                return null;
	            }
	        }
	    }
	    return rival;
	}
	
	public static void actualizarDinero(Connection con, int idEntrenador, int nuevoMonto) throws SQLException {
	    String SQL = "UPDATE ENTRENADOR SET POKEDOLLARS = ? WHERE ID_ENTRENADOR = ?";
	    try (PreparedStatement ps = con.prepareStatement(SQL)) {
	        ps.setInt(1, nuevoMonto);
	        ps.setInt(2, idEntrenador);
	        ps.executeUpdate();
	    }
	}
	
	public static void prepararCampeonEspejo(Connection con, int idJugador, int idCampeon) throws SQLException {
	    //  Limpieza del Campeón
	    String sqlDelete = "DELETE FROM POKEMON WHERE ID_ENTRENADOR = ?";
	    try (PreparedStatement ps = con.prepareStatement(sqlDelete)) {
	        ps.setInt(1, idCampeon);
	        ps.executeUpdate();
	    }

	    // Obtener el ID más alto
	    int ultimoId = 0;
	    String sqlMaxId = "SELECT MAX(ID_POKEMON) FROM POKEMON";
	    try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMaxId)) {
	        if (rs.next()) ultimoId = rs.getInt(1);
	    }

	    // Obtener tus Pokémon actuales
	    List<Integer> idsTusPokemon = new ArrayList<>();
	    String sqlIds = "SELECT ID_POKEMON FROM POKEMON WHERE ID_ENTRENADOR = ? AND UBICACION BETWEEN 1 AND 6";
	    try (PreparedStatement ps = con.prepareStatement(sqlIds)) {
	        ps.setInt(1, idJugador);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) idsTusPokemon.add(rs.getInt("ID_POKEMON"));
	        }
	    }

	    // SQL de Inserción (Incluyendo UBICACION/CAJA)
	    String sqlInsertPk = "INSERT INTO POKEMON (ID_POKEMON, NUM_POKEDEX, ID_ENTRENADOR, MOTE, VITALIDAD, VITALIDAD_MAX, " +
	                         "ATAQUE, ATA_ESPECIAL, DEFENSA, DEF_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, SEXO, ESTADO, UBICACION) " +
	                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    String sqlInsertMov = "INSERT INTO POKEMON_MOVIMIENTO (ID_POKEMON, ID_MOVIMIENTO, ACTIVO, NUM_PP) " +
	                          "SELECT ?, ID_MOVIMIENTO, ACTIVO, NUM_PP FROM POKEMON_MOVIMIENTO WHERE ID_POKEMON = ?";

	    // Usamos un contador para asignar las posiciones 1-6 en el equipo del Campeón
	    int contadorCaja = 1;

	    for (int idOld : idsTusPokemon) {
	        ultimoId++;
	        int idNuevo = ultimoId;

	        String sqlSelectOrig = "SELECT * FROM POKEMON WHERE ID_POKEMON = ?";
	        try (PreparedStatement psSelect = con.prepareStatement(sqlSelectOrig)) {
	            psSelect.setInt(1, idOld);
	            try (ResultSet rsOri = psSelect.executeQuery()) {
	                if (rsOri.next()) {
	                    try (PreparedStatement psPk = con.prepareStatement(sqlInsertPk)) {
	                        psPk.setInt(1, idNuevo);
	                        psPk.setInt(2, rsOri.getInt("NUM_POKEDEX"));
	                        psPk.setInt(3, idCampeon);
	                        psPk.setString(4, rsOri.getString("MOTE") + " (Sombra)");
	                        psPk.setInt(5, rsOri.getInt("VITALIDAD")); 
	                        psPk.setInt(6, rsOri.getInt("VITALIDAD_MAX")); 
	                        psPk.setInt(7, rsOri.getInt("ATAQUE"));
	                        psPk.setInt(8, rsOri.getInt("ATA_ESPECIAL"));
	                        psPk.setInt(9, rsOri.getInt("DEFENSA"));
	                        psPk.setInt(10, rsOri.getInt("DEF_ESPECIAL"));
	                        psPk.setInt(11, rsOri.getInt("VELOCIDAD"));
	                        psPk.setInt(12, rsOri.getInt("NIVEL"));
	                        psPk.setInt(13, rsOri.getInt("FERTILIDAD"));
	                        psPk.setString(14, rsOri.getString("SEXO"));
	                        psPk.setString(15, rsOri.getString("ESTADO"));
	                        
	                        // ASIGNACIÓN DE CAJA/UBICACIÓN (Parámetro 16)
	                        psPk.setString(16, String.valueOf(contadorCaja));
	                        
	                        psPk.executeUpdate();
	                        contadorCaja++; // Incrementamos para el siguiente Pokémon
	                    }

	                    // Clonar movimientos
	                    try (PreparedStatement psMov = con.prepareStatement(sqlInsertMov)) {
	                        psMov.setInt(1, idNuevo);
	                        psMov.setInt(2, idOld);
	                        psMov.executeUpdate();
	                    }
	                }
	            }
	        }
	    }
	}
}

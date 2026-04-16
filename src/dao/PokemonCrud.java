package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.LinkedList;

import modelo.Entrenador;
import modelo.Movimiento;
import modelo.Pokemon;
import modelo.Sexo;
import modelo.Tipo;

public class PokemonCrud {

	
	
	
	public static void guardarPokemon(Connection con, int numPokedex, Entrenador ent, String mote) throws SQLException {
		
		//Este es el insert para meter un pokemon en la tabla
		String sqlInsert = "INSERT INTO POKEMON (\r\n"
				+ "    ID_POKEMON, \r\n"
				+ "    NUM_POKEDEX, \r\n"
				+ "    ID_ENTRENADOR, \r\n"
				+ "    MOTE, \r\n"
				+ "    VITALIDAD, \r\n"
				+ "    ATAQUE, \r\n"
				+ "    ATA_ESPECIAL, \r\n"
				+ "    DEFENSA, \r\n"
				+ "    DEF_ESPECIAL, \r\n"
				+ "    VELOCIDAD, \r\n"
				+ "    NIVEL, \r\n"
				+ "    EXPERIENCIA, \r\n"
				+ "    FERTILIDAD, \r\n"
				+ "    SEXO, \r\n"
				+ "    ESTADO, \r\n"
				+ "    CAJA, \r\n"
				+ "    ID_OBJETO\r\n"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		String sqlSelect = "SELECT NOM_POKEMON,\r\n"
				+ "	TIPO1,\r\n"
				+ "	TIPO2\r\n"
				+ "FROM POKEDEX\r\n"
				+ "WHERE NUM_POKEDEX=?";
		
		String sqlMaxIdPokemon = "SELECT MAX(ID_POKEMON) FROM POKEMON";
		PreparedStatement psMax = con.prepareStatement(sqlMaxIdPokemon);
	    ResultSet rsMax = psMax.executeQuery();
	    int nuevoId = 1;
	    if (rsMax.next()) {
	        nuevoId = rsMax.getInt(1) + 1;
	    }
		
	    String sqlMaxCajaEntrenador = "SELECT MAX(CAJA) FROM POKEMON WHERE ID_ENTRENADOR = ?";
	    PreparedStatement psCaja = con.prepareStatement(sqlMaxCajaEntrenador);
	    psCaja.setInt(1, ent.getIdEntrenador()); // Filtramos por el ID del entrenador
	    ResultSet rsCaja = psCaja.executeQuery();
	    int proximaPosicion = 1; // Si es su primer pokemon, irá a la posicion 1
	    if (rsCaja.next()) {
	    	proximaPosicion = rsCaja.getInt(1) + 1;
	    }
	    psCaja.close();
	    
	    
		//Ya le estoy pasando una conexion, no hace falta darle una
		//ConexionBD con = new ConexionBD();
		//Connection conexion = con.getConnection();
		
		PreparedStatement psSel = con.prepareStatement(sqlSelect);
		psSel.setInt(1, numPokedex);
		ResultSet rs = psSel.executeQuery();
		if(rs.next()) {
			PreparedStatement psIns = con.prepareStatement(sqlInsert);
			psIns.setInt(1, nuevoId);
			psIns.setInt(2, numPokedex);
			psIns.setInt(3, ent.getIdEntrenador());
			psIns.setString(4, mote);
			psIns.setInt(5, (int)(Math.random() * 5) + 1); 
			psIns.setInt(6, (int)(Math.random() * 5) + 1);
			psIns.setInt(7, (int)(Math.random() * 5) + 1); 
			psIns.setInt(8, (int)(Math.random() * 5) + 1); 
			psIns.setInt(9, (int)(Math.random() * 5) + 1); 
			psIns.setInt(10, (int)(Math.random() * 5) + 1); 
			psIns.setInt(11, 1);
			psIns.setInt(12, 0);
			psIns.setInt(13, 5);
			psIns.setString(14, (Math.random() < 0.5) ? Sexo.M.toString() : Sexo.H.toString());
			psIns.setString(15, "VIVO");
			psIns.setInt(16, proximaPosicion);
			psIns.setNull(17, java.sql.Types.INTEGER);
			
			psIns.executeUpdate();
		}
		
	}
	 
	public static void obtenerPokemon(Connection conexion, Entrenador e, int caja) throws SQLException {
		String sql = "SELECT P.ID_POKEMON,\r\n"
				+ "		PX.NUM_POKEDEX,\r\n"
				+ "		PX.NOM_POKEMON,\r\n"
				+ "		P.MOTE,\r\n"
				+ "		P.VITALIDAD,\r\n"
				+ "		P.ATAQUE,\r\n"
				+ "		P.ATA_ESPECIAL,\r\n"
				+ "		P.DEFENSA,\r\n"
				+ "		P.DEF_ESPECIAL,\r\n"
				+ "		P.VELOCIDAD,\r\n"
				+ "		P.NIVEL,\r\n"
				+ "		P.FERTILIDAD,\r\n"
				+ "		P.SEXO,\r\n"
				+ "		P.ESTADO,\r\n"
				+ "		P.UBICACION,\r\n"
				+ "		P.ID_OBJETO,\r\n"
				+ "		P.EXPERIENCIA,\r\n"
				+ "		PX.TIPO1,\r\n"
				+ "		PX.TIPO2,\r\n"
				+ "		PX.IMG_FRONTAL,\r\n"
				+ "		PX.IMG_BACK,\r\n"
				+ "		PX.NIVEL_EVO\r\n"
				+ "FROM POKEMON P\r\n"
				+ "INNER JOIN POKEDEX PX\r\n"
				+ "	ON PX.NUM_POKEDEX=P.NUM_POKEDEX\r\n"
				+ "WHERE ID_ENTRENADOR = ?\r\n"
				+ "	AND P.CAJA=?";
		
		PreparedStatement ps = conexion.prepareStatement(sql);
		ps.setInt(1, e.getClaseEntrenador());
		ps.setInt(2, caja);
		
		ResultSet rs = ps.executeQuery();
		
		LinkedList<Pokemon> listadoPokemon = new LinkedList<Pokemon>();
		Pokemon p;
		while (rs.next()) {
			p = new Pokemon();
			p.setId_pokemon(rs.getInt("ID_POKEMON"));
			
		}
	}
	
	public static void obtenerPokemon1(Connection conexion, Entrenador e) throws SQLException {
		String sql = "SELECT P.ID_POKEMON, PX.NUM_POKEDEX, PX.NOM_POKEMON, P.MOTE, P.VITALIDAD, "
	            + "P.ATAQUE, P.ATA_ESPECIAL, P.DEFENSA, P.DEF_ESPECIAL, P.VELOCIDAD, "
	            + "P.NIVEL, P.FERTILIDAD, P.SEXO, P.ESTADO, P.CAJA, P.ID_OBJETO, "
	            + "PX.TIPO1, PX.TIPO2 "
	            + "FROM POKEMON P "
	            + "INNER JOIN POKEDEX PX ON PX.NUM_POKEDEX = P.NUM_POKEDEX "
	            + "WHERE P.ID_ENTRENADOR = ? AND P.CAJA BETWEEN 1 AND 6 "
	            + "ORDER BY P.CAJA ASC";

	    String sqlMovs = "SELECT M.ID_MOVIMIENTO, M.NOMBRE, PM.NUM_PP, PM.ACTIVO "
	            + "FROM POKEMON_MOVIMIENTO PM "
	            + "INNER JOIN MOVIMIENTO M ON PM.ID_MOVIMIENTO = M.ID_MOVIMIENTO "
	            + "WHERE PM.ID_POKEMON = ?";

	    PreparedStatement ps = conexion.prepareStatement(sql);
	    ps.setInt(1, 1); 
	    
	    
	    ResultSet rs = ps.executeQuery();
	    LinkedList<Pokemon> listadoPokemon = new LinkedList<Pokemon>();

	    while (rs.next()) {
	        Pokemon p = new Pokemon();
	        int idPkmn = rs.getInt("ID_POKEMON");

	        // Seteo de datos básicos
	        p.setId_pokemon(idPkmn);
	        p.setNombre(rs.getString("NOM_POKEMON"));
	        p.setMote(rs.getString("MOTE"));
	        p.setVitalidad(rs.getInt("VITALIDAD"));
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
	            p.setTipo2(Tipo.valueOf(rs.getString("TIPO2").toUpperCase()));
	        }

	        // --- CARGA DE MOVIMIENTOS ---
	        LinkedList<Movimiento> listaMovs = new LinkedList<Movimiento>();
	        PreparedStatement psMov = conexion.prepareStatement(sqlMovs);
	        psMov.setInt(1, idPkmn);
	        ResultSet rsMov = psMov.executeQuery();

	        while (rsMov.next()) {
	            Movimiento m = new Movimiento();
	            m.setIdMovimiento(rsMov.getInt("ID_MOVIMIENTO"));
	            m.setNombre(rsMov.getString("NOMBRE"));
	            m.setNumPP(rsMov.getInt("NUM_PP"));
	            //m.setActivo(rsMov.getInt("ACTIVO") == 1);
	            listaMovs.add(m);
	        }
	        
	        p.setMovimientos(listaMovs);
	        listadoPokemon.add(p);
	        
	        rsMov.close();
	        psMov.close();
	    }

	    // Guardar la lista en el entrenador (ajusta según el nombre de tu setter)
	    e.setEquipo1(listadoPokemon); 

	    rs.close();
	    ps.close();
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
	
	public static void sacarDatosPokedex(Connection con, int numeroPokedex) {
		
	}
}

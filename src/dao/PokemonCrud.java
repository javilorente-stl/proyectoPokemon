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

	    String sqlMaxIdPokemon = "SELECT MAX(ID_POKEMON) FROM POKEMON";
	    PreparedStatement psMax = con.prepareStatement(sqlMaxIdPokemon);
	    ResultSet rsMax = psMax.executeQuery();
	    int nuevoId = 1;
	    if (rsMax.next()) {
	        nuevoId = rsMax.getInt(1) + 1;
	    }
	    psMax.close();

	    String sqlMaxCajaEntrenador = "SELECT MAX(CAJA) FROM POKEMON WHERE ID_ENTRENADOR = ?";
	    PreparedStatement psCaja = con.prepareStatement(sqlMaxCajaEntrenador);
	    psCaja.setInt(1, ent.getIdEntrenador());
	    ResultSet rsCaja = psCaja.executeQuery();
	    int proximaPosicion = 1;
	    if (rsCaja.next()) {
	        proximaPosicion = rsCaja.getInt(1) + 1;
	    }
	    psCaja.close();

	    PreparedStatement psSel = con.prepareStatement(sqlSelect);
	    psSel.setInt(1, numPokedex);
	    ResultSet rs = psSel.executeQuery();
	    
	    if (rs.next()) {
	        int vitalidadAleatoria = (int) (Math.random() * 10) + 20; //
	        
	        PreparedStatement psIns = con.prepareStatement(sqlInsert);
	        psIns.setInt(1, nuevoId);
	        psIns.setInt(2, numPokedex);
	        psIns.setInt(3, ent.getIdEntrenador());
	        psIns.setString(4, mote);
	        psIns.setInt(5, vitalidadAleatoria);     // VITALIDAD (actual)
	        psIns.setInt(6, vitalidadAleatoria);     // VITALIDAD_MAX
	        psIns.setInt(7, (int) (Math.random() * 5) + 1); // ATAQUE
	        psIns.setInt(8, (int) (Math.random() * 5) + 1); // ATA_ESPECIAL
	        psIns.setInt(9, (int) (Math.random() * 5) + 1); // DEFENSA
	        psIns.setInt(10, (int) (Math.random() * 5) + 1); // DEF_ESPECIAL
	        psIns.setInt(11, (int) (Math.random() * 5) + 1); // VELOCIDAD
	        psIns.setInt(12, 1); // NIVEL
	        psIns.setInt(13, 0); // EXPERIENCIA
	        psIns.setInt(14, 5); // FERTILIDAD
	        psIns.setString(15, (Math.random() < 0.5) ? "M" : "H");
	        psIns.setString(16, "VIVO");
	        psIns.setInt(17, proximaPosicion);
	        psIns.setNull(18, java.sql.Types.INTEGER);

	        psIns.executeUpdate();
	        psIns.close();
	    }
	    rs.close();
	    psSel.close();
	}
	/*
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
	}*/
	
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

	    String sqlMovs = "SELECT M.ID_MOVIMIENTO, M.NOM_MOVIMIENTO, PM.NUM_PP, PM.ACTIVO "
	            + "FROM POKEMON_MOVIMIENTO PM "
	            + "INNER JOIN MOVIMIENTO M ON PM.ID_MOVIMIENTO = M.ID_MOVIMIENTO "
	            + "WHERE PM.ID_POKEMON = ?";

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

	        // --- CARGA DE MOVIMIENTOS ---
	        LinkedList<Movimiento> listaMovs = new LinkedList<Movimiento>();
	        PreparedStatement psMov = conexion.prepareStatement(sqlMovs);
	        psMov.setInt(1, idPkmn);
	        ResultSet rsMov = psMov.executeQuery();

	        while (rsMov.next()) {
	            Movimiento m = new Movimiento();
	            m.setIdMovimiento(rsMov.getInt("ID_MOVIMIENTO"));
	            m.setNombre(rsMov.getString("NOM_MOVIMIENTO"));
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
	
	public static void sacarDatosPokedex(Connection con, int numeroPokedex) {
		
	}
}

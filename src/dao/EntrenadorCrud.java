package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
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
		    // 1. Nos aseguramos de que el entrenador tenga una mochila instanciada
		    if (e.getMochila() == null) {
		        e.setMochila(new LinkedList<Objeto>());
		    }
		    
		    // 2. Inicializamos o limpiamos la LinkedList de objetos
		    // (Asegúrate de que en la clase Mochila, el atributo sea LinkedList<Objeto>)
		    LinkedList<Objeto> listaObjetos = new LinkedList<>();

		    // 3. Consulta SQL con JOIN para obtener los datos base y la cantidad actual
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
}

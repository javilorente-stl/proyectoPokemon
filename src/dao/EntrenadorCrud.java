package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import modelo.Entrenador;

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
	
}

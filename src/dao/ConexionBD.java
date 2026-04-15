package dao;

import java.sql.Connection; // IMPORTANTE: Que sea java.sql
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

	public Connection dataBaseLink;
	
	public Connection getConnection() {
	String url = "jdbc:mysql://localhost:3307/pokemon";
	String login = "root";
	String password = "";

	// Método para obtener la conexión
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			dataBaseLink = DriverManager.getConnection(url, login, password);
			//System.out.println("Conexión establecida");

		} catch (ClassNotFoundException e) {
			System.out.println("Error: No se encontró el Driver de MySQL");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error: No se pudo conectar a la base de datos");
			e.printStackTrace();
		}
		return dataBaseLink;

	/*
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Conexión cerrada");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}
	//No va aqui
	public static void printSQLException(SQLException ex) {
		ex.printStackTrace(System.err);
		System.err.println("SQLState: " + ex.getSQLState());
		System.err.println("Error code: " + ex.getErrorCode());
		System.err.println("Message: " + ex.getMessage());
		Throwable t = ex.getCause();
		while (t != null) {
			System.out.println("Cause: " + t);
			t = t.getCause();
		}
	}

}




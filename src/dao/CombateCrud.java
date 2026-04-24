package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import dao.ConexionBD;
import modelo.Movimiento;
import modelo.Pokemon;

public class CombateCrud {
	
	private ConexionBD conexionBD = new ConexionBD();

	public void actualizarEstadoVital(Connection con, Pokemon p) throws SQLException {
	    // Añadimos NIVEL y EXPERIENCIA a la consulta SQL
	    String sql = "UPDATE POKEMON SET VITALIDAD = ?, ESTADO = ?, NIVEL = ?, EXPERIENCIA = ? WHERE ID_POKEMON = ?";
	    
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        // --- LIMPIEZA Y SEGURIDAD ---
	        int vida = Math.max(0, p.getVitalidad());
	        
	        ps.setInt(1, vida);
	        ps.setString(2, p.getEstado().name()); 
	        ps.setInt(3, p.getNivel());      // <--- NUEVO
	        ps.setInt(4, p.getExperiencia()); // <--- NUEVO
	        ps.setInt(5, p.getId_pokemon());
	        
	        ps.executeUpdate();
	    }
	}

    /**
     * Actualiza los PP actuales en la tabla relacional pokemon_movimiento
     */
	public void actualizarPP(Connection con, Pokemon p) throws SQLException {
	    String sql = "UPDATE POKEMON_MOVIMIENTO SET NUM_PP = ? WHERE ID_POKEMON = ? AND ID_MOVIMIENTO = ?";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        for (Movimiento mov : p.getMovimientos()) {
	            // SEGURIDAD: Los PP nunca pueden ser menores a 0
	            int ppSeguros = Math.max(0, mov.getNumPP());
	            
	            ps.setInt(1, ppSeguros);
	            ps.setInt(2, p.getId_pokemon());
	            ps.setInt(3, mov.getIdMovimiento());
	            ps.addBatch();
	        }
	        ps.executeBatch();
	    }
	}

    /**
     * Método principal para persistir todos los cambios tras el combate
     */
    public void guardarProgresoCombate(Pokemon pJugador) {
        // Asumiendo que tienes una clase para gestionar la conexión
        try (Connection con = conexionBD.getConnection()) {
            con.setAutoCommit(false); // Iniciamos transacción por seguridad

            try {
                // 1. Guardar Vida y Estado del Pokémon
                actualizarEstadoVital(con, pJugador);

                // 2. Guardar los PP restantes de sus movimientos
                actualizarPP(con, pJugador);

                con.commit();
                System.out.println("Datos guardados: Vitalidad, Estado y NUM_PP actualizados.");
            } catch (SQLException e) {
                con.rollback(); // Si algo falla, no se guarda nada
                System.err.println("Error en la transacción: " + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

	
}

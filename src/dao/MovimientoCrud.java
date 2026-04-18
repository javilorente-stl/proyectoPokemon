package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import modelo.Estado;
import modelo.Movimiento;
import modelo.Pokemon;
import modelo.Tipo;

public class MovimientoCrud {

	public Movimiento obtenerMovimientoPorId(Connection con, int id) {
        Movimiento mov = null;
        String sql = "SELECT * FROM MOVIMIENTO WHERE ID_MOVIMIENTO = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Asignamos el ID al parámetro de la consulta
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mov = new Movimiento();
                    
                   
                    mov.setIdMovimiento(rs.getInt("ID_MOVIMIENTO"));
                    mov.setNombre(rs.getString("NOM_MOVIMIENTO"));
                    String tipoBD = rs.getString("TIPO");
                    if (tipoBD != null) {
                        mov.setTipo(Tipo.valueOf(tipoBD.toUpperCase()));
                    }
                    
                    String estadoBD = rs.getString("ESTADO");
                    if (estadoBD != null) {
                        mov.setEstado(Estado.valueOf(estadoBD.toUpperCase()));
                    } else {
                        mov.setEstado(null); // Para movimientos que no causan estado
                    }
                    
                    mov.setPotencia((Integer) rs.getObject("POTENCIA"));
                    mov.setNumTurnos((Integer) rs.getObject("NUM_TURNOS"));
                    mov.setMejora(rs.getString("MEJORA"));
                    mov.setDescMovimiento(rs.getString("DESC_MOVIMIENTO"));
                    mov.setClaseMov(rs.getInt("CLASE_MOVIMIENTO"));
                    mov.setNumPP((Integer) rs.getObject("PP"));
                    mov.setPrecision((Integer) rs.getObject("PRECISION_MOV"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el movimiento " + id + ": " + e.getMessage());
        }

        return mov;
    }
	
	public static Movimiento obtenerMovimientoInicial(Connection con, Tipo tipoPrincipal) throws SQLException {
	    Movimiento mov = null;
	    
	    // Buscamos solo del tipo principal, con potencia real, y ordenamos por la más baja
	    String sql = "SELECT * FROM MOVIMIENTO " +
	                 "WHERE TIPO = ? AND POTENCIA > 0 " +
	                 "ORDER BY POTENCIA ASC " +
	                 "LIMIT 1";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, tipoPrincipal.name());

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                mov = new Movimiento();
	                
	                // Datos esenciales para la vinculación y el objeto
	                mov.setIdMovimiento(rs.getInt("ID_MOVIMIENTO"));
	                mov.setNombre(rs.getString("NOM_MOVIMIENTO"));
	                mov.setNumPP(rs.getInt("PP"));
	                
	                // Mapeo del Tipo
	                String tipoBD = rs.getString("TIPO");
	                if (tipoBD != null) {
	                	mov.setTipo(Tipo.valueOf(tipoBD.toUpperCase()));
	                }

	                // Datos de combate (con control de nulos por seguridad)
	                Object potenciaObj = rs.getObject("POTENCIA");
	                mov.setPotencia(potenciaObj != null ? (Integer) potenciaObj : 0);

	                Object precisionObj = rs.getObject("PRECISION_MOV");
	                mov.setPrecision(precisionObj != null ? (Integer) precisionObj : 100);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al obtener el movimiento inicial: " + e.getMessage());
	        throw e;
	    }

	    return mov;
	}
	
}

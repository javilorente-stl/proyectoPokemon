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
	    
	    // Buscamos un movimiento del tipo del Pokémon que haga daño (Potencia > 0)
	    // Ordenamos por potencia de forma ascendente para que sea un ataque "básico"
	    String sql = "SELECT * FROM MOVIMIENTO " +
	                 "WHERE TIPO = ? AND POTENCIA > 0 " +
	                 "ORDER BY POTENCIA ASC " +
	                 "LIMIT 1";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, tipoPrincipal.name());

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                mov = new Movimiento();
	                
	                // 1. Identificadores básicos
	                mov.setIdMovimiento(rs.getInt("ID_MOVIMIENTO"));
	                mov.setNombre(rs.getString("NOM_MOVIMIENTO"));
	                mov.setDescMovimiento(rs.getString("DESC_MOVIMIENTO"));
	                
	                // 2. Lógica de Combate (LO QUE FALTABA)
	                // Usamos CLASE_MOV que es el nombre real en tu BD
	                mov.setClaseMov(rs.getInt("CLASE_MOVIMIENTO")); 
	                mov.setMejora(rs.getString("MEJORA"));
	                
	                // 3. Atributos numéricos con control de nulos
	                Object potObj = rs.getObject("POTENCIA");
	                mov.setPotencia(potObj != null ? (Integer) potObj : 0);

	                Object precObj = rs.getObject("PRECISION_MOV");
	                mov.setPrecision(precObj != null ? (Integer) precObj : 100);

	                mov.setNumPP(rs.getInt("PP"));
	                
	                // 4. Mapeo de Enums (Tipo y Estado)
	                String tipoBD = rs.getString("TIPO");
	                if (tipoBD != null) {
	                    mov.setTipo(Tipo.valueOf(tipoBD.toUpperCase()));
	                }

	                String estadoBD = rs.getString("ESTADO");
	                if (estadoBD != null) {
	                    mov.setEstado(Estado.valueOf(estadoBD.toUpperCase()));
	                }

	                // 5. Otros datos
	                mov.setNumTurnos(rs.getInt("NUM_TURNOS"));
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al obtener el movimiento inicial para tipo " + tipoPrincipal + ": " + e.getMessage());
	        throw e;
	    }

	    return mov;
	}
	
}

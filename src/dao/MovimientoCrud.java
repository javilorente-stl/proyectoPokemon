package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.Estado;
import modelo.Movimiento;
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
	
}

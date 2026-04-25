package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

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
	
	public static void buscarMovimientosPosibles(Connection con, Pokemon pk) throws SQLException {
	    pk.getMovimientosPosibles().clear();

	    // 1. Construimos la cláusula WHERE para los tipos
	    // Un Pokémon siempre tiene tipo1, pero tipo2 puede ser null
	    String sqlTipos = "(TIPO = ? OR TIPO = 'NORMAL'";
	    if (pk.getTipo2() != null) {
	        sqlTipos += " OR TIPO = ?";
	    }
	    sqlTipos += ")";

	    // 2. Construimos la cláusula para evitar duplicados
	    String sqlNoRepetidos = "";
	    if (!pk.getMovimientos().isEmpty()) {
	        sqlNoRepetidos = " AND ID_MOVIMIENTO NOT IN (";
	        for (int i = 0; i < pk.getMovimientos().size(); i++) {
	            sqlNoRepetidos += (i == 0 ? "?" : ", ?");
	        }
	        sqlNoRepetidos += ")";
	    }

	    String sqlFinal = "SELECT * FROM MOVIMIENTO WHERE " + sqlTipos + sqlNoRepetidos;

	    try (PreparedStatement ps = con.prepareStatement(sqlFinal)) {
	        int paramIndex = 1;
	        
	        // Parámetro Tipo 1
	        ps.setString(paramIndex++, pk.getTipo1().name());
	        
	        // Parámetro Tipo 2 (si existe)
	        if (pk.getTipo2() != null) {
	            ps.setString(paramIndex++, pk.getTipo2().name());
	        }

	        // Parámetros de movimientos ya conocidos para el NOT IN
	        for (Movimiento m : pk.getMovimientos()) {
	            ps.setInt(paramIndex++, m.getIdMovimiento());
	        }

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Movimiento mov = new Movimiento();
	                
	                // Mapeo de datos (ID, Nombre, Potencia, etc.)
	                mov.setIdMovimiento(rs.getInt("ID_MOVIMIENTO"));
	                mov.setNombre(rs.getString("NOM_MOVIMIENTO"));
	                mov.setDescMovimiento(rs.getString("DESC_MOVIMIENTO"));
	                mov.setClaseMov(rs.getInt("CLASE_MOVIMIENTO"));
	                
	                Object potObj = rs.getObject("POTENCIA");
	                mov.setPotencia(potObj != null ? (Integer) potObj : 0);
	                
	                Object precObj = rs.getObject("PRECISION_MOV");
	                mov.setPrecision(precObj != null ? (Integer) precObj : 100);
	                
	                mov.setNumPP(rs.getInt("PP"));
	                
	                String tipoBD = rs.getString("TIPO");
	                if (tipoBD != null) mov.setTipo(Tipo.valueOf(tipoBD.toUpperCase()));
	                
	                String estadoBD = rs.getString("ESTADO");
	                if (estadoBD != null) mov.setEstado(Estado.valueOf(estadoBD.toUpperCase()));

	                mov.setNumTurnos(rs.getInt("NUM_TURNOS"));

	                pk.getMovimientosPosibles().add(mov);
	            }
	        }
	    }
	}
	
	
	public static void gestionarAprendizajeUI(Connection con, Pokemon pk) throws SQLException {
	    LinkedList<Movimiento> posibles = pk.getMovimientosPosibles();
	    if (posibles.isEmpty()) return;

	    // 1. Selección ALEATORIA del nuevo movimiento
	    int r = (int) (Math.random() * posibles.size());
	    Movimiento nuevo = posibles.get(r);

	    if (pk.getMovimientos().size() < 4) {
	        // ESPACIO LIBRE: Insertamos directamente
	        pk.getMovimientos().add(nuevo);
	        pk.getMovimientosPosibles().remove(nuevo);
	        
	        guardarMovimientoEnBD(con, pk.getId_pokemon(), nuevo);
	        
	        JOptionPane.showMessageDialog(null, "¡" + pk.getNombre() + " aprendió " + nuevo.getNombre() + "!");
	        
	    } else {
	        // LISTA LLENA: El usuario debe elegir cuál borrar
	        JOptionPane.showMessageDialog(null, "¡" + pk.getNombre() + " quiere aprender " + nuevo.getNombre() + "!");
	        
	        Movimiento aOlvidar = (Movimiento) JOptionPane.showInputDialog(
	                null,
	                "Selecciona el movimiento que quieres eliminar para siempre:",
	                "Sustituir Movimiento",
	                JOptionPane.WARNING_MESSAGE,
	                null,
	                pk.getMovimientos().toArray(),
	                pk.getMovimientos().get(0)
	        );

	        if (aOlvidar != null) {
	            // A. Borramos el antiguo de la BD para evitar errores de PK/Unique
	            eliminarMovimientoDeBD(con, pk.getId_pokemon(), aOlvidar.getIdMovimiento());
	            
	            // B. Insertamos el nuevo en la BD
	            guardarMovimientoEnBD(con, pk.getId_pokemon(), nuevo);
	            
	            // C. Actualizamos las listas en la memoria de Java
	            int index = pk.getMovimientos().indexOf(aOlvidar);
	            pk.getMovimientos().set(index, nuevo);
	            pk.getMovimientosPosibles().remove(nuevo);
	            pk.getMovimientosPosibles().add(aOlvidar); // Opcional: el olvidado vuelve a "posibles"
	            
	            JOptionPane.showMessageDialog(null, "¡" + aOlvidar.getNombre() + " ha sido eliminado y " + 
	                nuevo.getNombre() + " guardado con éxito!");
	        }
	    }
	}
	
	public static void guardarMovimientoEnBD(Connection con, int idPokemon, Movimiento mov) throws SQLException {
	    // Usamos la estructura que mencionas
	    String sql = "INSERT INTO POKEMON_MOVIMIENTO (ID_POKEMON, ID_MOVIMIENTO, ACTIVO, NUM_PP) VALUES (?, ?, ?, ?)";
	    
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, idPokemon);
	        ps.setInt(2, mov.getIdMovimiento());
	        ps.setInt(3, 1); // 1 para ACTIVO (true)
	        ps.setInt(4, mov.getNumPP());
	        
	        ps.executeUpdate();
	    }
	}
	
	public static void eliminarMovimientoDeBD(Connection con, int idPokemon, int idMovimiento) throws SQLException {
	    String sql = "DELETE FROM POKEMON_MOVIMIENTO WHERE ID_POKEMON = ? AND ID_MOVIMIENTO = ?";
	    
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, idPokemon);
	        ps.setInt(2, idMovimiento);
	        ps.executeUpdate();
	    }
	}
	
}

package controller;

import java.sql.Connection;
import java.sql.SQLException;

import dao.ConexionBD;
import dao.PokemonCrud;
import javafx.animation.FadeTransition;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Entrenador;
import modelo.Pokemon;

public class CombateController {

	private MenuController menuController;
	private Stage stage;
	public Entrenador e;
	private boolean sonido=false;
	private MediaPlayer mediaPlayer;
	private FadeTransition animacionActiva;
	private int posicionOrigen = -1;
	private boolean modoMover = false;
	ConexionBD conBD = new ConexionBD();
	private Pokemon pokemonSeleccionado1;
	
	
	
	
	
	
	
	
	
	/*public void procesarSubidaNivel(Pokemon p) {
	    int nivelAnterior = p.getNivel();
	    
	    // 1. Usas tu método del modelo para alterar el objeto en memoria
	    // (Asegúrate de quitar el Scanner si estás en JavaFX, usa diálogos)
	    p.subirNivel(p, null); 

	    // 2. Si el nivel cambió, guardamos en la BD
	    if (p.getNivel() > nivelAnterior) {
	        try (Connection con = conBD.getConnection()) {
	            PokemonCrud.actualizarNivelStats(con, p);
	            System.out.println("Base de datos actualizada con el nuevo nivel.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}    */
}

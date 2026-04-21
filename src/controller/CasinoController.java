package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import dao.ConexionBD;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Entrenador;
import modelo.Pokemon;

public class CasinoController {

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
	
    @FXML
    private Button botonVolver;

    @FXML
    private ImageView imgAdivinarNumero;

    @FXML
    private ImageView imgCaraCruz;

    @FXML
    private ImageView imgRuleta;

    @FXML
    private Label lblPokedollars;

    @FXML
    void jugarAdivinarNumero(MouseEvent event) {

    }

    @FXML
    void jugarCaraCruz(MouseEvent event) {

    	String input = JOptionPane.showInputDialog(null, 
    	        "¿Cuánto quieres apostar?\n(Tienes: " + e.getPokedollars() + ")", 
    	        "Apuesta", 
    	        JOptionPane.QUESTION_MESSAGE);

    	    // Si el usuario cancela o no escribe nada, salimos
    	    if (input == null || input.isEmpty()) return;

    	    try {
    	        int apuesta = Integer.parseInt(input);

    	        // 2. Validaciones de la apuesta
    	        if (apuesta <= 0) {
    	            JOptionPane.showMessageDialog(null, "La apuesta debe ser mayor que 0.");
    	            return;
    	        }
    	        if (apuesta > e.getPokedollars()) {
    	            JOptionPane.showMessageDialog(null, "No tienes suficiente dinero.");
    	            return;
    	        }

    	        // 3. Elegir Cara o Cruz
    	        Object[] opciones = {"Cara", "Cruz", "Cancelar"};
    	        int seleccion = JOptionPane.showOptionDialog(null,
    	            "Has apostado " + apuesta + ". ¿A qué bando vas?",
    	            "Cara o Cruz",
    	            JOptionPane.DEFAULT_OPTION,
    	            JOptionPane.QUESTION_MESSAGE,
    	            null, opciones, opciones[0]);

    	        if (seleccion == 0 || seleccion == 1) {
    	            String eleccionUsuario = (seleccion == 0) ? "Cara" : "Cruz";
    	            
    	            // 4. EJECUTAR EL JUEGO
    	            String resultado = (Math.random() < 0.5) ? "Cara" : "Cruz";
    	            
    	            if (eleccionUsuario.equals(resultado)) {
    	                e.setPokedollars(e.getPokedollars() + apuesta);
    	                JOptionPane.showMessageDialog(null, "¡Salió " + resultado + "! Ganaste " + apuesta + ".");
    	            } else {
    	                e.setPokedollars(e.getPokedollars() - apuesta);
    	                JOptionPane.showMessageDialog(null, "¡Salió " + resultado + "! Perdiste " + apuesta + ".");
    	            }

    	            // 5. ACTUALIZAR INTERFAZ Y BD
    	            lblPokedollars.setText(String.valueOf(e.getPokedollars()));
    	            actualizarDineroEnBD(e.getPokedollars()); 

    	        }

    	    } catch (NumberFormatException ex) {
    	        JOptionPane.showMessageDialog(null, "Por favor, introduce un número válido.");
    	    }
    }
    

    @FXML
    void jugarRuleta(MouseEvent event) {

    }

    @FXML
    void volver(ActionEvent event) {

    }

    public void recibirDatos(Entrenador ent) {
        this.e = ent;
        // Ejemplo: Actualizar el dinero visualmente al entrar al casino
        lblPokedollars.setText(String.valueOf(e.getPokedollars()));
    }
    private void ejecutarJuego(String eleccionUsuario) {
        // Verificamos si tiene dinero (ejemplo: 100 por apuesta)
        if (this.e.getPokedollars() < 100) {
            JOptionPane.showMessageDialog(null, "No tienes suficientes Pokedollars.");
            return;
        }

        // Cobramos la apuesta
        this.e.setPokedollars(this.e.getPokedollars() - 100);
        
        // Lanzamiento aleatorio
        String resultado = (Math.random() < 0.5) ? "Cara" : "Cruz";
        
        if (eleccionUsuario.equals(resultado)) {
            // Gana el doble
            this.e.setPokedollars(this.e.getPokedollars() + 200);
            JOptionPane.showMessageDialog(null, "¡Salió " + resultado + "! Has ganado 200 Pokedollars.");
        } else {
            JOptionPane.showMessageDialog(null, "¡Salió " + resultado + "! Has perdido la apuesta.");
        }

        // ACTUALIZAMOS LA INTERFAZ
        lblPokedollars.setText(String.valueOf(e.getPokedollars()));
        
        // IMPORTANTE: Deberías llamar a un método de PokemonCrud para guardar el dinero en la BD
        // PokemonCrud.actualizarDinero(e); 
    }
    
    private void actualizarDineroEnBD(int nuevoDinero) {
        String sql = "UPDATE ENTRENADOR SET POKEDOLLARS = ? WHERE ID_ENTRENADOR = ?";
        try (Connection con = conBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevoDinero);
            ps.setInt(2, e.getIdEntrenador());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
}

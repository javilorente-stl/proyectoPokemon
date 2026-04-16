package controller;

	import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

import dao.ConexionBD;
import dao.PokemonCrud;
import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.control.ProgressBar;
	import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Entrenador;
import modelo.Movimiento;
import modelo.Pokemon;

public class EquipoController {
		
	private MenuController menuController;
	private Stage stage;
	public Entrenador e;
	private boolean sonido=false;
	private MediaPlayer mediaPlayer;
	
	
	    @FXML
	    private Label LblNivel1;

	    @FXML
	    private Label LblNivel2;

	    @FXML
	    private Label LblNivel3;

	    @FXML
	    private Label LblNivel4;

	    @FXML
	    private Label LblNivel5;

	    @FXML
	    private Label LblNivel6;

	    @FXML
	    private Label LblPokemon1;

	    @FXML
	    private Label LblPokemon2;

	    @FXML
	    private Label LblPokemon3;

	    @FXML
	    private Label LblPokemon4;

	    @FXML
	    private Label LblPokemon5;

	    @FXML
	    private Label LblPokemon6;

	    @FXML
	    private Label LblVida1;

	    @FXML
	    private Label LblVida2;

	    @FXML
	    private Label LblVida3;

	    @FXML
	    private Label LblVida4;

	    @FXML
	    private Label LblVida5;

	    @FXML
	    private Label LblVida6;

	    @FXML
	    private ProgressBar ataqueBar;

	    @FXML
	    private ProgressBar ataqueSpBar;

	    @FXML
	    private ProgressBar barPokemon1;

	    @FXML
	    private ProgressBar barPokemon2;

	    @FXML
	    private ProgressBar barPokemon3;

	    @FXML
	    private ProgressBar barPokemon4;

	    @FXML
	    private ProgressBar barPokemon5;

	    @FXML
	    private ProgressBar barPokemon6;

	    @FXML
	    private Button botonCaja;

	    @FXML
	    private Button botonMover1;

	    @FXML
	    private Button botonMover2;

	    @FXML
	    private Button botonMover3;

	    @FXML
	    private Button botonMover4;

	    @FXML
	    private Button botonMover5;

	    @FXML
	    private Button botonMover6;

	    @FXML
	    private Button botonMovimiento1;

	    @FXML
	    private Button botonMovimiento2;

	    @FXML
	    private Button botonMovimiento3;

	    @FXML
	    private Button botonMovimiento4;

	    @FXML
	    private Button botonPokemon1;

	    @FXML
	    private Button botonPokemon2;

	    @FXML
	    private Button botonPokemon3;

	    @FXML
	    private Button botonPokemon4;

	    @FXML
	    private Button botonPokemon5;

	    @FXML
	    private Button botonPokemon6;

	    @FXML
	    private Button botonVolver;

	    @FXML
	    private ProgressBar defensaBar;

	    @FXML
	    private ProgressBar defensaSpBar;

	    @FXML
	    private ImageView imgMover1;

	    @FXML
	    private ImageView imgMover2;

	    @FXML
	    private ImageView imgMover3;

	    @FXML
	    private ImageView imgMover4;

	    @FXML
	    private ImageView imgMover5;

	    @FXML
	    private ImageView imgMover6;

	    @FXML
	    private ImageView imgMovimiento1;

	    @FXML
	    private ImageView imgMovimiento2;

	    @FXML
	    private ImageView imgMovimiento3;

	    @FXML
	    private ImageView imgMovimiento4;

	    @FXML
	    private ImageView imgPokemon1;

	    @FXML
	    private ImageView imgPokemon2;

	    @FXML
	    private ImageView imgPokemon3;

	    @FXML
	    private ImageView imgPokemon4;

	    @FXML
	    private ImageView imgPokemon5;

	    @FXML
	    private ImageView imgPokemon6;

	    @FXML
	    private ImageView imgPokemonSelect;

	    @FXML
	    private ImageView imgTipo1;

	    @FXML
	    private ImageView imgTipo2;

	    @FXML
	    private Label lblMover1;

	    @FXML
	    private Label lblMover2;

	    @FXML
	    private Label lblMover3;

	    @FXML
	    private Label lblMover4;

	    @FXML
	    private Label lblMover5;

	    @FXML
	    private Label lblMover6;

	    @FXML
	    private Label lblMovimiento1;

	    @FXML
	    private Label lblMovimiento2;

	    @FXML
	    private Label lblMovimiento3;

	    @FXML
	    private Label lblMovimiento4;

	    @FXML
	    private Label statAtaqueLbl;

	    @FXML
	    private Label statAtaqueSpLbl;

	    @FXML
	    private Label statDefensaLbl;

	    @FXML
	    private Label statDefensaSpLbl;

	    @FXML
	    private Label statVelocidadLbl;

	    @FXML
	    private Label statVitalidadLbl;

	    @FXML
	    private ProgressBar velocidadBar;

	    @FXML
	    private ProgressBar vitalidadBar;

	    @FXML
	    void VolverMenu(ActionEvent event) {

	    }

	    @FXML
	    void abrirMovimiento1(ActionEvent event) {

	    }

	    @FXML
	    void abrirMovimiento2(ActionEvent event) {

	    }

	    @FXML
	    void abrirMovimiento3(ActionEvent event) {

	    }

	    @FXML
	    void abrirMovimiento4(ActionEvent event) {

	    }

	    public void init(Entrenador ent, Stage stage, MenuController menuController) throws SQLException {
	        this.e = ent;
	        this.stage = stage;
	        this.menuController = menuController;
	        
	        ConexionBD con = new ConexionBD();
	        Connection conexion = con.getConnection();
	        
	        PokemonCrud.obtenerPokemon1(conexion, e);
	        
	        if (conexion != null) {
	            conexion.close();
	        }
	        
	        rellenarDatosEquipo();
	    }
	    
	    public void recibirDatos(Entrenador ent) {
	        this.e = ent;
	        
	        // 1. Instanciamos tu clase de conexión
	        ConexionBD conBD = new ConexionBD();
	        
	        // 2. Obtenemos el objeto Connection llamando a tu método
	        Connection conexion = conBD.getConnection();
	        
	        try {
	            if (conexion != null) {
	                // 3. Ahora sí, pasamos la conexión al método que arreglamos al principio
	                PokemonCrud.obtenerPokemon1(conexion, this.e);
	                
	                // 4. Cerramos la conexión al terminar
	                conexion.close();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        
	        rellenarDatosEquipo();
	        if (!e.getEquipo1().isEmpty()) {
	            mostrarStats(e.getEquipo1().get(0));
	        }
	    }
	    
	    public void rellenarDatosEquipo() {
	        Label[] nombres = {LblPokemon1, LblPokemon2, LblPokemon3, LblPokemon4, LblPokemon5, LblPokemon6};
	        Label[] niveles = {LblNivel1, LblNivel2, LblNivel3, LblNivel4, LblNivel5, LblNivel6};
	        Label[] vidas = {LblVida1, LblVida2, LblVida3, LblVida4, LblVida5, LblVida6};
	        ProgressBar[] barrasVida = {barPokemon1, barPokemon2, barPokemon3, barPokemon4, barPokemon5, barPokemon6};
	        ImageView[] imagenes = {imgPokemon1, imgPokemon2, imgPokemon3, imgPokemon4, imgPokemon5, imgPokemon6};
	        Button[] botones = {botonPokemon1, botonPokemon2, botonPokemon3, botonPokemon4, botonPokemon5, botonPokemon6};

	        // Seguridad: Si e es null o el equipo es null, salimos para evitar errores
	        if (e == null || e.getEquipo1() == null) {
	            return;
	        }

	        LinkedList<Pokemon> equipo1 = e.getEquipo1();

	        for (int i = 0; i < 6; i++) {
	            if (i < equipo1.size()) {
	                Pokemon p = equipo1.get(i);
	                
	                // Si el mote está vacío en la DB, a veces llega como String vacío "" en lugar de null
	                String nombreAMostrar = (p.getMote() != null && !p.getMote().isEmpty()) ? p.getMote() : p.getNombre();
	                
	                nombres[i].setText(nombreAMostrar);
	                niveles[i].setText("Nv. " + p.getNivel());
	                vidas[i].setText(p.getVitalidad() + "/100"); 
	                barrasVida[i].setProgress(p.getVitalidad() / 100.0);

	                // SetVisible(true) para mostrar el slot ocupado
	                botones[i].setVisible(true);
	                nombres[i].setVisible(true);
	                niveles[i].setVisible(true);
	                vidas[i].setVisible(true);
	                barrasVida[i].setVisible(true);
	                imagenes[i].setVisible(true);
	            } else {
	                // SetVisible(false) para ocultar slots donde no hay pokemon
	                botones[i].setVisible(false);
	                nombres[i].setVisible(false);
	                niveles[i].setVisible(false);
	                vidas[i].setVisible(false);
	                barrasVida[i].setVisible(false);
	                imagenes[i].setVisible(false);
	            }
	        }
	        
	        if (!equipo1.isEmpty()) {
	            mostrarStats(equipo1.getFirst());
	        }
	    }

	    private void mostrarStats(Pokemon p) {
	        statAtaqueLbl.setText(String.valueOf(p.getAtaque()));
	        ataqueBar.setProgress(p.getAtaque() / 200.0); // Normalizado sobre 200 por ejemplo
	        
	        statAtaqueSpLbl.setText(String.valueOf(p.getAtaqueEspecial()));
	        ataqueSpBar.setProgress(p.getAtaqueEspecial() / 200.0);
	        
	        statDefensaLbl.setText(String.valueOf(p.getDefensa()));
	        defensaBar.setProgress(p.getDefensa() / 200.0);
	        
	        statDefensaSpLbl.setText(String.valueOf(p.getDefensaEspecial()));
	        defensaSpBar.setProgress(p.getDefensaEspecial() / 200.0);
	        
	        statVelocidadLbl.setText(String.valueOf(p.getVelocidad()));
	        velocidadBar.setProgress(p.getVelocidad() / 200.0);
	        
	        statVitalidadLbl.setText(String.valueOf(p.getVitalidad()));
	        vitalidadBar.setProgress(p.getVitalidad() / 200.0);
	        
	        // Movimientos del pokemon seleccionado
	        LinkedList<Movimiento> movs = p.getMovimientos();
	        Label[] lblMovs = {lblMovimiento1, lblMovimiento2, lblMovimiento3, lblMovimiento4};
	        
	        for (int i = 0; i < 4; i++) {
	            if (i < movs.size()) {
	                lblMovs[i].setText(movs.get(i).getNombre());
	            } else {
	                lblMovs[i].setText("-");
	            }
	        }
	    }
	    
	}


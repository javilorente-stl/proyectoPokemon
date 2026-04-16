package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import dao.ConexionBD;
import dao.PokemonCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Entrenador;

public class CapturaController {

	
	private boolean sonido=false;
	private MediaPlayer mediaPlayer;
	private MediaPlayer gritoPokemon;
	private boolean pokemonGenerado=false;
	private Stage stage;
	private int numPokemon;
	private Entrenador e;
	
	
    @FXML
    private ImageView imgPokemon;

    @FXML
    private ImageView imgRecuadro;
    
    @FXML
    private Button btnGenerarPokemon;
    
    @FXML
    private Button btnlanzarPokeball;
    
    @FXML
    private Button btnVolver;
    
    @FXML
    private Label lblnombrePokemon;
    
    @FXML
    private ImageView imgSonido;
    
    @FXML
    private Label lblCaptura;
    
    @FXML
    void ApagarEncenderMusica(MouseEvent event) {
    	sonido();
    }
    
    @FXML
    void volverMenu(ActionEvent event) throws IOException, SQLException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/menu.fxml"));
        Parent root = loader.load();
        
        MenuController controller = loader.getController();
        
        //Vuelvo a cargar el entrenador
        controller.init(this.e, (Stage) btnVolver.getScene().getWindow(), null);
        if(mediaPlayer!=null) {
        	mediaPlayer.stop();
        }
        //Cambiar la escena
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
    
    @FXML
    void generarPokemon(ActionEvent event) {
    	generarPokemonAleatorio();
    }
    
    public void generarPokemonAleatorio() {
    	Random rand = new Random();
    	numPokemon = rand.nextInt(151) + 1;
    	
    	imgPokemon.setImage(new Image(new File("./img/pokemon/front/"+numPokemon+".gif").toURI().toString()));	
    	pokemonGenerado=true;
    	
    	//hacer que ponga el sonido
    	String numeroFormateado = String.format("%03d", numPokemon);
    	String sonido="./son/pokemon/"+numeroFormateado+".wav";
    	Media sound = new Media(new File(sonido).toURI().toString());
    	gritoPokemon = new MediaPlayer(sound);
    	gritoPokemon.play();
    	try {
    	obtenerNombre(numPokemon);
    	}catch (SQLException e) {
			System.out.println(e.getMessage());		
		}
    }
    
   public void sonido() {
	   if(mediaPlayer==null) {
    	String sonido="./son/CapturaSon.mp3";
    	Media sound = new Media(new File(sonido).toURI().toString());
    	mediaPlayer = new MediaPlayer(sound);
    	mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    	//mediaPlayer.play();
	   }
    	
    	if(!this.sonido) {
    	mediaPlayer.play();
    	imgSonido.setImage(new Image(new File("./img/ChatotFeliz.png").toURI().toString()));
    	this.sonido=true;
    	}else {
    		mediaPlayer.stop();
    		this.sonido=false;
    		imgSonido.setImage(new Image(new File("./img/ChatotTriste.png").toURI().toString()));
    	}
    	
    }
    
   public void recibirDatos(Entrenador ent) {
       this.e = ent;
       System.out.println("Captura lista para: " + e.getNombre());
   }

    @FXML
    void lanzarPokeball(ActionEvent event) {
    	Random rand = new Random();
    	int numeroAleatorio = rand.nextInt(3) + 1;
    	if(pokemonGenerado==false) {
    		lblCaptura.setText("No has generado un pokemon");
    		return;
    	}
    	
    	if(numeroAleatorio==1) {
    		lblCaptura.setText("Has capturado un pokemon");
    		try {
    		ConexionBD con = new ConexionBD();
        	Connection conexion = con.getConnection();
    		PokemonCrud.guardarPokemon(conexion, numPokemon, e, pedirMote(obtenerNombre(numPokemon)));
    		generarPokemonAleatorio();
    		}catch (SQLException e) {
				System.out.println(e.getMessage());
			}
    	}else {
    		lblCaptura.setText("La pokeball ha fallado");
    	}
    }
    
    
    private String obtenerNombre(int numeroAleatorio) throws SQLException{
    	String sql = "SELECT NOM_POKEMON\n"
    			+ "FROM POKEDEX\n"
    			+ "WHERE NUM_POKEDEX=?";
    	
    	ConexionBD con = new ConexionBD();
    	Connection conexion = con.getConnection();
    	
    	PreparedStatement ps = conexion.prepareStatement(sql);
    	ps.setInt(1, numeroAleatorio);
    	
    	ResultSet rs = ps.executeQuery();
    	
    	if (!rs.next()) {
    		lblnombrePokemon.setText("Pokemon");
    		return "Pokemon";
        } else {
        	lblnombrePokemon.setText(rs.getString(1));
        	return rs.getString(1);
        }
            
    
    	
    }
    
    private static final List<String> PALABRAS_PROHIBIDAS = Arrays.asList("feo", "tonto", "malo", "idiota","pito","culo");
    
    
    private String pedirMote(String nombrePokemon) {
    	String entrada = "";
        boolean esValido = false;

        while (!esValido) {
            entrada = JOptionPane.showInputDialog(null, 
                "Introduce un mote (Solo letras, sin insultos):", 
                "Mote para " + nombrePokemon, 
                JOptionPane.QUESTION_MESSAGE);

            if (entrada == null) return nombrePokemon;
            
            entrada = entrada.trim();

            if (entrada.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El mote no puede estar vacío.");
                continue;
            }
            // [a-zA-ZáéíóúÁÉÍÓÚñÑ] permite letras y tildes
            if (!entrada.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ]+$")) {
                JOptionPane.showMessageDialog(null, "Error: Solo se permiten letras (sin números ni símbolos).");
                continue;
            }

            if (PALABRAS_PROHIBIDAS.contains(entrada.toLowerCase())) {
                JOptionPane.showMessageDialog(null, "Ese mote no está permitido. Elige otro.");
                continue;
            }

            esValido = true;
        }

        return entrada;
    }
}

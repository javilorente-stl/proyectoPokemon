package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
import modelo.Pokemon;

public class MenuController {

	private LoginController loginController;
	private Stage stage;
	public Entrenador e;
	private boolean sonido=false;
	private MediaPlayer mediaPlayer;
	
    @FXML
    private Button btnCaptura;

    @FXML
    private Button btnCasino;

    @FXML
    private Button btnCombate;

    @FXML
    private Button btnEntrenamiento;

    @FXML
    private Button btnEquipo;
    
    @FXML
    private Button btnCrianza;

    @FXML
    private Button btnHospital;

    @FXML
    private Button btnSalir;

    @FXML
    private Label lblEntrenador;

    @FXML
    private Label lblPokedollars;

    @FXML
    private ImageView imgSonido;

    @FXML
    void abrirCaptura(ActionEvent event) {
    	try {
			cerrarMenutoCaptura();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    @FXML
    void abrirCasino(ActionEvent event) throws IOException {
    	cerrarMenutoCasino();
    }

    @FXML
    void abrirCombate(ActionEvent event) throws IOException, SQLException {
    	cerrarMenutoCombate();
    }

    @FXML
    void abrirEntrenamiento(ActionEvent event) throws IOException {
    	cerrarMenutoEntrenamiento();
    }

    @FXML
    void abrirEquipo(ActionEvent event) {
    	try {
			cerrarMenutoEquipo();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    @FXML
    void abrirCrianza(ActionEvent event) throws IOException, SQLException {
    	cerrarMenutoCrianza();
    }
    
    

    @FXML
    void abrirHospital(ActionEvent event) {

    }

    @FXML
    void apagarEncenderMusica(MouseEvent event) {
    	sonido();
    }

    public void sonido() {
    	if (mediaPlayer == null) {
            try {
                String rutaSonido = "./son/musicaMenu.mp3";
                File archivo = new File(rutaSonido);
                
                if (archivo.exists()) {
                    Media sound = new Media(archivo.toURI().toString());
                    mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                } else {
                    System.err.println("Error: No se encuentra el archivo de sonido en " + rutaSonido);
                    return; // Si no hay archivo, no seguimos
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        if (!this.sonido) {
            // Estaba apagado -> Toca sonar
            mediaPlayer.play();
            this.sonido = true;
            // Cambiamos a Meloetta Feliz
            File imgFile = new File("./img/MeloettaFeliz.png");
            if (imgFile.exists()) {
                imgSonido.setImage(new Image(imgFile.toURI().toString()));
            }    
        } else {
            // Estaba encendido -> Toca silenciar
            // Usamos stop() para que la próxima vez empiece desde el principio
            mediaPlayer.stop(); 
            this.sonido = false;
            
            // Cambiamos a Meloetta Triste
            File imgFile = new File("./img/MeloettaTriste.png");
            if (imgFile.exists()) {
                imgSonido.setImage(new Image(imgFile.toURI().toString()));
            }
        }
    }
    
    
    @FXML
    void cerrarAplicacion(ActionEvent event) {
    	try {
			cerrarMenutoLoguin();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

    
    public void cerrarMenutoLoguin() throws IOException {
    	// Cargamos el FXML del Login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/Login.fxml"));
        Parent root = loader.load();
        

        Stage stage = (Stage) btnSalir.getScene().getWindow();
        Scene scene = new Scene(root);
        
     
        stage.setScene(scene);

        stage.sizeToScene();
        stage.centerOnScreen();
        
        stage.show();
        mediaPlayer.stop();
    }
    public void cerrarMenutoEntrenamiento() throws IOException {
        // 1. Cargamos el FXML de la vista de Entrenamiento
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/Entrenamiento.fxml"));
        Parent root = loader.load();
        
        // 2. Obtenemos el Stage actual a través de cualquier componente (en este caso btnSalir)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        
        // 3. Obtenemos el controlador de la nueva vista y le pasamos los datos del entrenador
        EntrenamientoController controller = loader.getController();
        controller.recibirDatos(this.e);
        
        // 4. Configuramos la nueva escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        // 5. Ajustamos el tamaño y centramos la ventana
        stage.sizeToScene();
        stage.centerOnScreen();
        
        // 6. Mostramos la ventana y detenemos la música de la vista actual
        stage.show();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    public void cerrarMenutoCasino() throws IOException {
        // 1. Cargamos el FXML de la vista del Casino
        // Asegúrate de que el archivo se llame Casino.fxml y esté en esa ruta
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/Casino.fxml"));
        Parent root = loader.load();
        
        // 2. Obtenemos el Stage actual (usando btnSalir o cualquier nodo de la vista)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        
        // 3. Obtenemos el controlador del Casino y le pasamos el objeto Entrenador
        // Cambia 'CasinoController' por el nombre exacto de tu clase
        CasinoController controller = loader.getController();
        
        // Pasamos los datos (asegúrate de que CasinoController tenga este método)
        controller.recibirDatos(this.e);
        
        // 4. Configuramos la nueva escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        // 5. Ajustes de ventana
        stage.sizeToScene();
        stage.centerOnScreen();
        
        // 6. Cambio de vista y gestión de audio
        stage.show();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    public void cerrarMenutoCaptura() throws IOException {
    	// Cargamos el FXML del Login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/Captura.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        
        CapturaController controller = loader.getController();
        controller.recibirDatos(this.e);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        
        stage.sizeToScene();
        stage.centerOnScreen();
        
        stage.show();
        mediaPlayer.stop();
    }
    
    public void cerrarMenutoCombate() throws IOException, SQLException {
        // 1. Generamos el Pokémon enemigo (necesitamos conexión para el CRUD)
        Pokemon enemigoRandom = null;
        ConexionBD conBD = new ConexionBD();
        
        try (Connection con = conBD.getConnection()) {
            if (con != null) {
                // Usamos el método que creamos en PokemonCrud para generar el rival en memoria
                enemigoRandom = PokemonCrud.generarPokemonAleatorio(con);
            }
        }

        // 2. Cargamos el FXML de la vista de Combate
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/Combate.fxml"));
        Parent root = loader.load();
        
        // 3. Obtenemos el Stage actual
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        
        // 4. Obtenemos el controlador y pasamos los datos
        CombateController controller = loader.getController();
        
        // IMPORTANTE: El CombateController ahora genera al rival dentro de su propio recibirDatos
        // o lo recibe por aquí. Según lo último que programamos, el controlador lo genera 
        // usando la conexión, así que solo le pasamos el entrenador.
        controller.recibirDatos(this.e);
        
        // 5. Configuramos la nueva escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        // 6. Ajustes de ventana y música
        stage.sizeToScene();
        stage.centerOnScreen();
        
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        
        stage.show();
    }
    
    public void cerrarMenutoCrianza() throws IOException, SQLException {
        // 1. Cargamos el FXML de la vista de Crianza
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/Crianza.fxml"));
        Parent root = loader.load();
        
        // 2. Obtenemos el Stage actual a través de cualquier botón (btnSalir en este caso)
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        
        // 3. Obtenemos el controlador de Crianza y le pasamos el objeto Entrenador 'e'
        CrianzaController controller = loader.getController();
        controller.recibirDatos(this.e);
        
        // 4. Configuramos la nueva escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        // 5. Ajustamos ventana (opcional pero recomendado para que cuadre el diseño)
        stage.sizeToScene();
        stage.centerOnScreen();
        
        // 6. Mostramos y paramos la música actual
        stage.show();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void cerrarMenutoEquipo() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/Equipo.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        
        EquipoController controller = loader.getController();
        controller.recibirDatos(this.e);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.sizeToScene();
        stage.centerOnScreen();
        
        stage.show();
        
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    
    
    public void init(Entrenador ent, Stage stage, LoginController loginController) throws SQLException {
        this.e = ent;
        this.stage = stage;
        this.loginController = loginController;
        
        lblEntrenador.setText(e.getNombre());
        lblPokedollars.setText(Integer.toString(e.getPokedollars()));
        
        ConexionBD con = new ConexionBD();
        Connection conexion = con.getConnection();
        
        PokemonCrud.obtenerPokemon1(conexion, e);
        
        
        if (conexion != null) {
            conexion.close();
        }
    }


    
}

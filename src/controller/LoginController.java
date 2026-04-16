package controller;


import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import dao.ConexionBD;
import dao.EntrenadorCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Entrenador;

public class LoginController {

	public Stage stage;
	private boolean sonido=true;
	private MediaPlayer mediaPlayer;
	  
    @FXML
    private Button btnAceptar;


    @FXML
    private Button btnCancelar;


    
    @FXML
    private ImageView imgFondo;

    @FXML
    private Label lblContraseña;

    @FXML
    private Label lblEntrenador;

    @FXML
    private Label lblError;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsuario;
    
    @FXML
    private ImageView imgSonido;



    //Metodo para salir de la aplicacion
    @FXML
    void cerrarAplicacion(ActionEvent event) {
    	Stage st = (Stage) btnCancelar.getScene().getWindow();
    	st.close();
    }
    /*
    @FXML
    void loguearse(ActionEvent event) {
    	if(txtUsuario.getText().isEmpty()) {
    		lblError.setText("Error: introduce un usuario");
    		lblError.setVisible(true);
    	}else if(txtPassword.getText().isEmpty()) {
    		lblError.setText("Error: introduce una contraseña");
    		lblError.setVisible(true);
    	}else {
    		lblError.setVisible(false);
    		String usuario = txtUsuario.getText();
    		String password = txtPassword.getText();
    		
    		String sql = "SELECT PASSWORD\r\n"
    				+ "FROM ENTRENADOR\r\n"
    				+ "WHERE NOM_ENTRENADOR = ?;";
    		
    		ConexionBD con =new ConexionBD();
    		Connection conexion = con.getConnection(); 
    		
    		try {
				PreparedStatement ps = conexion.prepareStatement(sql);
				ps.setString(1, usuario);
				
				ResultSet rs = ps.executeQuery();
				
				Entrenador entrenador = new Entrenador(usuario, password);
				
				if(!rs.isBeforeFirst()) {
					lblError.setText("Usuario no registrado");
					lblError.setVisible(true);
					
					int opcion = JOptionPane.showConfirmDialog(null, "Usuario no encontrado, ¿Desea registrarlo?");
					btnAceptar.setVisible(false);
					btnRegistrar.setVisible(true);
					
					//registrarEntrenador(event, entrenador);
					
					if (opcion == JOptionPane.YES_OPTION) {
						EntrenadorCrud.crearEntrenador(conexion, entrenador);
						System.out.println("Usuario registrado");
					
						abrirMenuPrincipal(entrenador);
					}else {
						txtUsuario.setText("");
						txtPassword.setText("");
					}
				}else {
					while(rs.next()) {
						if(rs.getString(1).equals(password)) {
							System.out.println("Usuario encontrado");
							EntrenadorCrud.obtenerIDPokedollares(conexion, entrenador);
							abrirMenuPrincipal(entrenador);
						}else {
							lblError.setText("Contraseña incorrecta");
							lblError.setVisible(true);
						}
					}
				}
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
    		}
    }
    */
    @FXML
    void loguearse(ActionEvent event) {
        // 1. Validaciones básicas de campos vacíos
        if (txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            lblError.setText("Error: rellene todos los campos");
            lblError.setVisible(true);
            return; 
        }

        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        String sql = "SELECT PASSWORD FROM ENTRENADOR WHERE NOM_ENTRENADOR = ?";

        //Uso el try with para cerrar la conexión automáticamente
        ConexionBD con = new ConexionBD();
        try (Connection conexion = con.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                
                Entrenador entrenador = new Entrenador(usuario, password);

                if (!rs.next()) {
                    // USUARIO NO ENCONTRADO
                    manejarUsuarioNoEncontrado(conexion, entrenador);
                } else {
                    // USUARIO ENCONTRADO -> Comprobar Password
                    String passBD = rs.getString("PASSWORD");
                    
                    if (passBD.equals(password)) {
                        System.out.println("Login correcto");
                        EntrenadorCrud.obtenerIDPokedollares(conexion, entrenador);
                        abrirMenuPrincipal(entrenador);
                    } else {
                        lblError.setText("Contraseña incorrecta");
                        lblError.setVisible(true);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            lblError.setText("Error de conexión con la base de datos");
            lblError.setVisible(true);
        }
    }

    
    private void manejarUsuarioNoEncontrado(Connection conexion, Entrenador entrenador) throws SQLException, IOException {
        lblError.setText("Usuario no registrado");
        lblError.setVisible(true);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea registrar al nuevo entrenador?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            EntrenadorCrud.crearEntrenador(conexion, entrenador);
            abrirMenuPrincipal(entrenador);
        } else {
            txtUsuario.clear();
            txtPassword.clear();
        }
    }
    
    //Tengo que ver como crear un boton para que me registre el entrenador
    
    @FXML
    void registrarEntrenador(ActionEvent event) {
    	/*
    	ConexionBD con =new ConexionBD();
		Connection conexion = con.getConnection();
		
		EntrenadorCrud.crearEntrenador(conexion, entrenador);
		System.out.println("Usuario registrado");
		
		abrirMenuPrincipal(entrenador);
		txtUsuario.setText("");
		txtPassword.setText("");
		*/
    }
    
    public void sonido() {
    	String sonido="./son/Opening.mp3";
    	Media sound = new Media(new File(sonido).toURI().toString());
    	mediaPlayer = new MediaPlayer(sound);
    	mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    	
    	if(!this.sonido) {
    	mediaPlayer.play();
    	imgSonido.setImage(new Image(new File("./img/SonidoActivo.png").toURI().toString()));
    	
    	this.sonido=true;
    	}else {
    		mediaPlayer.stop();
    		this.sonido=false;
    		imgSonido.setImage(new Image(new File("./img/SonidoDesactivado.png").toURI().toString()));
    	}
    }
    
    
    @FXML
    public void initialize() {
    	sonido();
    }
    
    
    @FXML
    void apagarEncenderMusica(MouseEvent event) {
    	sonido();
    }
    //Este cambia de stage, pero no hace falta
    /*
    private void abrirMenuPrincipal(Entrenador ent) throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/menu.fxml"));
    	Parent root = loader.load();
    	MenuController principalController = loader.getController();
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.setScene(scene);
    	
    	principalController.init(ent, stage, this);
    	stage.show();
    	
    	
    	Stage st = (Stage) btnCancelar.getScene().getWindow();
    	st.close();
    
    	
    }*/
    private void abrirMenuPrincipal(Entrenador ent) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/menu.fxml"));
        Parent root = loader.load();
        MenuController principalController = loader.getController();
        

        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        

        Scene scene = new Scene(root);
        stage.setScene(scene);
        

        principalController.init(ent, stage, this);
        
        stage.setTitle("Menú Principal");
        stage.centerOnScreen(); 
        mediaPlayer.stop();
        stage.show();
    }
}

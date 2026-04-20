package controller;

	import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dao.ConexionBD;
import dao.PokemonCrud;
import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Entrenador;
import modelo.Movimiento;
import modelo.Pokemon;
import modelo.Tipo;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class EquipoController {
		
	private MenuController menuController;
	private Stage stage;
	public Entrenador e;
	private boolean sonido=false;
	private MediaPlayer mediaPlayer;
	private FadeTransition animacionActiva;
	private int posicionOrigen = -1;
	private boolean modoMover = false;
	ConexionBD conBD = new ConexionBD();
	
	//Para hacer el cambio despues de estar en la caja
	private Pokemon pokemonVieneDeCaja;
	private CajaController cajaPadre;
	private boolean modoIntercambio = false;
	private Button[] botones;
	
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
	    private ImageView imgSelect1;

	    @FXML
	    private ImageView imgSelect2;

	    @FXML
	    private ImageView imgSelect3;

	    @FXML
	    private ImageView imgSelect4;

	    @FXML
	    private ImageView imgSelect5;

	    @FXML
	    private ImageView imgSelect6;
	    
	    @FXML
	    private ImageView imgSexo1;

	    @FXML
	    private ImageView imgSexo2;

	    @FXML
	    private ImageView imgSexo3;

	    @FXML
	    private ImageView imgSexo4;

	    @FXML
	    private ImageView imgSexo5;

	    @FXML
	    private ImageView imgSexo6;
	    
	    @FXML
	    private ImageView imgEstado1;

	    @FXML
	    private ImageView imgEstado2;

	    @FXML
	    private ImageView imgEstado3;

	    @FXML
	    private ImageView imgEstado4;

	    @FXML
	    private ImageView imgEstado5;

	    @FXML
	    private ImageView imgEstado6;
	    
	    @FXML
	    private Label lblACaja;

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
	    void mover1(ActionEvent event) {
	    	lblACaja.setVisible(true);
	    	lblACaja.setDisable(false);
	        this.posicionOrigen = 1; // Marcamos quién se va a mover
	        this.modoMover = true;   // Activamos el modo intercambio
	        aplicarParpadeoDestinos(1);
	        System.out.println("Moviendo Pokémon de la posición 1. Selecciona destino...");
	    }

	    @FXML
	    void mover2(ActionEvent event) {
	    	lblACaja.setVisible(true);
	    	lblACaja.setDisable(false);
	        this.posicionOrigen = 2; // Marcamos quién se va a mover
	        this.modoMover = true;   // Activamos el modo intercambio
	        aplicarParpadeoDestinos(2);
	        System.out.println("Moviendo Pokémon de la posición 1. Selecciona destino...");
	    }

	    @FXML
	    void mover3(ActionEvent event) {
	    	lblACaja.setVisible(true);
	    	lblACaja.setDisable(false);
	        this.posicionOrigen = 3; // Marcamos quién se va a mover
	        this.modoMover = true;   // Activamos el modo intercambio
	        aplicarParpadeoDestinos(3);
	        System.out.println("Moviendo Pokémon de la posición 1. Selecciona destino...");
	    }

	    @FXML
	    void mover4(ActionEvent event) {
	    	lblACaja.setVisible(true);
	    	lblACaja.setDisable(false);
	        this.posicionOrigen = 4; // Marcamos quién se va a mover
	        this.modoMover = true;   // Activamos el modo intercambio
	        aplicarParpadeoDestinos(4);
	        System.out.println("Moviendo Pokémon de la posición 1. Selecciona destino...");
	    }

	    @FXML
	    void mover5(ActionEvent event) {
	    	lblACaja.setVisible(true);
	    	lblACaja.setDisable(false);
	        this.posicionOrigen = 5; // Marcamos quién se va a mover
	        this.modoMover = true;   // Activamos el modo intercambio
	        aplicarParpadeoDestinos(5);
	        System.out.println("Moviendo Pokémon de la posición 1. Selecciona destino...");
	    }

	    @FXML
	    void mover6(ActionEvent event) {
	    	lblACaja.setVisible(true);
	    	lblACaja.setDisable(false);
	        this.posicionOrigen = 6; // Marcamos quién se va a mover
	        this.modoMover = true;   // Activamos el modo intercambio
	        aplicarParpadeoDestinos(6);
	        System.out.println("Moviendo Pokémon de la posición 1. Selecciona destino...");
	    }
	    
	    @FXML
	    void pokemonSeleccionado1(ActionEvent event) {
	    	if (modoMover||modoIntercambio) {
	            // Si estamos moviendo, el clic en el botón significa "destino"
	            gestionarClickPokemon(1); 
	        } else {
	            // Si no, es una selección normal para ver datos
	            if (e.getEquipo1().size() >= 1) {
	                actualizarPokemonSeleccionado(e.getEquipo1().get(0));
	                cargarTiposMovimientos(e.getEquipo1().get(0));
	                mostrarStats(e.getEquipo1().get(0));
	                aplicarParpadeoSeleccion(1);
	                activarMover(1);
	            }
	        }
	    }

	    @FXML
	    void pokemonSeleccionado2(ActionEvent event) {
	    	if (modoMover||modoIntercambio) {
	            // Si estamos moviendo, el clic en el botón significa "destino"
	            gestionarClickPokemon(2); 
	        } else {
	            // Si no, es una selección normal para ver datos
	            if (e.getEquipo1().size() >= 1) {
	    	actualizarPokemonSeleccionado(e.getEquipo1().get(1));
	    	cargarTiposMovimientos(e.getEquipo1().get(1));
	    	aplicarParpadeoSeleccion(2);
	    	activarMover(2);
	    	mostrarStats(e.getEquipo1().get(1));
	            }
	        }
	    }

	    @FXML
	    void pokemonSeleccionado3(ActionEvent event) {
	    	if (modoMover||modoIntercambio) {
	            // Si estamos moviendo, el clic en el botón significa "destino"
	            gestionarClickPokemon(3); 
	        } else {
	            // Si no, es una selección normal para ver datos
	            if (e.getEquipo1().size() >= 1) {
	    	actualizarPokemonSeleccionado(e.getEquipo1().get(2));
	    	cargarTiposMovimientos(e.getEquipo1().get(2));
	    	aplicarParpadeoSeleccion(3);
	    	activarMover(3);
	    	mostrarStats(e.getEquipo1().get(2));
	            }
	        }
	    }

	    @FXML
	    void pokemonSeleccionado4(ActionEvent event) {
	    	if (modoMover||modoIntercambio) {
	            // Si estamos moviendo, el clic en el botón significa "destino"
	            gestionarClickPokemon(4); 
	        } else {
	            // Si no, es una selección normal para ver datos
	            if (e.getEquipo1().size() >= 1) {
	    	actualizarPokemonSeleccionado(e.getEquipo1().get(3));
	    	cargarTiposMovimientos(e.getEquipo1().get(3));
	    	aplicarParpadeoSeleccion(4);
	    	activarMover(4);
	    	mostrarStats(e.getEquipo1().get(3));
	            }
	        }
	    }

	    @FXML
	    void pokemonSeleccionado5(ActionEvent event) {
	    	if (modoMover||modoIntercambio) {
	            // Si estamos moviendo, el clic en el botón significa "destino"
	            gestionarClickPokemon(5); 
	        } else {
	            // Si no, es una selección normal para ver datos
	            if (e.getEquipo1().size() >= 1) {
	    	actualizarPokemonSeleccionado(e.getEquipo1().get(4));
	    	cargarTiposMovimientos(e.getEquipo1().get(4));
	    	aplicarParpadeoSeleccion(5);
	    	activarMover(5);
	    	mostrarStats(e.getEquipo1().get(4));
	            }
	        }
	    }

	    @FXML
	    void pokemonSeleccionado6(ActionEvent event) {
	    	if (modoMover||modoIntercambio) {
	            // Si estamos moviendo, el clic en el botón significa "destino"
	            gestionarClickPokemon(1); 
	        } else {
	            // Si no, es una selección normal para ver datos
	            if (e.getEquipo1().size() >= 1) {
	    	actualizarPokemonSeleccionado(e.getEquipo1().get(5));
	    	cargarTiposMovimientos(e.getEquipo1().get(5));
	    	aplicarParpadeoSeleccion(6);
	    	activarMover(6);
	    	mostrarStats(e.getEquipo1().get(5));
	            }
	        }
	    }
	    
	    @FXML
	    void irACaja(ActionEvent event) throws SQLException, IOException {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/CajaAjustar.fxml"));
	        Parent root = loader.load();

	        // 2. Obtener el controlador de la Caja
	        CajaController controller = loader.getController();

	        // 3. Pasar los datos del Entrenador (el método que rellenará el GridPane)
	        controller.recibirDatos(this.e);

	        // 4. Parar la música si es necesario (como hacías en VolverMenu)
	        if (mediaPlayer != null) {
	            mediaPlayer.stop();
	        }

	        // 5. Cambiar la escena
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        Scene scene = new Scene(root);
	        stage.setScene(scene);

	        // 6. Ajustes de ventana
	        stage.sizeToScene();
	        stage.centerOnScreen();
	        stage.show();
	    }

	    @FXML
	    void moverACaja(MouseEvent event) {
	    	if (e.getEquipo1().size() <= 1) {
	            System.out.println("¡No puedes enviar tu último Pokémon a la caja!");
	            
	            // Opcional: Mostrar una alerta visual al usuario
	            // Alerta.mostrarError("Acción no permitida", "Debes tener al menos un Pokémon en tu equipo.");
	            
	            limpiarModoMover(); // Cancelamos el modo mover para que no se quede bloqueado
	            return;
	    	}
	    	try (Connection con = conBD.getConnection()) {
	            // 1. Calculamos el hueco en la DB (ej: 43)
	            int destinoCaja = PokemonCrud.obtenerSiguienteHuecoCaja(con, e.getIdEntrenador());
	            
	            // 2. Obtenemos el Pokémon real que está en la posición seleccionada
	            // Usamos posicionOrigen - 1 porque en Java las listas empiezan en 0
	            Pokemon p = e.getEquipo1().get(posicionOrigen - 1);

	            // 3. ACTUALIZAMOS LA BASE DE DATOS (Este es el paso que te faltaba)
	            // Cambiamos su columna CAJA de (1-6) a (43)
	            PokemonCrud.actualizarPosicionPokemon(con, p.getId_pokemon(), destinoCaja);
	            
	            // 4. Limpiamos el estado visual
	            limpiarModoMover();

	            PokemonCrud.compactarCaja(con, e.getIdEntrenador());
	            PokemonCrud.obtenerPokemon1(con, e); // El equipo ahora tendrá uno menos
	            PokemonCrud.obtenerPokemon2(con, e); // La caja ahora tendrá uno más
	            
	            // 6. Volvemos a pintar la interfaz
	            rellenarDatosEquipo();

	            // 7. SEGURIDAD: Solo intentamos seleccionar si queda alguien en el equipo
	            if (!e.getEquipo1().isEmpty()) {
	                // Seleccionamos por defecto el primero que quede (índice 0)
	                actualizarPokemonSeleccionado(e.getEquipo1().get(0));
	                cargarTiposMovimientos(e.getEquipo1().get(0));
	                aplicarParpadeoSeleccion(0);
	                
	            }
	            
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        } catch (IndexOutOfBoundsException ex) {
	            System.err.println("Error: El equipo se ha quedado vacío o el índice no es válido.");
	        }
	    }
	    
	    
	    @FXML
	    void VolverMenu(ActionEvent event) throws IOException, SQLException {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/menu.fxml"));
	        Parent root = loader.load();
	        
	        MenuController controller = loader.getController();
	        
	        //Vuelvo a cargar el entrenador
	        controller.init(this.e, (Stage) botonVolver.getScene().getWindow(), null);
	        if(mediaPlayer!=null) {
	        	mediaPlayer.stop();
	        }
	        //Cambiar la escena
	        Stage stage = (Stage) botonVolver.getScene().getWindow();
	        Scene scene = new Scene(root);
	        stage.setScene(scene);
	        
	        stage.sizeToScene();
	        stage.centerOnScreen();
	        stage.show();
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

	    public void recibirDatos(Entrenador ent) throws SQLException {
	        this.e = ent;
	        
	        
	        try (Connection conexion = conBD.getConnection()) {
	            if (conexion != null) {
	                PokemonCrud.compactarEquipo(conexion, e.getIdEntrenador());
	                PokemonCrud.obtenerPokemon1(conexion, this.e);
	                PokemonCrud.obtenerPokemon2(conexion, this.e);
	            }
	        }
	        
	        rellenarDatosEquipo();
	        aplicarParpadeoSeleccion(1);
	    	activarMover(1);
	    	actualizarPokemonSeleccionado(e.getEquipo1().get(0));
	    	cargarTiposMovimientos(e.getEquipo1().get(0));
	        if (e.getEquipo1() != null && !e.getEquipo1().isEmpty()) {
	            mostrarStats(e.getEquipo1().get(0));
	        }
	    }
	    
	    
	    public void rellenarDatosEquipo() {
	    	lblACaja.setStyle("-fx-background-color: white;");
	    	
	    	Label[] nombres = {LblPokemon1, LblPokemon2, LblPokemon3, LblPokemon4, LblPokemon5, LblPokemon6};
	        Label[] niveles = {LblNivel1, LblNivel2, LblNivel3, LblNivel4, LblNivel5, LblNivel6};
	        Label[] vidas = {LblVida1, LblVida2, LblVida3, LblVida4, LblVida5, LblVida6};
	        ProgressBar[] barrasVida = {barPokemon1, barPokemon2, barPokemon3, barPokemon4, barPokemon5, barPokemon6};
	        ImageView[] imagenes = {imgPokemon1, imgPokemon2, imgPokemon3, imgPokemon4, imgPokemon5, imgPokemon6};
	        ImageView[] sexos = {imgSexo1, imgSexo2, imgSexo3, imgSexo4, imgSexo5, imgSexo6};
	        ImageView[] estados = {imgEstado1, imgEstado2, imgEstado3, imgEstado4, imgEstado5, imgEstado6};
	        botones = new Button[]{botonPokemon1, botonPokemon2, botonPokemon3, botonPokemon4, botonPokemon5, botonPokemon6};
	        if (e == null || e.getEquipo1() == null) return;
	        

	        LinkedList<Pokemon> equipo1 = e.getEquipo1();

	        for (int i = 0; i < 6; i++) {
	            if (i < equipo1.size()) {
	                Pokemon p = equipo1.get(i);
	                
	                // 1. TEXTOS Y VIDA
	                String nombreAMostrar = (p.getMote() != null && !p.getMote().isEmpty()) ? p.getMote() : p.getNombre();
	                nombres[i].setText(nombreAMostrar);
	                niveles[i].setText("Nv. " + p.getNivel());
	                vidas[i].setText(p.getVitalidad() + "/" + p.getVitalidadMax()); 
	                //barrasVida[i].setProgress(p.getVitalidad() / p.getVitalidadMax());

	                // 2. IMAGEN POKEMON
	                File archivoPk = new File("img/pokemon/front/" + p.getNum_pokedex() + ".gif");
	                if (archivoPk.exists()) imagenes[i].setImage(new Image(archivoPk.toURI().toString()));

	                // 3. SEXO (Uso del Enum)
	                // Asumiendo que el Enum Sexo tiene valores como MACHO, HEMBRA o M, H
	                String nombreSexo = p.getSexo().name().toLowerCase(); 
	                File fileSexo = new File("img/" + nombreSexo + ".png");
	                if (fileSexo.exists()) sexos[i].setImage(new Image(fileSexo.toURI().toString()));

	                // 4. ESTADO (Uso del Enum)
	                // Buscamos el icono según el nombre del Enum (VIVO, DEBILITADO, PARALIZADO...)
	                String nombreEstado = p.getEstado().name().toLowerCase();
	                File fileEstado = new File("img/estados/" + nombreEstado + ".png");
	                
	                if (fileEstado.exists()) {
	                    estados[i].setImage(new Image(fileEstado.toURI().toString()));
	                } else {
	                    estados[i].setImage(null); // Si es VIVO y no tienes icono, se limpia
	                }
	                
	                // 5. BARRA DE VIDA
	                double porcentajeVida = (double) p.getVitalidad() / p.getVitalidadMax();
	                barrasVida[i].setProgress(porcentajeVida);
	                actualizarColorBarraVida(barrasVida[i], porcentajeVida);   

	                // VISIBILIDAD ON
	                toggleSlotVisible(i, true, botones, nombres, niveles, vidas, barrasVida, imagenes, sexos, estados);

	                final int posicion = i; // Necesitamos una variable final para el lambda
	                /*
	                botones[i].setOnAction(event -> {
	                    if (modoIntercambio) {
	                        ejecutarIntercambio(posicion);
	                    } else {
	                        // Aquí va tu lógica normal de cuando ves el equipo (ej: ver stats)
	                        mostrarStats(e.getEquipo1().get(posicion));
	                    }
	                });*/
	                
	            } else {
	                // VISIBILIDAD OFF
	                toggleSlotVisible(i, false, botones, nombres, niveles, vidas, barrasVida, imagenes, sexos, estados);
	            }
	        }
	        
	        if (!equipo1.isEmpty()) mostrarStats(equipo1.getFirst());
	        

	    }

	    // Método auxiliar para no repetir líneas de setVisible
	    private void toggleSlotVisible(int i, boolean visible, Button[] b, Label[] n, Label[] nv, Label[] v, ProgressBar[] bar, ImageView[] img, ImageView[] s, ImageView[] e) {
	        b[i].setVisible(visible);
	        n[i].setVisible(visible);
	        nv[i].setVisible(visible);
	        v[i].setVisible(visible);
	        bar[i].setVisible(visible);
	        img[i].setVisible(visible);
	        s[i].setVisible(visible);
	        e[i].setVisible(visible);
	    }
	    

	    private void mostrarStats(Pokemon p) {
	    	
	    	double maxStatGeneral = p.getNivel() * 5.0;
	    	
	    	double maxVidaSegunNivel = 15.0 + (p.getNivel() * 5.0);
	    	
	    	double minVidaSegunNivel = maxVidaSegunNivel - 5.0; 

	    	statVitalidadLbl.setText(p.getVitalidadMax() + "/" + (int)maxVidaSegunNivel);

	    	double progresoRelativo = (p.getVitalidadMax() - minVidaSegunNivel) / (maxVidaSegunNivel - minVidaSegunNivel);

	    	vitalidadBar.setProgress(progresoRelativo);
	    	
	    	statAtaqueLbl.setText(String.valueOf(p.getAtaque()));
	        ataqueBar.setProgress(p.getAtaque() / maxStatGeneral);
	        
	        statAtaqueSpLbl.setText(String.valueOf(p.getAtaqueEspecial()));
	        ataqueSpBar.setProgress(p.getAtaqueEspecial() / maxStatGeneral);
	        
	        statDefensaLbl.setText(String.valueOf(p.getDefensa()));
	        defensaBar.setProgress(p.getDefensa() / maxStatGeneral);
	        
	        statDefensaSpLbl.setText(String.valueOf(p.getDefensaEspecial()));
	        defensaSpBar.setProgress(p.getDefensaEspecial() / maxStatGeneral);
	        
	        statVelocidadLbl.setText(String.valueOf(p.getVelocidad()));
	        velocidadBar.setProgress(p.getVelocidad() / maxStatGeneral);
	        
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
	    
	    private void cargarImagenesTipos(Pokemon p) {
	        // Array de ImageViews para iterar
	        ImageView[] ivs = {imgTipo1, imgTipo2};
	        
	        // Obtenemos los Enums del objeto Pokemon
	        Tipo tipo1 = p.getTipo1();
	        Tipo tipo2 = p.getTipo2();
	        Tipo[] tipos = {tipo1, tipo2};

	        for (int i = 0; i < ivs.length; i++) {
	            Tipo t = tipos[i];
	            ImageView iv = ivs[i];

	            // Verificamos que el enum no sea null
	            if (t != null) {
	                try {
	                    // t.name() nos da el nombre exacto del Enum (ej: "FUEGO")
	                    String nombreArchivo = t.name().toLowerCase() + ".png";
	                    String rutaFisica = "img/tipos/" + nombreArchivo;
	                    
	                    File archivo = new File(rutaFisica);

	                    if (archivo.exists()) {
	                        iv.setImage(new Image(archivo.toURI().toString()));
	                        iv.setVisible(true);
	                    } else {
	                        System.out.println("No existe el archivo para el tipo: " + rutaFisica);
	                        iv.setVisible(false);
	                    }
	                } catch (Exception e) {
	                    iv.setVisible(false);
	                }
	            } else {
	                // Si el Enum es null (el segundo tipo suele serlo), limpiamos y ocultamos
	                iv.setImage(null);
	                iv.setVisible(false);
	            }
	        }
	    }
	    
	    private void cargarTiposMovimientos(Pokemon p) {
	        // 1. Array de ImageViews para los iconos de tipo de cada ataque
	        ImageView[] imgsMovs = {imgMovimiento1, imgMovimiento2, imgMovimiento3, imgMovimiento4};
	        
	        // 2. Obtenemos la lista de movimientos que ya cargamos en el PokemonCrud
	        List<Movimiento> movimientos = p.getMovimientos();

	        for (int i = 0; i < imgsMovs.length; i++) {
	            ImageView iv = imgsMovs[i];

	            // Verificamos si existe el movimiento en esta posición
	            if (movimientos != null && i < movimientos.size() && movimientos.get(i) != null) {
	                Movimiento m = movimientos.get(i);
	                
	                try {
	                    // --- LA CORRECCIÓN ESTÁ AQUÍ ---
	                    // Obtenemos el tipo del movimiento, lo pasamos a minúsculas y armamos la ruta
	                    if (m.getTipo() != null) {
	                        String nombreArchivo = m.getTipo().name().toLowerCase() + ".png";
	                        String rutaFisica = "img/tipos/" + nombreArchivo;
	                        
	                        File archivo = new File(rutaFisica);

	                        if (archivo.exists()) {
	                            iv.setImage(new Image(archivo.toURI().toString()));
	                            iv.setVisible(true);
	                        } else {
	                            // Si no existe la imagen del tipo, ocultamos el ImageView
	                            iv.setVisible(false);
	                        }
	                    } else {
	                        iv.setVisible(false);
	                    }
	                } catch (Exception e) {
	                    iv.setVisible(false);
	                }
	            } else {
	                // Si el slot está vacío (el Pokémon tiene menos de 4 ataques), limpiamos y ocultamos
	                iv.setImage(null);
	                iv.setVisible(false);
	            }
	        }
	    }
	    
	    private void actualizarPokemonSeleccionado(Pokemon p) {
	        
	        //Cargar la imagen del Pokémon
	        String rutaFisica = "img/pokemon/front/" + p.getNum_pokedex() + ".gif";
	        File archivo = new File(rutaFisica);

	        if (archivo.exists() && !archivo.isDirectory()) {
	            try {
	                imgPokemonSelect.setImage(new Image(archivo.toURI().toString()));
	            } catch (Exception ex) {
	                System.err.println("Error al cargar imagen seleccionada: " + rutaFisica);
	            }
	        }
	        cargarImagenesTipos(p);   
	    }
	    
	    private void actualizarColorBarraVida(ProgressBar barra, double progreso) {
	        // Eliminamos los estilos anteriores para no acumularlos
	        barra.getStyleClass().removeAll("barra-verde", "barra-amarilla", "barra-roja");

	        if (progreso > 0.5) {
	            // Más del 50%
	            barra.setStyle("-fx-accent: #2ecc71;"); // Verde
	        } else if (progreso > 0.2) {
	            // Entre 20% y 50%
	            barra.setStyle("-fx-accent: #f1c40f;"); // Amarillo
	        } else {
	            // Menos del 20%
	            barra.setStyle("-fx-accent: #e74c3c;"); // Rojo
	        }
	    }
	    
	    private void aplicarParpadeoSeleccion(int seleccion) {
	        //Agrupamos tus variables en un array
	        ImageView[] selects = {imgSelect1, imgSelect2, imgSelect3, imgSelect4, imgSelect5, imgSelect6};

	        //Limpieza total: paramos animación y OCULTAMOS todas las imágenes
	        if (animacionActiva != null) {
	            animacionActiva.stop();
	        }
	        
	        for (ImageView img : selects) {
	            if (img != null) {
	                img.setVisible(false); //Todas invisibles por defecto
	                img.setOpacity(1.0);    //Reset opacidad para la próxima vez
	            }
	        }

	        //Validar rango (1-6). Si no hay selección válida, no mostramos ninguna.
	        if (seleccion < 1 || seleccion > 6) return;

	        //Activar la imagen seleccionada
	        ImageView imgElegida = selects[seleccion - 1];

	        if (imgElegida != null) {
	            imgElegida.setVisible(true); //Solo esta se vuelve visible

	            //Configurar y lanzar el parpadeo
	            animacionActiva = new FadeTransition(Duration.millis(600), imgElegida);
	            animacionActiva.setFromValue(1.0);
	            animacionActiva.setToValue(0.2);
	            animacionActiva.setCycleCount(FadeTransition.INDEFINITE);
	            animacionActiva.setAutoReverse(true);
	            animacionActiva.play();
	        }
	    }
	    private void activarMover(int seleccion) {
	        //Agrupamos tus variables en un array
	        ImageView[] mover = {imgMover1, imgMover2, imgMover3, imgMover4, imgMover5, imgMover6};

	        Button[] botonesMover = {botonMover1, botonMover2, botonMover3, botonMover4, botonMover5, botonMover6};
	        
	        Label[] moverlbl = {lblMover1, lblMover2, lblMover3, lblMover4, lblMover5, lblMover6};
	        
	        for (ImageView img : mover) {
	            if (img != null) {
	                img.setVisible(false); //Todas invisibles por defecto
	            }
	        }
	        
	        for (Button button : botonesMover) {
	            if (button != null) {
	            	button.setDisable(true); //Todas invisibles por defecto
	            }
	        }
	        
	        for (Label label : moverlbl) {
	            if (label != null) {
	            	label.setVisible(false); //Todas invisibles por defecto
	            }
	        }

	        //Validar rango (1-6). Si no hay selección válida, no mostramos ninguna.
	        if (seleccion < 1 || seleccion > 6) return;

	        //Activar la imagen seleccionada
	        ImageView imgElegida = mover[seleccion - 1];

	        if (imgElegida != null) {
	            imgElegida.setVisible(true); //Solo esta se vuelve visible
	        }

	        
	        Button botonElegido = botonesMover[seleccion - 1];
	        
	        if (botonElegido != null) {
	        	botonElegido.setDisable(false); //Solo esta se vuelve visible
	        }
	        
	        Label moverElegido = moverlbl[seleccion - 1];
	        
	        if (moverElegido != null) {
	        	moverElegido.setVisible(true); //Solo esta se vuelve visible
	        }
	        
	    }
	    
	    private List<FadeTransition> animacionesMover = new ArrayList<>();

	    private void aplicarParpadeoDestinos(int seleccionOrigen) {
	        // 1. Limpiar animaciones anteriores
	        for (FadeTransition ft : animacionesMover) {
	            ft.stop();
	        }
	        animacionesMover.clear();

	        // 2. Array de tus ImageViews de movimiento
	        ImageView[] moverImgs = {imgSelect1, imgSelect2, imgSelect3, imgSelect4, imgSelect5, imgSelect6};

	        // Obtenemos cuántos pokémon hay realmente en el equipo
	        int numPokemonReales = (e.getEquipo1() != null) ? e.getEquipo1().size() : 0;

	        for (int i = 0; i < moverImgs.length; i++) {
	            int posicionActual = i + 1;
	            ImageView img = moverImgs[i];

	            if (img == null) continue;

	            // Resetear visibilidad y opacidad siempre al principio
	            img.setOpacity(1.0);
	            img.setVisible(false); // Por defecto todos invisibles

	            // --- LA CLAVE ESTÁ AQUÍ ---
	            // Solo parpadea si:
	            // 1. No es el origen.
	            // 2. La posición tiene un Pokémon real (está dentro del tamaño de la lista).
	            if (posicionActual != seleccionOrigen && posicionActual <= numPokemonReales) {
	                img.setVisible(true);
	                
	                FadeTransition ft = new FadeTransition(Duration.millis(500), img);
	                ft.setFromValue(1.0);
	                ft.setToValue(0.2);
	                ft.setCycleCount(Animation.INDEFINITE);
	                ft.setAutoReverse(true);
	                ft.play();
	                
	                animacionesMover.add(ft);
	            } else {
	                // Si es el origen o es un hueco vacío, se queda invisible
	                img.setVisible(false);
	            }
	        }
	    }
	    
	    private void limpiarModoMover() {
	        modoMover = false;
	        posicionOrigen = -1;
	        lblACaja.setVisible(false);
	        lblACaja.setDisable(true);
	        }
	    
	    private void ejecutarIntercambio(int posicionDestino) {
	        if (posicionOrigen == posicionDestino) {
	            limpiarModoMover();
	            return;
	        }

	        try (Connection conexion = conBD.getConnection()) {
	            if (conexion != null) {
	                // 1. Mover en la base de datos
	                PokemonCrud.moverPokemon(conexion, e.getIdEntrenador(), posicionOrigen, posicionDestino);
	                
	                // 2. Compactar el equipo
	                PokemonCrud.compactarEquipo(conexion, e.getIdEntrenador());

	                // 3. Volver a cargar los Pokémon de la DB al objeto Java 'e'
	                PokemonCrud.obtenerPokemon1(conexion, this.e); 

	                // 4. Pintar la interfaz con las nuevas posiciones
	                rellenarDatosEquipo(); 
	                
	                // --- NUEVA LÓGICA DE SELECCIÓN ---
	                // 5. Buscamos el Pokémon que ahora está en la posición de destino
	                int indice = posicionDestino - 1;
	                if (indice >= 0 && indice < e.getEquipo1().size()) {
	                    Pokemon p = e.getEquipo1().get(indice);
	                    
	                    // Actualizamos todos los componentes visuales
	                    actualizarPokemonSeleccionado(p);
	                    cargarTiposMovimientos(p);
	                    mostrarStats(p);
	                    aplicarParpadeoSeleccion(posicionDestino);
	                    activarMover(posicionDestino);
	                    
	                    // Muy importante: actualizamos posicionOrigen para que sea la nueva
	                    this.posicionOrigen = posicionDestino;
	                }
	                
	                System.out.println("Intercambio completado. Pokémon seleccionado en posición: " + posicionDestino);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        } finally {
	            // Al terminar, quitamos el modo mover (las flechas parpadeantes de destino)
	            // pero NO limpiamos la posicionOrigen si queremos que siga "marcado"
	            modoMover = false;
	            lblACaja.setVisible(false);
	            lblACaja.setDisable(true);
	            // Nota: He sacado el contenido de limpiarModoMover() aquí para no resetear posicionOrigen
	        }
	    }
	    
	    public void prepararParaIntercambio1(Entrenador ent, Pokemon pCaja, CajaController padre) {
	        this.e = ent;
	        this.pokemonVieneDeCaja = pCaja;
	        this.cajaPadre = padre;
	        this.modoIntercambio = true; // Variable nueva para diferenciar el estado
	        
	        // Cargamos los datos para que el equipo se vea
	        try (Connection con = conBD.getConnection()) {
	            PokemonCrud.obtenerPokemon1(con, e);
	        } catch (SQLException ex) { ex.printStackTrace(); }
	        
	        rellenarDatosEquipo();
	        aplicarParpadeoDestinos(pCaja.getCaja());
	        
	        
	        // UI: Puedes cambiar el título o un label para avisar: "Elige quién sale del equipo"
	        //lblACaja.setText("SELECCIONA EL DESTINO");
	        //lblACaja.setVisible(true);
	        
	    }
	    
	    private void gestionarClickPokemon(int posicionClicada) {
	        if (posicionClicada < 1) return;
	        int indice = posicionClicada - 1;

	        // --- RAMA 1: INTERCAMBIO CON LA CAJA ---
	        if (modoIntercambio) {
	            ejecutarIntercambioCajaEquipo(indice);
	            Stage stage = (Stage) LblPokemon1.getScene().getWindow();
	            stage.close();
	            return; 
	        }

	        // --- RAMA 2: MOVER DENTRO DEL EQUIPO ---
	        if (modoMover) {
	            ejecutarIntercambio(posicionClicada);
	            
	            // Refrescamos la UI seleccionando al primero por seguridad
	            if (e.getEquipo1() != null && !e.getEquipo1().isEmpty()) {
	                Pokemon p = e.getEquipo1().get(0);
	                //actualizarInterfaz(p, 1);
	            }
	        } 
	        // --- RAMA 3: SELECCIÓN NORMAL ---
	        else {
	            if (indice >= 0 && indice < e.getEquipo1().size()) {
	                this.posicionOrigen = posicionClicada;
	                Pokemon p = e.getEquipo1().get(indice);
	                actualizarInterfaz(p, posicionClicada);
	            } else {
	                System.out.println("Has clicado en un hueco vacío.");
	            }
	        }
	    }
	    
	    private void actualizarInterfaz(Pokemon p, int posicion) {
	        actualizarPokemonSeleccionado(p);
	        cargarTiposMovimientos(p);
	        mostrarStats(p);
	        aplicarParpadeoSeleccion(posicion);
	        activarMover(posicion);
	    }
	    
	    private void ejecutarIntercambioCajaEquipo(int indiceEquipo) {
	    	try (Connection conexion = conBD.getConnection()) {
	            // 1. Solo necesitamos al de la caja (el que viene de fuera)
	            Pokemon pCaja = this.pokemonVieneDeCaja;

	            // 2. Calculamos las posiciones basándonos en la lógica pura:
	            // posA: El slot del equipo que el usuario clicó (1, 2, 3, 4, 5 o 6)
	            int posEquipoReal = indiceEquipo + 1; 
	            
	            // posB: La posición que tiene el pokémon en la caja actualmente (> 6)
	            int posCajaReal = pCaja.getCaja();

	            System.out.println("INTERCAMBIO SEGURO: Equipo Pos " + posEquipoReal + " <-> Caja Pos " + posCajaReal);

	            // 3. Tu método del -1 (Asegúrate de que use el idEntrenador)
	            PokemonCrud.intercambiarPosicion(conexion, e.getIdEntrenador(), posEquipoReal, posCajaReal);

	            // 4. MANTENIMIENTO: Compactamos ambos para evitar ceros o huecos
	            PokemonCrud.compactarEquipo(conexion, e.getIdEntrenador());
	            PokemonCrud.compactarCaja(conexion, e.getIdEntrenador());

	            // 5. RECARGA: Traemos los datos nuevos de la DB al Entrenador
	            PokemonCrud.obtenerPokemon1(conexion, e);
	            PokemonCrud.obtenerPokemon2(conexion, e);

	            // 6. UI: Refrescamos la caja que se ve al fondo
	            if (cajaPadre != null) {
	                cajaPadre.rellenarCaja(e.getEquipo2());
	            }
	            
	            // 7. CIERRE: Adios ventana de selección
	            Stage stage = (Stage) LblPokemon1.getScene().getWindow();
	            stage.close();

	        } catch (SQLException ex) {
	            System.err.println("Error en el intercambio: " + ex.getMessage());
	            ex.printStackTrace();
	        }
	    }
	}


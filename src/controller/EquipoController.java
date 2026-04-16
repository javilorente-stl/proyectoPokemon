package controller;

	import java.io.File;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Entrenador;
import modelo.Movimiento;
import modelo.Pokemon;
import modelo.Tipo;
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
	    	//PokemonCrud.moverPokemon(conexion, e, 1, 0);
	    	lblACaja.setVisible(true);
	    }

	    @FXML
	    void mover2(ActionEvent event) {

	    }

	    @FXML
	    void mover3(ActionEvent event) {

	    }

	    @FXML
	    void mover4(ActionEvent event) {

	    }

	    @FXML
	    void mover5(ActionEvent event) {

	    }

	    @FXML
	    void mover6(ActionEvent event) {

	    }
	    
	    @FXML
	    void pokemonSeleccionado1(ActionEvent event) {
	    	aplicarParpadeoSeleccion(1);
	    	activarMover(1);
	    }

	    @FXML
	    void pokemonSeleccionado2(ActionEvent event) {
	    	aplicarParpadeoSeleccion(2);
	    	activarMover(2);
	    }

	    @FXML
	    void pokemonSeleccionado3(ActionEvent event) {
	    	aplicarParpadeoSeleccion(3);
	    	activarMover(3);
	    }

	    @FXML
	    void pokemonSeleccionado4(ActionEvent event) {
	    	aplicarParpadeoSeleccion(4);
	    	activarMover(4);
	    }

	    @FXML
	    void pokemonSeleccionado5(ActionEvent event) {
	    	aplicarParpadeoSeleccion(5);
	    	activarMover(5);
	    }

	    @FXML
	    void pokemonSeleccionado6(ActionEvent event) {
	    	aplicarParpadeoSeleccion(6);
	    	activarMover(6);
	    }
	    
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

	    public void recibirDatos(Entrenador ent) throws SQLException {
	        this.e = ent;
	        
	        ConexionBD conBD = new ConexionBD();
	        try (Connection conexion = conBD.getConnection()) {
	            if (conexion != null) {
	                PokemonCrud.compactarEquipo(conexion, e.getIdEntrenador());
	                PokemonCrud.obtenerPokemon1(conexion, this.e);
	            }
	        }
	        
	        rellenarDatosEquipo();
	        
	        if (e.getEquipo1() != null && !e.getEquipo1().isEmpty()) {
	            mostrarStats(e.getEquipo1().get(0));
	        }
	    }
	    
	    public void rellenarDatosEquipo() {
	        //Por defecto es el 1
	    	aplicarParpadeoSeleccion(1);
	    	activarMover(1);
	    	lblACaja.setStyle("-fx-background-color: white;");
	    	
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
	                vidas[i].setText(p.getVitalidad() +"/"+ p.getVitalidadMax()); 
	                
	                barrasVida[i].setProgress(p.getVitalidad() / p.getVitalidadMax());

	                try {
	                    //Construimos la ruta relativa desde la raíz del proyecto
	                    String rutaFisica = "img/pokemon/front/" + p.getNum_pokedex() + ".gif";
	                    
	                    //Creamos un objeto File
	                    File archivo = new File(rutaFisica);

	                    if (archivo.exists()) {
	                        //Convertimos el archivo a una URI que Image pueda entender
	                        Image img = new Image(archivo.toURI().toString());
	                        imagenes[i].setImage(img);
	                    } else {
	                        System.out.println("No existe el archivo físico en: " + archivo.getAbsolutePath());
	                    }
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	                
	                if(i==0) {
	                	
	                	String rutaFisica = "img/pokemon/front/" + p.getNum_pokedex() + ".gif";
	                	File archivo = new File(rutaFisica);

	                	if (archivo.exists() && !archivo.isDirectory()) {
	                	    try {
	                	        imgPokemonSelect.setImage(new Image(archivo.toURI().toString()));
	                	    } catch (Exception e) {
	                	        System.err.println("Error al procesar el archivo: " + rutaFisica);
	                	    }
	                	}
	                	
	                	//Método para cargar las imágenes de los tipos del pokemon seleccionado
	                	cargarImagenesTipos(p);
	                	
	               
	                }
	                	
	                
	                // SetVisible(true) para mostrar el slot ocupado
	                botones[i].setVisible(true);
	                nombres[i].setVisible(true);
	                niveles[i].setVisible(true);
	                vidas[i].setVisible(true);
	                //Tratamos la barras
	                double porcentajeVida = (double) p.getVitalidad() / p.getVitalidadMax();
	                barrasVida[i].setProgress(porcentajeVida);
	                actualizarColorBarraVida(barrasVida[i], porcentajeVida);   
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
	    	
	    	double maxStatGeneral = p.getNivel() * 5.0;
	    	
	    	double maxVidaSegunNivel = 15.0 + (p.getNivel() * 5.0);
	    	
	    	statVitalidadLbl.setText(p.getVitalidadMax() + "/" + (int)maxVidaSegunNivel);
	        vitalidadBar.setProgress(p.getVitalidadMax() / maxVidaSegunNivel);
	    	
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
	}


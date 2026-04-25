package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dao.CombateCrud;
import dao.ConexionBD;
import dao.EntrenadorCrud;
import dao.PokemonCrud;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.Combate;
import modelo.Entrenador;
import modelo.Estado;
import modelo.Log;
import modelo.Movimiento;
import modelo.Pokemon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LigaPokemonController {


		private Stage stage;
		public Entrenador e;
		private boolean sonido=false;
		private MediaPlayer mediaPlayer;
		private MediaPlayer reproductorGrito;
		ConexionBD conBD = new ConexionBD();
		private Pokemon pokemonSeleccionado1;
		private Pokemon pokemonEnemigo;
		private Combate combate;
		private CombateCrud combateCrud = new CombateCrud();
		private List<Entrenador> altoMando = new ArrayList<>();
		private boolean penalizacionDinero = false;
		private boolean cambioForzadoPorMuerte = false;
		private Log logCombate;
		
		private int faseActual = 0; // Para saber contra quién pelea el jugador (0 a 4)
		private int[] IDS_LIGA;
		
		
	    @FXML
	    private ProgressBar barAmigo;

	    @FXML
	    private ProgressBar barEnemigo;

	    @FXML
	    private Button botnLuchar;

	    @FXML
	    private Button botonHuir;

	    @FXML
	    private Button botonMochila;

	    @FXML
	    private Button botonPokemon;
	    
	    @FXML
	    private Button btnMov1;

	    @FXML
	    private Button btnMov2;

	    @FXML
	    private Button btnMov3;

	    @FXML
	    private Button btnMov4;

	    @FXML
	    private ImageView estadoAmigo;

	    @FXML
	    private ImageView estadoEnemigo;

	    @FXML
	    private ImageView imgBack;

	    @FXML
	    private ImageView imgFondo;

	    @FXML
	    private ImageView imgFront;

	    @FXML
	    private ImageView imgTipoMov1;

	    @FXML
	    private ImageView imgTipoMov2;

	    @FXML
	    private ImageView imgTipoMov3;

	    @FXML
	    private ImageView imgTipoMov4;

	    @FXML
	    private ImageView imgVolverMov;
	    
	    @FXML
	    private ImageView imgSonido;
	    
	    @FXML
	    private ImageView imgEntrenadora;
	    
	    @FXML
	    private Label lblNombreRival;

	    @FXML
	    private Label lblMov1;

	    @FXML
	    private Label lblMov2;

	    @FXML
	    private Label lblMov3;

	    @FXML
	    private Label lblMov4;

	    @FXML
	    private Label lblPP1;

	    @FXML
	    private Label lblPP2;

	    @FXML
	    private Label lblPP3;

	    @FXML
	    private Label lblPP4;

	    @FXML
	    private Label nivelAmigo;

	    @FXML
	    private Label nivelEnemigo;

	    @FXML
	    private Label nombreAmigo;

	    @FXML
	    private Label nombreEnemigo;

	    @FXML
	    private ImageView sexoAmigo;

	    @FXML
	    private ImageView sexoEnemigo;

	    @FXML
	    private Label vitalidadAmigo;

	    @FXML
	    private Label vitalidadEnemigo;
	    
	    @FXML
	    private Label lblTextoCombate;
	    
	    @FXML
	    private AnchorPane menuMovimientos;
	    
	    @FXML
	    void activarSonido(MouseEvent event) {
	    	sonido();
	    }
	    
	    @FXML
	    void sonidoPokemonAmigo(MouseEvent event) {
	        if (pokemonSeleccionado1 == null) return;

	        try {
	            // %03d significa: un número entero (d), con 3 dígitos (3), rellenando con ceros (0)
	            String numeroFormateado = String.format("%03d", pokemonSeleccionado1.getNum_pokedex());
	            
	            // Ahora la ruta será: son/gritos/001.mp3, son/gritos/025.mp3, etc.
	            String rutaGrito = "son/pokemon/" + numeroFormateado + ".wav";
	            File archivo = new File(rutaGrito);

	            if (archivo.exists()) {
	                if (reproductorGrito != null) {
	                    reproductorGrito.stop();
	                }

	                Media sound = new Media(archivo.toURI().toString());
	                reproductorGrito = new MediaPlayer(sound);
	                reproductorGrito.setVolume(0.8); 
	                reproductorGrito.play();
	            } else {
	                System.err.println("Archivo no encontrado: " + rutaGrito);
	            }
	        } catch (Exception e) {
	            System.err.println("Error al reproducir el grito: " + e.getMessage());
	        }
	    }

	    @FXML
	    void sonidoPokemonEnemigo(MouseEvent event) {
	    	if (pokemonEnemigo == null) return;

	        try {
	            // %03d significa: un número entero (d), con 3 dígitos (3), rellenando con ceros (0)
	            String numeroFormateado = String.format("%03d", pokemonEnemigo.getNum_pokedex());
	            
	            // Ahora la ruta será: son/gritos/001.mp3, son/gritos/025.mp3, etc.
	            String rutaGrito = "son/pokemon/" + numeroFormateado + ".wav";
	            File archivo = new File(rutaGrito);

	            if (archivo.exists()) {
	                if (reproductorGrito != null) {
	                    reproductorGrito.stop();
	                }

	                Media sound = new Media(archivo.toURI().toString());
	                reproductorGrito = new MediaPlayer(sound);
	                reproductorGrito.setVolume(0.8); 
	                reproductorGrito.play();
	            } else {
	                System.err.println("Archivo no encontrado: " + rutaGrito);
	            }
	        } catch (Exception e) {
	            System.err.println("Error al reproducir el grito: " + e.getMessage());
	        }
	    }
	    
	    @FXML
	    void usarMov1(ActionEvent event) {
	    	procesarTurno(0);
	    }

	    @FXML
	    void usarMov2(ActionEvent event) {
	    	procesarTurno(1);
	    }

	    @FXML
	    void usarMov3(ActionEvent event) {
	    	procesarTurno(2);
	    }

	    @FXML
	    void usarMov4(ActionEvent event) {
	    	procesarTurno(3);
	    }

	    @FXML
	    void volverMenuCombate(MouseEvent event) {
	    	menuMovimientos.setVisible(false);
	    	menuMovimientos.setDisable(true);
	    }
	    
	    @FXML
	    void abrirMenuMovimientos(ActionEvent event) {
	    	menuMovimientos.setVisible(true);
	        menuMovimientos.setDisable(false);
	    }

	    
	    @FXML
	    void abrirVistaEquipo(ActionEvent event) {
	        try {
	            //  Guardamos el ID antes del cambio para detectar si el usuario eligió a alguien nuevo
	            int idAntesDelCambio = pokemonSeleccionado1.getId_pokemon();

	            //  Carga de la vista Equipo
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/equipo.fxml"));
	            Parent root = loader.load();
	            EquipoController controller = loader.getController();
	            
	            // Pasamos la referencia de este controlador (Liga) al equipo
	            controller.prepararParaCanjeLiga(this.e, this);

	            Stage stage = new Stage();
	            stage.initModality(Modality.APPLICATION_MODAL); 
	            stage.setScene(new Scene(root));
	            
	            // Bloquea el hilo hasta que se cierre la ventana de equipo
	            stage.showAndWait(); 

	            // ---  LÓGICA POST-CAMBIO ---
	            // Si el ID es diferente, el usuario seleccionó un nuevo Pokémon
	            if (pokemonSeleccionado1.getId_pokemon() != idAntesDelCambio) {
	                
	            	String infoJugador = pokemonSeleccionado1.getInfoLog(this.e.getNombre());
	                String infoRival = pokemonEnemigo.getInfoLog(altoMando.get(faseActual).getNombre());
	                
	                logCombate.registrarTurno("cambio1", infoJugador, infoRival, combate.getTurno());
	                actualizarInterfaz();
	                cargarImagenesCombate();
	                prepararMenuMovimientos();
	                registrarEnPantalla("¡Adelante, " + pokemonSeleccionado1.getNombre() + "!");

	                // REVISIÓN DE LA BANDERA DE MUERTE
	                if (cambioForzadoPorMuerte) {
	                    // CASO A: Venimos de un Pokémon debilitado. 
	                    // El cambio es "limpio", el jugador recupera el control.
	                    cambioForzadoPorMuerte = false; // Reseteamos la bandera
	                    
	                    menuMovimientos.setVisible(true);
	                    menuMovimientos.setDisable(false);
	                    registrarEnPantalla("¿Qué debería hacer " + pokemonSeleccionado1.getNombre() + "?");
	                } else {
	                    // CASO B: Cambio manual estratégico durante el combate.
	                    // El rival aprovecha para atacar.
	                    procesarTurnoRivalSolo();
	                }
	            }

	        } catch (IOException e) {
	            System.err.println("Error al cargar la vista de equipo: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	    
	    private void procesarTurnoRivalSolo() {
	        //  Bloqueamos el menú para que el jugador no pueda clicar nada mientras el rival ataca
	        menuMovimientos.setVisible(false);
	        menuMovimientos.setDisable(true);

	        //  IA Rival decide movimiento
	        Random rand = new Random();
	        List<Movimiento> movsEnemigo = pokemonEnemigo.getMovimientos();
	        Movimiento movRival = movsEnemigo.get(rand.nextInt(movsEnemigo.size()));

	        Timeline secuenciaTurno = new Timeline();
	        int tiempoAcumulado = 1; // Un segundo de pausa para que el jugador vea a su nuevo Pokémon

	        // --- ATAQUE DEL RIVAL ---
	        secuenciaTurno.getKeyFrames().add(new KeyFrame(Duration.seconds(tiempoAcumulado), ev -> {
	            atacar(movRival, pokemonEnemigo, pokemonSeleccionado1);
	        }));

	        // --- EFECTOS FINALES ---
	        tiempoAcumulado += 2;
	        secuenciaTurno.getKeyFrames().add(new KeyFrame(Duration.seconds(tiempoAcumulado), ev -> {
	            combate.aplicarEfectosFinales(pokemonSeleccionado1);
	            combate.aplicarEfectosFinales(pokemonEnemigo);
	            actualizarInterfaz();
	        }));

	        // --- FINALIZACIÓN ---
	        tiempoAcumulado += 1;
	        secuenciaTurno.getKeyFrames().add(new KeyFrame(Duration.seconds(tiempoAcumulado), ev -> {
	            // Incrementamos el turno global del combate
	            combate.setTurno(combate.getTurno() + 1);
	            finalizarFlujoTurno();
	        }));

	        secuenciaTurno.play();
	    }

	    @FXML
	    void intentarHuir(ActionEvent event) {

	    }
	    
	    public void sonido() {
	        try {
	            // Inicializar el MediaPlayer si es la primera vez
	            if (mediaPlayer == null) {
	                // Cambia "combate.mp3" por el nombre real de tu archivo de música
	                String ruta = new File("son/Combate.mp3").toURI().toString(); 
	                Media sound = new Media(ruta);
	                mediaPlayer = new MediaPlayer(sound);
	                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Música en bucle
	                mediaPlayer.setVolume(0.5); // Volumen al 50% para no asustar
	            }

	            //  Lógica de Alternancia (Toggle)
	            if (!this.sonido) {
	                // ACTIVAR SONIDO
	                mediaPlayer.play();
	                this.sonido = true;
	                
	                // Actualizar imagen del botón (Asegúrate de que la ruta sea correcta)
	                File fileActivo = new File("img/combate/SonidoActivo.png");
	                if (fileActivo.exists()) {
	                    imgSonido.setImage(new Image(fileActivo.toURI().toString()));
	                }
	            } else {
	                // DESACTIVAR SONIDO
	                mediaPlayer.stop();
	                this.sonido = false;
	                
	                // Actualizar imagen del botón
	                File fileApagado = new File("img/combate/SonidoApagado.png");
	                if (fileApagado.exists()) {
	                    imgSonido.setImage(new Image(fileApagado.toURI().toString()));
	                }
	            }
	        } catch (Exception e) {
	            System.err.println("Error al reproducir el sonido: " + e.getMessage());
	        }
	    }
	    
	    public void recibirDatos(Entrenador entJugador) throws SQLException {
	        this.e = entJugador;

	        //  Definimos los IDs de la liga (el último es el 104, nuestro "Espejo")
	        this.IDS_LIGA = new int[]{100, 101, 102, 103, 104};

	        try (Connection conexion = conBD.getConnection()) {
	            
	            // --- PASO CLAVE: CLONACIÓN ---
	            // Preparamos la base de datos para que el entrenador 104 tenga tu equipo actual
	            // Suponiendo que el método está en EntrenadorCrud
	            EntrenadorCrud.prepararCampeonEspejo(conexion, this.e.getIdEntrenador(), 104);
	            
	            //  Ahora cargamos los datos del jugador (Pokémon y sus ataques)
	            PokemonCrud.obtenerPokemon1(conexion, this.e);

	            //  Cargamos a los rivales. 
	            // Como acabamos de clonar tu equipo al ID 104, cuando este método 
	            // llegue al último ID, cargará tu equipo clonado.
	            cargarRivalesLiga(conexion);

	            // 4. ASIGNAR TU POKÉMON ACTIVO
	            if (this.e.getEquipo1() != null && !this.e.getEquipo1().isEmpty()) {
	                this.pokemonSeleccionado1 = this.e.getEquipo1().stream()
	                    .filter(p -> p.getVitalidad() > 0 && p.getEstado() != Estado.DEBILITADO)
	                    .findFirst()
	                    .orElse(this.e.getEquipo1().get(0));
	            }

	            //  PREPARAR EL ENTORNO DEL PRIMER RIVAL (Fase 0)
	            if (altoMando != null && !altoMando.isEmpty()) {
	                Entrenador actual = altoMando.get(faseActual);
	                if (actual.getEquipo1() != null && !actual.getEquipo1().isEmpty()) {
	                    this.pokemonEnemigo = actual.getEquipo1().get(0);
	                }

	                if (this.pokemonSeleccionado1 != null && this.pokemonEnemigo != null) {
	                    this.combate = new Combate(this.pokemonSeleccionado1, this.pokemonEnemigo);
	                    
	                    // Registro de inicio en el Log
	                    String infoP1 = pokemonSeleccionado1.getInfoLog(this.e.getNombre());
	                    String infoP2 = pokemonEnemigo.getInfoLog(actual.getNombre());
	                    this.logCombate = new Log();
	                    this.logCombate.registrarTurno("inicioCombate", infoP1, infoP2, 1);

	                    mostrarRivalActual();      
	                    prepararMenuMovimientos(); 
	                    cargarImagenesCombate();   
	                    actualizarInterfaz();      
	                }
	            }
	        }
	    }
	    private void mostrarRivalActual() {
	        //  Obtenemos los datos del rival actual
	        Entrenador rival = altoMando.get(faseActual);
	        
	        //  Mostramos la imagen del ENTRENADOR primero
	        imgEntrenadora.setVisible(true);
	        File archivoEntrenador = new File("img/combate/" + rival.getNombre() + ".png");
	        if (archivoEntrenador.exists()) {
	            imgEntrenadora.setImage(new Image(archivoEntrenador.toURI().toString()));
	        }
	        
	        lblNombreRival.setText(rival.getNombre());
	        
	        // Ocultamos los Pokémon y labels de combate por un momento para el efecto visual
	        imgFront.setVisible(false);
	        lblTextoCombate.setText("¡El miembro del Alto Mando " + rival.getNombre() + " te desafía!");

	        // CREAR EL DELAY (2 segundos) para que el jugador vea al rival
	        PauseTransition delay = new PauseTransition(Duration.seconds(2));
	        
	        delay.setOnFinished(event -> {
	            //  Tras el delay, el entrenador lanza su Pokémon
	            if (pokemonEnemigo != null) {
	                
	                // Llamamos a TU MÉTODO para cargar los gifs de espalda y frente
	                cargarImagenesCombate();
	                
	                // Hacemos visible el gif del enemigo y actualizamos el texto
	                imgFront.setVisible(true);
	                lblTextoCombate.setText(rival.getNombre() + " envía a " + pokemonEnemigo.getNombre() + "!");
	                
	                imgEntrenadora.setVisible(false);
	            }
	        });

	        delay.play();
	    }
	    
	    public void siguienteCombate() throws SQLException {
	        faseActual++; // Incrementamos el índice para apuntar al siguiente miembro
	        
	        if (altoMando != null && faseActual < altoMando.size()) {
	            // En lugar de abrir una ventana nueva, reseteamos la actual
	            registrarEnPantalla("--- PREPARANDO SIGUIENTE DESAFÍO ---");
	            
	            // Llamamos al método que limpia el campo y pone al nuevo rival
	            prepararSiguienteCombateLiga();
	            
	        } else {
	            // Lógica de fin de Liga
	            registrarEnPantalla("¡HAS GANADO LA LIGA POKÉMON!");
	            registrarEnPantalla("¡Felicidades, eres el nuevo Campeón!");
	            //Ahora apareceras tu en el combate final
	            IDS_LIGA = new int[]{100, 101, 102, 103, e.getIdEntrenador()};
	            volverAlMenu(stage);
	            // Bloqueamos el menú de movimientos para que no se pueda seguir atacando al aire
	            menuMovimientos.setDisable(true);
	            
	            
	        }
	    }
	    
	    public void cargarRivalesLiga(Connection conexion) {
	        try {
	            altoMando.clear(); // Limpiamos por si acaso
	            for (int id : IDS_LIGA) {
	                Entrenador rival = EntrenadorCrud.obtenerEntrenadorFijo(conexion, id);
	                if (rival != null) {
	                    altoMando.add(rival);
	                    PokemonCrud.obtenerPokemon1(conexion, rival); 
	                    altoMando.add(rival);
	                }
	            }
	            System.out.println(">>> LIGA: Se han cargado " + altoMando.size() + " entrenadores.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.err.println("Error al cargar los entrenadores de la Liga.");
	        }
	    }
	    public void setPokemonSeleccionado1(Pokemon p) {
	        this.pokemonSeleccionado1 = p;
	    }
	    public Pokemon getPokemonSeleccionado1() {
	        return this.pokemonSeleccionado1;
	    }

	    private void cargarImagenesCombate() {
	        //  Cargar imagen del Pokémon del JUGADOR (Espalda - Back)
	        File archivoBack = new File("img/pokemon/back/" + pokemonSeleccionado1.getNum_pokedex() + ".gif");
	        if (archivoBack.exists()) {
	            imgBack.setImage(new Image(archivoBack.toURI().toString()));
	        } else {
	            System.err.println("No se encontró el gif de espalda para: " + pokemonSeleccionado1.getNombre());
	            // Opcional: Cargar una imagen por defecto si no existe el gif
	        }

	        //  Cargar imagen del Pokémon ENEMIGO (Frente - Front)
	        File archivoFront = new File("img/pokemon/front/" + pokemonEnemigo.getNum_pokedex() + ".gif");
	        if (archivoFront.exists()) {
	            imgFront.setImage(new Image(archivoFront.toURI().toString()));
	        } else {
	            System.err.println("No se encontró el gif de frente para: " + pokemonEnemigo.getNombre());
	        }


	        //Actualizar estados visuales
	        actualizarImagenEstado();
	    }
	    
	    
	    private void actualizarImagenEstado() {
	        //  PROCESAR ESTADO DEL POKÉMON AMIGO
	        if (pokemonSeleccionado1.getEstado() == Estado.VIVO || pokemonSeleccionado1.getEstado() == Estado.DEBILITADO) {
	            estadoAmigo.setVisible(false);
	        } else {
	            // Buscamos el archivo basado en el Enum (ej: img/estados/paralizado.png)
	            File fAmigo = new File("img/estados/" + pokemonSeleccionado1.getEstado().name().toLowerCase() + ".png");
	            if (fAmigo.exists()) {
	                estadoAmigo.setImage(new Image(fAmigo.toURI().toString()));
	                estadoAmigo.setVisible(true);
	            } else {
	                estadoAmigo.setVisible(false);
	            }
	        }

	        //  PROCESAR ESTADO DEL POKÉMON ENEMIGO 
	        if (pokemonEnemigo.getEstado() == Estado.VIVO || pokemonEnemigo.getEstado() == Estado.DEBILITADO) {
	            estadoEnemigo.setVisible(false);
	        } else {
	            File fEnemigo = new File("img/estados/" + pokemonEnemigo.getEstado().name().toLowerCase() + ".png");
	            if (fEnemigo.exists()) {
	                estadoEnemigo.setImage(new Image(fEnemigo.toURI().toString()));
	                estadoEnemigo.setVisible(true);
	            } else {
	                estadoEnemigo.setVisible(false);
	            }
	        }
	    }
	    
	    private void actualizarInterfaz() {
	        //  ACTUALIZACIÓN DEL POKÉMON AMIGO (JUGADOR)
	        // Texto de vitalidad: "100 / 120"
	        vitalidadAmigo.setText(pokemonSeleccionado1.getVitalidad() + " / " + pokemonSeleccionado1.getVitalidadMax());
	        
	        // Cálculo de progreso para la barra (0.0 a 1.0)
	        double ratioAmigo = (double) pokemonSeleccionado1.getVitalidad() / pokemonSeleccionado1.getVitalidadMax();
	        barAmigo.setProgress(ratioAmigo);
	        
	        // Cambiar color de la barra según la salud (Opcional pero recomendado)
	        cambiarColorBarra(barAmigo, ratioAmigo);

	        //  ACTUALIZACIÓN DEL POKÉMON ENEMIGO
	        vitalidadEnemigo.setText(pokemonEnemigo.getVitalidad() + " / " + pokemonEnemigo.getVitalidadMax());
	        
	        double ratioEnemigo = (double) pokemonEnemigo.getVitalidad() / pokemonEnemigo.getVitalidadMax();
	        barEnemigo.setProgress(ratioEnemigo);
	        
	        cambiarColorBarra(barEnemigo, ratioEnemigo);

	        //  ACTUALIZAR ICONOS DE ESTADO
	        // Llamamos al método que hicimos antes para ocultar/mostrar estados
	        actualizarImagenEstado();

	        // 4. ACTUALIZAR NOMBRES Y NIVELES (Por si hubo un cambio o inicialización)
	        nombreAmigo.setText(pokemonSeleccionado1.getNombre());
	        nombreEnemigo.setText(pokemonEnemigo.getNombre());
	        nivelAmigo.setText("Lv" + pokemonSeleccionado1.getNivel());
	        nivelEnemigo.setText("Lv" + pokemonEnemigo.getNivel());
	    }
	    
	    private void cambiarColorBarra(ProgressBar bar, double ratio) {
	        if (ratio > 0.5) {
	            bar.setStyle("-fx-accent: #4CAF50;"); // Verde
	        } else if (ratio > 0.2) {
	            bar.setStyle("-fx-accent: #FFEB3B;"); // Amarillo
	        } else {
	            bar.setStyle("-fx-accent: #F44336;"); // Rojo
	        }
	    }
	    
	    private void prepararMenuMovimientos() {
	        List<Movimiento> movs = pokemonSeleccionado1.getMovimientos();
	        
	        // Arrays para iterar Labels e ImageViews
	        Label[] nombres = {lblMov1, lblMov2, lblMov3, lblMov4};
	        Label[] pps = {lblPP1, lblPP2, lblPP3, lblPP4};
	        ImageView[] iconosTipo = {imgTipoMov1, imgTipoMov2, imgTipoMov3, imgTipoMov4};

	        for (int i = 0; i < 4; i++) {
	            if (i < movs.size()) {
	                Movimiento m = movs.get(i);
	                
	                //  Setear Textos
	                nombres[i].setText(m.getNombre());
	                pps[i].setText(String.valueOf(m.getNumPP())); 
	                
	                //  Cargar Imagen del Tipo
	                if (m.getTipo() != null) {
	                    // Convertimos el tipo a minúsculas para que coincida con el nombre del archivo
	                    String nombreTipo = m.getTipo().toString().toLowerCase();
	                    File file = new File("img/combate/" + nombreTipo + ".png");
	                    
	                    if (file.exists()) {
	                        iconosTipo[i].setImage(new Image(file.toURI().toString()));
	                        iconosTipo[i].setVisible(true);
	                    } else {
	                        iconosTipo[i].setImage(null);
	                        iconosTipo[i].setVisible(false);
	                    }
	                }

	                //  Habilitar el botón (asumiendo que el padre es el botón)
	                nombres[i].getParent().setDisable(false); 
	                
	            } else {
	                // Si el slot está vacío: limpiar textos e imagen
	                nombres[i].setText("-");
	                pps[i].setText("");
	                
	                iconosTipo[i].setImage(null);
	                iconosTipo[i].setVisible(false);
	                
	                // Deshabilitar botón
	                nombres[i].getParent().setDisable(true); 
	            }
	        }
	    }
	    
	    private void procesarTurno(int indice) {
	        // Validaciones previas
	        if (indice >= pokemonSeleccionado1.getMovimientos().size()) return;

	        Movimiento movJugador = pokemonSeleccionado1.getMovimientos().get(indice);
	        if (movJugador.getNumPP() <= 0) {
	            lblTextoCombate.setText("¡No quedan PP para este movimiento!");
	            return;
	        }

	        //  IA Rival
	        Random rand = new Random();
	        List<Movimiento> movsEnemigo = pokemonEnemigo.getMovimientos();
	        Movimiento movRival = movsEnemigo.get(rand.nextInt(movsEnemigo.size()));

	        //  Bloqueo de UI
	        menuMovimientos.setVisible(false);
	        menuMovimientos.setDisable(true);
	        lblTextoCombate.setText(""); // Limpiamos el log para el nuevo turno

	        //  Determinación de orden
	        boolean jugadorVaPrimero = pokemonSeleccionado1.getVelocidad() >= pokemonEnemigo.getVelocidad();

	        //  Cadena de eventos (Timeline)
	        Timeline secuenciaTurno = new Timeline();
	        int tiempoAcumulado = 0;

	        // --- PRIMER ATAQUE ---
	        secuenciaTurno.getKeyFrames().add(new KeyFrame(Duration.seconds(tiempoAcumulado), ev -> {
	            if (jugadorVaPrimero) {
	                atacar(movJugador, pokemonSeleccionado1, pokemonEnemigo);
	            } else {
	                atacar(movRival, pokemonEnemigo, pokemonSeleccionado1);
	            }
	        }));

	        // --- SEGUNDO ATAQUE (Si el defensor sigue vivo) ---
	        tiempoAcumulado += 2; 
	        secuenciaTurno.getKeyFrames().add(new KeyFrame(Duration.seconds(tiempoAcumulado), ev -> {
	            if (pokemonSeleccionado1.getVitalidad() > 0 && pokemonEnemigo.getVitalidad() > 0) {
	                if (jugadorVaPrimero) {
	                    atacar(movRival, pokemonEnemigo, pokemonSeleccionado1);
	                } else {
	                    atacar(movJugador, pokemonSeleccionado1, pokemonEnemigo);
	                }
	            }
	        }));

	        // --- EFECTOS FINALES (Veneno, Quemaduras, etc.) ---
	        tiempoAcumulado += 2;
	        secuenciaTurno.getKeyFrames().add(new KeyFrame(Duration.seconds(tiempoAcumulado), ev -> {
	            if (pokemonSeleccionado1.getVitalidad() > 0 && pokemonEnemigo.getVitalidad() > 0) {
	                combate.aplicarEfectosFinales(pokemonSeleccionado1);
	                combate.aplicarEfectosFinales(pokemonEnemigo);
	                actualizarInterfaz();
	            }
	        }));

	        // --- FINALIZACIÓN ---
	        tiempoAcumulado += 1;
	        secuenciaTurno.getKeyFrames().add(new KeyFrame(Duration.seconds(tiempoAcumulado), ev -> {
	        	int turnoActual = combate.getTurno();
	            combate.setTurno(turnoActual + 1);
	            
	            System.out.println("Siguiente Turno: " + combate.getTurno());
	            finalizarFlujoTurno();
	        }));

	        secuenciaTurno.play();
	    }

	    /**
	     * Método auxiliar para encapsular la lógica de "X usa Y" y actualizar visualmente
	     */
	    private void atacar(Movimiento mov, Pokemon atk, Pokemon def) {
	        // Mostrar quién ataca
	        registrarEnPantalla("¡" + atk.getNombre() + " usa " + mov.getNombre() + "!");
	        
	        // Ejecutar lógica en el modelo
	        combate.ejecutarTurno(mov, atk, def);
	        
	        // Actualizar barras y etiquetas de vida
	        actualizarInterfaz();
	        
	        // Si el defensor ha muerto, mostrarlo inmediatamente
	        if (def.getVitalidad() <= 0) {
	            registrarEnPantalla("¡" + def.getNombre() + " se ha debilitado!");
	            String infoJugador = pokemonSeleccionado1.getInfoLog(this.e.getNombre());
	            String infoRival = pokemonEnemigo.getInfoLog(altoMando.get(faseActual).getNombre());
	            
	            // Si el defensor es el enemigo, es debilitado2, si es el nuestro, debilitado1
	            String evento = (def == pokemonEnemigo) ? "debilitado2" : "debilitado1";
	            logCombate.registrarTurno(evento, infoJugador, infoRival, combate.getTurno());
	        }
	    }


	    private void registrarEnPantalla(String mensaje) {
	        //  Imprimimos en consola para debug
	        System.out.println(mensaje);
	        
	        //  Obtenemos el texto que ya había + el nuevo mensaje
	        String textoActual = lblTextoCombate.getText();
	        
	        // Si el label está vacío, no ponemos el salto de línea inicial
	        if (textoActual == null || textoActual.isEmpty()) {
	            lblTextoCombate.setText(mensaje);
	        } else {
	            lblTextoCombate.setText(textoActual + "\n" + mensaje);
	        }
	    }
	    
	    
	    
	    private void finalizarFlujoTurno() {
	    	prepararMenuMovimientos(); 
	        
	        combateCrud.guardarProgresoCombate(pokemonSeleccionado1);
	        
	        if (pokemonSeleccionado1.getVitalidad() > 0 && pokemonEnemigo.getVitalidad() > 0) {
	            // ACTIVACIÓN TOTAL: Visible y Habilitado
	            menuMovimientos.setVisible(true);
	            menuMovimientos.setDisable(false); // <--- IMPRESCINDIBLE
	        } else {
	            verificarFinDelCombate();
	        }
	    }
	    
	    
	    private void verificarFinDelCombate() {
	        // Caso 1: El enemigo se debilita
	        if (pokemonEnemigo.getVitalidad() <= 0) {
	        	aplicarExperiencia();
	            menuMovimientos.setDisable(true);
	            combateCrud.guardarProgresoCombate(pokemonSeleccionado1);
	            
	            // Buscamos si el rival actual tiene más Pokémon vivos
	            Entrenador rivalActual = altoMando.get(faseActual);
	            Pokemon siguientePkmEnemigo = rivalActual.getEquipo1().stream()
	                    .filter(p -> p.getVitalidad() > 0)
	                    .findFirst()
	                    .orElse(null);

	            if (siguientePkmEnemigo != null) {
	                // El rival aún tiene Pokémon, sacamos el siguiente
	                mandarSiguientePokemonRival(siguientePkmEnemigo);
	            } else {
	                // El rival ya no tiene más Pokémon, victoria total contra este entrenador
	                procesarVictoria();
	            }
	        } 
	        // Caso 2: Tu Pokémon se debilita
	        else if (pokemonSeleccionado1.getVitalidad() <= 0) {
	            menuMovimientos.setDisable(true);
	            combateCrud.guardarProgresoCombate(pokemonSeleccionado1);
	            procesarDebilitamientoJugador();
	        }
	    }
	    private void procesarDebilitamientoJugador() {
	        //registrarEnPantalla("¡" + pokemonSeleccionado1.getNombre() + " se ha debilitado!");
	        //registrarEnPantalla("¡" + pokemonSeleccionado1.getNombre() + " se ha debilitado!");

	        boolean tienePokemonVivos = e.getEquipo1().stream()
	                                     .anyMatch(p -> p.getVitalidad() > 0 && p.getEstado() != Estado.DEBILITADO);

	        if (tienePokemonVivos) {
	            registrarEnPantalla("¡Elige a otro Pokémon para continuar!");
	            
	            cambioForzadoPorMuerte = true;
	            
	            // La pausa de la Timeline se mantiene para que el texto sea legible
	            Timeline pausa = new Timeline(new KeyFrame(Duration.seconds(1.5), ev -> {
	                // USAMOS Platform.runLater para evitar el IllegalStateException
	                javafx.application.Platform.runLater(() -> {
	                    abrirVistaEquipo(null); 
	                });
	            }));
	            pausa.play();
	        } else {
	            registrarEnPantalla("¡No te quedan Pokémon utilizables!");
	            registrarEnPantalla("¡Has perdido el combate!");
	            String infoJugador = pokemonSeleccionado1.getInfoLog(this.e.getNombre());
		        String infoRival = pokemonEnemigo.getInfoLog(altoMando.get(faseActual).getNombre());
	            logCombate.registrarTurno("finPierdeCombate", infoJugador, infoRival, combate.getTurno());
	            
	            // Pausa antes de salir al menú por derrota
	            final Stage stageActual = (Stage) botnLuchar.getScene().getWindow();
	            Timeline pausaDerrota = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
	                try {
	                    volverAlMenu(stageActual);
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }));
	            pausaDerrota.play();
	        }
	    }
	    
	    private void procesarVictoria() {
	        registrarEnPantalla("¡Has derrotado al miembro del Alto Mando!");

	        String infoJugador = pokemonSeleccionado1.getInfoLog(this.e.getNombre());
	        String infoRival = pokemonEnemigo.getInfoLog(altoMando.get(faseActual).getNombre());
	        logCombate.registrarTurno("finGanaCombate", infoJugador, infoRival, combate.getTurno());
	        
	        // Ajustamos lo del dinero
	        int recompensaBase;
	        if (faseActual == 0) {
	            recompensaBase = 1000;
	        } else {
	            // La base sube 1000 por cada fase después de la primera
	            recompensaBase = faseActual * 1000;
	        }

	        // Si NO descansó, se duplica el premio (excepto quizás en el primero, según tu tabla)
	        int recompensaFinal = penalizacionDinero ? recompensaBase : (recompensaBase * 2);
	        
	        // Ajuste especial para el primer combate según tu lista (siempre 1000)
	        if (faseActual == 0) recompensaFinal = 1000;
	        
	        e.setPokedollars(e.getPokedollars() + recompensaFinal);
	        registrarEnPantalla("¡Has ganado " + recompensaFinal + " PokéDollars!");
	        
	        // Reseteamos penalización para el futuro
	        penalizacionDinero = false;

	        // Guardar dinero en BD
	        try (Connection con = conBD.getConnection()) {
	            EntrenadorCrud.actualizarDinero(con, e.getIdEntrenador(), e.getPokedollars());
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }

	        // Pregunta de curación con Alert de JavaFX
	        javafx.application.Platform.runLater(() -> {
	            
	            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
	            alert.setTitle("Descanso en la Liga");
	            alert.setHeaderText("¿Quieres curar a tu equipo?");
	            alert.setContentText("Si aceptas, la recompensa del próximo combate será de 1000 P$.");

	            javafx.scene.control.ButtonType btnSi = new javafx.scene.control.ButtonType("Sí, curar");
	            javafx.scene.control.ButtonType btnNo = new javafx.scene.control.ButtonType("No, seguir");
	            alert.getButtonTypes().setAll(btnSi, btnNo);

	            alert.showAndWait().ifPresent(respuesta -> {
	                if (respuesta == btnSi) {
	                    curarEquipoCompleto();
	                    penalizacionDinero = true;
	                    registrarEnPantalla("Equipo restaurado. Próxima recompensa penalizada.");
	                }
	            });
	        });

	            // Transición al siguiente rival (después de cerrar el diálogo)
	            Timeline pausa = new Timeline(new KeyFrame(Duration.seconds(2.0), ev -> {
	                faseActual++; // Avanzamos al siguiente miembro
	                
	                if (altoMando.get(faseActual).getEquipo1() == null || altoMando.get(faseActual).getEquipo1().isEmpty()) {
	                    System.err.println("ERROR: El rival " + " no tiene Pokémon.");
	                    // Podrías intentar recargar el equipo aquí o mostrar un mensaje de error
	                    return; 
	                }
	                
	                if (altoMando != null && faseActual < altoMando.size()) {
	                    prepararSiguienteCombateLiga(); // Llamamos a tu método actual
	                } else {
	                    registrarEnPantalla("¡HAS COMPLETADO LA LIGA POKÉMON!");
	                    try {
	                        volverAlMenu((Stage) botnLuchar.getScene().getWindow());
	                    } catch (SQLException e) { e.printStackTrace(); }
	                }
	            }));
	            pausa.play();
	        
	    }
	    
	    private void curarEquipoCompleto() {
	        try (Connection con = conBD.getConnection()) {
	            for (Pokemon p : e.getEquipo1()) {
	                PokemonCrud.curarPokemonTotal(con, p);
	            }
	            actualizarInterfaz();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	    
	    private void aplicarExperiencia() {
	       //registrarEnPantalla("¡" + pokemonEnemigo.getNombre() + " enemigo debilitado!");
	        
	        int expGanada = (pokemonEnemigo.getNivel() * 15) / 2;
	        registrarEnPantalla("¡" + pokemonSeleccionado1.getNombre() + " ha ganado " + expGanada + " puntos de EXP!");

	        int nivelAnterior = pokemonSeleccionado1.getNivel();
	        pokemonSeleccionado1.ganarExperiencia(expGanada);
	        
	        if (pokemonSeleccionado1.getNivel() > nivelAnterior) {
	            registrarEnPantalla("¡Subida de nivel! Ahora es nivel " + pokemonSeleccionado1.getNivel());
	            actualizarInterfaz(); 
	        }

	        // Guardamos el progreso del Pokémon inmediatamente
	        combateCrud.guardarProgresoCombate(pokemonSeleccionado1);
	    }

	    /**
	     * Resetea los componentes visuales para el siguiente miembro de la liga
	     * sin cambiar de ventana.
	     */
	    private void prepararSiguienteCombateLiga() {
	        Entrenador proximoRival = altoMando.get(faseActual);
	        this.pokemonEnemigo = proximoRival.getEquipo1().get(0);
	        
	        // Limpiar el label de texto para el nuevo combate
	        lblTextoCombate.setText(""); 

	        this.combate = new Combate(pokemonSeleccionado1, pokemonEnemigo);
	        
	        mostrarRivalActual(); 
	        actualizarInterfaz();
	        prepararMenuMovimientos();

	        menuMovimientos.setVisible(true);
	        menuMovimientos.setDisable(false);
	        
	        registrarEnPantalla("--- COMIENZA EL COMBATE CONTRA " + proximoRival.getNombre().toUpperCase() + " ---");
	    }
	    private void volverAlMenu(Stage stageActual) throws SQLException {
	        try {
	            if (mediaPlayer != null) mediaPlayer.stop();

	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/menu.fxml"));
	            Parent root = loader.load();

	            MenuController controller = loader.getController();
	            // Ya no buscamos el stage, usamos el que recibimos
	            controller.init(this.e, stageActual, null);

	            Scene scene = new Scene(root);
	            stageActual.setScene(scene);
	            stageActual.sizeToScene();
	            stageActual.centerOnScreen();
	            stageActual.show();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private void mandarSiguientePokemonRival(Pokemon siguiente) {
	        registrarEnPantalla("¡El rival va a enviar a " + siguiente.getNombre() + "!");
	        
	        PauseTransition pausa = new PauseTransition(Duration.seconds(2));
	        pausa.setOnFinished(ev -> {
	            this.pokemonEnemigo = siguiente;
	            // Reiniciamos el motor de combate con el nuevo oponente pero manteniendo nuestro pokemon
	            this.combate = new Combate(this.pokemonSeleccionado1, this.pokemonEnemigo);
	            
	            actualizarInterfaz();
	            cargarImagenesCombate();
	            prepararMenuMovimientos();
	            
	            registrarEnPantalla("¡Adelante, " + pokemonEnemigo.getNombre() + "!");
	            
	            // Rehabilitar el menú para que el jugador pueda seguir atacando
	            menuMovimientos.setVisible(true);
	            menuMovimientos.setDisable(false);
	        });
	        pausa.play();
	    }
	    
}



package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import dao.CombateCrud;
import dao.ConexionBD;
import dao.PokemonCrud;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
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

public class CombateController {

		private MenuController menuController;
		private Stage stage;
		public Entrenador e;
		private boolean sonido=false;
		private MediaPlayer mediaPlayer;
		private FadeTransition animacionActiva;
		private MediaPlayer reproductorGrito;
		private int posicionOrigen = -1;
		private boolean modoMover = false;
		ConexionBD conBD = new ConexionBD();
		private Pokemon pokemonSeleccionado1;
		private Pokemon pokemonEnemigo;
		private Combate combate;
		private CombateCrud combateCrud = new CombateCrud();
		
		
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
	            // Guardamos el ID del pokemon actual antes del posible cambio
	            int idAntesDelCambio = pokemonSeleccionado1.getId_pokemon();

	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/equipo.fxml"));
	            Parent root = loader.load();
	            EquipoController controller = loader.getController();
	            controller.prepararParaCanjeCombate(this.e, this);

	            Stage stage = new Stage();
	            stage.initModality(Modality.APPLICATION_MODAL); 
	            stage.setScene(new Scene(root));
	            stage.showAndWait(); // El código se detiene aquí hasta que se cierra el equipo

	            // --- LÓGICA POST-CAMBIO ---
	            // Si el ID es diferente, es que el usuario ha seleccionado a otro Pokémon
	            if (pokemonSeleccionado1.getId_pokemon() != idAntesDelCambio) {
	                actualizarInterfaz();
	                cargarImagenesCombate();
	                prepararMenuMovimientos();
	                registrarEnPantalla("¡Adelante, " + pokemonSeleccionado1.getNombre() + "!");

	                // SOLO si el Pokémon anterior estaba VIVO, el rival ataca (cambio por turno)
	                // Si estaba muerto, el cambio es "limpio" y el jugador puede elegir ataque
	                if (pokemonSeleccionado1.getVitalidad() > 0) {
	                    procesarTurnoRivalSolo();
	                } else {
	                    // El Pokémon murió, sacamos uno nuevo y habilitamos el menú para que el jugador ataque
	                    menuMovimientos.setVisible(true);
	                    menuMovimientos.setDisable(false);
	                }
	            
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private void procesarTurnoRivalSolo() {
	        // 1. Bloqueamos el menú para que el jugador no pueda clicar nada mientras el rival ataca
	        menuMovimientos.setVisible(false);
	        menuMovimientos.setDisable(true);

	        // 2. IA Rival decide movimiento
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
	            // 1. Inicializar el MediaPlayer si es la primera vez
	            if (mediaPlayer == null) {
	                // Cambia "combate.mp3" por el nombre real de tu archivo de música
	                String ruta = new File("son/Combate.mp3").toURI().toString(); 
	                Media sound = new Media(ruta);
	                mediaPlayer = new MediaPlayer(sound);
	                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Música en bucle
	                mediaPlayer.setVolume(0.5); // Volumen al 50% para no asustar
	            }

	            // 2. Lógica de Alternancia (Toggle)
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
	    
	    public void recibirDatos(Entrenador ent) throws SQLException {
	        this.e = ent;
	        
	        // 1. Obtener conexión para las consultas iniciales
	        try (Connection conexion = conBD.getConnection()) {
	            if (conexion != null) {
	                // 2. Cargamos el equipo del jugador desde la BD
	                PokemonCrud.compactarEquipo(conexion, e.getIdEntrenador());
	                PokemonCrud.obtenerPokemon1(conexion, this.e);
	                
	                // Seleccionamos al primer Pokémon del jugador para la batalla
	                if (!e.getEquipo1().isEmpty()) {
	                    boolean encontrado = false;
	                    for (Pokemon p : e.getEquipo1()) {
	                        if (p.getEstado() != Estado.DEBILITADO && p.getVitalidad() > 0) {
	                            this.pokemonSeleccionado1 = p;
	                            encontrado = true;
	                            break; // Salimos del bucle al encontrar el primero disponible
	                        }
	                    }

	                    // Opcional: ¿Qué pasa si todo el equipo está debilitado?
	                    if (!encontrado) {
	                        System.out.println("¡Todos tus Pokémon están debilitados! No puedes luchar.");
	                        // Aquí podrías cerrar la ventana o mandar al jugador al Centro Pokémon
	                    }
	                }

	                // 3. GENERACIÓN DEL RIVAL (Aquí es donde ocurre la magia)
	                // Generamos al enemigo y sus movimientos en memoria (sin INSERT)
	                this.pokemonEnemigo = PokemonCrud.generarPokemonAleatorio(conexion);
	            }
	        }

	        // 4. Inicializar la lógica de combate con los dos contendientes listos
	        if (pokemonSeleccionado1 != null && pokemonEnemigo != null) {
	            this.combate = new Combate(pokemonSeleccionado1, pokemonEnemigo);
	            
	            // 5. Preparar la interfaz visual
	            cargarImagenesCombate();  // Carga los .gif de front y back
	            actualizarInterfaz();     // Rellena barras de vida y nombres
	            
	            // El menú de movimientos se rellena pero se mantiene oculto hasta pulsar "Luchar"
	            prepararMenuMovimientos(); 
	        } else {
	            System.err.println("Error: No se pudo inicializar el combate (faltan Pokémon)");
	        }
	        lblTextoCombate.setText(""); // Limpiamos al empezar
	        registrarEnPantalla("¡Un " + pokemonEnemigo.getNombre() + " salvaje aparece!");
	    }
	    public void setPokemonSeleccionado1(Pokemon p) {
	        this.pokemonSeleccionado1 = p;
	    }
	    public Pokemon getPokemonSeleccionado1() {
	        return this.pokemonSeleccionado1;
	    }

	    private void cargarImagenesCombate() {
	        // 1. Cargar imagen del Pokémon del JUGADOR (Espalda - Back)
	        File archivoBack = new File("img/pokemon/back/" + pokemonSeleccionado1.getNum_pokedex() + ".gif");
	        if (archivoBack.exists()) {
	            imgBack.setImage(new Image(archivoBack.toURI().toString()));
	        } else {
	            System.err.println("No se encontró el gif de espalda para: " + pokemonSeleccionado1.getNombre());
	            // Opcional: Cargar una imagen por defecto si no existe el gif
	        }

	        // 2. Cargar imagen del Pokémon ENEMIGO (Frente - Front)
	        File archivoFront = new File("img/pokemon/front/" + pokemonEnemigo.getNum_pokedex() + ".gif");
	        if (archivoFront.exists()) {
	            imgFront.setImage(new Image(archivoFront.toURI().toString()));
	        } else {
	            System.err.println("No se encontró el gif de frente para: " + pokemonEnemigo.getNombre());
	        }


	        // 4. Actualizar estados visuales
	        actualizarImagenEstado();
	    }
	    
	    
	    private void actualizarImagenEstado() {
	        // --- 1. PROCESAR ESTADO DEL POKÉMON AMIGO ---
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

	        // --- 2. PROCESAR ESTADO DEL POKÉMON ENEMIGO ---
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
	        // 1. ACTUALIZACIÓN DEL POKÉMON AMIGO (JUGADOR)
	        // Texto de vitalidad: "100 / 120"
	        vitalidadAmigo.setText(pokemonSeleccionado1.getVitalidad() + " / " + pokemonSeleccionado1.getVitalidadMax());
	        
	        // Cálculo de progreso para la barra (0.0 a 1.0)
	        double ratioAmigo = (double) pokemonSeleccionado1.getVitalidad() / pokemonSeleccionado1.getVitalidadMax();
	        barAmigo.setProgress(ratioAmigo);
	        
	        // Cambiar color de la barra según la salud (Opcional pero recomendado)
	        cambiarColorBarra(barAmigo, ratioAmigo);

	        // 2. ACTUALIZACIÓN DEL POKÉMON ENEMIGO
	        vitalidadEnemigo.setText(pokemonEnemigo.getVitalidad() + " / " + pokemonEnemigo.getVitalidadMax());
	        
	        double ratioEnemigo = (double) pokemonEnemigo.getVitalidad() / pokemonEnemigo.getVitalidadMax();
	        barEnemigo.setProgress(ratioEnemigo);
	        
	        cambiarColorBarra(barEnemigo, ratioEnemigo);

	        // 3. ACTUALIZAR ICONOS DE ESTADO
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
	        
	        // Suponiendo que tienes los Labels en orden
	        Label[] nombres = {lblMov1, lblMov2, lblMov3, lblMov4};
	        Label[] pps = {lblPP1, lblPP2, lblPP3, lblPP4};

	        for (int i = 0; i < 4; i++) {
	            if (i < movs.size()) {
	                Movimiento m = movs.get(i);
	                nombres[i].setText(m.getNombre());
	                pps[i].setText(String.valueOf(m.getNumPP())); // Solo numPP
	                nombres[i].getParent().setDisable(false); // Habilitar botón
	            } else {
	                nombres[i].setText("-");
	                pps[i].setText("");
	                nombres[i].getParent().setDisable(true); // Deshabilitar si no hay movimiento
	            }
	        }
	    }
	    
	    private void procesarTurno(int indice) {
	        // 1. Validaciones previas
	        if (indice >= pokemonSeleccionado1.getMovimientos().size()) return;

	        Movimiento movJugador = pokemonSeleccionado1.getMovimientos().get(indice);
	        if (movJugador.getNumPP() <= 0) {
	            lblTextoCombate.setText("¡No quedan PP para este movimiento!");
	            return;
	        }

	        // 2. IA Rival
	        Random rand = new Random();
	        List<Movimiento> movsEnemigo = pokemonEnemigo.getMovimientos();
	        Movimiento movRival = movsEnemigo.get(rand.nextInt(movsEnemigo.size()));

	        // 3. Bloqueo de UI
	        menuMovimientos.setVisible(false);
	        menuMovimientos.setDisable(true);
	        lblTextoCombate.setText(""); // Limpiamos el log para el nuevo turno

	        // 4. Determinación de orden
	        boolean jugadorVaPrimero = pokemonSeleccionado1.getVelocidad() >= pokemonEnemigo.getVelocidad();

	        // 5. Cadena de eventos (Timeline)
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
	        // 1. Mostrar quién ataca
	        registrarEnPantalla("¡" + atk.getNombre() + " usa " + mov.getNombre() + "!");
	        
	        // 2. Ejecutar lógica en el modelo
	        combate.ejecutarTurno(mov, atk, def);
	        
	        // 3. Actualizar barras y etiquetas de vida
	        actualizarInterfaz();
	        
	        // 4. Si el defensor ha muerto, mostrarlo inmediatamente
	        if (def.getVitalidad() <= 0) {
	            registrarEnPantalla("¡" + def.getNombre() + " se ha debilitado!");
	        }
	    }


	    private void registrarEnPantalla(String mensaje) {
	        // 1. Imprimimos en consola para debug
	        System.out.println(mensaje);
	        
	        // 2. Obtenemos el texto que ya había + el nuevo mensaje
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
	        // Caso 1: El enemigo se debilita (Victoria)
	        if (pokemonEnemigo.getVitalidad() <= 0) {
	            menuMovimientos.setDisable(true);
	            combateCrud.guardarProgresoCombate(pokemonSeleccionado1);
	            procesarVictoria();
	        } 
	        // Caso 2: Tu Pokémon se debilita (Derrota o Cambio forzado)
	        else if (pokemonSeleccionado1.getVitalidad() <= 0) {
	            menuMovimientos.setDisable(true);
	            combateCrud.guardarProgresoCombate(pokemonSeleccionado1);
	            procesarDebilitamientoJugador();
	        }
	    }
	    private void procesarDebilitamientoJugador() {
	        registrarEnPantalla("¡" + pokemonSeleccionado1.getNombre() + " se ha debilitado!");
	        registrarEnPantalla("¡" + pokemonSeleccionado1.getNombre() + " se ha debilitado!");

	        boolean tienePokemonVivos = e.getEquipo1().stream()
	                                     .anyMatch(p -> p.getVitalidad() > 0 && p.getEstado() != Estado.DEBILITADO);

	        if (tienePokemonVivos) {
	            registrarEnPantalla("¡Elige a otro Pokémon para continuar!");
	            
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
	        registrarEnPantalla("¡" + pokemonEnemigo.getNombre() + " enemigo debilitado!");
	        
	        final Stage stageDeLaVentana = (Stage) botnLuchar.getScene().getWindow();
	        // 1. Calcular experiencia ganada
	        // Fórmula sugerida: (Nivel Rival * 15) / 2
	        int expGanada = (pokemonEnemigo.getNivel() * 15) / 2;
	        registrarEnPantalla("¡" + pokemonSeleccionado1.getNombre() + " ha ganado " + expGanada + " puntos de EXP!");

	        // 2. Aplicar experiencia y verificar subida de nivel
	        int nivelAnterior = pokemonSeleccionado1.getNivel();
	        pokemonSeleccionado1.ganarExperiencia(expGanada);
	        
	        if (pokemonSeleccionado1.getNivel() > nivelAnterior) {
	            registrarEnPantalla("¡Subida de nivel! Ahora es nivel " + pokemonSeleccionado1.getNivel());
	            actualizarInterfaz(); // Para que se vea el nuevo nivel y vida en pantalla
	        }

	        // 3. Persistir cambios en la Base de Datos
	        // Actualizamos el Crud para que guarde también el NIVEL y la EXPERIENCIA
	        combateCrud.guardarProgresoCombate(pokemonSeleccionado1);
	        
	        // 4. Bloquear botones o salir del combate tras una pausa
	        Timeline pausaFinal = new Timeline(new KeyFrame(Duration.seconds(3.5), ev -> {
	            // Pasamos la variable que capturamos arriba
	            try {
					volverAlMenu(stageDeLaVentana);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }));
	        pausaFinal.play();
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
	    
	    
}



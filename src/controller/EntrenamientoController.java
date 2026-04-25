package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.ConexionBD;
import dao.MovimientoCrud;
import dao.PokemonCrud;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.Entrenador;
import modelo.Pokemon;
import modelo.Sexo;
import modelo.Tipo;

public class EntrenamientoController {
	
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
    private ProgressBar ataqueBar;

    @FXML
    private ProgressBar ataqueSpBar;
    
    @FXML
    private Button botonMostrarStats;

    @FXML
    private Button botonEFuncional;

    @FXML
    private Button botonEFurioso;

    @FXML
    private Button botonEOnirico;

    @FXML
    private Button botonEPesado;

    @FXML
    private Button botonVolver;

    @FXML
    private VBox cajaSeleccion1;

    @FXML
    private Button cerrarCriado;

    @FXML
    private ProgressBar defensaBar;

    @FXML
    private ProgressBar defensaSpBar;

    @FXML
    private GridPane gridSeleccion1;

    @FXML
    private ImageView imgCriadoSexo;

    @FXML
    private ImageView imgCriadoTipo1;

    @FXML
    private ImageView imgCriadoTipo2;

    @FXML
    private ImageView imgPokemonCriado;

    @FXML
    private ImageView imgPokemonSeleccionado;

    @FXML
    private Label lblPokedollars;

    @FXML
    private Label lblNivel;
    
    @FXML
    private Label statAtaqueLbl;
    
    @FXML
    private Label lblDineroInsuf;

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
    private HBox statsCriado;

    @FXML
    private ProgressBar velocidadBar;

    @FXML
    private ProgressBar vitalidadBar;

    @FXML
    void CerrarCriado(ActionEvent event) {
    	statsCriado.setVisible(false);
    }
    
    @FXML
    void mostrarStats(ActionEvent event) {
    	statsCriado.setVisible(true);
    	
    }

    @FXML
    void seleccionarPokemon(MouseEvent event) {
    	renderizarPokemon(gridSeleccion1, cajaSeleccion1, 1, null);
    	
    	
    }
    
    @FXML
    void entrenarFuncional(ActionEvent event) {
    	if (pokemonSeleccionado1 == null) {
            System.out.println("No hay ningún Pokémon seleccionado para entrenar.");
            return;
        }

        //  Abrimos la conexión con un bloque try-with-resources para asegurar el cierre
        try (Connection conexion = conBD.getConnection()) {
            
            //  Llamamos al método pasándole la conexión real
            boolean exito = PokemonCrud.entrenamientoFuncional(conexion, e, pokemonSeleccionado1);
            
            if (exito) {
                //  Si tuvo éxito, refrescamos los datos visuales
            	chequearMovimientoNuevo(pokemonSeleccionado1, conexion);
                mostrarStatsP(pokemonSeleccionado1); // Para actualizar las barras y labels
                lblPokedollars.setText("Pokedollars: "+String.valueOf(e.getPokedollars()));
                lblDineroInsuf.setVisible(false);
                // Si tienes un Label para el dinero del entrenador, actualízalo aquí también:
                // txtDinero.setText(String.valueOf(e.getCartera()));
                boolean evoluciono = PokemonCrud.intentarEvolucionar(conexion, pokemonSeleccionado1);
                
                if (evoluciono) {
                    //  Si evolucionó, preparamos el nuevo GIF
                    File nuevoGif = new File("img/pokemon/front/" + pokemonSeleccionado1.getNum_pokedex() + ".gif");
                    
                    //  Lanzamos la animación sobre el ImageView de tu vista (ej: imgPokemonEntrenar)
                    animarEvolucion(imgPokemonSeleccionado, nuevoGif);
                    
                    
                    cargarImagenesTipos(pokemonSeleccionado1);
                    //  Opcional: Actualizar el nombre si cambió
                    // lblNombreP.setText(pokemonSeleccionado1.getNombre());
                }
                
                System.out.println("Entrenamiento pesado realizado con éxito.");
            } else {
            	lblDineroInsuf.setVisible(true);
                System.err.println("No se pudo realizar el entrenamiento (Posible falta de dinero).");
                // Aquí podrías mostrar una alerta visual al usuario
            }
            
        } catch (SQLException ex) {
            System.err.println("Error de conexión al intentar entrenar.");
            ex.printStackTrace();
        }
    }

    @FXML
    void entrenarFurioso(ActionEvent event) {
    	if (pokemonSeleccionado1 == null) {
            System.out.println("No hay ningún Pokémon seleccionado para entrenar.");
            return;
        }

        //  Abrimos la conexión con un bloque try-with-resources para asegurar el cierre
        try (Connection conexion = conBD.getConnection()) {
            
            //  Llamamos al método pasándole la conexión real
            boolean exito = PokemonCrud.entrenamientoFurioso(conexion, e, pokemonSeleccionado1);
            
            if (exito) {
                //  Si tuvo éxito, refrescamos los datos visuales
            	chequearMovimientoNuevo(pokemonSeleccionado1, conexion);
                mostrarStatsP(pokemonSeleccionado1); // Para actualizar las barras y labels
                lblPokedollars.setText("Pokedollars: "+String.valueOf(e.getPokedollars()));
                lblDineroInsuf.setVisible(false);
                
                boolean evoluciono = PokemonCrud.intentarEvolucionar(conexion, pokemonSeleccionado1);
                
                if (evoluciono) {
                    //  Si evolucionó, preparamos el nuevo GIF
                    File nuevoGif = new File("img/pokemon/front/" + pokemonSeleccionado1.getNum_pokedex() + ".gif");
                    
                    //  Lanzamos la animación sobre el ImageView de tu vista (ej: imgPokemonEntrenar)
                    animarEvolucion(imgPokemonSeleccionado, nuevoGif);
                    
                    cargarImagenesTipos(pokemonSeleccionado1);
                    
                    //  Opcional: Actualizar el nombre si cambió
                    // lblNombreP.setText(pokemonSeleccionado1.getNombre());
                }
                
                
                // Si tienes un Label para el dinero del entrenador, actualízalo aquí también:
                // txtDinero.setText(String.valueOf(e.getCartera()));
                
                System.out.println("Entrenamiento pesado realizado con éxito.");
            } else {
            	lblDineroInsuf.setVisible(true);
                System.err.println("No se pudo realizar el entrenamiento (Posible falta de dinero).");
                // Aquí podrías mostrar una alerta visual al usuario
            }
            
        } catch (SQLException ex) {
            System.err.println("Error de conexión al intentar entrenar.");
            ex.printStackTrace();
        }
    }

    @FXML
    void entrenarOnirico(ActionEvent event) {
    	if (pokemonSeleccionado1 == null) {
            System.out.println("No hay ningún Pokémon seleccionado para entrenar.");
            return;
        }

        //  Abrimos la conexión con un bloque try-with-resources para asegurar el cierre
        try (Connection conexion = conBD.getConnection()) {
            
            // Llamamos al método pasándole la conexión real
            boolean exito = PokemonCrud.entrenamientoOnirico(conexion, e, pokemonSeleccionado1);
            
            if (exito) {
                // Si tuvo éxito, refrescamos los datos visuales
            	chequearMovimientoNuevo(pokemonSeleccionado1, conexion);
                mostrarStatsP(pokemonSeleccionado1); // Para actualizar las barras y labels
                lblPokedollars.setText("Pokedollars: "+String.valueOf(e.getPokedollars()));
                lblDineroInsuf.setVisible(false);
                // Si tienes un Label para el dinero del entrenador, actualízalo aquí también:
                // txtDinero.setText(String.valueOf(e.getCartera()));
                
                boolean evoluciono = PokemonCrud.intentarEvolucionar(conexion, pokemonSeleccionado1);
                
                if (evoluciono) {
                    // Si evolucionó, preparamos el nuevo GIF
                    File nuevoGif = new File("img/pokemon/front/" + pokemonSeleccionado1.getNum_pokedex() + ".gif");
                    
                    // Lanzamos la animación sobre el ImageView de tu vista (ej: imgPokemonEntrenar)
                    animarEvolucion(imgPokemonSeleccionado, nuevoGif);
                    
                    cargarImagenesTipos(pokemonSeleccionado1);
                    
                    // Opcional: Actualizar el nombre si cambió
                    // lblNombreP.setText(pokemonSeleccionado1.getNombre());
                }
                
                System.out.println("Entrenamiento pesado realizado con éxito.");
            } else {
            	lblDineroInsuf.setVisible(true);
                System.err.println("No se pudo realizar el entrenamiento (Posible falta de dinero).");
                // Aquí podrías mostrar una alerta visual al usuario
            }
            
        } catch (SQLException ex) {
            System.err.println("Error de conexión al intentar entrenar.");
            ex.printStackTrace();
        }
    }

    @FXML
    void entrenarPesado(ActionEvent event) {
    	if (pokemonSeleccionado1 == null) {
            System.out.println("No hay ningún Pokémon seleccionado para entrenar.");
            return;
        }

        try (Connection conexion = conBD.getConnection()) {
            boolean exito = PokemonCrud.entrenamientoPesado(conexion, e, pokemonSeleccionado1);
            
            if (exito) {
            	chequearMovimientoNuevo(pokemonSeleccionado1, conexion);
                mostrarStatsP(pokemonSeleccionado1); 
                lblPokedollars.setText("Pokedollars: " + String.valueOf(e.getPokedollars()));
                lblDineroInsuf.setVisible(false);
                // Comprobamos evolución (ahora devuelve un booleano)
                boolean evoluciono = PokemonCrud.intentarEvolucionar(conexion, pokemonSeleccionado1);
                
                if (evoluciono) {
                    //  Si evolucionó, preparamos el nuevo GIF
                    File nuevoGif = new File("img/pokemon/front/" + pokemonSeleccionado1.getNum_pokedex() + ".gif");
                    
                    // 4. Lanzamos la animación sobre el ImageView de tu vista (ej: imgPokemonEntrenar)
                    animarEvolucion(imgPokemonSeleccionado, nuevoGif);
                    
                    cargarImagenesTipos(pokemonSeleccionado1);
                    
                    // Opcional: Actualizar el nombre si cambió
                    // lblNombreP.setText(pokemonSeleccionado1.getNombre());
                }

                System.out.println("Entrenamiento pesado realizado con éxito.");
            } else {
            	lblDineroInsuf.setVisible(true);
                System.err.println("No se pudo realizar el entrenamiento.");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void volver(ActionEvent event) throws IOException, SQLException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/menu.fxml"));
        Parent root = loader.load();
        
        // Obtenemos el controlador del menú
        MenuController controller = loader.getController();
        
        // Inicializamos el menú con los datos actualizados del entrenador
        // Importante: Usamos el método init que ya tienes definido en tu MenuController
        controller.init(this.e, (Stage) botonVolver.getScene().getWindow(), null);
        
        // 4. Gestión de música (detener música de entrenamiento si existe)
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        
        // Cambio de escena
        Stage stage = (Stage) botonVolver.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        //  Ajustes visuales de la ventana
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    public void recibirDatos(Entrenador ent) {
        this.e = ent;

        try (Connection conexion = conBD.getConnection()) {
            if (conexion != null) {
                //  Organizamos el equipo en la BD (evitar huecos)
                PokemonCrud.compactarEquipo(conexion, e.getIdEntrenador());
                
                //  Cargamos los Pokémon actualizados al objeto entrenador 'e'
                // Asumiendo que estos métodos rellenan la lista de equipo del objeto 'e'
                PokemonCrud.obtenerTodosLosPokemon(conexion, ent);
                lblPokedollars.setText("Pokedollars: "+ent.getPokedollars());
                
                
                // ACTUALIZACIÓN DE LA INTERFAZ
                // Llamamos a un método que pinte los Pokémon en los botones o Grid de entrenamiento
                //actualizarVistaEntrenamiento();
                
                System.out.println("[LOG] Datos del entrenador " + e.getNombre() + " cargados en Entrenamiento.");
            }
        } catch (SQLException ex) {
            System.err.println("[ERROR] No se pudieron cargar los datos en EntrenamientoController");
            ex.printStackTrace();
        }
    }
    
    public void renderizarPokemon(GridPane gridPanePokemon, Pane cajaContenedora, int slot, Pokemon compatibleCon) {
        if (this.e == null || e.getEquipo1() == null) return;

        // FILTRADO PREVIO: Creamos una lista con solo los Pokémon que pasan las reglas
        List<Pokemon> listaFiltrada = new ArrayList<>();
        
        for (Pokemon p : e.getEquipo1()) {
            if (slot == 2 && compatibleCon != null) {
                // Reglas de compatibilidad para el Slot 2
                boolean mismaEspecie = p.getNum_pokedex() == compatibleCon.getNum_pokedex();
                boolean distintoObjeto = p.getId_pokemon() != compatibleCon.getId_pokemon();
                boolean distintoSexo = !p.getSexo().equals(compatibleCon.getSexo());
                boolean tieneFertilidad = p.getFertilidad() > 0;

                if (mismaEspecie && distintoObjeto && distintoSexo && tieneFertilidad) {
                    listaFiltrada.add(p);
                }
            } else {
                // Para el Slot 1 (o si no hay filtro), solo pedimos que tengan fertilidad
                if (p.getFertilidad() > 0) {
                    listaFiltrada.add(p);
                }
            }
        }

        //  COMPROBACIÓN: Si no hay nadie, no hacemos nada y salimos
        if (listaFiltrada.isEmpty()) {
            System.out.println("[SISTEMA] No hay Pokémon compatibles para mostrar.");
            if (cajaContenedora != null) cajaContenedora.setVisible(false); // Por si acaso estaba abierta
            return; 
        }

        // RENDERIZADO: Si llegamos aquí, es que hay al menos uno.
        // Limpiamos y mostramos la caja.
        gridPanePokemon.getChildren().clear();
        gridPanePokemon.getRowConstraints().clear();
        gridPanePokemon.getColumnConstraints().clear();
        
        if (cajaContenedora != null) cajaContenedora.setVisible(true);

        int fila = 0;
        for (Pokemon p : listaFiltrada) {
            VBox card = new VBox(10);
            card.setStyle("-fx-border-color: #ccc; -fx-padding: 15; -fx-alignment: center; "
                        + "-fx-background-color: white; -fx-border-radius: 10;");
            card.setPrefWidth(200);

            File file = new File("img/pokemon/front/" + p.getNum_pokedex() + ".gif");
            if (file.exists()) {
                ImageView img = new ImageView(new Image(file.toURI().toString()));
                img.setFitHeight(65);
                img.setPreserveRatio(true);
                card.getChildren().add(img);
            }

            Label lblNombre = new Label(p.getMote().toUpperCase());
            lblNombre.setStyle("-fx-font-weight: bold;");
            Button btnSeleccionar = new Button("Seleccionar");
            
            btnSeleccionar.setOnAction(event -> {
                if (slot == 1) {
                    this.pokemonSeleccionado1 = p;
                    actualizarVistaSeleccion1(p);
                    mostrarStatsP(pokemonSeleccionado1);
                } 
                // Cerramos la caja al seleccionar
                if (cajaContenedora != null) cajaContenedora.setVisible(false);
            });

            card.getChildren().addAll(lblNombre, new Label("NIVEL: " + p.getNivel()), btnSeleccionar);
            gridPanePokemon.add(card, 0, fila);
            fila++;
        }
    }
    
    private void actualizarVistaSeleccion1(Pokemon p) {
        //  Cambiamos la foto del primer slot
        File file = new File("img/pokemon/front/" + p.getNum_pokedex() + ".gif");
        if (file.exists()) {
            imgPokemonSeleccionado.setImage(new Image(file.toURI().toString()));
        }
        
        //  Cerramos el panel de selección 1
        cajaSeleccion1.setVisible(false);
    }
    
    private void mostrarStatsP(Pokemon p) {
        if (p == null) return;

        // Aseguramos que el nivel sea al menos 1 para no dividir por 0
        int nivel = Math.max(1, p.getNivel());
        double maxStatGeneral = nivel * 5.0;
        
        
        double maxVidaTeorica = 15.0 + (nivel * 5.0); 
        // Ampliamos el margen a 100 para que Pokémon con menos genes o mucha vida entrenada quepan
        double minVidaTeorica = maxVidaTeorica - 100.0; 

        statVitalidadLbl.setText(p.getVitalidad() + " / " + p.getVitalidadMax());

        // Cálculo de progreso relativo
        double numerador = (double) p.getVitalidadMax() - minVidaTeorica;
        double denominador = maxVidaTeorica - minVidaTeorica;

        //  Normalización
        double progresoRelativo = (denominador != 0) ? (numerador / denominador) : 0.0;

        // Ahora con 493 y nivel 100:
        // (493 - 415) / (515 - 415) = 78 / 100 = 0.78 -> La barra se verá al 78%
        vitalidadBar.setProgress(Math.max(0.0, Math.min(1.0, progresoRelativo)));
        
        // Estadísticas base
        // Usamos una función auxiliar para no repetir código en cada barra
        statAtaqueLbl.setText(String.valueOf(p.getAtaque()));
        ataqueBar.setProgress(Math.min(1.0, p.getAtaque() / maxStatGeneral));
        
        statAtaqueSpLbl.setText(String.valueOf(p.getAtaqueEspecial()));
        ataqueSpBar.setProgress(Math.min(1.0, p.getAtaqueEspecial() / maxStatGeneral));
        
        statDefensaLbl.setText(String.valueOf(p.getDefensa()));
        defensaBar.setProgress(Math.min(1.0, p.getDefensa() / maxStatGeneral));
        
        statDefensaSpLbl.setText(String.valueOf(p.getDefensaEspecial()));
        defensaSpBar.setProgress(Math.min(1.0, p.getDefensaEspecial() / maxStatGeneral));
        
        statVelocidadLbl.setText(String.valueOf(p.getVelocidad()));
        velocidadBar.setProgress(Math.min(1.0, (double) p.getVelocidad() / maxStatGeneral));
        
        lblNivel.setText("Nivel: "+p.getNivel());
        
        // Imagen del Sexo (Enum)
        String rutaSexo = null;
        if (p.getSexo() == Sexo.M) {
            rutaSexo = "img/m.png";
        } else if (p.getSexo() == Sexo.H) {
            rutaSexo = "img/h.png";
        }

        // Si hay ruta (M o H), cargamos la imagen. Si es null (Sexo.X), ocultamos.
        if (rutaSexo != null) {
            File fileSexo = new File(rutaSexo);
            if (fileSexo.exists()) {
                imgCriadoSexo.setImage(new Image(fileSexo.toURI().toString()));
                imgCriadoSexo.setVisible(true);
            } else {
                imgCriadoSexo.setVisible(false);
            }
        } else {
            // Caso Sexo.X: Limpiamos la imagen y ocultamos el ImageView
            imgCriadoSexo.setImage(null);
            imgCriadoSexo.setVisible(false);
        }

        // Imagen del Pokémon
        File file = new File("img/pokemon/front/" + p.getNum_pokedex() + ".gif");
        imgPokemonCriado.setImage(new Image(file.toURI().toString()));

        cargarImagenesTipos(p);
    }
    private void cargarImagenesTipos(Pokemon p) {
        // Array de ImageViews para iterar
        ImageView[] ivs = {imgCriadoTipo1, imgCriadoTipo2};
        
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
    
    private void animarEvolucion(ImageView imgPokemon, File nuevoGif) {
        //  Animación de parpadeo
        FadeTransition fade = new FadeTransition(Duration.millis(150), imgPokemon);
        fade.setFromValue(1.0);
        fade.setToValue(0.1);
        fade.setCycleCount(20); // Más ciclos pero más cortos para que coincida con el tiempo total
        fade.setAutoReverse(true);

        //  Animación de escalado
        ScaleTransition scale = new ScaleTransition(Duration.millis(3000), imgPokemon);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.5);
        scale.setToY(1.5);

        //  Agrupamos ambas para que terminen juntas
        ParallelTransition combinacion = new ParallelTransition(fade, scale);

        // Cuando el GRUPO completo termine, ejecutamos el cambio
        combinacion.setOnFinished(event -> {
            // Primero: Cambiamos la imagen
            if (nuevoGif.exists()) {
                imgPokemon.setImage(new Image(nuevoGif.toURI().toString()));
            }
            
            // Segundo: Forzamos el reseteo de escala
            // Al usar ParallelTransition, nos aseguramos de que scale haya terminado del todo
            imgPokemon.setScaleX(1.0);
            imgPokemon.setScaleY(1.0);
            imgPokemon.setOpacity(1.0);
            
            System.out.println("¡Evolución completada y tamaño reseteado!");
        });

        combinacion.play();
    }
    
    public void chequearMovimientoNuevo(Pokemon pokemonSeleccionado, Connection con) {
        // Comprobamos si el nivel es múltiplo de 10
        if (pokemonSeleccionado.getNivel() % 10 == 0) {
            
            try {
                // Buscamos en la base de datos qué puede aprender según sus tipos (Tipo1, Tipo2 y Normal)
                // Este método rellena la lista movimientosPosibles del objeto Pokemon
                MovimientoCrud.buscarMovimientosPosibles(con, pokemonSeleccionado);
                
                // Verificamos si la lista de posibles movimientos no está vacía
                if (!pokemonSeleccionado.getMovimientosPosibles().isEmpty()) {
                    
                    int quiereAprender = JOptionPane.showConfirmDialog(null,
                        "¡" + pokemonSeleccionado.getNombre() + " ha alcanzado el nivel " + 
                        pokemonSeleccionado.getNivel() + "! \n¿Deseas que aprenda un nuevo movimiento aleatorio?",
                        "Aprendizaje por Nivel",
                        JOptionPane.YES_NO_OPTION);

                    if (quiereAprender == JOptionPane.YES_OPTION) {
                        // 4. Ejecutamos la lógica de aprendizaje aleatorio y persistencia en BD
                        // Ahora pasamos 'con' para que pueda hacer el DELETE e INSERT
                        MovimientoCrud.gestionarAprendizajeUI(con, pokemonSeleccionado);
                    }
                    
                } else {
                    System.out.println("Nivel múltiplo de 10 alcanzado, pero no hay movimientos compatibles nuevos en la BD.");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error de base de datos al procesar el nuevo movimiento.");
                e.printStackTrace();
            }
        }
    }
    
}

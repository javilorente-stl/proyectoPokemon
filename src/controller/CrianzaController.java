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
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import modelo.Entrenador;
import modelo.Movimiento;
import modelo.Pokemon;
import modelo.Sexo;
import modelo.Tipo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CrianzaController {
	
	private boolean sonido=false;
	private MediaPlayer mediaPlayer;
	private MediaPlayer gritoPokemon;
	private boolean pokemonGenerado=false;
	private Stage stage;
	private MenuController menuController;
	private int numPokemon;
	private Entrenador e;
	ConexionBD conBD = new ConexionBD();
	private Pokemon pokemonSeleccionado1;
    private Pokemon pokemonSeleccionado2;
    private Pokemon pokemonCriado;

    @FXML
    private ProgressBar ataqueBar;

    @FXML
    private ProgressBar ataqueSpBar;

    @FXML
    private Button botonCriar;

    @FXML
    private Button botonVolver;
    
    @FXML
    private Button botonAbrirHuevo;
   
    @FXML
    private Button cerrarCriado;

    @FXML
    private ProgressBar defensaBar;

    @FXML
    private ProgressBar defensaSpBar;
    
    @FXML
    private ImageView imgHuevo;
    
    @FXML
    private HBox statsCriado;
    
    @FXML
    private ImageView imgCriadoSexo;

    @FXML
    private ImageView imgCriadoTipo1;

    @FXML
    private ImageView imgCriadoTipo2;

    @FXML
    private ImageView imgPokemon1;

    @FXML
    private ImageView imgPokemon2;

    @FXML
    private ImageView imgPokemonCriado;

    @FXML
    private ImageView imgSexo1;

    @FXML
    private ImageView imgSexo2;
    
    @FXML
    private GridPane gridSeleccion1;
    
    @FXML
    private GridPane gridSeleccion2;

    @FXML
    private VBox cajaSeleccion1;

    @FXML
    private VBox cajaSeleccion2;
    
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
    void CerrarCriado(ActionEvent event) {
    	// 1. Ocultar los contenedores y elementos del resultado
        statsCriado.setVisible(false);
        imgPokemonCriado.setVisible(false);
        imgCriadoSexo.setVisible(false);
        imgHuevo.setVisible(false);
        
        imgSexo1.setVisible(false);
        imgSexo2.setVisible(false);
        
        Image imagenPorDefecto = new Image(new File("img/crianza/seleccionarPK.png").toURI().toString());
        imgPokemon1.setImage(imagenPorDefecto);
        imgPokemon2.setImage(imagenPorDefecto);
        imgPokemon2.setDisable(true);
        
        this.pokemonCriado = null;
        this.pokemonSeleccionado1 = null;
        this.pokemonSeleccionado2 = null;

    }
    
    @FXML
    void abrirHuevo(ActionEvent event) {
        System.out.println("Intentando abrir huevo...");
        if (pokemonCriado == null) {
            System.err.println("ERROR: La variable 'pokemonCriado' es NULL. ¡El método de crianza no guardó al Pokémon!");
            return;
        }
        
        System.out.println("Pokémon encontrado: " + pokemonCriado.getMote());
        statsCriado.setVisible(true);
        mostrarStats(this.pokemonCriado);
    }

    @FXML
    void Criar(ActionEvent event) {
    	// 1. Verificamos que los objetos no sean nulos
    	if (PokemonCrud.sonCompatibles(pokemonSeleccionado1, pokemonSeleccionado2)==false) {
    		System.out.println("No son compatibles");
    		return;
    		
    	}
    	
    	if (pokemonSeleccionado1 != null && pokemonSeleccionado2 != null) {
            
            // 1. Necesitas instanciar tu clase de conexión
            ConexionBD conBD = new ConexionBD(); 
            
            // 2. Usar un try-with-resources para asegurar que se abra y cierre
            try (Connection conexion = conBD.getConnection()) {
                
                if (conexion != null) {
                    // Aquí es donde llamas al método
                    // Pasamos: conexion, el ID del entrenador (e.getId()), y los dos padres
                	if (pokemonSeleccionado1.getFertilidad() <= 0 || pokemonSeleccionado2.getFertilidad() <= 0) {
                        System.err.println("Uno de los Pokémon no tiene suficiente fertilidad para criar.");
                        // Mostrar alerta al usuario aquí
                        return; 
                    }
                	imgHuevo.setVisible(true);
                	botonAbrirHuevo.setDisable(false);
                	botonAbrirHuevo.setVisible(true);
                    this.pokemonCriado = PokemonCrud.pokemonCriado(conexion, e, pokemonSeleccionado1, pokemonSeleccionado2);
                    System.out.println("¡Crianza completada!");
                    // Aquí podrías cerrar el panel o refrescar la lista
                
                }
            } catch (SQLException ex) {
                System.err.println("Error al conectar con la base de datos para criar.");
                ex.printStackTrace();
            }
        }
    }

    

    @FXML
    void SeleccionarPokemon1(MouseEvent event) {
    	renderizarPokemon(gridSeleccion1, cajaSeleccion1, 1, null);
    }

    @FXML
    void SeleccionarPokemon2(MouseEvent event) {
    	renderizarPokemon(gridSeleccion2, cajaSeleccion2, 2, this.pokemonSeleccionado1);
    }

    @FXML
    void volver(ActionEvent event) throws IOException, SQLException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/menu.fxml"));
    	Parent root = loader.load();

    	MenuController controller = loader.getController();

    	// Inicializamos el menú pasando el entrenador y la ventana actual
    	controller.init(this.e, (Stage) botonVolver.getScene().getWindow(), null);

    	// Detener música si existe
    	if (mediaPlayer != null) {
    	    mediaPlayer.stop();
    	}

    	// Configuración de la escena y ventana
    	Stage stage = (Stage) botonVolver.getScene().getWindow();
    	Scene scene = new Scene(root);
    	stage.setScene(scene);

    	stage.sizeToScene();
    	stage.centerOnScreen();
    	stage.show();
    }
    
    public void recibirDatos(Entrenador ent) throws SQLException {
        // 1. Asignamos las referencias
        this.e = ent;

        // 3. Gestión de la conexión
        ConexionBD con = new ConexionBD();
        Connection conexion = con.getConnection();
        
        if (conexion != null) {
            // 4. Cargamos los datos en el objeto entrenador
            // Nota: He usado el método que me pasaste antes para traer a todos
            PokemonCrud.obtenerTodosLosPokemon(conexion, e);
            
            // 5. IMPORTANTE: Renderizamos la vista AHORA que 'e' ya tiene datos
            //renderizarPokemon(gridSeleccion1);
            
            // 6. Cerramos conexión
            conexion.close();
            System.out.println("VISTA DE CRIANZA INICIALIZADA CORRECTAMENTE.");
        }
    }
    
    public void renderizarPokemon(GridPane gridPanePokemon, Pane cajaContenedora, int slot, Pokemon compatibleCon) {
        if (this.e == null || e.getEquipo1() == null) return;

        // 1. FILTRADO PREVIO: Creamos una lista con solo los Pokémon que pasan las reglas
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

        // 2. COMPROBACIÓN: Si no hay nadie, no hacemos nada y salimos
        if (listaFiltrada.isEmpty()) {
            System.out.println("[SISTEMA] No hay Pokémon compatibles para mostrar.");
            if (cajaContenedora != null) cajaContenedora.setVisible(false); // Por si acaso estaba abierta
            return; 
        }

        // 3. RENDERIZADO: Si llegamos aquí, es que hay al menos uno.
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
                } else {
                    this.pokemonSeleccionado2 = p;
                    actualizarVistaSeleccion2(p);
                }
                // Cerramos la caja al seleccionar
                if (cajaContenedora != null) cajaContenedora.setVisible(false);
            });

            card.getChildren().addAll(lblNombre, new Label("NIVEL: " + p.getNivel()), btnSeleccionar);
            gridPanePokemon.add(card, 0, fila);
            fila++;
        }
    }
 /*
    public void renderizarPokemon1(GridPane gridPanePokemon, Pane cajaContenedora, int slot, Pokemon compatibleCon) {

        if (this.e == null || e.getEquipo1() == null) return;

        gridPanePokemon.getChildren().clear();
        gridPanePokemon.getRowConstraints().clear();
        gridPanePokemon.getColumnConstraints().clear();
        
        if (cajaContenedora != null) cajaContenedora.setVisible(true);

        int fila = 0;
        for (Pokemon p : e.getEquipo1()) {
            
            // --- LÓGICA DE FILTRO PARA EL SLOT 2 ---
        	int compatibles = 0;
        	// Si estamos en el slot 2 y ya elegimos al primero, filtramos
            if (slot == 2 && compatibleCon != null) {
                // Condición: Deben ser de la misma especie (mismo número de Pokédex)
                // Y no puede ser el mismo objeto físico (no puede criar consigo mismo)
            	if (p.getNum_pokedex() != compatibleCon.getNum_pokedex() || 
            	        p.getId_pokemon() == compatibleCon.getId_pokemon() ||
            	        p.getSexo().equals(compatibleCon.getSexo())) {
            	        
            	        continue; // Si se cumple cualquiera de estas, lo ignoramos y no lo dibujamos
            	    }
            }

            // --- CREACIÓN DE LA TARJETA (Solo si pasa el filtro) ---
            VBox card = new VBox(10);
            card.setStyle("-fx-border-color: #ccc; -fx-padding: 15; -fx-alignment: center; "
                        + "-fx-background-color: white; -fx-border-radius: 10;");
            card.setPrefWidth(200);

            // (Carga de imagen, labels, etc. - se mantiene igual que antes)
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
                    this.pokemonSeleccionado1 = p; // Guardamos en la variable de clase
                    actualizarVistaSeleccion1(p);  // Llamamos al método 1
                } else {
                    this.pokemonSeleccionado2 = p; // Guardamos en la variable de clase
                    actualizarVistaSeleccion2(p);  // Llamamos al método 2
                }
                System.out.println("Elegido: " + p.getNombre());
            });

            card.getChildren().addAll(lblNombre, new Label("NIVEL: " + p.getNivel()), btnSeleccionar);
            gridPanePokemon.add(card, 0, fila);
            fila++;
        }
    }*/
    
    
    private void actualizarVistaSeleccion1(Pokemon p) {
        // 1. Cambiamos la foto del primer slot
        File file = new File("img/pokemon/front/" + p.getNum_pokedex() + ".gif");
        if (file.exists()) {
            imgPokemon1.setImage(new Image(file.toURI().toString()));
        }

        // Cargar imagen del sexo (Enum)
        // Ajusta "MACHO" y "HEMBRA" según los nombres exactos de tu Enum Sexo
        String rutaSexo = (p.getSexo() == Sexo.M) ? "img/m.png" : "img/h.png";
        File fileSexo = new File(rutaSexo);

        if (fileSexo.exists()) {
            imgSexo1.setImage(new Image(fileSexo.toURI().toString()));
            imgSexo1.setVisible(true);
        }

        // 2. "Encendemos" visualmente el segundo slot para indicar que toca elegir pareja
        imgPokemon2.setOpacity(1.0);
        imgPokemon2.setDisable(false);
        
        // 3. Cerramos el panel de selección 1
        cajaSeleccion1.setVisible(false);
    }
    
    private void actualizarVistaSeleccion2(Pokemon p) {
        // 1. Cambiamos la foto del segundo slot
        File file = new File("img/pokemon/front/" + p.getNum_pokedex() + ".gif");
        if (file.exists()) {
            imgPokemon2.setImage(new Image(file.toURI().toString()));
        }
        
        String rutaSexo = (p.getSexo() == Sexo.M) ? "img/m.png" : "img/h.png";
        File fileSexo = new File(rutaSexo);

        if (fileSexo.exists()) {
            imgSexo2.setImage(new Image(fileSexo.toURI().toString()));
            imgSexo2.setVisible(true);
        }

        // 2. Cerramos el panel de selección 2
        cajaSeleccion2.setVisible(false);
        
        // 3. Opcional: Si ya tienes los dos, podrías habilitar el botón de Criar
        if (pokemonSeleccionado1 != null && pokemonSeleccionado2 != null) {
            botonCriar.setDisable(false);
        }
    }
    private void mostrarStats(Pokemon p) {
        if (p == null) return;

        // Aseguramos que el nivel sea al menos 1 para no dividir por 0
        int nivel = Math.max(1, p.getNivel());
        double maxStatGeneral = nivel * 5.0;
        
        // Lógica de Vitalidad
        double maxVidaSegunNivel = 15.0 + (nivel * 5.0);
        double minVidaSegunNivel = maxVidaSegunNivel - 5.0; 

        statVitalidadLbl.setText(p.getVitalidadMax() + "/" + (int)maxVidaSegunNivel);

        // Progreso relativo (aseguramos que esté entre 0 y 1)
        double progresoRelativo = (p.getVitalidadMax() - minVidaSegunNivel) / (maxVidaSegunNivel - minVidaSegunNivel);
        vitalidadBar.setProgress(Math.max(0, Math.min(1, progresoRelativo)));
        
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
        velocidadBar.setProgress(Math.min(1.0, p.getVelocidad() / maxStatGeneral));
        
        // Imagen del Sexo (Enum)
        String rutaSexo = (p.getSexo() == Sexo.M) ? "img/m.png" : "img/ha.png";
        imgCriadoSexo.setImage(new Image(new File(rutaSexo).toURI().toString()));

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
    
}
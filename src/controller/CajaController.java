package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import dao.ConexionBD;
import dao.PokemonCrud;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Entrenador;
import modelo.Movimiento;
import modelo.Pokemon;
import modelo.Tipo;

public class CajaController {

	public Entrenador e;
	//private boolean sonido = false;
	private MediaPlayer mediaPlayer;
	//private FadeTransition animacionActiva;
	//private int posicionOrigen = -1;
	//private boolean modoMover = false;
	ConexionBD conBD = new ConexionBD();
	private Pokemon pokemonSeleccionado;

	@FXML
	private ProgressBar ataqueBar;

	@FXML
	private ProgressBar ataqueSpBar;

	@FXML
	private Button botonVolver;

	@FXML
	private Button botonEliminar;

	@FXML
	private ProgressBar defensaBar;

	@FXML
	private ProgressBar defensaSpBar;

	@FXML
	private GridPane gridCaja;

	@FXML
	private ImageView imgMovimiento1;

	@FXML
	private ImageView imgMovimiento2;

	@FXML
	private ImageView imgMovimiento3;

	@FXML
	private ImageView imgMovimiento4;

	@FXML
	private ImageView imgPokemonSeleccionado;

	@FXML
	private ImageView imgTipo1;

	@FXML
	private ImageView imgTipo2;

	@FXML
	private Label lblMovimiento1;

	@FXML
	private Label lblMovimiento2;

	@FXML
	private Label lblMovimiento3;

	@FXML
	private Label lblMovimiento4;

	@FXML
	private Button moverAEquipo;

	@FXML
	private ScrollPane scrollCaja;

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
	void volverAEquipo(ActionEvent event) throws IOException, SQLException {
		// Cargamos la vista del Equipo
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/equipo.fxml"));
		Parent root = loader.load();

		// Obtenemos el controlador del Equipo
		EquipoController controller = loader.getController();

		// Pasamos los datos del Entrenador para que el equipo no salga vacío
		// Usamos recibirDatos porque es el método que ya tenemos configurado
		controller.recibirDatos(this.e);

		// Parar música si fuera necesario
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}

		// Obtener el Stage actual desde cualquier nodo (por ejemplo, el botón que lanza
		// el evento)
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		// Cambiar la escena
		Scene scene = new Scene(root);
		stage.setScene(scene);

		// Ajustes finales de ventana
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.show();
	}

	@FXML
	void eliminarPokemon(ActionEvent event) {
		if (pokemonSeleccionado == null) {
			System.out.println("No hay ningún Pokémon seleccionado para eliminar.");
			return;
		}

		// Crear Alerta de Confirmación
		Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
		confirmacion.setTitle("Confirmar Eliminación");
		confirmacion.setHeaderText("¿Estás seguro de que quieres liberar a " + pokemonSeleccionado.getNombre() + "?");
		confirmacion.setContentText("Esta acción no se puede deshacer y el Pokémon desaparecerá para siempre.");

		// Esperar respuesta del usuario
		Optional<ButtonType> resultado = confirmacion.showAndWait();

		if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
			try (Connection con = conBD.getConnection()) {

				// Llamar al CRUD
				boolean eliminado = PokemonCrud.eliminarPokemon(con, pokemonSeleccionado.getId_pokemon(),
						e.getIdEntrenador());

				if (eliminado) {
					System.out.println("Pokémon liberado con éxito.");

					// Actualizar los datos del objeto Entrenador en memoria
					PokemonCrud.obtenerPokemon1(con, e);
					PokemonCrud.obtenerPokemon2(con, e);

					// Refrescar la interfaz
					rellenarCaja(e.getEquipo2());
					mostrarStats(null); // Limpiamos el panel de estadísticas
					pokemonSeleccionado = null;

				} else {
					System.err.println("No se pudo eliminar el Pokémon de la base de datos.");
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} else {
			System.out.println("Eliminación cancelada.");
		}
	}

	public void recibirDatos(Entrenador ent) throws SQLException {
		this.e = ent;

		try (Connection conexion = conBD.getConnection()) {
			if (conexion != null) {
				// Mantenemos la lógica de cargar el equipo actual
				PokemonCrud.compactarEquipo(conexion, e.getIdEntrenador());
				PokemonCrud.obtenerPokemon1(conexion, this.e);
				PokemonCrud.obtenerPokemon2(conexion, this.e); // Carga equipo

				// NUEVO: Cargar todos los Pokémon (Caja + Equipo)

				LinkedList<Pokemon> pokemonEnCaja = e.getEquipo2();

				// Dibujamos el Grid con todos los Pokémon
				rellenarCaja(pokemonEnCaja);
			}
		}

		// Mostramos por defecto el primer Pokémon del equipo en el panel de stats
		if (e.getEquipo2() != null && !e.getEquipo2().isEmpty()) {
			Pokemon inicial = e.getEquipo2().get(0);
			mostrarStats(inicial); // El método que rellena tus labels y progressBars
		}
	}

	public void rellenarCaja(List<Pokemon> listaCaja) {
		gridCaja.getChildren().clear();
		gridCaja.setHgap(0);
		gridCaja.setVgap(0);

		gridCaja.getChildren().clear();
		gridCaja.getRowConstraints().clear(); // Borra restricciones de filas
		gridCaja.getColumnConstraints().clear(); // Borra restricciones de columnas

		for (int i = 0; i < listaCaja.size(); i++) {
			Pokemon p = listaCaja.get(i);

			// Creamos el VBox (la tarjeta)
			VBox slot = new VBox();
			slot.setAlignment(Pos.CENTER);
			slot.setSpacing(8);
			slot.setPadding(new Insets(20));
			slot.setPrefSize(140, 190);
			slot.setStyle("-fx-background-color: rgba(255,255,255,0.4); -fx-background-radius: 10;");

			// Nombre (Arriba) - Texto en NEGRO
			Label lblNombre = new Label(p.getMote());
			lblNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

			// Imagen
			File file = new File("img/pokemon/front/" + p.getNum_pokedex() + ".gif");
			ImageView img = new ImageView(new Image(file.toURI().toString()));
			img.setFitHeight(65);
			img.setPreserveRatio(true);

			// Botón
			Button btnMover = new Button("Equipar");
			btnMover.setOnAction(e -> gestionarMoverAEquipo(p));

			// Nivel - Textos en NEGRO
			HBox hbNivel = new HBox(5);
			hbNivel.setAlignment(Pos.CENTER);

			Label lblNvl = new Label("Nvl.");
			lblNvl.setStyle("-fx-text-fill: black;"); // Forzamos negro

			Label lblLvl = new Label(String.valueOf(p.getNivel()));
			lblLvl.setStyle("-fx-text-fill: black;"); // Forzamos negro

			hbNivel.getChildren().addAll(lblNvl, lblLvl);

			// Añadimos todo al VBox según tu jerarquía
			slot.getChildren().addAll(lblNombre, img, btnMover, hbNivel);

			// Añadir al grid (i % 3 para 3 columnas según tu código actual)
			gridCaja.add(slot, i % 3, i / 3);

			slot.setOnMouseClicked(event -> {
				this.pokemonSeleccionado = p;
				mostrarStats(p);
			});
		}
	}

	private void mostrarStats(Pokemon p) {
		if (p == null) {
			// Si el pokemon es null, limpiamos los textos y barras
			statVitalidadLbl.setText("-");
			vitalidadBar.setProgress(0);
			statAtaqueLbl.setText("-");
			ataqueBar.setProgress(0);
			statDefensaLbl.setText("-");
			defensaBar.setProgress(0);
			statAtaqueSpLbl.setText("-");
			ataqueSpBar.setProgress(0);
			statDefensaSpLbl.setText("-");
			defensaSpBar.setProgress(0);
			statVelocidadLbl.setText("-");
			velocidadBar.setProgress(0);
			imgTipo1.setVisible(false);
			imgTipo2.setVisible(false);
			imgPokemonSeleccionado.setVisible(false);
			imgMovimiento1.setVisible(false);
			imgMovimiento2.setVisible(false);
			imgMovimiento3.setVisible(false);
			imgMovimiento4.setVisible(false);
			lblMovimiento1.setText("-");
			lblMovimiento2.setText("-");
			lblMovimiento3.setText("-");
			lblMovimiento4.setText("-");

			return; // Salimos del método para no ejecutar lo de abajo
		}

		double maxStatGeneral = p.getNivel() * 5.0;

		double maxVidaSegunNivel = 15.0 + (p.getNivel() * 5.0);
		// double minVidaSegunNivel = maxVidaSegunNivel - 5.0;

		statVitalidadLbl.setText(p.getVitalidadMax() + "");

		double progresoRelativo = (p.getVitalidadMax()) / (maxVidaSegunNivel);

		progresoRelativo = Math.max(0.0, Math.min(1.0, progresoRelativo));
		vitalidadBar.setProgress(progresoRelativo);

		// Actualización de Labels y ProgressBars
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

		// imagen del pokemon
		File archivoPk = new File("img/pokemon/front/" + p.getNum_pokedex() + ".gif");
		if (archivoPk.exists()) {
			imgPokemonSeleccionado.setImage(new Image(archivoPk.toURI().toString()));
			imgPokemonSeleccionado.setVisible(true);
		}

		// metodos de tipos
		cargarImagenesTipos(p);
		cargarTiposMovimientos(p);

		// actualizacion de movimientos
		LinkedList<Movimiento> movs = p.getMovimientos();
		Label[] lblMovs = { lblMovimiento1, lblMovimiento2, lblMovimiento3, lblMovimiento4 };
		// Si tienes ImageViews para el tipo de cada movimiento,
		// podrías crear otro método similar al tuyo para ellos.

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
		ImageView[] ivs = { imgTipo1, imgTipo2 };

		// Obtenemos los Enums del objeto Pokemon
		Tipo tipo1 = p.getTipo1();
		Tipo tipo2 = p.getTipo2();
		Tipo[] tipos = { tipo1, tipo2 };

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
		// Array de ImageViews para los iconos de tipo de cada ataque
		ImageView[] imgsMovs = { imgMovimiento1, imgMovimiento2, imgMovimiento3, imgMovimiento4 };

		// Obtenemos la lista de movimientos que ya cargamos en el PokemonCrud
		List<Movimiento> movimientos = p.getMovimientos();

		for (int i = 0; i < imgsMovs.length; i++) {
			ImageView iv = imgsMovs[i];

			// Verificamos si existe el movimiento en esta posición
			if (movimientos != null && i < movimientos.size() && movimientos.get(i) != null) {
				Movimiento m = movimientos.get(i);

				try {

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
				// Si el slot está vacío (el Pokémon tiene menos de 4 ataques), limpiamos y
				// ocultamos
				iv.setImage(null);
				iv.setVisible(false);
			}
		}
	}

	private void gestionarMoverAEquipo(Pokemon pOrigen) {
		// Verificar si hay hueco en el equipo (menos de 6 Pokémon)
		if (e.getEquipo1().size() < 6) {
			// Movimiento directo (el equipo tiene espacio)
			moverDirectoAEquipo(pOrigen);
		} else {
			// El equipo está lleno, abrimos el selector de intercambio
			abrirSelectorIntercambio(pOrigen);
		}
	}

	private void moverDirectoAEquipo(Pokemon pOrigen) {
		try (Connection con = conBD.getConnection()) {
			// La nueva posición será la siguiente disponible (tamaño actual + 1)
			int nuevaPosicion = e.getEquipo1().size() + 1;

			// Actualizamos en la base de datos
			PokemonCrud.actualizarPosicionPokemon(con, pOrigen.getId_pokemon(), nuevaPosicion);

			// Recargamos las listas del entrenador para que los cambios se reflejen
			PokemonCrud.obtenerPokemon1(con, this.e);
			PokemonCrud.obtenerPokemon2(con, this.e);

			// Refrescamos la UI de la caja para que el Pokémon desaparezca de aquí
			rellenarCaja(e.getEquipo2());

			System.out.println(pOrigen.getNombre() + " se ha unido al equipo directamente.");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void abrirSelectorIntercambio(Pokemon pOrigen) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/equipo.fxml"));
			Parent root = loader.load();

			// Obtenemos el controlador del equipo
			EquipoController controller = loader.getController();

			controller.prepararParaIntercambio1(this.e, pOrigen, this);

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Equipo lleno: Elige a quién sustituir");
			stage.setScene(new Scene(root));

			// Usamos showAndWait para que el código de la caja se detenga hasta que elijas
			stage.showAndWait();
			int posicionBuscada = pOrigen.getCaja();
			Pokemon recienLlegado = null;

			for (Pokemon p : e.getEquipo2()) {
				if (p.getCaja() == posicionBuscada) {
					recienLlegado = p;
					break;
				}
			}
			if (recienLlegado != null) {
				mostrarStats(recienLlegado);
			}

			rellenarCaja(e.getEquipo2());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

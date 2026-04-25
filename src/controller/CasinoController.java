package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import dao.ConexionBD;
import javafx.animation.FadeTransition;
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

public class CasinoController {

	private MenuController menuController;
	private Stage stage;
	public Entrenador e;
	private boolean sonido = false;
	private MediaPlayer mediaPlayer;
	ConexionBD conBD = new ConexionBD();

	@FXML
	private Button botonVolver;

	@FXML
	private ImageView imgSonido;

	@FXML
	private ImageView imgAdivinarNumero;

	@FXML
	private ImageView imgCaraCruz;

	@FXML
	private ImageView imgRuleta;

	@FXML
	private Label lblPokedollars;

	@FXML
	void activarSonido(MouseEvent event) {
		sonido();
	}

	@FXML
	void jugarAdivinarNumero(MouseEvent event) {
		// Configuración del juego
		int numeroSecreto = (int) (Math.random() * 20) + 1; // Entre 1 y 20
		int intentosMax = 5;
		int premio = 0;
		boolean acertado = false;

		JOptionPane.showMessageDialog(null,
				"¡Bienvenido a la Adivinanza!\nHe pensado un número entre 1 y 20.\nTienes 5 intentos para ganar hasta 1000 Pokedollars.",
				"Casino - Adivina el número", JOptionPane.INFORMATION_MESSAGE);

		// 2. Bucle de intentos
		for (int i = 1; i <= intentosMax; i++) {
			String input = JOptionPane.showInputDialog(null,
					"Intento " + i + " de " + intentosMax + "\n¿Qué número crees que es?", "Adivina el número",
					JOptionPane.QUESTION_MESSAGE);

			// Si el usuario cancela o cierra la ventana
			if (input == null)
				return;

			try {
				int numeroUsuario = Integer.parseInt(input);

				if (numeroUsuario == numeroSecreto) {
					acertado = true;
					// Cálculo de premios según el intento
					switch (i) {
					case 1:
						premio = 1000;
						break;
					case 2:
						premio = 750;
						break;
					case 3:
						premio = 500;
						break;
					case 4:
						premio = 250;
						break;
					case 5:
						premio = 0;
						break;
					}
					break; // Sale del bucle porque acertó
				} else {
					// Pista para el usuario
					String pista = (numeroUsuario < numeroSecreto) ? "Es MAYOR" : "Es MENOR";
					if (i < intentosMax) {
						JOptionPane.showMessageDialog(null, "¡Fallo! Una pista: " + pista);
					}
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Introduce un número válido. Pierdes un intento por despiste.");
			}
		}

		// Resultado final y actualización
		if (acertado) {
			if (premio > 0) {
				e.setPokedollars(e.getPokedollars() + premio);
				JOptionPane.showMessageDialog(null,
						"¡Increíble! Acertaste el " + numeroSecreto + ".\nHas ganado " + premio + " Pokedollars.");
			} else {
				JOptionPane.showMessageDialog(null,
						"¡Por los pelos! Acertaste en el último intento.\nNo ganas dinero, ¡pero te llevas la gloria!");
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Te has quedado sin intentos. El número era el " + numeroSecreto + ".\nMás suerte la próxima vez.");
		}

		// Actualizar Interfaz y Base de Datos
		lblPokedollars.setText(String.valueOf(e.getPokedollars()));
		actualizarDineroEnBD(e.getPokedollars());
	}

	@FXML
	void jugarCaraCruz(MouseEvent event) {

		String input = JOptionPane.showInputDialog(null,
				"¿Cuánto quieres apostar?\n(Tienes: " + e.getPokedollars() + ")", "Apuesta",
				JOptionPane.QUESTION_MESSAGE);

		// Si el usuario cancela o no escribe nada, salimos
		if (input == null || input.isEmpty())
			return;

		try {
			int apuesta = Integer.parseInt(input);

			// Validaciones de la apuesta
			if (apuesta <= 0) {
				JOptionPane.showMessageDialog(null, "La apuesta debe ser mayor que 0.");
				return;
			}
			if (apuesta > e.getPokedollars()) {
				JOptionPane.showMessageDialog(null, "No tienes suficiente dinero.");
				return;
			}

			// Elegir Cara o Cruz
			Object[] opciones = { "Cara", "Cruz", "Cancelar" };
			int seleccion = JOptionPane.showOptionDialog(null, "Has apostado " + apuesta + ". ¿A qué bando vas?",
					"Cara o Cruz", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones,
					opciones[0]);

			if (seleccion == 0 || seleccion == 1) {
				String eleccionUsuario = (seleccion == 0) ? "Cara" : "Cruz";
				// EJECUTAR EL JUEGO
				String resultado = (Math.random() < 0.5) ? "Cara" : "Cruz";

				if (eleccionUsuario.equals(resultado)) {
					e.setPokedollars(e.getPokedollars() + apuesta);
					JOptionPane.showMessageDialog(null, "¡Salió " + resultado + "! Ganaste " + apuesta + ".");
				} else {
					e.setPokedollars(e.getPokedollars() - apuesta);
					JOptionPane.showMessageDialog(null, "¡Salió " + resultado + "! Perdiste " + apuesta + ".");
				}

				// ACTUALIZAR INTERFAZ Y BD
				lblPokedollars.setText(String.valueOf(e.getPokedollars()));
				actualizarDineroEnBD(e.getPokedollars());

			}

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Por favor, introduce un número válido.");
		}
	}

	@FXML
	void jugarRuleta(MouseEvent event) {

		String inputDinero = JOptionPane.showInputDialog(null,
				"¿Cuánto quieres apostar?\n(Dinero disponible: " + e.getPokedollars() + ")", "Ruleta - Apuesta",
				JOptionPane.QUESTION_MESSAGE);

		if (inputDinero == null || inputDinero.isEmpty())
			return;

		try {
			int apuesta = Integer.parseInt(inputDinero);
			if (apuesta <= 0 || apuesta > e.getPokedollars()) {
				JOptionPane.showMessageDialog(null, "Cantidad inválida.");
				return;
			}

			// Elegir Tipo de Apuesta
			Object[] tipos = { "Solo Número", "Solo Color", "Número y Color" };
			int tipoApuesta = JOptionPane.showOptionDialog(null, "Selecciona tu tipo de apuesta:", "Ruleta",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);

			if (tipoApuesta == -1)
				return; // Cerró la ventana

			int numApostado = -1;
			String colorApostado = null;

			// Recoger datos según el tipo elegido
			if (tipoApuesta == 0 || tipoApuesta == 2) { // Requiere Número
				String n = JOptionPane.showInputDialog("Elige un número entre 1 y 37:");
				if (n == null)
					return;
				numApostado = Integer.parseInt(n);
			}
			if (tipoApuesta == 1 || tipoApuesta == 2) { // Requiere Color
				Object[] colores = { "Rojo", "Negro" };
				int c = JOptionPane.showOptionDialog(null, "Elige un color:", "Color", JOptionPane.DEFAULT_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, colores, colores[0]);
				if (c == -1)
					return;
				colorApostado = (c == 0) ? "Rojo" : "Negro";
			}

			// Generar Resultado de la Ruleta
			int numGanador = (int) (Math.random() * 37) + 1;
			String colorGanador = (Math.random() < 0.5) ? "Rojo" : "Negro";

			// Calcular Premios
			boolean acertoNumero = (numApostado == numGanador);
			boolean acertoColor = (colorGanador.equals(colorApostado));

			int premioTotal = 0;
			String mensaje = "La ruleta se detiene en... ¡" + numGanador + " " + colorGanador + "!\n";

			if (acertoNumero) {
				premioTotal += apuesta * 10;
				mensaje += "- ¡Acertaste el NÚMERO! (x10)\n";
			}
			if (acertoColor) {
				premioTotal += apuesta * 2;
				mensaje += "- ¡Acertaste el COLOR! (x2)\n";
			}

			// Actualizar Saldo
			if (premioTotal > 0) {
				// Si ganó algo, sumamos el premio (restamos la apuesta original primero para
				// lógica limpia)
				e.setPokedollars(e.getPokedollars() - apuesta + premioTotal);
				JOptionPane.showMessageDialog(null, mensaje + "\nTotal ganado: " + premioTotal + " Pokedollars.");
			} else {
				e.setPokedollars(e.getPokedollars() - apuesta);
				JOptionPane.showMessageDialog(null, mensaje + "\nLo siento, has perdido tu apuesta.");
			}

			// Refrescar UI y BD
			lblPokedollars.setText(String.valueOf(e.getPokedollars()));
			actualizarDineroEnBD(e.getPokedollars());

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Introduce valores numéricos válidos.");
		}
	}

	@FXML
	void volver(ActionEvent event) throws SQLException, IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../vistas/menu.fxml"));
		Parent root = loader.load();

		// Obtenemos el controlador del menú
		MenuController controller = loader.getController();

		// Inicializamos el menú pasando el entrenador (con su dinero actualizado del
		// casino)
		// Usamos el Stage obtenido del botón que lanza el evento
		controller.init(this.e, (Stage) botonVolver.getScene().getWindow(), null);

		// Detener la música del Casino si está sonando
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}

		// Configuración de la transición de escena
		Stage stage = (Stage) botonVolver.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);

		// Ajustes finales de la ventana
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.show();
	}

	public void recibirDatos(Entrenador ent) {
		this.e = ent;
		// Ejemplo: Actualizar el dinero visualmente al entrar al casino
		lblPokedollars.setText(String.valueOf(e.getPokedollars()));
		tienesPokedollars();
	}

	private void ejecutarJuego(String eleccionUsuario) {
		// Verificamos si tiene dinero (ejemplo: 100 por apuesta)
		if (this.e.getPokedollars() < 100) {
			JOptionPane.showMessageDialog(null, "No tienes suficientes Pokedollars.");
			return;
		}

		// Cobramos la apuesta
		this.e.setPokedollars(this.e.getPokedollars() - 100);

		// Lanzamiento aleatorio
		String resultado = (Math.random() < 0.5) ? "Cara" : "Cruz";

		if (eleccionUsuario.equals(resultado)) {
			// Gana el doble
			this.e.setPokedollars(this.e.getPokedollars() + 200);
			JOptionPane.showMessageDialog(null, "¡Salió " + resultado + "! Has ganado 200 Pokedollars.");
		} else {
			JOptionPane.showMessageDialog(null, "¡Salió " + resultado + "! Has perdido la apuesta.");
		}

		// ACTUALIZAMOS LA INTERFAZ
		lblPokedollars.setText(String.valueOf(e.getPokedollars()));

	}

	private void actualizarDineroEnBD(int nuevoDinero) {
		String sql = "UPDATE ENTRENADOR SET POKEDOLLARS = ? WHERE ID_ENTRENADOR = ?";
		try (Connection con = conBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, nuevoDinero);
			ps.setInt(2, e.getIdEntrenador());
			ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void tienesPokedollars() {
		if (e.getPokedollars() <= 0) {
			imgAdivinarNumero.setDisable(true);
			imgCaraCruz.setDisable(true);
			imgRuleta.setDisable(true);
			lblPokedollars.setText("No puedes jugar sin pokedollars");
		}
	}

	public void sonido() {
		if (mediaPlayer == null) {
			String ruta = "./son/Casino.mp3";
			Media sound = new Media(new File(ruta).toURI().toString());
			mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		}

		if (!this.sonido) {
			mediaPlayer.play();
			this.sonido = true;
			imgSonido.setImage(new Image(new File("./img/Casino/SonidoActivo.png").toURI().toString()));
		} else {
			mediaPlayer.stop(); // Ahora sí parará el único que existe
			this.sonido = false;
			imgSonido.setImage(new Image(new File("./img/Casino/SonidoApagado.png").toURI().toString()));
		}
	}

}

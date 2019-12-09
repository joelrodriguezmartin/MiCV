package tabs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import classes.Nacionalidad;
import classes.Personal;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class PersonalController implements Initializable {
	@FXML
	private VBox root;

	@FXML
	private TextField dniText;

	@FXML
	private TextField nombreText;

	@FXML
	private TextField apellidosText;

	@FXML
	private DatePicker fechaDate;

	@FXML
	private TextField direccionText;

	@FXML
	private TextField postalText;

	@FXML
	private TextField localidadText;

	@FXML
	private ComboBox<String> paisCombo;

	@FXML
	private ListView<Nacionalidad> nacionalidadList;

	@FXML
	private Button añadirNacButton;

	@FXML
	private Button eliminarNacButton;

	// model
	private ObjectProperty<Personal> personal = new SimpleObjectProperty<Personal>(new Personal());

	// lista de nacionalidades y de paises
	private ListProperty<String> paises = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<>()));
	private ArrayList<String> nacionalidades = new ArrayList<>();

	public PersonalController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PersonalView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		leerNacionalidades();

		paisCombo.itemsProperty().bind(paises);
		leerPaises();

		personal.addListener((o, ol, nv) -> cambiarBindings(ol, nv));
		personal.set(new Personal());

		añadirNacButton.setOnAction(e -> addNac());
		eliminarNacButton.setOnAction(e -> removeNac());

	}

	private void removeNac() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Confirmar");
		alert.setContentText("¿Seguro que quiere eliminar?");
		boolean confirmed = (alert.showAndWait().get() == ButtonType.OK);

		Nacionalidad nacionalidad = nacionalidadList.getSelectionModel().getSelectedItem();

		if (nacionalidad != null && confirmed) {
			getPersonal().getNacionalidades().remove(nacionalidad);
		}
	}

	private void addNac() {
		ChoiceDialog<String> dialog = new ChoiceDialog<String>();
		dialog.setTitle("Elige nacionalidad");
		dialog.setHeaderText("Elige nacionalidad");
		dialog.setContentText("Nacionalidades:");
		dialog.getItems().addAll(nacionalidades);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent() && result.get()!=null) {
			getPersonal().nacionalidadesProperty().add(new Nacionalidad(result.get()));
		}
	}

	private void cambiarBindings(Personal ol, Personal nuevoPersonal) {

		if (ol != null) {

			nombreText.textProperty().unbindBidirectional(ol.nombreProperty());
			apellidosText.textProperty().unbindBidirectional(ol.apellidosProperty());
			postalText.textProperty().unbindBidirectional(ol.codigoPostalProperty());
			localidadText.textProperty().unbindBidirectional(ol.localidadProperty());
			fechaDate.valueProperty().unbindBidirectional(ol.fechaNacimientoProperty());
			dniText.textProperty().unbindBidirectional(ol.identificacionProperty());
			direccionText.textProperty().unbindBidirectional(ol.direccionProperty());
			paisCombo.valueProperty().unbindBidirectional(ol.paisProperty());
		}

		// Bidireccional, puesto que queremos añadir cambios en el CV de una persona
		// Primero ponemos el de la interfaz y luego el del objeto, el orden importa,
		// al menos si lo usas como arriba.
		Bindings.bindBidirectional(nombreText.textProperty(), nuevoPersonal.nombreProperty());
		Bindings.bindBidirectional(apellidosText.textProperty(), nuevoPersonal.apellidosProperty());
		Bindings.bindBidirectional(postalText.textProperty(), nuevoPersonal.codigoPostalProperty());
		Bindings.bindBidirectional(localidadText.textProperty(), nuevoPersonal.localidadProperty());
		Bindings.bindBidirectional(fechaDate.valueProperty(), nuevoPersonal.fechaNacimientoProperty());
		Bindings.bindBidirectional(dniText.textProperty(), nuevoPersonal.identificacionProperty());
		Bindings.bindBidirectional(direccionText.textProperty(), nuevoPersonal.direccionProperty());
		Bindings.bindBidirectional(paisCombo.valueProperty(), nuevoPersonal.paisProperty());

		nacionalidadList.itemsProperty().bindBidirectional(nuevoPersonal.nacionalidadesProperty());
	}

	private void leerNacionalidades() {
		FileInputStream file = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		try {

			file = new FileInputStream("paises.csv");
			in = new InputStreamReader(file, StandardCharsets.UTF_8);
			reader = new BufferedReader(in);

			String line = null;

			while ((line = reader.readLine()) != null) {
				paises.add(line);
			}

			file.close();
			in.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void leerPaises() {
		FileInputStream file = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		try {

			file = new FileInputStream("nacionalidades.csv");
			in = new InputStreamReader(file, StandardCharsets.UTF_8);
			reader = new BufferedReader(in);

			String line = null;

			while ((line = reader.readLine()) != null) {
				nacionalidades.add(line);
			}

			file.close();
			in.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public VBox getRoot() {
		return root;
	}

	public final ObjectProperty<Personal> personalProperty() {
		return this.personal;
	}

	public final Personal getPersonal() {
		return this.personalProperty().get();
	}

	public final void setPersonal(final Personal personal) {
		this.personalProperty().set(personal);
	}

}

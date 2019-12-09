package main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import classes.CV;
import classes.Contacto;
import classes.Personal;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import tabs.ConocimientosController;
import tabs.ContactoController;
import tabs.ExperienciaController;
import tabs.FormacionController;
import tabs.PersonalController;

public class RootController implements Initializable {
	@FXML
	private BorderPane root;

	@FXML
	private MenuItem nuevoItem;

	@FXML
	private MenuItem abrirItem;

	@FXML
	private MenuItem guardarItem;

	@FXML
	private MenuItem guardarComoItem;

	@FXML
	private MenuItem salirItem;

	@FXML
	private Tab personalTab;

	@FXML
	private Tab contactoTab;

	@FXML
	private Tab formacionTab;

	@FXML
	private Tab experienciaTab;

	@FXML
	private Tab conocimientosTab;

	// model
	private ObjectProperty<CV> cv = new SimpleObjectProperty<>();

	private PersonalController personalController;
	private ContactoController contactoController;
	private FormacionController formacionController;
	private ExperienciaController experienciaController;
	private ConocimientosController conocimientosController;
	// guardar referencia pa guardar luego
	File file;

	public RootController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			personalController = new PersonalController();
			personalTab.setContent(personalController.getRoot());

			contactoController = new ContactoController();
			contactoTab.setContent(contactoController.getRoot());

			formacionController = new FormacionController();
			formacionTab.setContent(formacionController.getRoot());

			experienciaController = new ExperienciaController();
			experienciaTab.setContent(experienciaController.getRoot());

			conocimientosController = new ConocimientosController();
			conocimientosTab.setContent(conocimientosController.getRoot());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cv.set(new CV());
		cv.get().setPersonal(new Personal());
		cv.get().setContacto(new Contacto());
		cargarCV();

		abrirItem.setOnAction(e -> onAbrir());
		nuevoItem.setOnAction(e -> onNuevo());
		salirItem.setOnAction(e -> Platform.exit());
		guardarItem.setOnAction(e -> guardar());
		guardarComoItem.setOnAction(e -> guardarComo());
	}

	private void guardarComo() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CV", "*.cv"));
		fileChooser.setTitle("Guardar");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/src"));

		File newFile = fileChooser.showSaveDialog(getRoot().getScene().getWindow());
		if (newFile != null) {
			try {
				JAXBUtils.save(cv.get(), newFile);
				file = newFile;
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Error");
				alert.setTitle("Error");
				alert.showAndWait();

			}
		}
	}

	private void guardar() {
		if (file == null) {
			guardarComo();
		}
		try {
			JAXBUtils.save(cv.get(), file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error");
			alert.setContentText("Error");
			alert.setTitle("Error");
			alert.showAndWait();
		}
	}

	private void onNuevo() {
		file = null;
		cv.set(new CV());
		cv.get().setPersonal(new Personal());
		cv.get().setContacto(new Contacto());
		cargarCV();
	}

	private void cargarCV() {
		personalController.setPersonal(cv.get().getPersonal());
		contactoController.setContacto(cv.get().getContacto());

		formacionController.titulosProperty().bindBidirectional(cv.get().formacionProperty());
		experienciaController.experienciasProperty().bindBidirectional(cv.get().experienciaProperty());
		conocimientosController.conocimientosProperty().bindBidirectional(cv.get().habilidadesProperty());
	}

	private void onAbrir() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CV", "*.cv"));
		fileChooser.setTitle("Abrir");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/src"));

		file = fileChooser.showOpenDialog(getRoot().getScene().getWindow());

		if (file != null) {
			try {
				CV otroCV = JAXBUtils.load(CV.class, file);

				cv.set(otroCV);
				cargarCV();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BorderPane getRoot() {
		return root;
	}

	public final ObjectProperty<CV> cvProperty() {
		return this.cv;
	}

	public final CV getCv() {
		return this.cvProperty().get();
	}

	public final void setCv(final CV cv) {
		this.cvProperty().set(cv);
	}

}

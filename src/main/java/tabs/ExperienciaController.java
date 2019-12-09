package tabs;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import classes.Experiencia;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import main.LocalDateTableCell;

public class ExperienciaController implements Initializable {
	@FXML
	private HBox root;

	@FXML
	private TableView<Experiencia> experienciaTable;

	@FXML
	private Button addButton;

	@FXML
	private Button removeButton;

	@FXML
	private TableColumn<Experiencia, LocalDate> desdeColumn;

	@FXML
	private TableColumn<Experiencia, LocalDate> hastaColumn;

	// model
	private ListProperty<Experiencia> experiencias = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<>()));

	public ExperienciaController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExperienciaView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		desdeColumn.setCellFactory(t -> {
			return new LocalDateTableCell<>(t);
		});
		hastaColumn.setCellFactory(t -> {
			return new LocalDateTableCell<>(t);
		});
		experienciaTable.itemsProperty().bindBidirectional(experienciasProperty());
		addButton.setOnAction(e -> addExperiencia());
		removeButton.setOnAction(e -> removeExperiencia());

	}

	private void removeExperiencia() {
		Experiencia experiencia = experienciaTable.getSelectionModel().getSelectedItem();

		if (experiencia != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Confirmar");
			alert.setContentText("¿Seguro que quieres eliminar esta experiencia?");

			boolean confirmed = (alert.showAndWait().get() == ButtonType.OK);

			if (experiencia != null && confirmed) {
				experiencias.remove(experiencia);
			}
		}
	}

	private void addExperiencia() {
		// ni caso a los comentarios, son del ejemplo de dialog login
		// Create the custom dialog.
		Dialog<Experiencia> dialog = new Dialog<>();
		dialog.setTitle("Nueva experiencia");
		dialog.setHeaderText("Nueva experiencia");

		// Set the button types.
		ButtonType addButton = new ButtonType("Añadir", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField denominacionText = new TextField();
		grid.addRow(0, new Label("Denominacion"), denominacionText);

		TextField empleadorText = new TextField();
		grid.addRow(1, new Label("Empleador"), empleadorText);

		GridPane.setHgrow(denominacionText, Priority.ALWAYS);
		GridPane.setHgrow(empleadorText, Priority.ALWAYS);

		DatePicker desde = new DatePicker();
		grid.addRow(2, new Label("Desde"), desde);

		DatePicker hasta = new DatePicker();
		grid.addRow(3, new Label("Hasta"), hasta);

		dialog.getDialogPane().setContent(grid);

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButton) {
				Experiencia experiencia = new Experiencia(desde.getValue(), hasta.getValue(),
						denominacionText.getText(), empleadorText.getText());
				return experiencia;
			}
			return null;
		});
		Experiencia experiencia = dialog.showAndWait().get();

		if (experiencia != null) {
			experiencias.add(experiencia);
		}
	}

	public HBox getRoot() {
		return root;
	}

	public final ListProperty<Experiencia> experienciasProperty() {
		return this.experiencias;
	}
	

	public final ObservableList<Experiencia> getExperiencias() {
		return this.experienciasProperty().get();
	}
	

	public final void setExperiencias(final ObservableList<Experiencia> experiencias) {
		this.experienciasProperty().set(experiencias);
	}
	

	

}

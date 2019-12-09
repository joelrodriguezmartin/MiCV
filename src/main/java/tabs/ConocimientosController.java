package tabs;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import classes.Conocimiento;

import classes.Idioma;
import classes.Conocimiento.Nivel;
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
import javafx.scene.control.ComboBox;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ConocimientosController implements Initializable {

	@FXML
	private HBox root;

	@FXML
	private TableView<Conocimiento> conocimientoTable;

	@FXML
	private Button addConocimientoButton;

	@FXML
	private Button addIdiomaButton;

	@FXML
	private Button deleteButton;

	@FXML
	private TableColumn<Conocimiento, Conocimiento.Nivel> nivelColumn;

	// model
	private ListProperty<Conocimiento> conocimientos = new SimpleListProperty<>(
			FXCollections.observableArrayList(new ArrayList<>()));

	public ConocimientosController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConocimientosView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		conocimientoTable.itemsProperty().bind(conocimientos);
		nivelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Conocimiento.niveles()));

		addConocimientoButton.setOnAction(e -> addConocimiento());
		addIdiomaButton.setOnAction(e -> addIdioma());
		deleteButton.setOnAction(e -> delete());
	}

	private void delete() {
		Conocimiento conocimiento = conocimientoTable.getSelectionModel().getSelectedItem();

		if (conocimiento != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Confirmar");
			alert.setContentText("¿Seguro que quieres eliminar este conocimiento?");

			boolean confirmed = (alert.showAndWait().get() == ButtonType.OK);

			if (conocimiento != null && confirmed) {
				conocimientos.remove(conocimiento);
			}
		}
	}

	private void addIdioma() {
		// ni caso a los comentarios, son del ejemplo de dialog login
				// Create the custom dialog.
				Dialog<Idioma> dialog = new Dialog<>();
				dialog.setTitle("Nuevo idioma");
				dialog.setHeaderText("Nuevo idioma");

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

				ComboBox<Nivel> nivelCombo = new ComboBox<Nivel>();
				nivelCombo.setPromptText("Seleccione un nivel");
				nivelCombo.getItems().addAll(Conocimiento.niveles());
				Button reset = new Button("X");
				reset.setOnAction(e -> nivelCombo.setValue(null));
				grid.addRow(1, new Label("Tipo"), new HBox(5, nivelCombo, reset));
				
				TextField idiomaText = new TextField();
				grid.addRow(2, new Label("Certificacion"), idiomaText);
				
				GridPane.setHgrow(denominacionText, Priority.ALWAYS);

				dialog.getDialogPane().setContent(grid);

				// Convert the result to a username-password-pair when the login button is
				// clicked.
				dialog.setResultConverter(dialogButton -> {
					if (dialogButton == addButton) {
						Idioma idioma = new Idioma(nivelCombo.getValue(), denominacionText.getText(), idiomaText.getText());
						return idioma;
					}
					return null;
				});
				Idioma idioma = dialog.showAndWait().get();

				if (idioma != null) {
					conocimientos.add(idioma);
				}
	}

	private void addConocimiento() {
		// ni caso a los comentarios, son del ejemplo de dialog login
		// Create the custom dialog.
		Dialog<Conocimiento> dialog = new Dialog<>();
		dialog.setTitle("Nueva conocimiento");
		dialog.setHeaderText("Nueva conocimiento");

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

		ComboBox<Nivel> nivelCombo = new ComboBox<Nivel>();
		nivelCombo.setPromptText("Seleccione un nivel");
		nivelCombo.getItems().addAll(Conocimiento.niveles());
		Button reset = new Button("X");
		reset.setOnAction(e -> nivelCombo.setValue(null));
		grid.addRow(1, new Label("Tipo"), new HBox(5, nivelCombo, reset));
		
		GridPane.setHgrow(denominacionText, Priority.ALWAYS);

		dialog.getDialogPane().setContent(grid);

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addButton) {
				Conocimiento conocimiento = new Conocimiento(nivelCombo.getValue(), denominacionText.getText());
				return conocimiento;
			}
			return null;
		});
		Conocimiento conocimiento = dialog.showAndWait().get();

		if (conocimiento != null) {
			conocimientos.add(conocimiento);
		}
	}

	public HBox getRoot() {
		return root;
	}

	public final ListProperty<Conocimiento> conocimientosProperty() {
		return this.conocimientos;
	}

	public final ObservableList<Conocimiento> getConocimientos() {
		return this.conocimientosProperty().get();
	}

	public final void setConocimientos(final ObservableList<Conocimiento> conocimientos) {
		this.conocimientosProperty().set(conocimientos);
	}

}

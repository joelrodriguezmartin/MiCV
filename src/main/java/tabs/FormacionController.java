package tabs;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


import classes.Titulo;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import main.LocalDateTableCell;

public class FormacionController implements Initializable {
	@FXML
    private HBox root;

    @FXML
    private TableView<Titulo> formacionTable;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;
    
    @FXML
    private TableColumn<Titulo, LocalDate> desdeColumn;
    
    @FXML
    private TableColumn<Titulo, LocalDate> hastaColumn;
    
    //model
    private ListProperty<Titulo> titulos = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    
	public FormacionController() throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormacionView.fxml"));
		loader.setController(this);
		loader.load();
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		desdeColumn.setCellFactory( t -> {return new LocalDateTableCell<>(t); });
		hastaColumn.setCellFactory( t -> {return new LocalDateTableCell<>(t); });
		formacionTable.itemsProperty().bindBidirectional(titulos);
		addButton.setOnAction(e -> addTitulo());
		removeButton.setOnAction(e -> removeTitulo());
	}
	private void removeTitulo() {
		Titulo titulo = formacionTable.getSelectionModel().getSelectedItem();
		
		if (titulo != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Confirmar");
			alert.setContentText("¿Seguro que quieres eliminar este titulo?");
			
			boolean confirmed = (alert.showAndWait().get() == ButtonType.OK);
			
	
			if( titulo != null && confirmed) {
				titulos.remove(titulo);
			}
		}
	}

	private void addTitulo() {
		//ni caso a los comentarios, son del ejemplo de dialog login
		// Create the custom dialog.
				Dialog<Titulo> dialog = new Dialog<>();
				dialog.setTitle("Nuevo titulo");
				dialog.setHeaderText("Nuevo titulo");

				// Set the button types.
				ButtonType addButton = new ButtonType("Añadir", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

				// Create the username and password labels and fields.
				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20, 150, 10, 10));

				TextField denominacionText = new TextField();
				grid.addRow(0, new Label("Denominación"), denominacionText);
					 
				TextField tituloText = new TextField();
				grid.addRow(1,  new Label("Organizador"), tituloText);
				
				GridPane.setHgrow(denominacionText, Priority.ALWAYS);
				GridPane.setHgrow(tituloText, Priority.ALWAYS);

				DatePicker desde = new DatePicker();
				grid.addRow(2, new Label("Desde"), desde);

				DatePicker hasta = new DatePicker();
				grid.addRow(3,  new Label("Hasta"), hasta);

				dialog.getDialogPane().setContent(grid);

				// Convert the result to a username-password-pair when the login button is clicked.
				dialog.setResultConverter(dialogButton -> {
				    if (dialogButton == addButton) {
				       Titulo titulo = new Titulo(desde.getValue(), hasta.getValue(), denominacionText.getText(), tituloText.getText());
				        return titulo;
				    }
				    return null;
				});
				Titulo titulo = dialog.showAndWait().get();
				
				if (titulo != null) {
					titulos.add(titulo);
				}
	}

	public HBox getRoot() {
		return root;
	}

	public final ListProperty<Titulo> titulosProperty() {
		return this.titulos;
	}
	

	public final ObservableList<Titulo> getTitulos() {
		return this.titulosProperty().get();
	}
	

	public final void setTitulos(final ObservableList<Titulo> titulos) {
		this.titulosProperty().set(titulos);
	}
	

}

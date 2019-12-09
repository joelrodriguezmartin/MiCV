package tabs;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import classes.Contacto;
import classes.Email;
import classes.Telefono;
import classes.Telefono.TipoTelefono;
import classes.Web;
import javafx.beans.binding.Bindings;

import javafx.beans.property.ObjectProperty;

import javafx.beans.property.SimpleObjectProperty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class ContactoController implements Initializable {
	@FXML
	private VBox root;

	@FXML
	private TableView<Telefono> telefonoTable;

	@FXML
	private Button addTlfButton;

	@FXML
	private Button removeTlfButton;

	@FXML
	private TableView<Email> emailTable;

	@FXML
	private Button addEmailButton;

	@FXML
	private Button removeEmailButton;

	@FXML
	private TableView<Web> webTable;

	@FXML
	private Button addWebButton;

	@FXML
	private Button removeWebButton;

	@FXML
	private TableColumn<Telefono, TipoTelefono> tipoColumn;

	private Alert alert;

	// model
	private ObjectProperty<Contacto> contacto = new SimpleObjectProperty<Contacto>();

	public ContactoController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ContactoView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tipoColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Telefono.getTipos()));
		//parece que los bindings se emparanoian asi que hay que cambiarlos cada vez 
		contacto.addListener((o, ol, nv) -> cambiarBindings(ol, nv));
		contacto.set(new Contacto());

		addTlfButton.setOnAction(e -> addTlf());
		addEmailButton.setOnAction(e -> addEmail());
		addWebButton.setOnAction(e -> addWeb());

		removeTlfButton.setOnAction(e -> removeTlf());
		removeEmailButton.setOnAction(e -> removeEmail());
		removeWebButton.setOnAction(e -> removeWeb());
	}

	private void removeWeb() {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Confirmar");
		alert.setContentText("¿Quiere eliminar este email?");
		boolean confirmed = (alert.showAndWait().get() == ButtonType.OK);
		
		Web web = webTable.getSelectionModel().getSelectedItem();
		if( web != null && confirmed) {
			getContacto().getWebs().remove(web);
		}
	}

	private void removeEmail() {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Confirmar");
		alert.setContentText("¿Quiere eliminar este email?");
		boolean confirmed = (alert.showAndWait().get() == ButtonType.OK);
		
		Email email = emailTable.getSelectionModel().getSelectedItem();
		if(email != null && confirmed) {
			getContacto().getEmails().remove(email);
		}
	}

	private void removeTlf() {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Confirmar");
		alert.setContentText("¿Quiere eliminar este telefono?");
		boolean confirmed = (alert.showAndWait().get() == ButtonType.OK);

		Telefono telefono = telefonoTable.getSelectionModel().getSelectedItem();
		if (telefono != null && confirmed) {
			getContacto().getTelefonos().remove(telefono);
		}
	}

	private void addWeb() {
		TextInputDialog in = new TextInputDialog("http://");
		in.setTitle("Nueva web");
		in.setHeaderText("Nueva web");
		in.setContentText("URL: ");
		
		String web = in.showAndWait().get();
		
		
		if(web != null) {
			getContacto().getWebs().add(new Web(web));
		}
	
	}

	private void addEmail() {
		TextInputDialog in = new TextInputDialog();
		in.setTitle("Nuevo email");
		in.setHeaderText("Nuevo email");
		in.setContentText("Direccion email: ");
		
		String email = in.showAndWait().get();
		
		
		if(email != null) {
			getContacto().getEmails().add(new Email(email));
		}
	}

	private void addTlf() {
		// Create the custom dialog.
		Dialog<Telefono> dialog = new Dialog<>();
		dialog.setTitle("Nuevo telefono");
		dialog.setHeaderText("Nuevo telefono");

		// Set the button types.
		ButtonType addButton = new ButtonType("Añadir", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		Label numeroLabel = new Label("Numero ");
		TextField numeroText = new TextField();

		Label tipoLabel = new Label("Tipo ");
		ComboBox<TipoTelefono> tipo = new ComboBox<Telefono.TipoTelefono>();
		tipo.setPromptText("Elige tipo");
		tipo.getItems().addAll(Telefono.getTipos());
		grid.add(numeroLabel, 0, 0);
		grid.add(numeroText, 1, 0);
		grid.add(tipoLabel, 0, 1);
		grid.add(tipo, 1, 1);


		dialog.getDialogPane().setContent(grid);

		

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == addButton) {
		        Telefono telefono = new Telefono(numeroText.getText(), tipo.getSelectionModel().getSelectedItem());
		        return telefono;
		    }
		    return null;
		});
		
		Telefono telefono = dialog.showAndWait().get();
		
		if (telefono != null) {
			getContacto().getTelefonos().add(telefono);
		}

		
	}

	private void cambiarBindings(Contacto ol, Contacto nv) {

		if (ol != null) {
			telefonoTable.itemsProperty().unbindBidirectional(ol.telefonosProperty());
			emailTable.itemsProperty().unbindBidirectional(ol.emailsProperty());
			webTable.itemsProperty().unbindBidirectional(ol.websProperty());
		}

		Bindings.bindBidirectional(telefonoTable.itemsProperty(), nv.telefonosProperty());
		Bindings.bindBidirectional(emailTable.itemsProperty(), nv.emailsProperty());
		Bindings.bindBidirectional(webTable.itemsProperty(), nv.websProperty());
	}

	

	public VBox getRoot() {
		return root;
	}

	public final ObjectProperty<Contacto> contactoProperty() {
		return this.contacto;
	}

	public final Contacto getContacto() {
		return this.contactoProperty().get();
	}

	public final void setContacto(final Contacto contacto) {
		this.contactoProperty().set(contacto);
	}

}

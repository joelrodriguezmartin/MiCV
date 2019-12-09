package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MiCVApp extends Application {
	
	RootController rootController;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        rootController = new RootController();
		
		Scene scene = new Scene(rootController.getRoot(), 800, 600);
		primaryStage.setTitle("MiCV");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);

	}

}

package main;

import java.io.IOException;

import controller.EffectOverviewController;
import controller.ImageOverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.EffectFile;
import model.ImageFile;

public class MainApp extends Application {

	private Stage stage;
	private BorderPane rootLayout;
	private AnchorPane view;

	@Override
	public void start(Stage primaryStage) {
		ImageFile.load();
		EffectFile.load();
		
		stage = new Stage();
		stage.setTitle("RW Bit Images");
		stage.setMinWidth(500);
		stage.setMinHeight(500);
		Image applicationIcon = new Image(getClass().getResourceAsStream("../resources/icon.png"));
		stage.getIcons().add(applicationIcon);

		initRootLayout();

		showImageOverview();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showImageOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/ImageOverview.fxml"));
			view = (AnchorPane) loader.load();

			rootLayout.setCenter(view);

			ImageOverviewController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showEffectOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/EffectOverview.fxml"));
			view = (AnchorPane) loader.load();

			rootLayout.setCenter(view);

			EffectOverviewController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public Stage getPrimaryStage() {
//		return stage;
//	}

}

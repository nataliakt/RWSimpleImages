package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.MainApp;

public class RootLayoutController {
	
	private MainApp mainApp;
	
	public MainApp getMainApp() {
		return mainApp;
	}

	@FXML
	private void handleInfo() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sobre");
		alert.setHeaderText("Sobre o RW Bit Images");
		Text text = new Text("Desenvolvido por Natalia Kelim Thiel para a "
				+ "disciplina de Computação Gráfica do curso de Bacharelado em Ciência da Computação "
				+ "no Instituto Federal Catarinense, unidade urbana de Rio do Sul - SC.\nMarço de 2017");
		text.setWrappingWidth(350);
		text.setTextAlignment(TextAlignment.JUSTIFY);
		text.setLineSpacing(5);
		BorderPane bp = new BorderPane();
		bp.setCenter(text);
		alert.getDialogPane().setContent(bp);
		alert.getDialogPane().setPrefWidth(400);
		alert.showAndWait();
	}
}

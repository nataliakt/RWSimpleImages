package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.MainApp;
import model.Image;
import model.ImageFile;
import model.ImageList;

public class ImageOverviewController {

	@FXML
	private TableView<Image> imageTable;

	@FXML
	private TableColumn<Image, String> nameColumn;

	@FXML
	private TableColumn<Image, String> idColumn;

	@FXML
	private TableColumn<Image, String> widthColumn;

	@FXML
	private TableColumn<Image, String> heightColumn;
	
	@FXML
	private TextField filter;

	private MainApp mainApp;

	public ImageOverviewController() {
		super();
	}

	private void updateTable() {
		ObservableList<Image> ol = FXCollections.observableArrayList(ImageList.getInstance().getImages());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		widthColumn.setCellValueFactory(cellData -> cellData.getValue().getWidthProperty());
		heightColumn.setCellValueFactory(cellData -> cellData.getValue().getHeightProperty());
		imageTable.setItems(ol);
	}

	@FXML
	private void initialize() {
		ImageList.getInstance().clearFilter();
		updateTable();
	}
	
	@FXML
	private void focusTextField() {
		filter.requestFocus();
	}

	@FXML
	private void handleUpload() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Arquios BitMap", "*.ppm", "*.pgm", "*.pbm", "*.pnm"),
				new ExtensionFilter("Arquivos PPM", "*.ppm"),
				new ExtensionFilter("Arquivos PGM", "*.pgm"),
				new ExtensionFilter("Arquivos PBM", "*.pbm"));
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			ImageList il = ImageList.getInstance();
			Image i = ImageFile.load(selectedFile.getAbsolutePath());
			String input = i.getName();
			while (il.hasFileName(input + i.getExtension())) {
				input = inputDialog("Carreagar Imagem", "Nome em uso, informe outro nome", "Nome:", i.getName());
				i.setName(input);
			}
			if (!input.equals("")) {
				ImageFile.save(i);
				ImageList.getInstance().addImage(i);
				updateTable();
			}
		}
	}

	@FXML
	private void handleView() {
		int selectedIndex = imageTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			ImageList il = ImageList.getInstance();
			Image i = il.getImage(selectedIndex);
			String path = i.getFilePath();
			if (!i.isLoad()) {
				i = ImageFile.load(path);
			}
			new Thread(() -> {
				try {
					Desktop.getDesktop().open(new File(path));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}).start();
		} else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhuma imagem selecionada", "Selecione uma imagem para visualizar!");
		}
	}
	
	@FXML
	private void handleHistogram() {
		int selectedIndex = imageTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			ImageList il = ImageList.getInstance();
			Image i = il.getImage(selectedIndex);
			String channel = "R";
			if (i.getId().contains("P3")) {
				channel = inputDialog("Canal", "Informe um canal (R, G ou B)", "Canal", "R");
			}
			if (channel != null) {
				channel = channel.toUpperCase();
				if (channel.equals("R") || channel.equals("G") || channel.equals("B")) {
				
					FileChooser fileChooser = new FileChooser();
			        
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivo CSV", "*.csv");
		            fileChooser.getExtensionFilters().add(extFilter);
		            
			        File file = fileChooser.showSaveDialog(null);
			        
			        if(file != null){
			        	String ext = "";
			        	if (!file.getName().substring(file.getName().length() - 4, file.getName().length()).equals(".csv")) {
				        	ext = ".csv";
			        	}
			        	try {
				        	FileWriter fw = new FileWriter(file.getAbsolutePath() + ext, false);
							fw.write(i.getHistogram().getChannel(channel));
							fw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
			        }
				} else {
					alertDialog(AlertType.ERROR, "Erro", "Canal inválido", "Informe um canal válido ou deixe o padrão!");
				}
			}
		} else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhuma imagem selecionada", "Selecione uma imagem para gerar o histograma!");
		}
	}

	@FXML
	private void handleRename() {
		int selectedIndex = imageTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			ImageList il = ImageList.getInstance();
			Image i = il.getImage(selectedIndex);
			String input = inputDialog("Renomear", "Insira um novo nome", "Nome:", i.getName());
			while (il.hasFileName(input + i.getExtension()) && !input.equals("")) {
				input = inputDialog("Renomear", "Nome em uso, insira outro nome", "Nome:", i.getName());
			}
			if (input != null && !input.equals("")) {
				if (!i.isLoad()) {
					i = ImageFile.load(i.getFilePath());
				}
				ImageFile.remove(i);
				i.setName(input);
				ImageFile.save(i);
				ImageFile.load();
				updateTable();
			}
		} else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhuma imagem selecionada", "Selecione uma imagem para renomear!");
		}
	}

	@FXML
	private void handleRemove() {
		int selectedIndex = imageTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			ImageList il = ImageList.getInstance();
			Image i = il.getImage(selectedIndex);
			if (alertDialog(AlertType.CONFIRMATION, "Remover", 
					"Confirme a remoção",
					"Tem certeza que deseja remover " + i.getName() + i.getExtension() + " da lista de images?")) {
				imageTable.getItems().remove(selectedIndex);
				il.remove(i);
				ImageFile.remove(i);
			}
		} else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhuma imagem selecionada", "Selecione uma imagem para remover!");
		}
	}

	@FXML
	private void handleEffect() {
		int selectedIndex = imageTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			ImageList il = ImageList.getInstance();
			Image i = il.getImage(selectedIndex);
			il.setSelected(i);
			//System.out.println(i);
			mainApp.showEffectOverview();
		} else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhuma imagem selecionada", "Selecione uma imagem aplicar um efeito!");
		}
	}
	
	private void filter (String text) {
		ImageList.getInstance().filter(text);
		updateTable();
	}
	
	@FXML
	private void filterLoad() {
		filter(filter.getText());
	}

	private boolean alertDialog(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		Text text = new Text(content);
		text.setWrappingWidth(350);
		text.setTextAlignment(TextAlignment.JUSTIFY);
		text.setLineSpacing(5);
		BorderPane bp = new BorderPane();
		bp.setCenter(text);
		alert.getDialogPane().setContent(bp);
		alert.getDialogPane().setPrefWidth(400);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			return true;
		}
		return false;
	}

	private String inputDialog(String title, String header, String label, String input) {
		TextInputDialog dialog = new TextInputDialog(input);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(label);
		dialog.getDialogPane().setPrefWidth(400);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}

package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import main.MainApp;
import model.Effect;
import model.EffectFile;
import model.EffectList;
import model.Image;
import model.ImageFile;
import model.ImageList;
import model.Parameter;

public class EffectOverviewController {

	@FXML
	private TableView<Effect> effectTable;

	@FXML
	private TableColumn<Effect, String> nameColumn;

	@FXML
	private TableColumn<Effect, String> toColumn;

	@FXML
	private Label imageName;

	@FXML
	private Label imageId;

	@FXML
	private Label imageWidth;

	@FXML
	private Label imageHeight;

	@FXML
	private Label imageMax;

	@FXML
	private TextField filter;

	private MainApp mainApp;

	public EffectOverviewController() {
		super();

	}

	@FXML
	private void initialize() {
		EffectFile.load();
		filter("");
		Image i = ImageList.getInstance().getSelected();
		imageName.setText(i.getName());
		imageId.setText(i.getId());
		imageWidth.setText(Integer.toString(i.getWidth()));
		imageHeight.setText(Integer.toString(i.getHeight()));
		imageMax.setText(Integer.toString(i.getMax()));
	}

	private void updateTable() {
		ObservableList<Effect> ol = FXCollections.observableArrayList(EffectList.getInstance().getEffects());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		toColumn.setCellValueFactory(cellData -> cellData.getValue().getToProperty());
		effectTable.setItems(ol);
	}

	private void filter(String text) {
		EffectList.getInstance().filter(text);
		updateTable();
	}
	
	@FXML
	private void focusTextField() {
		filter.requestFocus();
	}
	
	@FXML
	private void filterLoad() {
		filter(filter.getText());
	}

	@FXML
	private void handleReturn() {
		mainApp.showImageOverview();
	}

	@FXML
	private void handleDescription() {
		int selectedIndex = effectTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			EffectList el = EffectList.getInstance();
			Effect e = el.getEffect(selectedIndex);
			alertDialog(AlertType.INFORMATION, "Sobre o efeito", e.getName(), e.getDescription());
		} else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhum efeito selecionado", "Selecione um efeito para ver a descrição!");
		}
	}

	@FXML
	private void handleView() {
		String path = ImageList.getInstance().getSelected().getFilePath();
		new Thread(() -> {
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (Exception e1) {
				alertDialog(AlertType.ERROR, "Erro",
						"Problema ao exibir a imagem",
						"Certifique-se de que seu sistema operacional têm suporte a imagens binárias!");
			}
		}).start();
	}
	
	@FXML
	private void handleViewEffect() {
		int selectedIndex = effectTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			Effect e = EffectList.getInstance().getEffect(selectedIndex);
			Image i = makeEffect(e);
			if (i != null) {
				ImageFile.saveTemp(i);
				new Thread(() -> {
					try {
						Desktop.getDesktop().open(new File(ImageFile.TEMPFILE));
					} catch (Exception e1) {
						alertDialog(AlertType.ERROR, "Erro",
								"Problema ao exibir a imagem",
								"Certifique-se de que seu sistema operacional têm suporte a imagens binárias!");
					}
				}).start();
			}
		} else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhum efeito selecionado", "Selecione um efeito para ver a imagem com efeito!");
		}
	}
	
	@FXML
	private void handleSave() {
		int selectedIndex = effectTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			Effect e = EffectList.getInstance().getEffect(selectedIndex);
			Image i = makeEffect(e);
			if (i != null) {
				String input = inputDialog("Salvar Como", "Informe um nome", "Nome:", i.getName());
				i.setName(input);
				while (ImageList.getInstance().hasFileName(input + i.getExtension())) {
					input = inputDialog("Salvar Como", "Nome em uso, informe outro nome", "Nome:", i.getName());
					i.setName(input);
				}
				if (input != null && !input.equals("")) {
					ImageFile.save(i);
					ImageList.getInstance().addImage(i);
					mainApp.showImageOverview();
				}
			}
		} else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhum efeito selecionado", "Selecione um efeito para ver a imagem com efeito!");
		}
	}
	
	@FXML
	private void handleDownloadEffect() {
		int selectedIndex = effectTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > -1) {
			Effect e = EffectList.getInstance().getEffect(selectedIndex);
			Image select = makeEffect(e);
		
			FileChooser fileChooser = new FileChooser();
			
	        
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivo " + select.getExtension().substring(1).toUpperCase(), "*" + select.getExtension());
            fileChooser.getExtensionFilters().add(extFilter);
            FileChooser.ExtensionFilter extFilterPattern = new FileChooser.ExtensionFilter("Arquivo PNM", "*.pnm");
            fileChooser.getExtensionFilters().add(extFilterPattern);
	        
            
	        File file = fileChooser.showSaveDialog(null);
	        
	        if(file != null){
	        	String ext = "";
	        	if (!file.getName().contains(".")) {
		        	if (fileChooser.getSelectedExtensionFilter().equals(extFilter)) {
		        		ext = select.getExtension();
		        	} else {
		        		ext = ".pnm";
		        	}
	        	}
				ImageFile.save(select, file.getAbsolutePath() + ext);
	        }
		}else {
			alertDialog(AlertType.ERROR, "Erro", "Nenhum efeito selecionado", "Selecione um efeito para baixar a imagem!");
		}
    }
	
	@FXML
	private void handleHistogram() {
		Image i = ImageList.getInstance().getSelected();
		
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
	}
	
	@FXML
	private void handleDownload() {
		FileChooser fileChooser = new FileChooser();
		
	    
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivo " + ImageList.getInstance().getSelected().getExtension().substring(1).toUpperCase(), "*" + ImageList.getInstance().getSelected().getExtension());
	    fileChooser.getExtensionFilters().add(extFilter);
	    FileChooser.ExtensionFilter extFilterPattern = new FileChooser.ExtensionFilter("Arquivo PNM", "*.pnm");
	    fileChooser.getExtensionFilters().add(extFilterPattern);
	    
	    
	    File file = fileChooser.showSaveDialog(null);
	    
	    if(file != null){
	    	String ext = "";
	    	if (!file.getName().contains(".")) {
	        	if (fileChooser.getSelectedExtensionFilter().equals(extFilter)) {
	        		ext = ImageList.getInstance().getSelected().getExtension();
	        	} else {
	        		ext = ".pnm";
	        	}
	    	}
			ImageFile.save(ImageList.getInstance().getSelected(), file.getAbsolutePath() + ext);
	    }
	
    }
	
	private Image makeEffect(Effect e) {
		e.setImage(ImageList.getInstance().getSelected());
		String result = "";
		if (e.isParameterized()) {
			boolean cancel = false;
			int index = 0;
			for (Parameter p : e.getParameters()) {
				if (!cancel) {
					result = inputDialog("Parâmetro de " + e.getName(), p);
					if (result != null) {
						if (result.equals("")) {
							p.setValue(p.getDefaultValue());
						} else {
							try {
								if (p.getType().equals("integer")) {
									e.setParameter(index, Integer.parseInt(result));
								} else if (p.getType().equals("image")){
									Image selected = ImageList.getInstance().getImage(result);
									if (!selected.isLoad()) {
										selected = ImageFile.load(selected.getFilePath());
										selected.setLoad(true);
									}
									e.setParameter(index, selected);
								} else {
									e.setParameter(index, result);
								}
							} catch (Exception e1) {
								alertDialog(AlertType.ERROR, "Erro",
										"Parâmetros não suportados",
										"Informe parâmetros suportados.\nCaso não saiba os parâmetros deixe padrão ou leia a descrição do efeito.");
							}
						}
					} else {
						cancel = true;
					}
				}
				index++;
			}
		}
		if (result != null) {
			try {
				Image i = e.effect();
				if (i != null) {
					return i;
				} else {
					alertDialog(AlertType.ERROR, "Erro",
							"Parâmetros não suportados",
							"Informe parâmetros suportados.\nCaso não saiba os parâmetros deixe padrão ou leia a descrição do efeito.");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				alertDialog(AlertType.ERROR, "Erro",
						"Algo aconteceu ao aplicar o efeito",
						"Verifique se os parâmetros estão corretos.\n"
						+ "Caso o problema persistir verifique e a implementação do algorítmo!\n"
						+ "Exception no console.");
			}
		}
		return null;
	}

	private void alertDialog(AlertType type, String title, String header, String content) {
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
		alert.showAndWait();
	}

	private String inputDialog(String title, String header, String label, String input) {
		TextInputDialog dialog = new TextInputDialog(input);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(label);
		dialog.getDialogPane().setPrefWidth(400);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent() && result.isPresent()) {
			return result.get();
		}
		return null;
	}

	private String inputDialog(String title, Parameter p) {
		if (p.getType().equals("image")) {
			List<String> choices = new ArrayList<>();
			ImageList.getInstance().clearFilter();
			for (Image i : ImageList.getInstance().getImages()) {
				choices.add(i.getName() + i.getExtension());
			}

			ChoiceDialog<String> dialog = new ChoiceDialog<>(p.getDefaultValue(), choices);
			dialog.setTitle(title);
			dialog.setHeaderText(p.getRequest());
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				return result.get();
			}
		} else {
			TextInputDialog dialog = new TextInputDialog(p.getDefaultValue());
			dialog.setTitle(title);
			dialog.setHeaderText(p.getRequest());
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				return result.get();
			}
		}
		return null;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}

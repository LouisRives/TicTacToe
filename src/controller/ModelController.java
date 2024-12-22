package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import application.Main;
import java.io.File;

public class ModelController {
	
	private Main main;

	@FXML
	ListView<String> listViewFile;
	@FXML
	ListView<Button> listViewButton;
	
	@FXML
    private void initialize() {
		
        setMain(main);

        ObservableList<String> fileList = FXCollections.observableArrayList();
        ObservableList<Button> buttonList = FXCollections.observableArrayList();

        File folder = new File("resources/models");
        File[] files = folder.listFiles();
        listViewFile.setStyle("-fx-font-size: 16;");

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file.getName());
                    Button button = new Button("Supprimer");
                    button.setOnAction(event -> {
                        String chemin = "resources/models/" + file.getName();
                        File fichierASupprimer = new File(chemin);
                        if (fichierASupprimer.delete()) {
                            System.out.println("Le fichier " + fichierASupprimer.getName() + " a été supprimé avec succès.");
                            fileList.remove(file.getName());
                            buttonList.remove(button);
                            listViewButton.refresh();
                            listViewFile.refresh();
                        } else {
                            System.out.println("Impossible de supprimer le fichier " + fichierASupprimer.getName() + ".");
                        }
                    });
                    buttonList.add(button);
                }
            }
        }
        listViewButton.setItems(buttonList);
        listViewFile.setItems(fileList);
    }
	
	public void setMain(Main main) {
		this.main = main;
	}
	
    @FXML
    private void onClickButtonAccueil() {
    	main.showWelcome();
    }
}
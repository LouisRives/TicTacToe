package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingController {
	
	private Main main;

	@FXML
	StackPane paneStatus;
	@FXML
	Button buttonSoumettre;
	@FXML
	TextField fieldF1;
	@FXML
	TextField fieldF2;
	@FXML
	TextField fieldF3;
	@FXML
	TextField fieldM1;
	@FXML
	TextField fieldM2;
	@FXML
	TextField fieldM3;
	@FXML
	TextField fieldD1;
	@FXML
	TextField fieldD2;
	@FXML
	TextField fieldD3;
	
	@FXML
	private void initialize() {
        setMain(main);
        paneStatus.setVisible(false);
		//Lecture du textFile et récupération des valeurs
		try {
		       double[][] tableau = new double[3][3];
		        try (BufferedReader reader = new BufferedReader(new FileReader("resources/config.txt"))) {
		            String line;
		            int rowIndex = 0;
		            while ((line = reader.readLine()) != null && rowIndex < tableau.length) {
		                String[] parts = line.split(":");
		                for (int i = 1; i < parts.length && i - 1 < tableau[rowIndex].length; i++) {
	                        tableau[rowIndex][i - 1] = Double.parseDouble(parts[i]);
		                }
		                rowIndex++;
		            }
		            for (double[] row : tableau) {
		                for (double value : row) {
		                    System.out.print(value + " ");
		                }
		                System.out.println();
		            }
		        }
		        setupTextField(fieldF1);
                setupTextField(fieldF2);
                setupTextField(fieldF3);
                setupTextField(fieldM1);
                setupTextField(fieldM2);
                setupTextField(fieldM3);
                setupTextField(fieldD1);
                setupTextField(fieldD2);
                setupTextField(fieldD3);
		        
		        //ecriture des valeurs dans les cases textfield
		        fieldF1.setText(Double.toString(tableau[0][0]));
		        fieldF2.setText(Double.toString(tableau[0][1]));
		        fieldF3.setText(Double.toString(tableau[0][2]));
		        fieldM1.setText(Double.toString(tableau[1][0]));
		        fieldM2.setText(Double.toString(tableau[1][1]));
		        fieldM3.setText(Double.toString(tableau[1][2]));
		        fieldD1.setText(Double.toString(tableau[2][0]));
		        fieldD2.setText(Double.toString(tableau[2][1]));
		        fieldD3.setText(Double.toString(tableau[2][2]));

		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
    @FXML
    private void onClickButtonAccueil() {
    	main.showWelcome();
    }
    
	
	@FXML
	private void onClickSoumettre() {
		try {
		    double[][] tableau = new double[3][3];
		    // Récupération des valeurs des TextField
		    tableau[0][0] = Double.parseDouble(fieldF1.getText());
		    tableau[0][1] = Double.parseDouble(fieldF2.getText());
		    tableau[0][2] = Double.parseDouble(fieldF3.getText());
		    tableau[1][0] = Double.parseDouble(fieldM1.getText());
		    tableau[1][1] = Double.parseDouble(fieldM2.getText());
		    tableau[1][2] = Double.parseDouble(fieldM3.getText());
		    tableau[2][0] = Double.parseDouble(fieldD1.getText());
		    tableau[2][1] = Double.parseDouble(fieldD2.getText());
		    tableau[2][2] = Double.parseDouble(fieldD3.getText());
		    // Écriture des valeurs dans le fichier
		    try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/config.txt"))) {
		        for (double[] row : tableau) {
		            writer.write(String.format(Locale.US, "%s:%.0f:%.0f:%.2f%n", 
		                                       (row == tableau[0] ? "F" : (row == tableau[1] ? "M" : "D")), 
		                                       row[0], row[1], row[2]));
		        }
		        writer.flush();
		    }
		    paneStatus.setVisible(true);
		} catch (IOException | NumberFormatException e) {
		    e.printStackTrace();
		}
	}
	
	// Protèges les cases TextField afin de n'y insérer que des integer ou double.
	private void setupTextField(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                field.setText(oldValue);
            }
        });
    }
}
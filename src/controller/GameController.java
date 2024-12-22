package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;

import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import application.Main;
import javafx.collections.ObservableList;
import ai.*;
import model.*;

public class GameController {

	private Main main;

    private int coup = 1;
	private boolean adversaire;
	private String difficulteAI;
    private MultiLayerPerceptron model;

    private int[] plateau = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private SequentialTransition sequentialTransition;

    private MediaPlayer soundWin;
    private MediaPlayer soundDefeat;
    private int secondes;
    private Timeline chronometre;
    
    @FXML
    private AnchorPane modal;
    @FXML
    private Text indicateur;

    @FXML
    private Pane c1, c2, c3, c4, c5, c6, c7, c8, c9;
    
    @FXML
    private Line l1,l2,l3,l4,l5,l6,l7,l8;
    
    @FXML
    private Button rejouer;
    
    @FXML
    private Label chronometreLabel;

    @FXML
    private void initialize() {
    	setMain(main);
		modal.setVisible(false);
		
		Pane[] paneArray = new Pane[]{c1, c2, c3, c4, c5, c6, c7, c8, c9}; // Cache tous les ronds et croix lorsque la partie commence
		for (int i = 0; i < paneArray.length; i++) {
		    ObservableList<Node> children = paneArray[i].getChildren();
		    Pane innerPaneCroix = (Pane) children.get(1); // Croix
		    innerPaneCroix.setVisible(false);
		    Pane innerPaneRond = (Pane) children.get(2); // Cond
		    innerPaneRond.setVisible(false);
		}
		
		Line[] lineArray = new Line[]{l1,l2,l3,l4,l5,l6,l7,l8}; // Cache toutes les lignes de victoire lorsque la partie commence
		for (int i = 0; i < lineArray.length; i++) {
			lineArray[i].setVisible(false);
		}

		rejouer.setOpacity(0); // Cache le bouton "rejouer", il apparaitra une fois la partie finit
    	Platform.runLater(() -> {
			indicateur.setText("C'est à vous de jouer !");
			initializeChronometre();
			
			if (adversaire==false) { // Récupération du modèle si l'adversaire = false (soit on joue contre l'IA)
				String modelPath = "resources/models/"+difficulteAI;
		        model = MultiLayerPerceptron.load(modelPath);
		        
		        if (model == null) {
		            // Le chargement du modèle a échoué, gérer l'erreur
		            System.err.println("Impossible de charger le modèle.");
		        } else {
		            System.out.println("Modèle "+difficulteAI+" chargé avec succès !");
		        }
			}
    	});
    	setSoundsEffects(); // Affectation des sons de victoire ainsi que défaite
    }
    
	public void setMain(Main main) {
		this.main = main;
	}

    public void setAdversaire(boolean adversaire) {
        this.adversaire = adversaire;
    }
    
    public void setDifficulteAI(String difficulteAI) {
        this.difficulteAI = difficulteAI;
    }
	
    @FXML
    private void onClickButtonAccueil() {
    	main.showWelcome();
    }
    
    @FXML
    private void onClickCase(MouseEvent event) {
    	if (coup!=3)
    		coup=GameModel.onClickCaseModel(event, model, rejouer, coup, indicateur, adversaire, soundDefeat, soundWin, plateau, sequentialTransition, c1,c2,c3,c4,c5,c6,c7,c8,c9,l1,l2,l3,l4,l5,l6,l7,l8, chronometre);
    }
    
    @FXML
    private void onRejouerClicked() {
    	if (adversaire) {
    		main.showGame(adversaire, difficulteAI);
    	}
    	else {
    		modal.setVisible(true);
    	}
//        main.showGame(adversaire, difficulteAI);
    }

    private void setSoundsEffects() { // Méthode d'aaffectation des sons de victoire & défaite
    	String soundFileWin = "/audio/win_sound_effect.mp3";
        Media soundWinn = new Media(getClass().getResource(soundFileWin).toString());
        soundWin = new MediaPlayer(soundWinn);
       
    	String soundFileDefeat = "/audio/defeat_sound_effect.mp3";
        Media soundDefeatt = new Media(getClass().getResource(soundFileDefeat).toString());
        soundDefeat = new MediaPlayer(soundDefeatt);
    }
    
	@FXML
    private void onClickButtonLancerAI_Facile() { // Boutons rejouer pour le modal lorsque l'on rejoue contre l'IA 
		GameModel.onClickButtonLancerAI_Facile(main);
	}
	
	@FXML
    private void onClickButtonLancerAI_Moyen() { 
		GameModel.onClickButtonLancerAI_Moyen(main);
	}
	
	@FXML
    private void onClickButtonLancerAI_Difficile() {
		GameModel.onClickButtonLancerAI_Difficile(main);
	}
	
	private void initializeChronometre() { // Initialisation du chronomètre
        chronometre = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Action à effectuer chaque seconde
            secondes++;
            String temps=GameModel.updateChronometreLabel(secondes);
            chronometreLabel.setText(temps);
        }));
        chronometre.setCycleCount(Timeline.INDEFINITE); // Le chronomètre se répète indéfiniment
        chronometre.play(); // Démarrer le chronomètre
    }
}










//Samy Louis









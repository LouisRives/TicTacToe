package application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import controller.GameController;
import controller.LearnController;
import controller.ModelController;
import controller.ParametreController;
import controller.RootController;
import controller.SettingController;
import controller.WelcomeController;

public class Main extends Application {

    private Stage primaryStage;
    private MediaPlayer mediaPlayer;
    private BorderPane root;
    
    @Override
    public void start(Stage primaryStage) {
    	
        // Initialisation de la fenêtre principale
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Morpion");
        initRootLayout();

        // Lecture de la musique en arrière-plan
        String audioFile = "/audio/music.mp3";
        Media media = new Media(getClass().getResource(audioFile).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        // Lecture d'un effet sonore au clic de la souris
        String soundFile = "/audio/click_sound_effect.mp3";
        Media sound = new Media(getClass().getResource(soundFile).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        // Action à effectuer lors d'un clic de la souris
        primaryStage.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                mediaPlayer.stop();
                mediaPlayer.play();
            }
        });
        // Affichage de l'écran d'accueil
        showWelcome();
    }

    // Initialisation de la mise en page principale
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Root.fxml"));
            root = (BorderPane) loader.load();
            RootController rootController = loader.getController(); 
            rootController.setMain(this);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Affichage de l'écran d'accueil
    public void showWelcome() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Welcome.fxml"));
            BorderPane welcome = (BorderPane) loader.load();
            WelcomeController welcomeController = loader.getController();
            welcomeController.setMain(this);
            root.setCenter(welcome);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Affichage de la page d'apprentissage IA
    public void showLearns(String config) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Learn.fxml"));
            BorderPane learns = (BorderPane) loader.load();
            LearnController learnController = loader.getController();
            learnController.setMain(this);
            learnController.setConfig(config);

            root.setCenter(learns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Affichage de l'écran Setting (ParamètreIA)
    public void showSetting() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Setting.fxml"));
            BorderPane learns = (BorderPane) loader.load();
            SettingController settingController = loader.getController();
            settingController.setMain(this);
            root.setCenter(learns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Affichage de la page des modèles
    public void showModel() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Model.fxml"));
            BorderPane learns = (BorderPane) loader.load();
            ModelController modelController = loader.getController();
            modelController.setMain(this);
            root.setCenter(learns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Affichage de l'écran de Jeu
    public void showGame(boolean adversaireValue, String difficulteAI) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Game.fxml"));
            BorderPane learns = (BorderPane) loader.load();

            GameController controller = loader.getController();
            controller.setAdversaire(adversaireValue);
            controller.setDifficulteAI(difficulteAI);

            GameController gameController = loader.getController();
            gameController.setMain(this);
            
            root.setCenter(learns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affichage de la page Paramètre
    public void showParametre() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/View/Parametre.fxml"));
            BorderPane parametre = (BorderPane) loader.load();

            ParametreController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.setMediaPlayer(mediaPlayer);
            
            ParametreController parametreController = loader.getController();
            parametreController.setMain(this);
            
            root.setCenter(parametre);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public Stage getPrimaryStage() {
		return primaryStage;
	}

    // Méthode principale pour lancer l'application
	public static void main(String[] args) {
		launch(args);
	}
}

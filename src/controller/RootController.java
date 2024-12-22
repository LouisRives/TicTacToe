package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import java.io.IOException;
import java.util.HashMap;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import application.Main;

public class RootController {
	@FXML
	MenuItem learn;
	@FXML
	MenuItem accueil;
	@FXML
	MenuItem setting;
	@FXML
	MenuItem model;
	@FXML
	MenuItem parametre;

	@FXML
	private BorderPane root;
	private Main main;
	
	@FXML
    public void initialize() {
    	setMain(main);
    	
    }
    
	public void setMain(Main main) {
		this.main = main;
	}
	
	@FXML
	private void onClickAccueil() {
		main.showWelcome();
	}
	
	@FXML
	private void onClickParametre() {
		main.showParametre();
	}
	
	@FXML
	private void onClickLearn() {
		main.showLearns("config");
	}
	
	@FXML
	private void onClickSetting() {
		main.showSetting();
	}
	
	@FXML
	private void onClickModel() {
		main.showModel();
	}
	
}
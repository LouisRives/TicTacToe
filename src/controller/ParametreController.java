package controller;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class ParametreController {

	private Main main;

    private ToggleGroup toggleGroup;

    @FXML
    private CheckBox resolution;

    @FXML
    private Slider volumeSlider;

//    @FXML
//    private RadioButton optionGris;
//
//    @FXML
//    private RadioButton optionRouge;
//
//    @FXML
//    private RadioButton optionBleu;
//
//    @FXML
//    private RadioButton optionVert;

    private Stage stage;
    private MediaPlayer mediaPlayer;

    public void setStage(Stage stage) {
        this.stage = stage;
        if (stage.isFullScreen())
            resolution.setSelected(true);
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        volumeSlider.setValue(mediaPlayer.getVolume() * 100.0);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
        });
    }

    @FXML
    public void initialize() {
        setMain(main);

    }

	public void setMain(Main main) {
		this.main = main;
	}
	
    @FXML
    private void onClickButtonAccueil() {
    	main.showWelcome();
    }
    
    @FXML
    private void toggleFullscreen() {
        Stage stage = (Stage) resolution.getScene().getWindow();
        stage.setFullScreen(resolution.isSelected());
    }
}

package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import application.Main;

public class WelcomeController {
	
	private Main main;
	
	@FXML
	Button AI_F;
	Button AI_M;
	Button AI_D;
	Button J1vsJ2;
	Button quit;
	
	@FXML
    public void initialize() {
    	setMain(main);
    }
    
	public void setMain(Main main) {
		this.main = main;
	}
	
	@FXML
    private void onClickButtonLancer() {
		main.showGame(true,"");
	}
	
	@FXML
    private void onClickButtonLancerAI_Facile() {
		String difficulteAI=getLastModelFileOfDifficulty("F");
		if (difficulteAI == null) {
			main.showLearns("configF");
		}
		else 
			main.showGame(false,difficulteAI);
	}
	
	@FXML
    private void onClickButtonLancerAI_Moyen() {
		String difficulteAI=getLastModelFileOfDifficulty("M");
		if (difficulteAI == null) {
			main.showLearns("configM");
		}
		else 
			main.showGame(false,difficulteAI);
	}
	
	@FXML
    private void onClickButtonLancerAI_Difficile() {
		String difficulteAI=getLastModelFileOfDifficulty("D");
		if (difficulteAI == null) {
			main.showLearns("configD");
		}
		else 
			main.showGame(false,difficulteAI);
	}
	
	@FXML
    private void onClickButtonQuit() {
        Platform.exit();
	}
	
	public static String getLastModelFileOfDifficulty(String difficulteAI) {
        File folder = new File("resources/models");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("net_"+difficulteAI+"_") && name.endsWith(".srl"));

        if (files != null && files.length > 0) {
            // Trier les fichiers par ordre décroissant de numéro de modèle
            Arrays.sort(files, Comparator.comparingInt(f -> Integer.parseInt(f.getName().split("_")[2].split("\\.")[0])));
            // Retourner le nom du dernier fichier
            return files[files.length - 1].getName();
        } else {
            return null;
        }
    }
}

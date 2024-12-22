package model;

import ai.Coup;
import ai.MultiLayerPerceptron;
import application.Main;
import controller.GameController;
import controller.WelcomeController;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Arrays;

public class GameModel {    
    public static void onClickButtonLancerAI_Facile(Main main) { // Bouton pour rejouer en fin de partie contre l'IA avec difficultés
		String difficulteAI=WelcomeController.getLastModelFileOfDifficulty("F"); // Récupère le dernier modèle de difficulté "F" (facile)
		if (difficulteAI == null) { // Si aucun modèle facile n'existe, l'utilisateur est renvoyé sur la page "Learn" afin de créer un modèle facile
			main.showLearns("configF");
		}
		else  // Si un modèle Facile existe déjà, la partie se lance avec celui-ci
			main.showGame(false,difficulteAI);
	}
	
	public static void onClickButtonLancerAI_Moyen(Main main) { 
		String difficulteAI=WelcomeController.getLastModelFileOfDifficulty("M");
		if (difficulteAI == null) {
			main.showLearns("configM");
		}
		else 
			main.showGame(false,difficulteAI);
	}
	
    public static void onClickButtonLancerAI_Difficile(Main main) {
		String difficulteAI=WelcomeController.getLastModelFileOfDifficulty("D");
		if (difficulteAI == null) {
			main.showLearns("configD");
		}
		else 
			main.showGame(false,difficulteAI);
	}
	
    //Fonction OnClickCasModel: cette fonction gère le jeu, elle s'occupe du évènement OnClick sur notre jeu
    public static int onClickCaseModel(MouseEvent event, MultiLayerPerceptron model, Button rejouer, int coup, Text indicateur, boolean adversaire, MediaPlayer soundDefeat, MediaPlayer soundWin, int[] plateau, SequentialTransition sequentialTransition, Pane c1, Pane c2, Pane c3, Pane c4, Pane c5, Pane c6, Pane c7, Pane c8, Pane c9, Line l1, Line l2, Line l3, Line l4, Line l5, Line l6, Line l7, Line l8, Timeline chronometre) {
    	if (coup != 3) { // coup == 3 signifie que la partie est finit donc on vérifie que la partie n'est pas finit.
            Pane clickedPane = (Pane) event.getSource(); // Récupération de notre Pane cliqué
            String nodeId = clickedPane.getId(); // Récupération de son id
            int cellIndex = Integer.parseInt(nodeId.substring(1)) - 1;

            // Vérifies si la case est vide avant de jouer et mets à jour le plateau de jeu avec la case cliquée
            if (plateau[cellIndex] == 0) {
                if (coup == 1) // Si c'est le tour du joueur 1, on met à jour le tableau avec une croix
                    plateau[cellIndex] = 1; 
                if (coup == 2) // Si c'est le tour du joueur 2, on met à jour le tableau avec un rond
                    plateau[cellIndex] = 2;

                System.out.println("Nouveau plateau: " + Arrays.toString(plateau));
                //Mise à jour visuel du plateau
                updateCellAppearance(clickedPane, coup);

                if (coup == 1) { //Tour joueur 1
                    try {
                        coup=waitForPlayerMove(model, indicateur, coup, adversaire, soundDefeat, soundWin, plateau, sequentialTransition, c1,c2,c3,c4,c5,c6,c7,c8,c9,l1,l2,l3,l4,l5,l6,l7,l8, chronometre);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (coup == 2) { //Tour joueur 2
                    try {
                        coup=waitForPlayerMove2(indicateur, coup);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else { // Message d'erreur en cas où la case est déjà jouée 
                indicateur.setText("CASE DÉJÀ JOUÉE!");
            }  
        }
    	// Vérification si la partie est finie
        if (isGameFinishedAnimation(adversaire, indicateur, coup, soundDefeat, soundWin, plateau, sequentialTransition, c1,c2,c3,c4,c5,c6,c7,c8,c9,l1,l2,l3,l4,l5,l6,l7,l8, chronometre) == 3) {
	            coup=3;
        		System.out.println("-------Fin de Partie-------");
	            rejouer.setOpacity(1.0);
        }
    	return coup;
    }

    // Permet le changement de tour après joueur 1 
    public static int waitForPlayerMove(MultiLayerPerceptron model, Text indicateur, int coup, boolean adversaire, MediaPlayer soundDefeat, MediaPlayer soundWin, int[] plateau, SequentialTransition sequentialTransition, Pane c1, Pane c2, Pane c3, Pane c4, Pane c5, Pane c6, Pane c7, Pane c8, Pane c9, Line l1, Line l2, Line l3, Line l4, Line l5, Line l6, Line l7, Line l8, Timeline chronometre) throws InterruptedException {
    	indicateur.setText("En attente du coup du joueur 1");
        if (adversaire==true) // Vérification s'il on joue contre un joueur ou une IA
        	coup = 2; // modification du tour pour joueur 2 
        else {
        	coup = 0; // modification du tour pour joueur 2 
        	if (isGameFinishedAnimation(adversaire, indicateur, coup, soundDefeat, soundWin, plateau, sequentialTransition, c1,c2,c3,c4,c5,c6,c7,c8,c9,l1,l2,l3,l4,l5,l6,l7,l8, chronometre) != 3) // Vérification si la partie est finie avant que l'IA joue
        		moveAI(coup, plateau, model, c1,c2,c3,c4,c5,c6,c7,c8,c9); // Coup de l'IA
        		coup=1; // Retour au tour de joueur 1
        }
        return coup;
    }     
    
    // Permet le changement de tour après joueur 2
    public static int waitForPlayerMove2(Text indicateur, int coup) throws InterruptedException {
    	indicateur.setText("En attente du coup du joueur 2");
    	coup=1;
    	return coup;
    }   
	
    // Tour de l'IA 
    public static int[] moveAI(int coup, int[] plateau, MultiLayerPerceptron model, Pane c1, Pane c2, Pane c3, Pane c4, Pane c5, Pane c6, Pane c7, Pane c8, Pane c9) {
        // Convertir le plateau de jeu en un tableau de double pour l'entrée du modèle
        double[] input = new double[9];
        for (int i = 0; i < plateau.length; i++) {
            input[i] = plateau[i];
        }
        
        Coup coupAI = new Coup(9, "Tic Tac Toe");
        
        // Ajouter le plateau de jeu à l'entrée du coup
        coupAI.addInBoard(input);
        int nextMoveIndex = predictNextMove(coupAI, model);
        
        if (nextMoveIndex >= 0 && nextMoveIndex < plateau.length) {
	        plateau[nextMoveIndex] = 2; // Supposons que l'IA soit le joueur 2 (O)

	        switch (nextMoveIndex) { //Met à jour la case jouée par l'IA avec la méthode updateCellAppearance()
	            case 0:
	                updateCellAppearance(c1, 2);
	                break;
	            case 1:
	                updateCellAppearance(c2, 2);
	                break;
	            case 2:
	                updateCellAppearance(c3, 2);
	                break;
	            case 3:
	                updateCellAppearance(c4, 2);
	                break;
	            case 4:
	                updateCellAppearance(c5, 2);
	                break;
	            case 5:
	                updateCellAppearance(c6, 2);
	                break;
	            case 6:
	                updateCellAppearance(c7, 2);
	                break;
	            case 7:
	                updateCellAppearance(c8, 2);
	                break;
	            case 8:
	                updateCellAppearance(c9, 2);
	                break;
	            default:
	                break;
	        }
        }
        else {
            // Gérer le cas où nextMoveIndex est invalide
            System.err.println("Index de coup invalide retourné par l'IA: " + nextMoveIndex);
        }
        return plateau;
	}
    
    public static int predictNextMove(Coup coup, MultiLayerPerceptron model) {
        // Convertir le tableau de l'entrée du coup en un tableau de double
        double[] input = coup.in;

        // Utiliser la propagation avant pour obtenir la sortie du réseau
        double[] output = model.forwardPropagation(input);

        // Trouver l'index de la case avec la valeur de sortie maximale
        int nextMoveIndex = -1;
        double maxOutput = Double.MIN_VALUE;
        for (int i = 0; i < output.length; i++) {
            if (coup.cellAvailable(i) && output[i] > maxOutput) {
                maxOutput = output[i];
                nextMoveIndex = i;
            }
        }

        return nextMoveIndex;
    }
    
    public static void updateCellAppearance(Pane clickedPane, int coup) {
    	ObservableList<Node> children = clickedPane.getChildren();
    	if (coup == 1) { // Si c'est le tour de joueur 1, on rend visible le croix de Pane cliqué
	        Pane innerPane = (Pane) children.get(1);
	    	innerPane.setVisible(true);
        }
    	else { // Si c'est le tour de joueur 2 ou de l'IA, on rend visible le rond du Pane cliqué
	        Pane innerPane = (Pane) children.get(2);
	    	innerPane.setVisible(true);
    	}
    }
	
    public static int isGameFinishedAnimation(Boolean adversaire, Text indicateur, int coup, MediaPlayer soundDefeat, MediaPlayer soundWin, int[] plateau, SequentialTransition sequentialTransition, Pane c1, Pane c2, Pane c3, Pane c4, Pane c5, Pane c6, Pane c7, Pane c8, Pane c9, Line l1, Line l2, Line l3, Line l4, Line l5, Line l6, Line l7, Line l8,Timeline chronometre) {
        // Les combinaisons gagnantes
        int[][] winningCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontale
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Verticale
                 {0, 4, 8}, {2, 4, 6}             // Diagonale
        };

        // Vérifie chaque combinaisons
        for (int[] combination : winningCombinations) {
            int a = combination[0];
            int b = combination[1];
            int c = combination[2];

            // Vérifie si elles ont les même valeur autre que 0 (1,1,1 ou 2,2,2)
            if (plateau[a] != 0 && plateau[a] == plateau[b] && plateau[a] == plateau[c]) {
            	 if (a == 0 && b == 1 && c == 2) {
            		 victoryAnimation(l3,c1,c2,c3,sequentialTransition,chronometre);
            	    } else if (a == 3 && b == 4 && c == 5) {
            	    	victoryAnimation(l4,c4,c5,c6,sequentialTransition,chronometre);
            	    } else if (a == 6 && b == 7 && c == 8) {
            	    	victoryAnimation(l5,c7,c8,c9,sequentialTransition,chronometre);
            	    } else if (a == 0 && b == 3 && c == 6) {
            	    	victoryAnimation(l6,c1,c4,c7,sequentialTransition,chronometre);
            	    } else if (a == 1 && b == 4 && c == 7) {
            	    	victoryAnimation(l7,c2,c5,c8,sequentialTransition,chronometre);
            	    } else if (a == 2 && b == 5 && c == 8) {
            	    	victoryAnimation(l8,c3,c6,c9,sequentialTransition,chronometre);
            	    } else if (a == 0 && b == 4 && c == 8) {
            	    	victoryAnimation(l1,c1,c5,c9,sequentialTransition,chronometre);
            	    } else if (a == 2 && b == 4 && c == 6) {
            	    	victoryAnimation(l2,c3,c5,c7,sequentialTransition,chronometre);
            	    }
            	if (adversaire==true) { // Victoire du joueur x dans le mode Joueur VS Joueur
            		indicateur.setText("-------Victoire de Joueur "+plateau[a]+"-------");    
	                soundWin.stop();
	                soundWin.play();
            	}
            	else {
            		if (plateau[a]==1) { // Victoire du Joueur 1 dans le mode Joueur VS IA
                		indicateur.setText("-------Victoire de Joueur 1-------"); 
	            		soundWin.stop();
		                soundWin.play();
            		}
            		else { // Victoire de l'IA dans le mode Joueur VS IA
            			indicateur.setText("----------- Défaite -----------");
            			soundDefeat.stop();
            			soundDefeat.play();
            		}
            	}
                return 3; // Fin de la partie (permet de bloquer le plateau de jeu une fois la partie finie)
            }
        }
        // Vérifie si il reste de la place sur le plateau
        for (int i = 0; i < plateau.length; i++) {
            if (plateau[i] == 0) { // S'il reste des cases et qu'il n'y pas d'alignement la partie continue
                return coup; 
            }
        }
        indicateur.setText("-------Egalité-------");
        return 3;
    }
    
    public static void victoryAnimation(Line line, Pane c1, Pane c2, Pane c3, SequentialTransition sequentialTransition, Timeline chronometre) {
    	stopChronometre(chronometre);
    	
    	line.setVisible(true);
        // Animation qui fait progressivement apparaître la ligne
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), line);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(1), line);
        fadeTransition2.setFromValue(1.0);
        fadeTransition2.setToValue(0.0);

        // Animation des cases qui tournent
        RotateTransition rotateTransition1 = createRotationTransition(c1);
        RotateTransition rotateTransition2 = createRotationTransition(c2);
        RotateTransition rotateTransition3 = createRotationTransition(c3);
        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition1, rotateTransition2, rotateTransition3);

        // On lance les animations à la suite
        sequentialTransition = new SequentialTransition(fadeTransition, fadeTransition2, parallelTransition);
        sequentialTransition.play();
    }
    
    private static RotateTransition createRotationTransition(Pane pane) {
        // Créez la transition de rotation comme avant
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), pane);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setAutoReverse(false);
        
        return rotateTransition;
    }
    
    // Méthode de mise à jour du chronomètre
    public static String updateChronometreLabel(int secondes) { 
        int heures = secondes / 3600;
        int minutes = (secondes % 3600) / 60;
        int secs = secondes % 60;
        // Format heure:minutes:secs
        String temps = String.format("%02d:%02d:%02d", heures, minutes, secs);
        return temps;
    }

    // Méthode d'arrêt du chronomètre
    private static void stopChronometre(Timeline chronometre) {
        if (chronometre != null) {
            chronometre.stop();
        }
    }
}

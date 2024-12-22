package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

import ai.MultiLayerPerceptron;
import ai.Coup;
import controller.WelcomeController;
import ai.SigmoidalTransferFunction;
import application.Main;

public class LearnController {

	private Main main;
	private String config;
	
    @FXML
    private TextField messageTextField;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button buttonProgress;

    public void initialize  () {
        messageTextField.setEditable(false);
        setMain(main);
    }
    
	public void setMain(Main main) {
		this.main = main;
	}
	
	public void setConfig(String config) {
		this.config = config;
	}
    
    @FXML
    private void onClickButtonAccueil() {
    	main.showWelcome();
    }
    	
    @FXML
    private void onClickButtonProgress() {
        HashMap<Integer, Coup> mapTrain = loadCoupsFromFile("./resources/train_dev_test/train.txt");

        int size = 9;
        int h = 64;
        double lr = 0.01;
        int l = 2;
        boolean verbose = true;
        double epochs = 100000;

        int[] layers = new int[l + 2];
        layers[0] = size;
        for (int i = 0; i < l; i++) {
            layers[i + 1] = h;
        }
        layers[layers.length - 1] = size;

        MultiLayerPerceptron net = new MultiLayerPerceptron(layers, lr, new SigmoidalTransferFunction());
        Task<Void> task = new Task<Void>() {
            double error = 0.0;

            @Override
            protected Void call() throws Exception {
                updateMessage("Chargement des données ...");

                for (int i = 0; i < epochs; i++) {
                    Coup c = null;
                    while (c == null)
                        c = mapTrain.get((int) (Math.round(Math.random() * mapTrain.size())));
                    error += net.backPropagate(c.in, c.out);

                    if (i % 10000 == 0 && verbose) updateMessage("L'erreur " + i + " est à l'étape " + (error / (double) i));

                    double progress = (double) i / epochs;
                    updateProgress(i, epochs);
                }
                if (verbose)
                    updateMessage("Apprentissage complété!");
                	updateMessage("!");

                return null;
            }
        };

        messageTextField.textProperty().bind(task.messageProperty());
        progressBar.progressProperty().bind(task.progressProperty());

        String fileName = getNextModelFileName(true);
        saveModelToFile(net, "resources/models/" + fileName);

        task.messageProperty().addListener((observable, oldValue, newValue) -> {
            if ("!".equals(newValue)) {
            	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                main.showGame(false,getNextModelFileName(false));
            }
        });
        
        new Thread(task).start();
    }


    public static HashMap<Integer, Coup> loadCoupsFromFile(String file) {
        System.out.println("loadCoupsFromFile from " + file + " ...");
        HashMap<Integer, Coup> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))))) {
            String s;
            while ((s = br.readLine()) != null) {
                String[] sIn = s.split("\t")[0].split(" ");
                String[] sOut = s.split("\t")[1].split(" ");

                double[] in = new double[sIn.length];
                double[] out = new double[sOut.length];

                for (int i = 0; i < sIn.length; i++) {
                    in[i] = Double.parseDouble(sIn[i]);
                }

                for (int i = 0; i < sOut.length; i++) {
                    out[i] = Double.parseDouble(sOut[i]);
                }

                Coup c = new Coup(9, "");
                c.in = in;
                c.out = out;
                map.put(map.size(), c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return map;
    }

    private void saveModelToFile(MultiLayerPerceptron model, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(model);
            System.out.println("Model saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNextModelFileName(boolean next) {
        File folder = new File("resources/models");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("net_") && name.endsWith(".srl"));
        double[] values = loadValuesFromConfigFile("resources/"+config+".txt");
        char highestDifficulty = findHighestDifficulty(values);

        int maxModelNumber = -1;
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                char fileDifficulty = fileName.charAt(4); 
                int modelNumber = Integer.parseInt(fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".")));
                if (fileDifficulty == highestDifficulty && modelNumber > maxModelNumber) {
                    maxModelNumber = modelNumber;
                }
            }
        }
        int nextModelNumber=0;
        if (next) 
        	nextModelNumber = maxModelNumber + 1;
        else nextModelNumber = maxModelNumber;
        
        return "net_" + highestDifficulty + "_" + nextModelNumber + ".srl";
    }


    private double[] loadValuesFromConfigFile(String filePath) {
        double[] values = new double[3];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                char difficulty = parts[0].charAt(0);
                double value = Double.parseDouble(parts[1]);
                if (difficulty == 'F') {
                    values[0] = value;
                } else if (difficulty == 'M') {
                    values[1] = value;
                } else if (difficulty == 'D') {
                    values[2] = value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

    private char findHighestDifficulty(double[] values) {
        char highestDifficulty = 'F';
        double maxValue = values[0];

        for (int i = 1; i < values.length; i++) {
            if (values[i] > maxValue) {
                maxValue = values[i];
                if (i == 1) {
                    highestDifficulty = 'M';
                } else if (i == 2) {
                    highestDifficulty = 'D';
                }
            }
        }

        return highestDifficulty;
    }

}

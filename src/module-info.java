module Morpion2 {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.media;
	exports controller; 
	exports application;
	opens controller to javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}

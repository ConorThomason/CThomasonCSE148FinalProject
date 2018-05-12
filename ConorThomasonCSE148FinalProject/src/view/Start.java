package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.*;

public class Start extends Application {
	private final Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	
	public void start(Stage primaryStage) throws Exception{
		//Initial setup
		primaryStage.setTitle("Conor Thomason CSE148 Final Project");
		ScreenSizes screenSizes = new ScreenSizes(primaryStage, screenSize, 0);
		primaryStage.setHeight(screenSize.getHeight() / 1.75);
		primaryStage.setWidth(screenSize.getWidth() / 2.8);
		BorderPane root = new BorderPane();
		AllBags allBags = new AllBags();
		allBags.load();
		//Initial menu
		Scene scene = new Scene(root, primaryStage.getHeight(), primaryStage.getWidth());
		TopPane topPane = new TopPane(allBags, primaryStage, root, screenSizes);
		MainPane mainPane = new MainPane(screenSizes, root, allBags);
		root.setTop(topPane.getMenuBar());
		root.setCenter(mainPane.getPane());
		
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				allBags.save();	
				Platform.exit();
			}
		});
	}

}
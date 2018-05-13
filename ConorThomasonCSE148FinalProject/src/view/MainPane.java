package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.AllBags;


public class MainPane {

	private BorderPane pane;
	
	public MainPane(ScreenSizes screenSizes, BorderPane root, AllBags allBags) {
		buildPane(screenSizes, root, allBags);
		Label placeHolder = new Label("SUNYSuffolk Course Management");
		placeHolder.setStyle("-fx-font-size: 50");
		pane.setTop(placeHolder);
		BorderPane.setAlignment(placeHolder, Pos.CENTER);
		BorderPane.setMargin(placeHolder, new Insets(50));
	}
	private void buildPane(ScreenSizes screenSizes, BorderPane root, AllBags allBags) {
		pane = new BorderPane();
		VBox navigationButtons = new VBox(30);
		
		Button peopleButton = new Button("People");
		peopleButton.setMaxSize(screenSizes.getStageWidth() / 5, screenSizes.getStageHeight() / 3);
		peopleButton.setOnAction(e ->{
			PeoplePane peoplePane = new PeoplePane(allBags, screenSizes, root);
			root.setCenter(peoplePane.getPane());
			screenSizes.setCurrentScene(1);
		});
		
		
		Button coursesButton = new Button("Courses");
		coursesButton.setMaxSize(screenSizes.getStageWidth() / 5, screenSizes.getStageHeight() / 3);
		coursesButton.setOnAction(e ->{
			CoursePane coursePane = new CoursePane(allBags, screenSizes, root);
			root.setCenter(coursePane.getPane());
			screenSizes.setCurrentScene(2);
		});
		
		Button textbookButton = new Button("Textbooks");
		textbookButton.setMaxSize(screenSizes.getStageWidth() / 5, screenSizes.getStageHeight() / 3);
		textbookButton.setOnAction(e ->{
			TextbookPane textbookPane = new TextbookPane(allBags, screenSizes, root);
			root.setCenter(textbookPane.getPane());
			screenSizes.setCurrentScene(3);
		});
		
		Button majorsButton = new Button("Majors");
		majorsButton.setMaxSize(screenSizes.getStageWidth() / 5,  screenSizes.getStageHeight() / 3);
		majorsButton.setOnAction(e ->{
			MajorPane majorPane = new MajorPane(allBags, screenSizes, root);
			root.setCenter(majorPane.getPane());
			screenSizes.setCurrentScene(4);
		});
		navigationButtons.getChildren().addAll(peopleButton, textbookButton, coursesButton, majorsButton);
		navigationButtons.setAlignment(Pos.CENTER);
		pane.setCenter(navigationButtons);
	}
	public BorderPane getPane() {
		return pane;
	}
}

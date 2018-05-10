package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.AllBags;


public class MainPane {

	private BorderPane pane;
	
	public MainPane(ScreenSizes screenSizes, BorderPane root, AllBags allBags) {
		buildPane(screenSizes, root, allBags);
		Label placeHolder = new Label("PLACEHOLDER");
		placeHolder.setStyle("-fx-font-size: 75");
		pane.setTop(placeHolder);
		BorderPane.setAlignment(placeHolder, Pos.CENTER);
	}
	private void buildPane(ScreenSizes screenSizes, BorderPane root, AllBags allBags) {
		pane = new BorderPane();
		HBox navigationButtons = new HBox(5);
		
		Button peopleButton = new Button("People");
		peopleButton.setMaxSize(Double.MAX_VALUE, screenSizes.getStageHeight() / 5);
		peopleButton.setOnAction(e ->{
			PeoplePane peoplePane = new PeoplePane(allBags, screenSizes, root);
			root.setCenter(peoplePane.getPane());
			screenSizes.setCurrentScene(1);
		});
		Button textbookButton = new Button("Textbooks");
		textbookButton.setMaxSize(Double.MAX_VALUE, screenSizes.getStageHeight() / 5);
		
		Button coursesButton = new Button("Courses");
		coursesButton.setMaxSize(Double.MAX_VALUE, screenSizes.getStageHeight() / 5);
		coursesButton.setOnAction(e ->{
			CoursePane coursePane = new CoursePane(allBags, screenSizes, root);
			root.setCenter(coursePane.getPane());
			screenSizes.setCurrentScene(2);
		});
		HBox.setHgrow(peopleButton, Priority.ALWAYS);
		HBox.setHgrow(textbookButton, Priority.ALWAYS);
		HBox.setHgrow(coursesButton, Priority.ALWAYS);
		navigationButtons.getChildren().addAll(peopleButton, textbookButton, coursesButton);
		navigationButtons.setAlignment(Pos.CENTER);
		pane.setCenter(navigationButtons);
	}
	public BorderPane getPane() {
		return pane;
	}
}

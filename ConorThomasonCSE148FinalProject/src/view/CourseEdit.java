package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.AllBags;
import model.Course;
import model.CourseBag;
import model.Faculty;
import model.MasterCourseBag;
import model.PeopleBag;
import model.Person;
import model.Student;

public class CourseEdit {
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private Stage editCourseStage;
	private Scene courseAddScene;
	private Course selectedCourse;
	private TextField courseTitleField;
	private TextField courseNumberField;
	private TextField textbookIsbnField;
	private TextField creditsField;
	private TextField addButtonField;
	private Button saveButton, cancelButton, addButton;
	public CourseEdit(AllBags allBags, ScreenSizes screenSizes, Course selectedCourse) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		this.selectedCourse = selectedCourse;
		buildStage();
		buildScene();
		editCourseStage.setScene(courseAddScene);
	}
	private void buildStage() {
		editCourseStage = new Stage();
		editCourseStage.setTitle("Edit a Course");
		editCourseStage.setHeight(screenSizes.getScreenHeight() / 5.5);
		editCourseStage.setWidth(screenSizes.getScreenWidth() / 7);
	}
	private void buildScene() {
		BorderPane inputRoot = new BorderPane();
		//Save button setup and scene setup
		HBox courseDetails = buildDetails();
		courseDetails.setAlignment(Pos.CENTER);
		HBox buttonBox = buildButtonBox();
		buttonBox.setAlignment(Pos.CENTER);
		inputRoot.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new Insets(10));

		//Acquires a VBox containing all of the text input boxes.
		inputRoot.setTop(courseDetails);
		inputRoot.setBottom(buttonBox);
		courseAddScene = new Scene(inputRoot, editCourseStage.getHeight(), editCourseStage.getWidth());
		editCourseStage.setHeight(screenSizes.getScreenHeight() / 6.5);
		editCourseStage.setWidth(screenSizes.getScreenWidth() / 5);
	}
	private HBox buildDetails() {

		VBox leftBox = new VBox(15);
		VBox rightBox = new VBox(5);
		HBox detailsBox = new HBox(5);

		Label courseTitleLabel = new Label("Course Title: ");
		courseTitleField = new TextField(selectedCourse.getCourseTitle());
		courseTitleField.setMinWidth(editCourseStage.getWidth() / 2);
		
		Label courseNumberLabel = new Label("Course Number: ");
		courseNumberField = new TextField(selectedCourse.getCourseNumber());

		Label textbookIsbn = new Label("Textbook ISBN: ");
		textbookIsbnField = new TextField(selectedCourse.getTextbookIsbn());
		
		Label creditsLabel = new Label("Credits: ");
		creditsField = new TextField(Integer.toString(selectedCourse.getNumberOfCredits()));
		
		leftBox.getChildren().addAll(courseTitleLabel, courseNumberLabel, textbookIsbn, creditsLabel);
		rightBox.getChildren().addAll(courseTitleField, courseNumberField, textbookIsbnField, creditsField);
		detailsBox.getChildren().addAll(leftBox, rightBox);
		detailsBox.setAlignment(Pos.CENTER);
		return detailsBox;
	}
	
	private HBox buildButtonBox() {
		HBox bottomButtonBox = new HBox();
		saveButton = new Button("Save");
		saveButton.setMinWidth(editCourseStage.getWidth() / 8);
		saveButton.setMaxHeight(editCourseStage.getHeight() / 10);
		saveButton.setOnAction(e ->{
			selectedCourse.setCourseTitle(courseTitleField.getText());
			selectedCourse.setCourseNumber(courseNumberField.getText());
			selectedCourse.setTextbookIsbn(textbookIsbnField.getText());
			try {
				selectedCourse.setNumberOfCredits(Integer.parseInt(creditsField.getText()));
			} catch(NumberFormatException f) {
				Util.displayError("Invalid input for credits, value will remain unchanged");
			}
			editCourseStage.close();

		});
		cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(editCourseStage.getWidth() / 8);
		cancelButton.setMaxHeight(editCourseStage.getHeight() / 10);
		cancelButton.setOnAction(e ->{
			editCourseStage.close();
		});

		bottomButtonBox.getChildren().addAll(saveButton, cancelButton);
		HBox.setMargin(saveButton, new Insets(10));
		HBox.setMargin(cancelButton, new Insets(10));
		bottomButtonBox.setAlignment(Pos.CENTER_RIGHT);
		return bottomButtonBox;
	}
	public AllBags getAllBags() {
		return allBags;
	}
	public Stage getStage() {
		return editCourseStage;
	}
}

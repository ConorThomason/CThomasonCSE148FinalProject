package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
import model.Textbook;

public class CourseAdd {
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private Stage addCourseStage;
	private Scene courseAddScene;
	private TextField courseTitleField;
	private TextField courseNumberField;
	private TextField textbookIsbnField;
	private TextField creditsField;
	private TextField titleField;
	private TextField salaryField;
	private Button saveButton, cancelButton;
	public CourseAdd(AllBags allBags, ScreenSizes screenSizes) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		buildStage();
		buildScene();
		addCourseStage.setScene(courseAddScene);
	}
	private void buildStage() {
		addCourseStage = new Stage();
		addCourseStage.setTitle("Add a Course");
		addCourseStage.setHeight(screenSizes.getScreenHeight() / 6);
		addCourseStage.setWidth(screenSizes.getScreenWidth() / 7);
	}
	private void buildScene() {
		BorderPane inputRoot = new BorderPane();
		
		//Save button setup and scene setup
		HBox buttonBox = buildButtonBox();
		inputRoot.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new Insets(10));
		saveButton.setOnAction(e ->{
			MasterCourseBag bagToConvert = allBags.getMasterCourseBag();
			MasterCourseBag convertBag = new MasterCourseBag(bagToConvert.getNumberOfCourses() + 1);
			Textbook constructorTextbook;
			if (allBags.getTextbookBag().find(textbookIsbnField.getText()) != -1) {
				constructorTextbook = allBags.getTextbookBag().getTextbook(textbookIsbnField.getText());
			}
			else if (textbookIsbnField.getText().equals("")) {
				constructorTextbook = null;
			}
			else {
				Util.displayError("Textbook not found in Textbook Bag; no textbook will be assigned.");
				constructorTextbook = null;
			}
			int constructorCredits = 0;
			try {
				constructorCredits = Integer.parseInt(creditsField.getText());
			} catch(NumberFormatException f) {
				Util.displayError("Invalid input for credits, value defaulted to 0");
			}
			Course course = new Course(courseTitleField.getText(), courseNumberField.getText(), constructorTextbook, constructorCredits);
			for (int i = 0; i < bagToConvert.getNumberOfCourses(); i++) {
				convertBag.add(bagToConvert.getCourse(i));
			}
			convertBag.add(course);
			allBags.setMasterCourseBag(convertBag);
			allBags.getMasterCourseBag().save();
			addCourseStage.close();
		});
		cancelButton.setOnAction(e ->{
			addCourseStage.close();
		});
		
		//Acquires a VBox containing all of the text input boxes.
		inputRoot.setCenter(buildFormBoxes());
		courseAddScene = new Scene(inputRoot, addCourseStage.getHeight(), addCourseStage.getWidth());
	}
	public BorderPane buildFormBoxes() {
		BorderPane splitter = new BorderPane();
		HBox universalBox = buildUniversal();
		universalBox.setAlignment(Pos.CENTER);
		BorderPane.setMargin(universalBox, new Insets(10));
		splitter.setCenter(universalBox);
		return splitter;
	}
	private HBox buildUniversal() {
		
		HBox universalBox = new HBox(5);
		VBox leftBox = new VBox(14);
		VBox rightBox = new VBox(5);
		
		Label courseTitleLabel = new Label("Course Title: ");
		courseTitleField = new TextField();
		
		Label courseNumberLabel = new Label("Course Number: ");
		courseNumberField = new TextField();
		
		Label textbookIsbnLabel = new Label("Textbook ISBN:  ");
		textbookIsbnField = new TextField();
		
		Label creditsLabel = new Label("Credits: ");
		creditsField = new TextField();
		
		leftBox.getChildren().addAll(courseTitleLabel, courseNumberLabel, textbookIsbnLabel, creditsLabel);
		rightBox.getChildren().addAll(courseTitleField, courseNumberField, textbookIsbnField, creditsField);
		
		universalBox.getChildren().addAll(leftBox, rightBox);
		return universalBox;
	}
	private HBox buildButtonBox() {
		HBox bottomButtonBox = new HBox();
		saveButton = new Button("Save");
		saveButton.setMinWidth(addCourseStage.getWidth() / 8);
		saveButton.setMinHeight(addCourseStage.getHeight() / 9);
		
		cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(addCourseStage.getWidth() / 8);
		cancelButton.setMinHeight(addCourseStage.getHeight() / 9);
		
		bottomButtonBox.getChildren().addAll(saveButton, cancelButton);
		bottomButtonBox.setAlignment(Pos.CENTER);
		HBox.setMargin(saveButton, new Insets(10));
		return bottomButtonBox;
	}
	public AllBags getAllBags() {
		return allBags;
	}
	public Stage getStage() {
		return addCourseStage;
	}
}

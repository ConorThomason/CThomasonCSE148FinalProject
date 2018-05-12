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
		addCourseStage.setHeight(screenSizes.getScreenHeight() / 5.5);
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
			Textbook constructorTextbook = allBags.getTextbookBag().getTextbook(textbookIsbnField.getText());
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
		VBox universalBox = buildUniversal();
		splitter.setLeft(universalBox);
		return splitter;
	}
	private VBox buildUniversal() {
		
		VBox universalBox = new VBox(5);
		
		HBox courseTitleBox = new HBox();
		Label courseTitleLabel = new Label("Course Title: ");
		courseTitleField = new TextField();
		courseTitleBox.getChildren().addAll(courseTitleLabel, courseTitleField);
		courseTitleBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(courseTitleBox, new Insets(5));
		
		HBox courseNumberBox = new HBox();
		Label courseNumberLabel = new Label("Course Number: ");
		courseNumberField = new TextField();
		courseNumberBox.getChildren().addAll(courseNumberLabel, courseNumberField);
		courseNumberBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(courseNumberBox, new Insets(5));
		
		HBox textbookIsbnBox = new HBox();
		Label textbookIsbnLabel = new Label("Textbook ISBN:  ");
		textbookIsbnField = new TextField();
		textbookIsbnBox.getChildren().addAll(textbookIsbnLabel, textbookIsbnField);
		textbookIsbnBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(textbookIsbnBox, new Insets(5));
		
		HBox creditsBox = new HBox();
		Label creditsLabel = new Label("Credits: ");
		creditsField = new TextField();
		creditsBox.getChildren().addAll(creditsLabel, creditsField);
		creditsBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(creditsBox, new Insets(5));
		
		universalBox.getChildren().addAll(courseTitleBox, courseNumberBox, textbookIsbnBox, creditsBox);
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

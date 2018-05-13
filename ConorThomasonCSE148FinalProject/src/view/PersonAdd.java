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
import model.CourseBag;
import model.Faculty;
import model.PeopleBag;
import model.Person;
import model.Student;

public class PersonAdd {
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private Stage addPersonStage;
	private Scene personAddScene;
	private RadioButton selectedRadioButton, studentButton, facultyButton;
	private ToggleGroup toggleGroup;
	private TextField firstNameField;
	private TextField lastNameField;
	private TextField phoneNumberField;
	private TextField majorField;
	private TextField titleField;
	private TextField salaryField;
	private Button saveButton, cancelButton;
	public PersonAdd(AllBags allBags, ScreenSizes screenSizes) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		buildStage();
		buildScene();
		addPersonStage.setScene(personAddScene);
	}
	private void buildStage() {
		addPersonStage = new Stage();
		addPersonStage.setTitle("Add a Person");
		addPersonStage.setHeight(screenSizes.getScreenHeight() / 7);
		addPersonStage.setWidth(screenSizes.getScreenWidth() / 4.75);
	}
	private void buildScene() {
		BorderPane inputRoot = new BorderPane();

		//Save button setup and scene setup
		HBox buttonBox = buildButtonBox();
		inputRoot.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new Insets(10));
		saveButton.setOnAction(e ->{
			if (selectedRadioButton == studentButton) {
				//Check to make sure that the new input has a unique id (When taking into account the data imported via save/load or import/export)
				PeopleBag bagToConvert = allBags.getPeopleBag();
				PeopleBag convertBag = new PeopleBag(bagToConvert.getPeopleCount() + 1);
				int largestId = 0;
				for (int i = 0; i < bagToConvert.getPeopleCount(); i++) {
					if (largestId < Integer.parseInt(bagToConvert.getPerson(i).getId()))
						largestId = Integer.parseInt(bagToConvert.getPerson(i).getId());
				}
				Person.setCurrentId(largestId + 1);
				if (majorField.getText() != null) {
					Student student = new Student(firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText(), majorField.getText());
					for (int i = 0; i < bagToConvert.getPeopleCount(); i++) {
						convertBag.add(bagToConvert.getPerson(i));
					}
					convertBag.add(student);
					allBags.setPeopleBag(convertBag);
					addPersonStage.close();
				}
				else
				{
					Student student = new Student(firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText(), null);
					for (int i = 0; i < bagToConvert.getPeopleCount(); i++) {
						convertBag.add(bagToConvert.getPerson(i));
					}
					convertBag.add(student);
					allBags.setPeopleBag(convertBag);
					addPersonStage.close();
				}
			}
			if (selectedRadioButton == facultyButton) {
				//Check to make sure that the new input has a unique id (When taking into account the data imported via save/load or import/export)
				PeopleBag bagToConvert = allBags.getPeopleBag();
				PeopleBag convertBag = new PeopleBag(bagToConvert.getPeopleCount() + 1);
				int largestId = 0;
				for (int i = 0; i < bagToConvert.getPeopleCount(); i++) {
					if (largestId < Integer.parseInt(bagToConvert.getPerson(i).getId()))
						largestId = Integer.parseInt(bagToConvert.getPerson(i).getId());
				}
				Person.setCurrentId(largestId + 1);
				Faculty faculty = new Faculty(firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText(), titleField.getText(), salaryField.getText());
				for (int i = 0; i < bagToConvert.getPeopleCount(); i++) {
					convertBag.add(bagToConvert.getPerson(i));
				}
				convertBag.add(faculty);
				allBags.setPeopleBag(convertBag);
				addPersonStage.close();
			}
		});
		cancelButton.setOnAction(e ->{
			addPersonStage.close();
		});
		//Radio buttons placement
		VBox radioButtons = new VBox(5);
		toggleGroup = new ToggleGroup();
		studentButton = new RadioButton("Student");
		studentButton.setSelected(true);
		studentButton.setToggleGroup(toggleGroup);
		studentButton.setOnAction(e -> {
			inputRoot.setCenter(buildFormBoxes());
		});
		facultyButton = new RadioButton("Faculty");
		facultyButton.setToggleGroup(toggleGroup);
		facultyButton.setOnAction(e -> {
			inputRoot.setCenter(buildFormBoxes());
		});
		radioButtons.getChildren().addAll(studentButton, facultyButton);

		inputRoot.setLeft(radioButtons);
		radioButtons.setAlignment(Pos.CENTER_LEFT);
		BorderPane.setMargin(radioButtons, new Insets(10));

		//Acquires a VBox containing all of the text input boxes.
		inputRoot.setCenter(buildFormBoxes());
		personAddScene = new Scene(inputRoot, addPersonStage.getHeight(), addPersonStage.getWidth());
	}
	public BorderPane buildFormBoxes() {
		BorderPane splitter = new BorderPane();
		VBox universalBox = buildUniversal();
		splitter.setLeft(universalBox);

		VBox typeBox = new VBox();
		updateSelectedButton();
		if (selectedRadioButton == facultyButton)
			typeBox = buildFacultySpecific();
		else 
			typeBox = buildStudentSpecific();
		splitter.setRight(typeBox);
		return splitter;
	}
	private void updateSelectedButton() {
		selectedRadioButton = (RadioButton)toggleGroup.getSelectedToggle();
	}
	private VBox buildStudentSpecific() {
		VBox studentBox = new VBox(5);

		HBox majorBox = new HBox(5);
		Label majorLabel = new Label("Major:");
		majorField = new TextField();
		majorBox.getChildren().addAll(majorLabel, majorField);

		studentBox.getChildren().addAll(majorBox);
		majorBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(majorBox, new Insets(5));
		return studentBox;

	}
	private VBox buildFacultySpecific() { 
		VBox facultyBox = new VBox(5);

		HBox titleBox = new HBox();
		Label titleLabel = new Label("Title: ");
		titleField = new TextField();
		titleBox.getChildren().addAll(titleLabel, titleField);
		titleBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(titleBox, new Insets(5));

		HBox salaryBox = new HBox();
		Label salaryLabel = new Label("Salary: ");
		salaryField = new TextField();
		salaryBox.getChildren().addAll(salaryLabel, salaryField);
		salaryBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(salaryBox, new Insets(5));

		facultyBox.getChildren().addAll(titleBox, salaryBox);
		return facultyBox;
	}
	private VBox buildUniversal() {

		VBox universalBox = new VBox(5);

		HBox firstNameBox = new HBox();
		Label firstNameLabel = new Label("First Name: ");
		firstNameField = new TextField();
		firstNameBox.getChildren().addAll(firstNameLabel, firstNameField);
		firstNameBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(firstNameBox, new Insets(5));

		HBox lastNameBox = new HBox();
		Label lastNameLabel = new Label("Last Name: ");
		lastNameField = new TextField();
		lastNameBox.getChildren().addAll(lastNameLabel, lastNameField);
		lastNameBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(lastNameBox, new Insets(5));

		HBox phoneNumberBox = new HBox();
		Label phoneNumberLabel = new Label("Phone: ");
		phoneNumberField = new TextField();
		phoneNumberBox.getChildren().addAll(phoneNumberLabel, phoneNumberField);
		phoneNumberBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(phoneNumberBox, new Insets(5));

		universalBox.getChildren().addAll(firstNameBox, lastNameBox, phoneNumberBox);
		return universalBox;
	}
	private HBox buildButtonBox() {
		HBox bottomButtonBox = new HBox();
		saveButton = new Button("Save");
		saveButton.setMinWidth(addPersonStage.getWidth() / 8);
		saveButton.setMinHeight(addPersonStage.getHeight() / 8);

		cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(addPersonStage.getWidth() / 8);
		cancelButton.setMinHeight(addPersonStage.getHeight() / 8);

		bottomButtonBox.getChildren().addAll(saveButton, cancelButton);
		bottomButtonBox.setAlignment(Pos.CENTER);
		HBox.setMargin(saveButton, new Insets(10));
		return bottomButtonBox;
	}
	public AllBags getAllBags() {
		return allBags;
	}
	public Stage getStage() {
		return addPersonStage;
	}
}

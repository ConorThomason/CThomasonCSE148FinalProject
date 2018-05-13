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

public class PersonEdit {
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private Stage editPersonStage;
	private Scene personAddScene;
	private Student selectedStudent;
	private Faculty selectedFaculty;
	private TextField firstNameField;
	private TextField lastNameField;
	private TextField phoneNumberField;
	private TextField majorField;
	private TextField titleField;
	private TextField salaryField;
	private TextField addButtonField;
	private StudentCourseEdit courseEdit;
	private CompactCourse selectedCourse;
	private Button saveButton, cancelButton, addButton, applyButton, deleteButton;
	private TableView<CompactCourse> table;
	public PersonEdit(AllBags allBags, ScreenSizes screenSizes, Faculty selectedFaculty) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		this.selectedFaculty = selectedFaculty;
		buildStage();
		buildScene(selectedFaculty);
		editPersonStage.setScene(personAddScene);
	}
	public PersonEdit(AllBags allBags, ScreenSizes screenSizes, Student selectedStudent) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		this.selectedStudent = selectedStudent;
		buildStage();
		buildScene(selectedStudent);
		editPersonStage.setScene(personAddScene);
	}
	private void buildStage() {
		editPersonStage = new Stage();
		editPersonStage.setTitle("Edit a Person");
		editPersonStage.setHeight(screenSizes.getScreenHeight() / 1.8);
		editPersonStage.setWidth(screenSizes.getScreenWidth() / 5);
	}
	private void buildScene(Faculty selectedFaculty) {
		BorderPane inputRoot = new BorderPane();
		//Save button setup and scene setup
		HBox facultyDetails = buildDetails(selectedFaculty);
		facultyDetails.setAlignment(Pos.CENTER);
		HBox buttonBox = buildButtonBox();
		buttonBox.setAlignment(Pos.CENTER);
		inputRoot.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new Insets(10));
		saveButton.setOnAction(e ->{
			selectedFaculty.setFirstName(firstNameField.getText());
			selectedFaculty.setLastName(lastNameField.getText());
			selectedFaculty.setPhoneNumber(phoneNumberField.getText());
			selectedFaculty.setTitle(titleField.getText());
			selectedFaculty.setSalary(salaryField.getText());
			editPersonStage.close();
		});
		cancelButton.setOnAction(e ->{
			editPersonStage.close();
		});

		//Acquires a VBox containing all of the text input boxes.
		inputRoot.setTop(facultyDetails);
		inputRoot.setBottom(buttonBox);
		personAddScene = new Scene(inputRoot, editPersonStage.getHeight(), editPersonStage.getWidth());
		editPersonStage.setHeight(screenSizes.getScreenHeight() / 6.5);
		editPersonStage.setWidth(screenSizes.getScreenWidth() / 5);
	}
	private HBox buildAddButton() {
		VBox leftBox = new VBox(5);
		VBox rightBox = new VBox(5);
		HBox completeBox = new HBox(5);
		addButton = new Button("Add Course");
		HBox applyButtonBox = new HBox(5);
		applyButton = new Button("Apply Course Changes");
		applyButtonBox.getChildren().add(applyButton);
		Label addButtonLabel = new Label("Course Number:");
		addButtonField = new TextField();
		leftBox.getChildren().add(addButtonLabel);
		rightBox.getChildren().add(addButtonField);
		completeBox.getChildren().addAll(leftBox, rightBox, addButton, applyButtonBox);
		completeBox.setAlignment(Pos.CENTER);
		return completeBox;
	}
	
	private void buildScene(Student selectedStudent) {
		BorderPane inputRoot = new BorderPane();
		VBox studentDetails = new VBox(5);
		studentDetails.getChildren().addAll(buildDetails(selectedStudent), buildCourseEditPane());
		studentDetails.setAlignment(Pos.CENTER);
		VBox bottomBox = new VBox(5);
		HBox buttonBox = buildButtonBox();
		bottomBox.getChildren().addAll(buildAddButton(), buttonBox);
		deleteButton.setAlignment(Pos.CENTER);
		buttonBox.setAlignment(Pos.CENTER);
		inputRoot.setTop(studentDetails);
		inputRoot.setBottom(bottomBox);
		BorderPane.setMargin(bottomBox, new Insets(10));
		saveButton.setOnAction(e ->{
			selectedStudent.setFirstName(firstNameField.getText());
			selectedStudent.setLastName(lastNameField.getText());
			selectedStudent.setPhoneNumber(phoneNumberField.getText());
			if (majorField.getText() == null) {
				selectedStudent.setMajor(null);
			}
			if (!selectedStudent.getMajor().equals(majorField.getText()))
				selectedStudent.setMajor(majorField.getText());
			editPersonStage.close();
		});
		applyButton.setOnAction(e ->{
			selectedCourse = courseEdit.getSelectedCourse();
			CompactCourse courseCopy = new CompactCourse(selectedCourse.getCourseNumber(), selectedCourse.getCourseGrade(), selectedCourse.getCourseType());
			String courseGrade = courseEdit.getCourseGradeField().getText();
			if (courseGrade.equals("A") || courseGrade.equals("B") || courseGrade.equals("C") || courseGrade.equals("D") || courseGrade.equals("F"))
				courseCopy.setCourseType("HAVE");
			else
				courseCopy.setCourseType(courseEdit.getCourseTypeField().getValue().toString());
			courseCopy.setCourseGrade(courseGrade);
			selectedStudent.getCourseBagArray().delete(courseCopy.getCourseNumber());
			selectedStudent.getCourseBagArray().add(courseCopy.getCourseBagStyle());
			selectedStudent.calculateGpa(selectedStudent.getCourseBagArray());
			PersonEdit newEditPersonStage = new PersonEdit(allBags, screenSizes, selectedStudent);
			editPersonStage.close();
			newEditPersonStage.getStage().show();
//			VBox newStudentDetails = new VBox(5);
//			newStudentDetails.getChildren().addAll(buildDetails(selectedStudent), buildCourseEditPane());
//			newStudentDetails.setAlignment(Pos.CENTER);
//			inputRoot.setTop(newStudentDetails);
		});
		cancelButton.setOnAction(e ->{
			editPersonStage.close();
		});
		addButton.setOnAction(e -> {
			if (allBags.getMasterCourseBag().find(addButtonField.getText()) != -1) {
				if (selectedStudent.getCourseBagArray().find(addButtonField.getText()) == -1)
				selectedStudent.getCourseBagArray().addByCourseNumber(addButtonField.getText());
				else
					Util.displayError("Course is already contained in Student");
			}
			else {
				Util.displayError("Course was not found in Master Course Bag; please add it to the Master before continuing");
			}
			PersonEdit newEditPersonStage = new PersonEdit(allBags, screenSizes, selectedStudent);
			editPersonStage.close();
			newEditPersonStage.getStage().show();
		});
		deleteButton.setOnAction(e ->{
			selectedCourse = courseEdit.getSelectedCourse();
			selectedStudent.getCourseBagArray().delete(selectedCourse.getCourseNumber());
			PersonEdit newEditPersonStage = new PersonEdit(allBags, screenSizes, selectedStudent);
			editPersonStage.close();
			newEditPersonStage.getStage().show();
		});
		personAddScene = new Scene(inputRoot, editPersonStage.getHeight(), editPersonStage.getWidth());
	}
	public BorderPane buildFacultyBoxes() {
		BorderPane splitter = new BorderPane();
		splitter.setTop(buildDetails(selectedFaculty));
		splitter.setBottom(buildFacultySpecific());
		return splitter;
	}
	
	private VBox buildCourseEditPane() {
		courseEdit = new StudentCourseEdit(allBags, screenSizes, selectedStudent, editPersonStage);
		courseEdit.getPane().setAlignment(Pos.CENTER);
		VBox returnedPane = new VBox(5);
		deleteButton = new Button("Delete Selected Course");
		returnedPane.getChildren().addAll(courseEdit.getPane(), deleteButton);
		deleteButton.setAlignment(Pos.CENTER);
		returnedPane.setAlignment(Pos.CENTER);
		return returnedPane;
			
	}
	private HBox buildFacultySpecific() { 
		
		VBox leftBox = new VBox(15);
		VBox rightBox = new VBox(5);
		HBox specificBox = new HBox(5);
		
		Label titleLabel = new Label("Title: ");
		titleField = new TextField(selectedFaculty.getTitle());

		Label salaryLabel = new Label("Salary: ");
		salaryField = new TextField(selectedFaculty.getSalary());

		leftBox.getChildren().addAll(titleLabel, salaryLabel);
		rightBox.getChildren().addAll(titleField, salaryField);
		specificBox.getChildren().addAll(leftBox, rightBox);
		return specificBox;
	}
	
	private HBox buildDetails(Student selectedStudent) {

		VBox leftBox = new VBox(15);
		VBox rightBox = new VBox(5);
		HBox detailsBox = new HBox(5);

		Label firstNameLabel = new Label("First Name: ");
		firstNameField = new TextField(selectedStudent.getFirstName());
		
		Label lastNameLabel = new Label("Last Name: ");
		lastNameField = new TextField(selectedStudent.getLastName());

		Label phoneNumberLabel = new Label("Phone: "); 
		phoneNumberField = new TextField(selectedStudent.getPhoneNumber());
		
		Label majorLabel = new Label("Major:");
		try {
		majorField = new TextField(selectedStudent.getMajor());
		} catch (NullPointerException e) {
			majorField.setText("No Major");
		}
		
		VBox studentBox = new VBox();
		leftBox.getChildren().addAll(firstNameLabel, lastNameLabel, phoneNumberLabel, majorLabel);
		leftBox.setAlignment(Pos.CENTER);
		rightBox.getChildren().addAll(firstNameField, lastNameField, phoneNumberField, majorField);
		rightBox.setAlignment(Pos.CENTER);
		detailsBox.getChildren().addAll(leftBox, rightBox);
		detailsBox.setAlignment(Pos.CENTER);
		return detailsBox;
	}
	
	private HBox buildDetails(Faculty selectedFaculty) {

		VBox leftBox = new VBox(15);
		VBox rightBox = new VBox(5);
		HBox detailsBox = new HBox(5);

		Label firstNameLabel = new Label("First Name: ");
		firstNameField = new TextField(selectedFaculty.getFirstName());
		
		Label lastNameLabel = new Label("Last Name: ");
		lastNameField = new TextField(selectedFaculty.getLastName());

		Label phoneNumberLabel = new Label("Phone: ");
		phoneNumberField = new TextField(selectedFaculty.getPhoneNumber());
		
		VBox facultyBox = new VBox();
		facultyBox.getChildren().addAll(buildFacultySpecific());
		leftBox.getChildren().addAll(firstNameLabel, lastNameLabel, phoneNumberLabel);
		rightBox.getChildren().addAll(firstNameField, lastNameField, phoneNumberField);
		detailsBox.getChildren().addAll(leftBox, rightBox, facultyBox);
		detailsBox.setAlignment(Pos.CENTER);
		return detailsBox;
	}
	
	private HBox buildButtonBox() {
		HBox bottomButtonBox = new HBox();
		saveButton = new Button("Save");
		saveButton.setMinWidth(editPersonStage.getWidth() / 8);
		saveButton.setMaxHeight(editPersonStage.getHeight() / 10);
		saveButton.setOnAction(e ->{
			selectedStudent.setFirstName(firstNameField.getText());
			selectedStudent.setLastName(lastNameField.getText());
			selectedStudent.setPhoneNumber(phoneNumberField.getText());
			selectedStudent.setMajor(majorField.getText());
			selectedStudent.setCourseBag(courseEdit.getChangedCourseBag());
			editPersonStage.close();

		});
		cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(editPersonStage.getWidth() / 8);
		cancelButton.setMaxHeight(editPersonStage.getHeight() / 10);
		cancelButton.setOnAction(e ->{
			editPersonStage.close();
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
		return editPersonStage;
	}
}

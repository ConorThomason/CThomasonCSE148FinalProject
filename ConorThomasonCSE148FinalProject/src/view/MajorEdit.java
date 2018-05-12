package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.AllBags;
import model.Course;
import model.CourseBag;
import model.Faculty;
import model.MajorCourseBag;
import model.MasterCourseBag;
import model.PeopleBag;
import model.Person;
import model.Student;

public class MajorEdit {
	private TableView<Course> table;
	private TableView<CompactCourse> currentTable;
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private Stage editMajorStage;
	private Scene courseAddScene;
	private MajorCourseBag selectedMajor;
	private Course selectedCourse;
	private String selectedMajorCourse;
	private Button saveButton, cancelButton, addCourseButton;
	public MajorEdit(AllBags allBags, ScreenSizes screenSizes, MajorCourseBag selectedMajor) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		this.selectedMajor = selectedMajor;
		buildStage();
		buildScene();
		editMajorStage.setScene(courseAddScene);
	}
	private void buildStage() {
		editMajorStage = new Stage();
		editMajorStage.setTitle("Edit a Course");
	}
	private void buildScene() {
		refreshData(allBags);
		refreshMajorData(allBags);
		editMajorStage.setHeight(screenSizes.getScreenHeight() / 2);
		editMajorStage.setWidth(screenSizes.getScreenWidth() / 3);
		BorderPane inputRoot = new BorderPane();
		//Save button setup and scene setup
		VBox buttonBox = buildButtonBox();
		buttonBox.setAlignment(Pos.CENTER);
		inputRoot.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new Insets(10));
		buildMasterTable();
		buildCurrentTable();
		HBox tableBox = new HBox(50);
		tableBox.getChildren().addAll(table, currentTable);
		inputRoot.setCenter(tableBox);
		tableBox.setAlignment(Pos.CENTER);
		//Acquires a VBox containing all of the text input boxes.
		inputRoot.setBottom(buttonBox);
		courseAddScene = new Scene(inputRoot, editMajorStage.getHeight(), editMajorStage.getWidth());
		
	}
	private void buildMasterTable() {
		table = new TableView<Course>();


		TableColumn<Course, String> courseTitleCol = new TableColumn<>("Course Title");
		courseTitleCol.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));

		TableColumn<Course, String> courseNumberCol = new TableColumn<>("Course Number");
		courseNumberCol.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		courseNumberCol.setEditable(false);
		courseNumberCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<Course, String> isbnCol = new TableColumn<>("Textbook ISBN");
		isbnCol.setCellValueFactory(new PropertyValueFactory<>("textbookIsbn"));

		TableColumn<Course, String> creditsCol = new TableColumn<>("Credits");
		creditsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfCredits"));


		table.setItems(refreshData(allBags));
		table.getColumns().addAll(courseTitleCol, courseNumberCol, isbnCol, creditsCol);
		table.getSortOrder().add(courseNumberCol);
		table.getSelectionModel().selectFirst();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setPrefWidth(editMajorStage.getWidth() / 2);
		table.setOnMousePressed(e ->{
			updateSelectedCourse();
			refreshData(allBags);
//			buildPane();
//			root.setCenter(this.getPane());
		});
		
	}
	private void buildCurrentTable() {
		currentTable = new TableView<CompactCourse>();
		
		TableColumn<CompactCourse, String> courseNumberCol = new TableColumn<>("Course Number");
		courseNumberCol.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		courseNumberCol.setSortType(TableColumn.SortType.ASCENDING);
		
		currentTable.setItems(refreshMajorData(allBags));
		currentTable.getColumns().add(courseNumberCol);
		currentTable.getSortOrder().add(courseNumberCol);
		currentTable.getSelectionModel().selectFirst();
		currentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		currentTable.setPrefWidth(editMajorStage.getWidth() / 2);
		currentTable.setOnMousePressed(e ->{
			updateSelectedMajorCourse();
			refreshMajorData(allBags);
		});
	}
	public void updateSelectedMajorCourse() {
		selectedMajorCourse = currentTable.getSelectionModel().getSelectedItem().getCourseNumber();
	}
	public void updateSelectedCourse() {
		selectedCourse = table.getSelectionModel().getSelectedItem();
	}
	private ObservableList<CompactCourse> refreshMajorData(AllBags allBags){
		ObservableList<CompactCourse> currentCourses = FXCollections.observableArrayList();
		int searchIndex = allBags.getAllMajorBags().find(selectedMajor.getMajorName());
		String[] courses = allBags.getAllMajorBags().getMajor(searchIndex).getCourseNumbers();
		for (int i = 0; i < courses.length; i++) {
			String addCurrentCourse = courses[i];
			CompactCourse compactCourseConversion = new CompactCourse(addCurrentCourse);
			currentCourses.add(compactCourseConversion);
		}
		return currentCourses;
	}
	private ObservableList<Course> refreshData(AllBags allBags) {
		ObservableList<Course> courses = FXCollections.observableArrayList();
		MasterCourseBag mCourseBag = allBags.getMasterCourseBag();
		for (int i = 0; i < mCourseBag.getNumberOfCourses(); i++) {
			Course addCourse = mCourseBag.getCourse(i);
			courses.add(addCourse);
		}
		return courses;
	}
	private VBox buildButtonBox() {
		HBox bottomButtonBox = new HBox();
		VBox returnedBox = new VBox(5);
		
		Button addCourseButton = new Button("Add Selected Course");
		addCourseButton.setOnAction(e ->{
			selectedMajor.add(selectedCourse.getCourseNumber());
			MajorEdit newEditMajorStage = new MajorEdit(allBags, screenSizes, selectedMajor);
			editMajorStage.close();
			newEditMajorStage.getStage().show();
		});
		Button deleteCourseButton = new Button("Delete Major Course");
		deleteCourseButton.setOnAction(e ->{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Confirmation");
			alert.setHeaderText("Are you sure you want to delete this Course?");
			alert.setContentText("Continue?");
			alert.showAndWait().ifPresent(type -> {
				if (type == ButtonType.OK) {
					selectedMajor.delete(selectedMajorCourse);
					MajorEdit newEditMajorStage = new MajorEdit(allBags, screenSizes, selectedMajor);
					editMajorStage.close();
					newEditMajorStage.getStage().show();
				};
			});
		});
		
		saveButton = new Button("Save");
		saveButton.setMinWidth(editMajorStage.getWidth() / 8);
		saveButton.setMaxHeight(editMajorStage.getHeight() / 10);
		saveButton.setOnAction(e ->{
			editMajorStage.close();
		});
		cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(editMajorStage.getWidth() / 8);
		cancelButton.setMaxHeight(editMajorStage.getHeight() / 10);
		cancelButton.setOnAction(e ->{
			editMajorStage.close();
		});

		bottomButtonBox.getChildren().addAll(saveButton, cancelButton);
		HBox.setMargin(saveButton, new Insets(10));
		HBox.setMargin(cancelButton, new Insets(10));
		bottomButtonBox.setAlignment(Pos.CENTER);
		returnedBox.getChildren().addAll(addCourseButton, deleteCourseButton, bottomButtonBox);
		return returnedBox;
	}
	public AllBags getAllBags() {
		return allBags;
	}
	public Stage getStage() {
		return editMajorStage;
	}
}

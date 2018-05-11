package view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

public class StudentCourseEdit {
	private CompactCourse selectedCourse;
	private AllBags allBags;
	private TextField courseGradeField;
	private TableView<CompactCourse> table;
	private ComboBox<String> courseTypeField;
	private BorderPane tablePane;
	private Button applyButton;
	private CourseBag inputCourseBag, courseBag;
	private VBox paneToReturn;
	private Stage editPersonStage;
	private Student selectedStudent;
	private Faculty selectedFaculty;
	public StudentCourseEdit(AllBags allBags, ScreenSizes screenSizes, Student selectedStudent, Stage editPersonStage) {
		this.editPersonStage = editPersonStage;
		this.courseBag = selectedStudent.getCourseBagArray();
		this.allBags = allBags;
		this.selectedStudent = selectedStudent;
		buildTable();
		buildPane();
	}
	public void buildPane() {
		tablePane = new BorderPane();
		tablePane.setTop(buildUniversal());
		HBox tableBox = new HBox(5);
		tableBox.setAlignment(Pos.CENTER);
		tableBox.getChildren().add(buildTable());
		tablePane.setCenter(tableBox);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		paneToReturn = new VBox(10);
		paneToReturn.getChildren().add(tablePane);
	}
	public void refreshFieldInfo() {
		courseGradeField.setText(selectedCourse.getCourseGrade());
		courseTypeField.setValue(selectedCourse.getCourseType());
	}
	public TableView<CompactCourse> buildTable(){
		//Courses table display pane
				HBox tableBox = new HBox(5);
				table = new TableView<CompactCourse>();
				TableColumn<CompactCourse, String> courseNumberCol = new TableColumn<>("Course #");
				courseNumberCol.prefWidthProperty().bind(table.widthProperty().divide(3));
				courseNumberCol.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
				courseNumberCol.setSortType(TableColumn.SortType.ASCENDING);
				
				TableColumn<CompactCourse, String> courseGradeCol = new TableColumn<>("Grade");
				courseGradeCol.prefWidthProperty().bind(table.widthProperty().divide(3));
				courseGradeCol.setCellValueFactory(new PropertyValueFactory<>("courseGrade"));
				
				TableColumn<CompactCourse, String> courseTypeCol = new TableColumn<>("Type");
				courseTypeCol.prefWidthProperty().bind(table.widthProperty().divide(3));
				courseTypeCol.setCellValueFactory(new PropertyValueFactory<>("courseType"));
				
				table.setItems(refreshData());
				table.getColumns().addAll(courseNumberCol, courseGradeCol, courseTypeCol);
				table.getSelectionModel().selectFirst();
				table.getSortOrder().add(courseNumberCol);
				table.setOnMousePressed(e ->{
					updateSelectedCourse();
					refreshFieldInfo();
				});
				table.setMinWidth(editPersonStage.getWidth());
				editPersonStage.widthProperty().addListener(new ChangeListener<Number>() {
					@Override public void changed(ObservableValue o, Number oldWidth, Number newWidth) {
						table.setMinWidth(editPersonStage.getWidth());
					}
				});
				return table;
	}
	public void refreshTable() {
		HBox tableBox = new HBox(5);
		tableBox.setAlignment(Pos.CENTER);
		tableBox.getChildren().add(buildTable());
		tablePane.setCenter(tableBox);
	}
	public ObservableList<CompactCourse> refreshData(){
		ObservableList<CompactCourse> coursesList = FXCollections.observableArrayList();
		for (int i = 0; i < courseBag.getCourseCount(); i++) {
			String[] courseToConvert = courseBag.getCourseInfo(i);
			CompactCourse courseToAdd = new CompactCourse(courseToConvert[0], courseToConvert[1], courseToConvert[2]);
			coursesList.add(courseToAdd);
		}
		return coursesList;
	}
	private HBox buildUniversal() {
		updateSelectedCourse();
		
		HBox textDetails = new HBox(10);
		VBox leftBox = new VBox(15);
		VBox rightBox = new VBox(5);
		
		Label courseGradeLabel = new Label("Course Grade:");
		courseGradeField = new TextField(selectedCourse.getCourseGrade());
		
		Label courseTypeLabel = new Label("Course Type:");
		courseTypeField = new ComboBox<String>();
		courseTypeField.getItems().addAll(
				"NEED",
				"HAVE",
				"TAKING",
				"NOT REQUIRED"
				);
		courseTypeField.setValue(selectedCourse.getCourseType());
		courseTypeField.setMaxWidth(editPersonStage.getWidth());
		leftBox.getChildren().addAll(courseGradeLabel, courseTypeLabel);
		rightBox.getChildren().addAll(courseGradeField, courseTypeField);
		textDetails.getChildren().addAll(leftBox, rightBox);
		textDetails.setAlignment(Pos.CENTER);
		return textDetails;
		
	}
	public void updateSelectedCourse() {
		selectedCourse = table.getSelectionModel().getSelectedItem();
	}
	public void scrollToCourse() {
		String searchId = selectedCourse.getCourseNumber();
		table.getItems().stream().filter(currentlySelected -> currentlySelected.getCourseNumber().equals(searchId))
		.findAny().ifPresent(currentlySelected -> {
			table.getSelectionModel().select(currentlySelected);
			table.scrollTo(currentlySelected);
		});
	}
	public VBox getPane() {
		return paneToReturn;
	}
	public CourseBag getChangedCourseBag() {
		return courseBag;
	}
	public Button getApplyButton() {
		return applyButton;
	}
	public CompactCourse getSelectedCourse() {
		return selectedCourse;
	}
	public TextField getCourseGradeField() {
		return courseGradeField;
	}
	public ComboBox<String> getCourseTypeField() {
		return courseTypeField;
	}
}

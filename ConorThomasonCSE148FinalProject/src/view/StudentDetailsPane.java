package view;

import model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class StudentDetailsPane {
	private BorderPane pane;
	private Label gpaOutput =  new Label("");
	private Label majorOutput = new Label("");
	
	public StudentDetailsPane(Student currentlySelected) {
		buildPane(currentlySelected);
	}
	private void buildPane(Student currentlySelected) {
		
		//Student details display pane
		pane = new BorderPane();
		VBox studentDetails = new VBox(5);
		Label gpa = new Label("GPA: ");
		gpa.setStyle("-fx-font-weight: bold");
		String gpaValue = Double.toString(currentlySelected.getGpa());
		if (gpaValue.length() <= 3) {
			gpaValue = gpaValue.substring(0, 3);
		}
		else {
			gpaValue = gpaValue.substring(0, 4);
		}
		Label gpaOutput = new Label(gpaValue);
		
		Label major = new Label("Major: ");
		major.setStyle("-fx-font-weight: bold");
		Label majorOutput = new Label(currentlySelected.getMajor());
		studentDetails.getChildren().addAll(gpa, gpaOutput, major, majorOutput);
		pane.setTop(studentDetails);
		
		//Courses table display pane
		TableView<CompactCourse> table = new TableView<CompactCourse>();
		TableColumn<CompactCourse, String> courseNumberCol = new TableColumn<>("Course #");
		courseNumberCol.prefWidthProperty().bind(table.widthProperty().divide(3));
		courseNumberCol.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		
		TableColumn<CompactCourse, String> courseGradeCol = new TableColumn<>("Grade");
		courseGradeCol.prefWidthProperty().bind(table.widthProperty().divide(3));
		courseGradeCol.setCellValueFactory(new PropertyValueFactory<>("courseGrade"));
		
		TableColumn<CompactCourse, String> courseTypeCol = new TableColumn<>("Type");
		courseTypeCol.prefWidthProperty().bind(table.widthProperty().divide(3));
		courseTypeCol.setCellValueFactory(new PropertyValueFactory<>("courseType"));
		
		table.setItems(refreshData(currentlySelected));
		table.getColumns().addAll(courseNumberCol, courseGradeCol, courseTypeCol);
		table.getSelectionModel().selectFirst();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setPrefWidth(pane.getWidth());
		pane.setCenter(table);
	}
	private void insertInformation(Student student) {
		gpaOutput.setText(Double.toString(student.getGpa()));
		majorOutput.setText(student.getMajor());
	}
	public BorderPane getPane() {
		return pane;
	}
	private ObservableList<CompactCourse> refreshData(Student currentlySelected){
		ObservableList<CompactCourse> coursesList = FXCollections.observableArrayList();
		CourseBag courseBag = currentlySelected.getCourseBagArray();
		for (int i = 0; i < courseBag.getCourseCount(); i++) {
			String[] courseToConvert = courseBag.getCourseInfo(i);
			CompactCourse courseToAdd = new CompactCourse(courseToConvert[0], courseToConvert[1], courseToConvert[2]);
			coursesList.add(courseToAdd);
		}
		return coursesList;
	}
}

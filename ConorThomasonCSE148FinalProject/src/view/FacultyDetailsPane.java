package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.CourseBag;
import model.Faculty;
import model.Student;

public class FacultyDetailsPane {
	private BorderPane pane;
	private Label titleOutput;
	private Label salaryOutput;
	public FacultyDetailsPane(Faculty currentlySelected) {
		buildPane(currentlySelected);
	}
	private void buildPane(Faculty currentlySelected) {
		
		//Student details display pane
		pane = new BorderPane();
		VBox facultyDetails = new VBox(5);
		Label title = new Label("Title: ");
		title.setStyle("-fx-font-weight: bold");
		Label titleOutput = new Label(currentlySelected.getTitle());
		
		Label salary = new Label("Salary: ");
		salary.setStyle("-fx-font-weight: bold");
		Label salaryOutput = new Label(currentlySelected.getSalary());
		facultyDetails.getChildren().addAll(title, titleOutput, salary, salaryOutput);
		pane.setTop(facultyDetails);
	}
	private void insertInformation(Faculty faculty) {
		titleOutput.setText(faculty.getTitle());
		salaryOutput.setText(faculty.getSalary());
	}
	public BorderPane getPane() {
		return pane;
	}
}

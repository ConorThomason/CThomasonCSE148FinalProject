package view;

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

public class CoursePane {
	private TableView<Course> table;
	private BorderPane borderPane;
	private BorderPane details;
	private Course currentlySelected;
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private BorderPane root;
	private PersonEdit personEditStage;
	private Button editButton, addButton, deleteButton, findButton;
	public CoursePane(AllBags allBags, ScreenSizes screenSizes, BorderPane root) {
		this.root = root;
		this.allBags = allBags;
		this.screenSizes = screenSizes;
		buildAll();
	}
	private void buildAll() {
		buildTable();
		buildDetails();
		buildPane();
	}
	private void buildPane() {
		borderPane = new BorderPane();
		StackPane center = new StackPane(table);
		StackPane left = new StackPane(details);
		center.setMinWidth(screenSizes.getStageWidth() * 0.75);
		borderPane.setCenter(center);
		borderPane.setLeft(left);
	}
	private void buildDetails() {
		details = new BorderPane();
		VBox mainDetails = new VBox(5);
		updateSelectedCourse();
		//Person-type specific section (Faculty has title/salary, student has major, etc)
		details.setTop(buildUniversal(mainDetails));

		//Button Section (Bottom of BorderPane)
		editButton = new Button("Edit");

		editButton.setOnAction(e ->{
			personEditStage.getStage().showAndWait();
			buildTable();
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
		addButton = new Button("Add");
		addButton.setOnAction(e ->{
			PersonAdd personAddStage = new PersonAdd(allBags, screenSizes);
			personAddStage.getStage().showAndWait();
			buildTable();
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
		deleteButton = new Button("Delete");
		deleteButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Confirmation");
			alert.setHeaderText("Are you sure you want to delete this Course?");
			alert.setContentText("Continue?");
			alert.showAndWait().ifPresent(type -> {
				if (type == ButtonType.OK) {
					allBags.getMasterCourseBag().delete(currentlySelected.getCourseNumber());
					buildTable();
					buildDetails();
					buildPane();
					root.setCenter(this.getPane());
				};
			});
		});
		findButton = new Button("Find");
		findButton.setOnAction(e -> {
			Stage findStage = new Stage();
			findStage.setTitle("Find Course");
			HBox findBox = new HBox(5);
			Label findLabel = new Label("Course #:");
			TextField findField = new TextField();
			Button findButton = new Button("Search");
			findButton.setOnAction(f ->{
				int findIndex = allBags.getMasterCourseBag().find(findField.getText());
				if (findIndex == -1)
				{
					Alert notFound = new Alert(AlertType.ERROR);
					notFound.setTitle("ID Search Error");
					notFound.setHeaderText("ID Not Found");
					notFound.showAndWait();
				}
				else {
					Course course = allBags.getMasterCourseBag().getCourse(findIndex);
					currentlySelected = course;
					findStage.close();
					scrollToCourse();
				}
			});
			findBox.getChildren().addAll(findLabel, findField, findButton);
			findBox.setAlignment(Pos.CENTER);
			Scene findScene = new Scene(findBox);
			findStage.setScene(findScene);
			findStage.show();
		});
		HBox buttonBox = new HBox(5);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(addButton, editButton, findButton, deleteButton);
		details.setBottom(buttonBox);

	}
	private void buildTable() {
	table = new TableView<Course>();
	
	
	TableColumn<Course, String> courseTitleCol = new TableColumn<>("Course Title");
	courseTitleCol.minWidthProperty().bind(table.widthProperty().multiply(0.5));
	courseTitleCol.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
	
	TableColumn<Course, String> courseNumberCol = new TableColumn<>("Course #");
	courseNumberCol.minWidthProperty().bind(table.widthProperty().multiply(0.15));
	courseNumberCol.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
	courseNumberCol.setSortType(TableColumn.SortType.ASCENDING);
	
	TableColumn<Course, String> textbookAssignedCol = new TableColumn<>("Textbook ISBN");
	textbookAssignedCol.minWidthProperty().bind(table.widthProperty().multiply(0.2));
	textbookAssignedCol.setCellValueFactory(new PropertyValueFactory<>("textbookIsbn"));
	
	TableColumn<Course, String> creditsCol = new TableColumn<>("Credits");
	creditsCol.minWidthProperty().bind(table.widthProperty().multiply(0.15));
	creditsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfCredits"));
	
	
	table.setItems(refreshData(allBags));
	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	table.getColumns().addAll(courseTitleCol, courseNumberCol, textbookAssignedCol, creditsCol);
	table.getSortOrder().add(courseNumberCol);
	table.getSelectionModel().selectFirst();
	table.setOnMousePressed(e ->{
		buildDetails();
		buildPane();
		root.setCenter(this.getPane());
	});
	}
	public TableView<Course> getTable(){
		return table;
	}
	public void updateSelectedCourse() {
		currentlySelected = table.getSelectionModel().getSelectedItem();
	}
	private ObservableList<Course> refreshData(AllBags allBags) {
		ObservableList<Course> courses = FXCollections.observableArrayList();
		MasterCourseBag courseBag = allBags.getMasterCourseBag();
		for (int i = 0; i < courseBag.getNumberOfCourses(); i++) {
			Course addCourse = courseBag.getCourse(i);
			courses.add(addCourse);
		};
		return courses;
	}
	private VBox buildUniversal(VBox mainDetails) {
		updateSelectedCourse();
		
		//Main Details Section (Top of BorderPane)
		details.setPadding(new Insets(5));
		
		Label courseTitle = new Label("Course Title: ");
		courseTitle.setStyle("-fx-font-weight: bold");
		Label courseTitleOutput = new Label(currentlySelected.getCourseTitle());
		
		Label courseNumber = new Label("Course Number: ");
		courseNumber.setStyle("-fx-font-weight: bold");
		Label courseNumberOutput = new Label(currentlySelected.getCourseNumber());
		
		Label textbookIsbn = new Label("Textbook ISBN: ");
		textbookIsbn.setStyle("-fx-font-weight: bold");
		Label textbookIsbnOutput = new Label();
		Textbook textbookAssigned = currentlySelected.getTextbookAssigned();
		if (textbookAssigned == null)
			textbookIsbnOutput.setText("No Textbook");
		else
			textbookIsbnOutput.setText(textbookAssigned.getIsbn());
		Label credits = new Label("Credits: " );
		credits.setStyle("-fx-font-weight: bold");
		Label creditsOutput = new Label(Integer.toString(currentlySelected.getNumberOfCredits()));
		
		mainDetails.getChildren().addAll(courseTitle, courseTitleOutput, courseNumber, courseNumberOutput, textbookIsbn, 
				textbookIsbnOutput, credits, creditsOutput);
		return mainDetails;
	}
	public BorderPane getPane() {
		return borderPane;
	}
	public void scrollToCourse() {
		String searchId = currentlySelected.getCourseNumber();
		table.getItems().stream().filter(currentlySelected -> currentlySelected.getCourseNumber().equals(searchId))
		.findAny().ifPresent(currentlySelected -> {
			table.getSelectionModel().select(currentlySelected);
			table.scrollTo(currentlySelected);
		});
	}
}

package view;

import javafx.application.Platform;
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
	private CourseEdit courseEditStage;
	private Button editButton, addButton, deleteButton, findButton;
	private boolean firstStart = true;
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
		left.setMinWidth(screenSizes.getStageWidth() * 0.3);
		borderPane.setLeft(left);
		borderPane.setCenter(center);
		editButton.setMinWidth(left.getWidth() * 0.25);
		addButton.setMinWidth(left.getWidth() * 0.25);
		deleteButton.setMinWidth(left.getWidth() * 0.25);
		findButton.setMinWidth(left.getWidth() * 0.25);
	}
	private void buildDetails() {
		details = new BorderPane();
		VBox mainDetails = new VBox(5);
		updateSelectedCourse();
		
		details.setTop(buildUniversal(mainDetails));

		//Button Section (Bottom of BorderPane)
		editButton = new Button("Edit");

		editButton.setOnAction(e ->{
			courseEditStage = new CourseEdit(allBags, screenSizes, currentlySelected);
			courseEditStage.getStage().showAndWait();
			buildTable();
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
		addButton = new Button("Add");
		addButton.setOnAction(e ->{
			CourseAdd courseAddStage = new CourseAdd(allBags, screenSizes);
			courseAddStage.getStage().showAndWait();
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
					notFound.setTitle("Course Number Search Error");
					notFound.setHeaderText("Course Number Not Found");
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
		courseTitleCol.minWidthProperty().bind(table.widthProperty().multiply(0.4));
		courseTitleCol.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));

		TableColumn<Course, String> courseNumberCol = new TableColumn<>("Course Number");
		courseNumberCol.minWidthProperty().bind(table.widthProperty().multiply(0.2));
		courseNumberCol.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		courseNumberCol.setEditable(false);
		courseNumberCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<Course, String> isbnCol = new TableColumn<>("Textbook ISBN");
		isbnCol.minWidthProperty().bind(table.widthProperty().multiply(0.2));
		isbnCol.setCellValueFactory(new PropertyValueFactory<>("textbookIsbn"));

		TableColumn<Course, String> creditsCol = new TableColumn<>("Credits");
		creditsCol.minWidthProperty().bind(table.widthProperty().multiply(0.2));
		creditsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfCredits"));


		table.setItems(refreshData(allBags));
		table.getColumns().addAll(courseTitleCol, courseNumberCol, isbnCol, creditsCol);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
		MasterCourseBag mCourseBag = allBags.getMasterCourseBag();
		for (int i = 0; i < mCourseBag.getNumberOfCourses(); i++) {
			Course addCourse = mCourseBag.getCourse(i);
			courses.add(addCourse);
		}
		return courses;
	}
	private VBox buildUniversal(VBox mainDetails) {
		updateSelectedCourse();
		
		try {
		Label courseHeader = new Label("Course");
		courseHeader.setStyle("-fx-font-size: 24");
		//Main Details Section (Top of BorderPane)
		details.setPadding(new Insets(5));
		
		
		Label courseTitle = new Label("Course Title: ");
		courseTitle.setStyle("-fx-font-weight: bold");
		Label courseTitleOutput = new Label(currentlySelected.getCourseTitle());

		Label courseNumber = new Label("Course Number: ");
		courseNumber.setStyle("-fx-font-weight: bold");
		Label courseNumberOutput = new Label(currentlySelected.getCourseNumber());

		Label isbn = new Label("ISBN: ");
		Label isbnOutput = new Label();
		isbn.setStyle("-fx-font-weight: bold");
		
		if (currentlySelected.getTextbookIsbn() == null) {
			isbnOutput.setText("No Textbook");
		}
		else
			isbnOutput.setText(currentlySelected.getTextbookIsbn());

		Label credits = new Label("Credits: " );
		credits.setStyle("-fx-font-weight: bold");
		Label creditsOutput = new Label(Integer.toString(currentlySelected.getNumberOfCredits()));

		mainDetails.getChildren().addAll(courseHeader, courseTitle, courseTitleOutput, courseNumber, 
				courseNumberOutput, isbn, isbnOutput, credits, creditsOutput);
		return mainDetails;
		}catch(NullPointerException e) {
			Label statusLabel = new Label("No Course Selected");
			mainDetails.getChildren().add(statusLabel);
			return mainDetails;
		}
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

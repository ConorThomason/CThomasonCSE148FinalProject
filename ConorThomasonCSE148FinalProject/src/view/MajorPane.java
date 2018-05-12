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

public class MajorPane {
	private TableView<MajorCourseBag> table;
	private BorderPane borderPane;
	private BorderPane details;
	private MajorCourseBag currentlySelected;
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private BorderPane root;
	private MajorEdit majorEditStage;
	private Button editButton, addButton, deleteButton, findButton;
	private boolean firstStart = true;
	public MajorPane(AllBags allBags, ScreenSizes screenSizes, BorderPane root) {
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
			majorEditStage = new MajorEdit(allBags, screenSizes, currentlySelected);
			majorEditStage.getStage().showAndWait();
			buildTable();
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
		addButton = new Button("Add");
		addButton.setOnAction(e ->{
			MajorAdd majorAddStage = new MajorAdd(allBags, screenSizes);
			majorAddStage.getStage().showAndWait();
			buildTable();
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
		deleteButton = new Button("Delete");
		deleteButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Confirmation");
			alert.setHeaderText("Are you sure you want to delete this Major?");
			alert.setContentText("Continue?");
			alert.showAndWait().ifPresent(type -> {
				if (type == ButtonType.OK) {
					allBags.getAllMajorBags().delete(currentlySelected.getMajorName());
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
			findStage.setTitle("Find Major");
			HBox findBox = new HBox(5);
			Label findLabel = new Label("Major Name:");
			TextField findField = new TextField();
			Button findButton = new Button("Search");
			findButton.setOnAction(f ->{
				int findIndex = allBags.getAllMajorBags().find(findField.getText().toUpperCase());
				if (findIndex == -1)
				{
					Alert notFound = new Alert(AlertType.ERROR);
					notFound.setTitle("Major Search Error");
					notFound.setHeaderText("Major Not Found");
					notFound.showAndWait();
				}
				else {
					MajorCourseBag course = allBags.getAllMajorBags().getMajor(findIndex);
					currentlySelected = course;
					findStage.close();
					scrollToMajorBag();
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
		table = new TableView<MajorCourseBag>();


		TableColumn<MajorCourseBag, String> majorNameCol = new TableColumn<>("Major");
		majorNameCol.minWidthProperty().bind(table.widthProperty().multiply(0.25));
		majorNameCol.setCellValueFactory(new PropertyValueFactory<>("majorName"));
		majorNameCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<MajorCourseBag, String> coursesCol = new TableColumn<>("Required Courses");
		coursesCol.minWidthProperty().bind(table.widthProperty().multiply(0.75));
		coursesCol.setCellValueFactory(new PropertyValueFactory<>("courseNumberStringFormat"));
		


		table.setItems(refreshData(allBags));
		table.getColumns().addAll(majorNameCol, coursesCol);
		table.getSortOrder().add(majorNameCol);
		table.getSelectionModel().selectFirst();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setOnMousePressed(e ->{
			updateSelectedCourse();
			refreshData(allBags);
			allBags.getAllMajorBags().exportAllMajors();
			table.getColumns().get(0).setVisible(false);
			table.getColumns().get(0).setVisible(true);
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
	}
	public TableView<MajorCourseBag> getTable(){
		return table;
	}
	public void updateSelectedCourse() {
		currentlySelected = table.getSelectionModel().getSelectedItem();
	}
	private ObservableList<MajorCourseBag> refreshData(AllBags allBags) {
		ObservableList<MajorCourseBag> majors = FXCollections.observableArrayList();
		AllMajorBags allMajorBag = allBags.getAllMajorBags();
		for (int i = 0; i < allMajorBag.getItemCount(); i++) {
			MajorCourseBag addMajor = allMajorBag.getMajor(i);
			majors.add(addMajor);
		}
		return majors;
	}
	private VBox buildUniversal(VBox mainDetails) {
		updateSelectedCourse();
		
		try {
		Label majorHeader = new Label("Major");
		majorHeader.setStyle("-fx-font-size: 24");
		//Main Details Section (Top of BorderPane)
		details.setPadding(new Insets(5));

		Label majorName = new Label("Major: ");
		majorName.setStyle("-fx-font-weight: bold");
		Label majorNameOutput = new Label(currentlySelected.getMajorName());

		Label courses = new Label("Required Courses:");
		courses.setStyle("-fx-font-weight: bold");
		String coursesToVertical = currentlySelected.getCourseNumberStringFormat();
		if (coursesToVertical != null) {
			coursesToVertical = coursesToVertical.substring(1, coursesToVertical.length()).replaceAll(" ", "\n");
		}
		Label coursesOutput = new Label(coursesToVertical);


		mainDetails.getChildren().addAll(majorHeader, majorName, majorNameOutput, courses, coursesOutput);
		return mainDetails;
		}catch(NullPointerException e) {
			Label statusLabel = new Label("No Major Selected");
			mainDetails.getChildren().add(statusLabel);
			return mainDetails;
		}
	}
	public BorderPane getPane() {
		return borderPane;
	}
	public void scrollToMajorBag() {
		String searchId = currentlySelected.getMajorName();
		table.getItems().stream().filter(currentlySelected -> currentlySelected.getMajorName().equals(searchId))
		.findAny().ifPresent(currentlySelected -> {
			table.getSelectionModel().select(currentlySelected);
			table.scrollTo(currentlySelected);
		});
	}
}

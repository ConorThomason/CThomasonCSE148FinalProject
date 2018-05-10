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

public class PeoplePane {
	private TableView<Person> table;
	private BorderPane borderPane;
	private BorderPane details;
	private Person currentlySelected;
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private BorderPane root;
	private PersonEdit personEditStage;
	private Button editButton, addButton, deleteButton, findButton;
	private boolean firstStart = true;
	public PeoplePane(AllBags allBags, ScreenSizes screenSizes, BorderPane root) {
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
		borderPane.setCenter(center);
		borderPane.setLeft(left);
		editButton.setMinWidth(left.getWidth() * 0.25);
		addButton.setMinWidth(left.getWidth() * 0.25);
		deleteButton.setMinWidth(left.getWidth() * 0.25);
		findButton.setMinWidth(left.getWidth() * 0.25);
	}
	private void buildDetails() {
		details = new BorderPane();
		VBox mainDetails = new VBox(5);
		updateSelectedPerson();
		//Person-type specific section (Faculty has title/salary, student has major, etc)
		if (currentlySelected instanceof Student) {
			Label personType = new Label("Student");
			personType.setStyle("-fx-font-size: 24"); //Found out you can use CSS in the .setStyle
			mainDetails.getChildren().add(personType);
			StudentDetailsPane studentPane = new StudentDetailsPane((Student)currentlySelected);
			details.setCenter(studentPane.getPane());
		}
		else {
			Label personType = new Label("Faculty");
			personType.setStyle("-fx-font-size: 24");
			mainDetails.getChildren().add(personType);
			FacultyDetailsPane facultyPane = new FacultyDetailsPane((Faculty)currentlySelected);
			details.setCenter(facultyPane.getPane());
		}

		details.setTop(buildUniversal(mainDetails));

		//Button Section (Bottom of BorderPane)
		editButton = new Button("Edit");

		editButton.setOnAction(e ->{
			if (currentlySelected instanceof Student) {
				personEditStage = new PersonEdit(allBags, screenSizes, (Student)currentlySelected);
			}
			else {
				personEditStage = new PersonEdit(allBags, screenSizes, (Faculty)currentlySelected);
			}
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
			alert.setHeaderText("Are you sure you want to delete this Person?");
			alert.setContentText("Continue?");
			alert.showAndWait().ifPresent(type -> {
				if (type == ButtonType.OK) {
					allBags.getPeopleBag().delete(currentlySelected.getId());
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
			findStage.setTitle("Find Person");
			HBox findBox = new HBox(5);
			Label findLabel = new Label("ID:");
			TextField findField = new TextField();
			Button findButton = new Button("Search");
			findButton.setOnAction(f ->{
				int findIndex = allBags.getPeopleBag().find(findField.getText());
				if (findIndex == -1)
				{
					Alert notFound = new Alert(AlertType.ERROR);
					notFound.setTitle("ID Search Error");
					notFound.setHeaderText("ID Not Found");
					notFound.showAndWait();
				}
				else {
					Person person = allBags.getPeopleBag().getPerson(findIndex);
					currentlySelected = person;
					findStage.close();
					scrollToPerson();
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
		table = new TableView<Person>();


		TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
		firstNameCol.minWidthProperty().bind(table.widthProperty().multiply(0.35));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
		lastNameCol.minWidthProperty().bind(table.widthProperty().multiply(0.35));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		TableColumn<Person, String> idCol = new TableColumn<>("ID");
		idCol.minWidthProperty().bind(table.widthProperty().multiply(0.1));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		idCol.setEditable(false);
		idCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<Person, String> phoneNumberCol = new TableColumn<>("Phone Number");
		phoneNumberCol.minWidthProperty().bind(table.widthProperty().multiply(0.2));
		phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


		table.setItems(refreshData(allBags));
		table.getColumns().addAll(firstNameCol, lastNameCol, idCol, phoneNumberCol);
		table.getSortOrder().add(idCol);
		table.getSelectionModel().selectFirst();
		table.setOnMousePressed(e ->{
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
	}
	public TableView<Person> getTable(){
		return table;
	}
	public void updateSelectedPerson() {
		currentlySelected = table.getSelectionModel().getSelectedItem();
	}
	private ObservableList<Person> refreshData(AllBags allBags) {
		ObservableList<Person> people = FXCollections.observableArrayList();
		PeopleBag peopleBag = allBags.getPeopleBag();
		for (int i = 0; i < peopleBag.getPeopleCount(); i++) {
			Person addPerson = peopleBag.getPerson(i);
			people.add(addPerson);
		}
		return people;
	}
	private VBox buildUniversal(VBox mainDetails) {
		updateSelectedPerson();

		//Main Details Section (Top of BorderPane)
		details.setPadding(new Insets(5));

		Label firstName = new Label("First Name: ");
		firstName.setStyle("-fx-font-weight: bold");
		Label firstNameOutput = new Label(currentlySelected.getFirstName());

		Label lastName = new Label("Last Name: ");
		lastName.setStyle("-fx-font-weight: bold");
		Label lastNameOutput = new Label(currentlySelected.getLastName());

		Label id = new Label("ID: ");
		id.setStyle("-fx-font-weight: bold");
		Label idOutput = new Label(currentlySelected.getId());

		Label phoneNumber = new Label("Phone Number: " );
		phoneNumber.setStyle("-fx-font-weight: bold");
		Label phoneNumberOutput = new Label(currentlySelected.getPhoneNumber());

		mainDetails.getChildren().addAll(firstName, firstNameOutput, lastName, 
				lastNameOutput, id, idOutput, phoneNumber, phoneNumberOutput);
		return mainDetails;
	}
	public BorderPane getPane() {
		return borderPane;
	}
	public void scrollToPerson() {
		String searchId = currentlySelected.getId();
		table.getItems().stream().filter(currentlySelected -> currentlySelected.getId().equals(searchId))
		.findAny().ifPresent(currentlySelected -> {
			table.getSelectionModel().select(currentlySelected);
			table.scrollTo(currentlySelected);
		});
	}
}

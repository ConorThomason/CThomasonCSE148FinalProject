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

public class TextbookPane {
	private TableView<Textbook> table;
	private BorderPane borderPane;
	private BorderPane details;
	private Textbook currentlySelected;
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private BorderPane root;
	private TextbookEdit textbookEditStage;
	private Button editButton, addButton, deleteButton, findButton;
	private boolean firstStart = true;
	
	public TextbookPane(AllBags allBags, ScreenSizes screenSizes, BorderPane root) {
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
		details.setTop(buildUniversal(mainDetails));

		//Button Section (Bottom of BorderPane)
		editButton = new Button("Edit");

		editButton.setOnAction(e ->{
			textbookEditStage = new TextbookEdit(allBags, screenSizes, currentlySelected);
			textbookEditStage.getStage().showAndWait();
			buildTable();
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
		addButton = new Button("Add");
		addButton.setOnAction(e ->{
			TextbookAdd textbookAddStage = new TextbookAdd(allBags, screenSizes);
			textbookAddStage.getStage().showAndWait();
			buildTable();
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
		deleteButton = new Button("Delete");
		deleteButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Confirmation");
			alert.setHeaderText("Are you sure you want to delete this Textbook?");
			alert.setContentText("Continue?");
			alert.showAndWait().ifPresent(type -> {
				if (type == ButtonType.OK) {
					allBags.getTextbookBag().delete(currentlySelected.getIsbn());
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
			findStage.setTitle("Find Textbook");
			HBox findBox = new HBox(5);
			Label findLabel = new Label("ISBN:");
			TextField findField = new TextField();
			Button findButton = new Button("Search");
			findButton.setOnAction(f ->{
				String convertedString = "";
				String originalText = findField.getText();
				for (int i = 0; i < originalText.length(); i++) {
					if (originalText.charAt(i) == '-') {
						continue;
					}
					else if (convertedString.length() == 13)
						break;
					else {
						convertedString += originalText.charAt(i);
					}
				}
				int findIndex = allBags.getTextbookBag().find(convertedString);
				if (findIndex == -1)
				{
					Alert notFound = new Alert(AlertType.ERROR);
					notFound.setTitle("ISBN Search Error");
					notFound.setHeaderText("ISBN Not Found");
					notFound.showAndWait();
				}
				else {
					Textbook textbook = allBags.getTextbookBag().getTextbook(convertedString);
					currentlySelected = textbook;
					findStage.close();
					scrollToTextbook();
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
		table = new TableView<Textbook>();

		TableColumn<Textbook, String> bookTitleCol = new TableColumn<>("Book Title");
		bookTitleCol.minWidthProperty().bind(table.widthProperty().multiply(0.3));
		bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
		bookTitleCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<Textbook, String> authorNameCol = new TableColumn<>("Author Name");
		authorNameCol.minWidthProperty().bind(table.widthProperty().multiply(0.14));
		authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));

		TableColumn<Textbook, String> publisherCol = new TableColumn<>("Publisher");
		publisherCol.minWidthProperty().bind(table.widthProperty().multiply(0.14));
		publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));

		TableColumn<Textbook, String> bookPriceCol = new TableColumn<>("Book Price");
		bookPriceCol.minWidthProperty().bind(table.widthProperty().multiply(0.1));
		bookPriceCol.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
		
		TableColumn<Textbook, String> isbnCol = new TableColumn<>("ISBN");
		isbnCol.minWidthProperty().bind(table.widthProperty().multiply(0.12));
		isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));


		table.setItems(refreshData(allBags));
		table.getColumns().addAll(bookTitleCol, authorNameCol, publisherCol, bookPriceCol, isbnCol);
		table.getSortOrder().add(bookTitleCol);
		table.getSelectionModel().selectFirst();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setOnMousePressed(e ->{
			buildDetails();
			buildPane();
			root.setCenter(this.getPane());
		});
	}
	public TableView<Textbook> getTable(){
		return table;
	}
	public void updateSelectedPerson() {
		currentlySelected = table.getSelectionModel().getSelectedItem();
	}
	private ObservableList<Textbook> refreshData(AllBags allBags) {
		ObservableList<Textbook> textbooks = FXCollections.observableArrayList();
		TextBookBag textbookBag = allBags.getTextbookBag();
		for (int i = 0; i < textbookBag.getNumberOfTextbooks(); i++) {
			Textbook addTextbook = textbookBag.getTextbookDirect(i);
			textbooks.add(addTextbook);
		}
		return textbooks;
	}
	private VBox buildUniversal(VBox mainDetails) {
		updateSelectedPerson();
		try {
		Label textbookHeader = new Label("Textbook");
		textbookHeader.setStyle("-fx-font-size: 24");
		//Main Details Section (Top of BorderPane)
		details.setPadding(new Insets(5));

		Label bookTitle = new Label("Book Title: ");
		bookTitle.setStyle("-fx-font-weight: bold");
		Label bookTitleOutput = new Label(currentlySelected.getBookTitle());

		Label authorName = new Label("Author Name(s): ");
		authorName.setStyle("-fx-font-weight: bold");
		Label authorNameOutput = new Label(currentlySelected.getAuthorName());

		Label publisherName = new Label("Publisher: ");
		publisherName.setStyle("-fx-font-weight: bold");
		Label publisherNameOutput = new Label(currentlySelected.getPublisher());

		Label bookPrice = new Label("Book Price: " );
		bookPrice.setStyle("-fx-font-weight: bold");
		Label bookPriceOutput = new Label(currentlySelected.getBookPrice());
		
		Label isbn = new Label("ISBN: ");
		isbn.setStyle("-fx-font-weight: bold");
		Label isbnOutput = new Label(currentlySelected.getIsbn());

		mainDetails.getChildren().addAll(textbookHeader, bookTitle, bookTitleOutput, authorName, authorNameOutput, publisherName, publisherNameOutput,
				bookPrice, bookPriceOutput, isbn, isbnOutput);
		return mainDetails;
		} catch (NullPointerException e) {
			Label statusLabel = new Label("No Textbook Selected");
			VBox statusBox = new VBox(5);
			statusBox.getChildren().add(statusLabel);
			return statusBox;
		}
	}
	public BorderPane getPane() {
		return borderPane;
	}
	public void scrollToTextbook() {
		String searchId = currentlySelected.getIsbn();
		table.getItems().stream().filter(currentlySelected -> currentlySelected.getIsbn().equals(searchId))
		.findAny().ifPresent(currentlySelected -> {
			table.getSelectionModel().select(currentlySelected);
			table.scrollTo(currentlySelected);
		});
	}
}

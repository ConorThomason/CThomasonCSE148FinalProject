package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
import model.TextBookBag;
import model.Textbook;

public class TextbookAdd {
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private Stage addTextbookStage;
	private Scene textbookAddScene;
	private TextField bookTitleField;
	private TextField authorNameField;
	private TextField publisherField;
	private TextField bookPriceField;
	private TextField isbnField;
	private Button saveButton, cancelButton;
	public TextbookAdd(AllBags allBags, ScreenSizes screenSizes) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		buildStage();
		buildScene();
		addTextbookStage.setScene(textbookAddScene);
	}
	private void buildStage() {
		addTextbookStage = new Stage();
		addTextbookStage.setTitle("Add a Textbook");
		addTextbookStage.setHeight(screenSizes.getScreenHeight() / 5.5);
		addTextbookStage.setWidth(screenSizes.getScreenWidth() / 7);
	}
	private void buildScene() {
		BorderPane inputRoot = new BorderPane();
		
		//Save button setup and scene setup
		HBox buttonBox = buildButtonBox();
		inputRoot.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new Insets(10));
		saveButton.setOnAction(e ->{
			TextBookBag bagToConvert = allBags.getTextbookBag();
			TextBookBag convertBag = new TextBookBag(bagToConvert.getNumberOfTextbooks() + 1);
			String inputIsbn = isbnField.getText();
			Textbook burnerTextbook = new Textbook("Burner", "Book", "Workaround", "99.99", "TOBEINPUT");
			if (!burnerTextbook.checkValidIsbn(inputIsbn)) {
				if (allBags.getTextbookBag().find("TOBEINPUT") != -1) {
					Util.displayError("Please resolve previous ISBN entry before continuing");
					addTextbookStage.close();
				}
				else {
				Util.displayError("Input ISBN does not conform to ISBN standards; ISBN set to \"TO BE INPUT\"");
				inputIsbn = "TOBEINPUT";
				}
			}
			else {
			Textbook textbook = new Textbook(bookTitleField.getText(), authorNameField.getText(), publisherField.getText(), bookPriceField.getText(), inputIsbn);
			for (int i = 0; i < bagToConvert.getNumberOfTextbooks(); i++) {
				convertBag.add(bagToConvert.getTextbookDirect(i));
			}
			convertBag.add(textbook);
			allBags.setTextbookBag(convertBag);
			addTextbookStage.close();
			}
		});
		cancelButton.setOnAction(e ->{
			addTextbookStage.close();
		});	
		//Acquires a VBox containing all of the text input boxes.
		inputRoot.setCenter(buildFormBoxes());
		textbookAddScene = new Scene(inputRoot, addTextbookStage.getHeight(), addTextbookStage.getWidth());
	}
	public BorderPane buildFormBoxes() {
		BorderPane splitter = new BorderPane();
		HBox universalBox = buildUniversal();
		universalBox.setAlignment(Pos.CENTER);
		BorderPane.setMargin(universalBox, new Insets(10));
		splitter.setCenter(universalBox);
		return splitter;
	}
	private HBox buildUniversal() {
		
		HBox universalBox = new HBox(5);
		VBox leftBox = new VBox(14);
		VBox rightBox = new VBox(5);
		
		HBox bookTitleBox = new HBox(15);
		Label bookTitleLabel = new Label("Book Title: ");
		bookTitleField = new TextField();
		bookTitleBox.getChildren().addAll(bookTitleLabel, bookTitleField);
		bookTitleBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(bookTitleBox, new Insets(5));
		
		HBox authorNameBox = new HBox(15);
		Label authorNameLabel = new Label("Author Name(s): ");
		authorNameField = new TextField();
		authorNameBox.getChildren().addAll(authorNameLabel, authorNameField);
		authorNameBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(authorNameBox, new Insets(5));
		
		HBox publisherBox = new HBox(15);
		Label publisherLabel = new Label("Publisher: ");
		publisherField = new TextField();
		publisherBox.getChildren().addAll(publisherLabel, publisherField);
		publisherBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(publisherBox, new Insets(5));
		
		HBox bookPriceBox = new HBox(15);
		Label bookPriceLabel = new Label("Book Price: ");
		bookPriceField = new TextField();
		bookPriceBox.getChildren().addAll(bookPriceLabel, bookPriceField);
		bookPriceBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(bookPriceBox, new Insets(5));
		
		HBox isbnBox = new HBox(15);
		Label isbnLabel = new Label("ISBN: ");
		isbnField = new TextField();
		isbnBox.getChildren().addAll(isbnLabel, isbnField);
		isbnBox.setAlignment(Pos.CENTER_RIGHT);
		VBox.setMargin(isbnBox, new Insets(5));
		
		leftBox.getChildren().addAll(bookTitleLabel, authorNameLabel, publisherLabel, bookPriceLabel, isbnLabel);
		rightBox.getChildren().addAll(bookTitleField, authorNameField, publisherField, bookPriceField, isbnField);
		
		universalBox.getChildren().addAll(leftBox, rightBox);
		return universalBox;
	}
	private HBox buildButtonBox() {
		HBox bottomButtonBox = new HBox();
		saveButton = new Button("Save");
		saveButton.setMinWidth(addTextbookStage.getWidth() / 8);
		saveButton.setMinHeight(addTextbookStage.getHeight() / 8);
		
		cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(addTextbookStage.getWidth() / 8);
		cancelButton.setMinHeight(addTextbookStage.getHeight() / 8);
		
		bottomButtonBox.getChildren().addAll(saveButton, cancelButton);
		bottomButtonBox.setAlignment(Pos.CENTER);
		HBox.setMargin(saveButton, new Insets(10));
		return bottomButtonBox;
	}
	public AllBags getAllBags() {
		return allBags;
	}
	public Stage getStage() {
		return addTextbookStage;
	}
}

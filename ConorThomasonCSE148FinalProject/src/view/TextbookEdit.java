package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
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
import model.Textbook;

public class TextbookEdit {
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private Stage editTextbookStage;
	private Scene textbookAddScene;
	private Textbook selectedTextbook;
	private TextField bookTitleField;
	private TextField authorNameField;
	private TextField publisherField;
	private TextField bookPriceField;
	private TextField isbnField;
	private Button saveButton, cancelButton;
	public TextbookEdit(AllBags allBags, ScreenSizes screenSizes, Textbook selectedTextbook) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		this.selectedTextbook = selectedTextbook;
		buildStage();
		buildScene();
		editTextbookStage.setScene(textbookAddScene);
	}
	private void buildStage() {
		editTextbookStage = new Stage();
		editTextbookStage.setTitle("Edit a Course");
		editTextbookStage.setHeight(screenSizes.getScreenHeight() / 5.5);
		editTextbookStage.setWidth(screenSizes.getScreenWidth() / 7);
	}
	private void buildScene() {
		BorderPane inputRoot = new BorderPane();
		//Save button setup and scene setup
		HBox courseDetails = buildDetails();
		courseDetails.setAlignment(Pos.CENTER);
		BorderPane.setMargin(courseDetails, new Insets(10));
		HBox buttonBox = buildButtonBox();
		buttonBox.setAlignment(Pos.CENTER);
		inputRoot.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new Insets(10));

		//Acquires a VBox containing all of the text input boxes.
		inputRoot.setTop(courseDetails);
		inputRoot.setBottom(buttonBox);
		textbookAddScene = new Scene(inputRoot, editTextbookStage.getHeight(), editTextbookStage.getWidth());
	}
	private HBox buildDetails() {

		VBox leftBox = new VBox(15);
		VBox rightBox = new VBox(5);
		HBox detailsBox = new HBox(5);

		Label bookTitleLabel = new Label("Book Title: ");
		bookTitleField = new TextField(selectedTextbook.getBookTitle());
		bookTitleField.setMinWidth(editTextbookStage.getWidth() / 2);
		
		Label authorNameLabel = new Label("Author Name(s): ");
		authorNameField = new TextField(selectedTextbook.getAuthorName());

		Label publisherLabel = new Label("Publisher: ");
		publisherField = new TextField(selectedTextbook.getPublisher());
		
		Label bookPriceLabel = new Label("Credits: ");
		bookPriceField = new TextField(selectedTextbook.getBookPrice());
		
		Label isbnLabel = new Label("ISBN: ");
		isbnField = new TextField(selectedTextbook.getIsbn());
		
		leftBox.getChildren().addAll(bookTitleLabel, authorNameLabel, publisherLabel, bookPriceLabel, isbnLabel);
		rightBox.getChildren().addAll(bookTitleField, authorNameField, publisherField, bookPriceField, isbnField);
		detailsBox.getChildren().addAll(leftBox, rightBox);
		detailsBox.setAlignment(Pos.CENTER);
		return detailsBox;
	}
	
	private HBox buildButtonBox() {
		HBox bottomButtonBox = new HBox();
		saveButton = new Button("Save");
		saveButton.setMinWidth(editTextbookStage.getWidth() / 8);
		saveButton.setMaxHeight(editTextbookStage.getHeight() / 10);
		saveButton.setOnAction(e ->{
			selectedTextbook.setBookTitle(bookTitleField.getText());
			selectedTextbook.setAuthorName(authorNameField.getText());
			selectedTextbook.setPublisher(publisherField.getText());
			selectedTextbook.setBookPrice(bookPriceField.getText());
			String inputIsbn = isbnField.getText();
			if (selectedTextbook.checkValidIsbn(inputIsbn))
				selectedTextbook.setIsbn(isbnField.getText());
			else {
				Util.displayError("Input ISBN does not conform to ISBN standards; ISBN set to \"TO BE INPUT\"");
				selectedTextbook.setIsbn("TOBEINPUT");
			}
			allBags.getTextbookBag().save();
			editTextbookStage.close();
		});
		cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(editTextbookStage.getWidth() / 8);
		cancelButton.setMaxHeight(editTextbookStage.getHeight() / 10);
		cancelButton.setOnAction(e ->{
			editTextbookStage.close();
		});

		bottomButtonBox.getChildren().addAll(saveButton, cancelButton);
		HBox.setMargin(saveButton, new Insets(10));
		HBox.setMargin(cancelButton, new Insets(10));
		bottomButtonBox.setAlignment(Pos.CENTER_RIGHT);
		return bottomButtonBox;
	}
	public AllBags getAllBags() {
		return allBags;
	}
	public Stage getStage() {
		return editTextbookStage;
	}
}

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
import model.AllMajorBags;
import model.Course;
import model.CourseBag;
import model.Faculty;
import model.MajorCourseBag;
import model.MasterCourseBag;
import model.PeopleBag;
import model.Person;
import model.Student;
import model.Textbook;

public class MajorAdd {
	private AllBags allBags;
	private ScreenSizes screenSizes;
	private Stage addMajorStage;
	private Scene majorAddScene;
	private TextField majorNameField;
	private TextField courseNumberField;
	private Button saveButton, cancelButton;
	public MajorAdd(AllBags allBags, ScreenSizes screenSizes) {
		this.screenSizes = screenSizes;
		this.allBags = allBags;
		buildStage();
		buildScene();
		addMajorStage.setScene(majorAddScene);
	}
	private void buildStage() {
		addMajorStage = new Stage();
		addMajorStage.setTitle("Add a Major");
		addMajorStage.setHeight(screenSizes.getScreenHeight() / 8);
		addMajorStage.setWidth(screenSizes.getScreenWidth() / 7);
	}
	private void buildScene() {
		BorderPane inputRoot = new BorderPane();
		
		//Save button setup and scene setup
		HBox buttonBox = buildButtonBox();
		inputRoot.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new Insets(10));
		saveButton.setOnAction(e ->{
			AllMajorBags bagToConvert = allBags.getAllMajorBags();
			AllMajorBags convertBag = new AllMajorBags(bagToConvert.getItemCount() + 1);
			MajorCourseBag majorBag = new MajorCourseBag(1);
			majorBag.setMajorName(majorNameField.getText().toUpperCase());
			for (int i = 0; i < bagToConvert.getItemCount(); i++) {
				convertBag.add(bagToConvert.getMajor(i));
			}
			if (convertBag.isDuplicate(majorBag.getMajorName())){
				Util.displayError("Major already exists");
			}
			else {
				convertBag.add(majorBag);
				allBags.setAllMajorBags(convertBag);
				addMajorStage.close();
			}
		});
		cancelButton.setOnAction(e ->{
			addMajorStage.close();
		});
		
		//Acquires a VBox containing all of the text input boxes.
		inputRoot.setCenter(buildFormBoxes());
		majorAddScene = new Scene(inputRoot, addMajorStage.getHeight(), addMajorStage.getWidth());
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
		
		Label majorNameLabel = new Label("Major: ");
		majorNameField = new TextField();
		
		
		leftBox.getChildren().addAll(majorNameLabel);
		rightBox.getChildren().addAll(majorNameField);
		
		universalBox.getChildren().addAll(leftBox, rightBox);
		return universalBox;
	}
	private HBox buildButtonBox() {
		HBox bottomButtonBox = new HBox();
		saveButton = new Button("Save");
		saveButton.setMinWidth(addMajorStage.getWidth() / 8);
		saveButton.setMinHeight(addMajorStage.getHeight() / 9);
		
		cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(addMajorStage.getWidth() / 8);
		cancelButton.setMinHeight(addMajorStage.getHeight() / 9);
		
		bottomButtonBox.getChildren().addAll(saveButton, cancelButton);
		bottomButtonBox.setAlignment(Pos.CENTER);
		HBox.setMargin(saveButton, new Insets(10));
		return bottomButtonBox;
	}
	public AllBags getAllBags() {
		return allBags;
	}
	public Stage getStage() {
		return addMajorStage;
	}
}

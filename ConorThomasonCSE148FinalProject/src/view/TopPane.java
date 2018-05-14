package view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.*;


public class TopPane {
	private Stage stage;
	private MenuBar menuBar;
	private Menu viewMenu;
	private MenuItem viewMainItem;
	private MenuItem viewPeopleItem;
	private MenuItem viewTextbooksItem;
	private MenuItem viewCoursesItem;
	private MenuItem viewMajorsItem;
	private Menu importExportMenu;
	private Menu importMenu;
	private MenuItem importTextbooksItem;
	private MenuItem importPeopleItem;
	private MenuItem importCoursesItem;
	private MenuItem importMajorsItem;
	private Menu exportMenu;
	private MenuItem exportTextbooksItem;
	private MenuItem exportPeopleItem;
	private MenuItem exportCoursesItem;
	private MenuItem exportMajorsItem;
	private MenuItem loadMenuItem;
	
	private AllBags allBags;
	private BorderPane root;
	private ScreenSizes screenSizes;
	
	//Make the menuItems global, define their actions here, define the TopPane, set action in Start.
	public TopPane(AllBags allBags, Stage stage, BorderPane root, ScreenSizes screenSizes) {
		this.stage = stage;
		this.root = root;
		this.allBags = allBags;
		this.screenSizes = screenSizes;
		buildMenuBar(allBags, screenSizes);
	}
	private void buildMenuBar(AllBags allBags, ScreenSizes screenSizes) {
		Menu fileMenu = new Menu("File");
		MenuItem saveMenuItem = new MenuItem("Save All");
		saveMenuItem.setOnAction(e -> {
			allBags.save();
		});
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Are you sure you want to continue?");
			alert.setHeaderText("Any data that hasn't been saved may be lost...");
			alert.setContentText("Continue?");
			alert.showAndWait().ifPresent(type -> {
				if (type == ButtonType.OK) {
					Platform.exit();
				};
			});
		});
		loadMenuItem = new MenuItem("Load All");
		loadMenuItem.setOnAction(e ->{
			allBags.load();
			rebuildScene(screenSizes);
		});
		viewMenu = new Menu("View");
		viewMainItem = new MenuItem("Main Menu");
		viewPeopleItem = new MenuItem("People");
		viewTextbooksItem = new MenuItem("Textbooks");
		viewCoursesItem = new MenuItem("Courses");
		viewMajorsItem = new MenuItem("Majors");
		
		importExportMenu = new Menu("Import/Export");
		importMenu = new Menu("Import");
		
		importTextbooksItem = new MenuItem("Textbooks");
		importTextbooksItem.setOnAction(e ->{
			String fileToImport = Util.getFile(stage);
			if (fileToImport == null)
				fileToImport = "DEFAULT";
			allBags.getTextbookBag().importData(fileToImport);
			rebuildScene(screenSizes);
		});
		importPeopleItem = new MenuItem("People");
		importPeopleItem.setOnAction(e ->{
			String fileToImport = Util.getFile(stage);
			if (fileToImport == null)
				fileToImport = "DEFAULT";
			allBags.getPeopleBag().importData(fileToImport);
			rebuildScene(screenSizes);
		});
		importCoursesItem = new MenuItem("Courses");
		importCoursesItem.setOnAction(e ->{
			String fileToImport = Util.getFile(stage);
			if (fileToImport == null)
				fileToImport = "DEFAULT";
			allBags.getMasterCourseBag().importData(fileToImport);
			rebuildScene(screenSizes);
		});
		importMajorsItem = new MenuItem("Majors");
		importMajorsItem.setOnAction(e ->{
			String fileToImport = Util.getFile(stage);
			if (fileToImport == null)
				fileToImport = "DEFAULT";
			allBags.getAllMajorBags().importAllMajors(fileToImport);
			rebuildScene(screenSizes);
		});
		
		exportMenu = new Menu("Export");
		exportTextbooksItem = new MenuItem("Textbooks");
		exportTextbooksItem.setOnAction(e ->{
			allBags.getTextbookBag().exportData();
		});
		exportPeopleItem = new MenuItem("People");
		exportPeopleItem.setOnAction(e ->{
			allBags.getPeopleBag().exportData();
		});
		exportCoursesItem = new MenuItem("Courses");
		exportCoursesItem.setOnAction(e ->{
			allBags.getMasterCourseBag().exportData();
		});
		exportMajorsItem = new MenuItem("Majors");
		exportMajorsItem.setOnAction(e ->{
			allBags.getAllMajorBags().exportAllMajors();
		});
		
		fileMenu.getItems().addAll(loadMenuItem, saveMenuItem, exitMenuItem);
		viewMenu.getItems().addAll(viewMainItem, viewPeopleItem, viewTextbooksItem, viewCoursesItem, viewMajorsItem);
		importMenu.getItems().addAll(importTextbooksItem, importPeopleItem, importCoursesItem, importMajorsItem);
		exportMenu.getItems().addAll(exportTextbooksItem, exportPeopleItem, exportCoursesItem, exportMajorsItem);
		
		importExportMenu.getItems().addAll(importMenu, exportMenu);
		menuBar = new MenuBar(); //Removal of this breaks everything. Don't remove it.
		menuBar.getMenus().addAll(fileMenu, viewMenu, importExportMenu);
	}
	public void rebuildScene(ScreenSizes screenSizes) {
		int currentScene = screenSizes.getCurrentScene();
		switch (currentScene) {
		case 1:
			PeoplePane rebuildPeople = new PeoplePane(allBags, screenSizes, root);
			root.setCenter(rebuildPeople.getPane());
			break;
		case 2://course
			CoursePane rebuildCourse = new CoursePane(allBags, screenSizes, root);
			root.setCenter(rebuildCourse.getPane());
			break;
		case 3: //textbook
			TextbookPane rebuildTextbook = new TextbookPane(allBags, screenSizes, root);
			root.setCenter(rebuildTextbook.getPane());
			break;
		case 4: //major
			MajorPane rebuildMajor = new MajorPane(allBags, screenSizes, root);
			root.setCenter(rebuildMajor.getPane());
			break;
		}
	}
	public MenuItem getLoadMenuItem() {
		return loadMenuItem;
	}
	public void setLoadMenuItem(MenuItem loadMenuItem) {
		this.loadMenuItem = loadMenuItem;
	}
	public MenuItem getViewMajorsItem() {
		return viewMajorsItem;
	}
	public void setViewMajorsItem(MenuItem viewMajorsItem) {
		this.viewMajorsItem = viewMajorsItem;
	}
	public Menu getViewMenu() {
		return viewMenu;
	}
	public void setViewMenu(Menu viewMenu) {
		this.viewMenu = viewMenu;
	}
	public MenuItem getViewMainItem() {
		return viewMainItem;
	}
	public void setViewMainItem(MenuItem viewMainItem) {
		this.viewMainItem = viewMainItem;
	}
	public MenuItem getViewPeopleItem() {
		return viewPeopleItem;
	}
	public void setViewPeopleItem(MenuItem viewPeopleItem) {
		this.viewPeopleItem = viewPeopleItem;
	}
	public MenuItem getViewTextbooksItem() {
		return viewTextbooksItem;
	}
	public void setViewTextbooksItem(MenuItem viewTextbooksItem) {
		this.viewTextbooksItem = viewTextbooksItem;
	}
	public MenuItem getViewCoursesItem() {
		return viewCoursesItem;
	}
	public void setViewCoursesItem(MenuItem viewCoursesItem) {
		this.viewCoursesItem = viewCoursesItem;
	}
	public void setMenuBar(MenuBar menuBar) {
		this.menuBar = menuBar;
	}
	public MenuBar getMenuBar() {
		return menuBar;
	}
}

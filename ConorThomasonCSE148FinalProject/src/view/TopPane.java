package view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import model.*;


public class TopPane {
	private MenuBar menuBar;
	private Menu viewMenu;
	private MenuItem viewMainItem;
	private MenuItem viewPeopleItem;
	private MenuItem viewTextbooksItem;
	private MenuItem viewCoursesItem;
	private MenuItem viewMajorsItem;
	//Make the menuItems global, define their actions here, define the TopPane, set action in Start.
	public TopPane(AllBags allBags) {
		buildMenuBar(allBags);
	}
	private void buildMenuBar(AllBags allBags) {
		Menu fileMenu = new Menu("File");
		MenuItem  newMenuItem = new MenuItem("New");
		MenuItem saveMenuItem = new MenuItem("Save All");
		saveMenuItem.setOnAction(e -> {
			allBags.save();
		});
		MenuItem loadMenuItem = new MenuItem("Open");
		loadMenuItem.setOnAction(e -> {
			allBags.load();
		});
		
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Are you sure you want to continue?");
			alert.setHeaderText("Any data that has not been saved may be lost...");
			alert.setContentText("Continue?");
			alert.showAndWait().ifPresent(type -> {
				if (type == ButtonType.OK) {
					Platform.exit();
				};
			});
		});
		viewMenu = new Menu("View");
		viewMainItem = new MenuItem("Main Menu");
		viewPeopleItem = new MenuItem("People");
		viewTextbooksItem = new MenuItem("Textbooks");
		viewCoursesItem = new MenuItem("Courses");
		viewMajorsItem = new MenuItem("Majors");
		
		fileMenu.getItems().addAll(newMenuItem, loadMenuItem, saveMenuItem, exitMenuItem);
		viewMenu.getItems().addAll(viewMainItem, viewPeopleItem, viewTextbooksItem, viewCoursesItem, viewMajorsItem);
		menuBar = new MenuBar(); //Removal of this breaks everything. Don't remove it.
		menuBar.getMenus().addAll(fileMenu, viewMenu);
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

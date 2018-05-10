package view;

import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;

public class ScreenSizes {
	private int screenHeight;
	private int screenWidth;
	private int stageHeight;
	private int stageWidth;
	private int sceneCode; //0 = mainPane, 1 = peoplePane, 2 = coursePane
	private final Stage primaryStage;
	public ScreenSizes(Stage primaryStage, Rectangle2D screenSize, int currentScene) {
		this.primaryStage = primaryStage;
		screenHeight = (int) screenSize.getHeight();
		screenWidth = (int) screenSize.getWidth();
		stageHeight = (int) primaryStage.getHeight();
		stageWidth = (int) primaryStage.getWidth();
		this.setCurrentScene(currentScene);
	}
	public int getScreenHeight() {
		return screenHeight;
	}
	public int getScreenWidth() {
		return screenWidth;
	}
	public int getStageHeight() {
		stageHeight = (int) primaryStage.getHeight();
		return stageHeight;
	}
	public int getStageWidth() {
		stageWidth = (int) primaryStage.getWidth();
		return stageWidth;
	}
	public void setCurrentScene(int sceneCode) {
		this.sceneCode = sceneCode;
	}
	public int getCurrentScene() {
		return sceneCode;
	}
}

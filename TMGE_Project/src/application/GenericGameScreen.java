package application;

import GameModules.Board;
import GameModules.GameEngine;
import GameModules.GameManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public abstract class GenericGameScreen {
	protected VBox boardBox;
	protected BorderPane gamePane;
	protected Integer width;
	protected Integer height;
	protected GameManager gameManager;
	protected Button endGameHandler;

	public void SetWidth(Integer width) {
		this.width = width;
	}

	public void SetHeight(Integer height) {
		this.height = height;
	}
	
	public void SetGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
	
	public void SetEndGameHandler(Button endGameHandler) {
		this.endGameHandler = endGameHandler;
	}
	
	protected abstract VBox GetBoard(Board board);
	
	public Scene CreateGameScene(GameEngine gameEngine) {

		gamePane = new BorderPane();
		VBox boardBox = GetBoard(gameEngine.GetBoardState());
		gamePane.getChildren().add(boardBox);
		gamePane.setBottom(CreateControlPanel(gameEngine));
		Scene scene = new Scene(gamePane, this.width, this.height);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		EditScene(scene, gameEngine);
		return scene;
	}
	
	public abstract void EditScene(Scene gameScene, GameEngine gameEngine);
	
	protected void UpdateGuiBoard() {
		gamePane.getChildren().remove(boardBox);
		gamePane.getChildren().add(GetBoard(gameManager.GetBoardState()));
	}
	
	protected void GameOver() {
		this.gameManager.SetScoresAfterGame();
		this.endGameHandler.fire();
	}
	
	protected Boolean CheckGameOver() {
		if (gameManager.IsGameEnded()) {
			System.out.println("Game ended");
			this.GameOver();
			return true;
		}
		
		return false;
	}
	
	protected abstract VBox CreateControlPanel (GameEngine gameEngine);
}

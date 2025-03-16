package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import GameModules.GameEngine;
import GameModules.GameManager;
import GameModules.GameManager.GameLeaderBoard;
import GameModules.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application implements EventHandler<ActionEvent>{
	private GameManager gameManager;
	private Stage window;
	private Button eventFireButton;
	private GenericGameScreen gameScreen;
	
	private final Integer WIDTH = 400;
	private final Integer HEIGHT = 600;
	
	
	@Override
	public void start(Stage primaryStage) {
		this.window = primaryStage;
		this.gameManager = new GameManager();
		this.gameManager.AddUser("Player1", 1);
		this.gameManager.AddUser("Player2", 2);
		
		this.eventFireButton = new Button();
		this.eventFireButton.setOnAction(this); //change to inner class
		
		this.gameScreen = new ColumnsGameScreen(WIDTH, HEIGHT, gameManager, eventFireButton);
		
		try {
			
			primaryStage.setTitle("TGME");
			
			Scene userSelectionScene = CreateUserSelectionScene();
			primaryStage.setScene(userSelectionScene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void ChangeScene(Scene newScene) {
		this.window.setScene(newScene);
	}
	
	private Scene CreateUserSelectionScene() {
		HBox hUserSelectionBox = new HBox();
		hUserSelectionBox.getChildren().add(new Text("Enter userId"));
		TextField userInput = new TextField();
		hUserSelectionBox.getChildren().add(userInput);
		
		Button submitButton = new Button("submit");
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == submitButton) {
						Integer userId = Integer.parseInt(userInput.getText());
						if (gameManager.IsUser(userId)) {
							gameManager.SetCurrentUser(userId);
							ChangeScene(CreateGameSelectionScene());
						}
					}
				}
			});
		hUserSelectionBox.getChildren().add(submitButton);
		
		Button leaderBoardButton = new Button("leaderboards");
		leaderBoardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() == leaderBoardButton) {
					ChangeScene(CreateGameLeaderBoardScreen());
				}
			}
		});
		
		BorderPane layout = new BorderPane(hUserSelectionBox);
		layout.setBottom(leaderBoardButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	
	private Scene CreateGameSelectionScene() {	
		HBox hGameButtonBox = new HBox();
		hGameButtonBox.getChildren().add(new Text("Select a game"));

		for (GameEngine engine : gameManager.GetAllGameEngines()) {
			Button gameButton = new Button();
			gameButton.setText(engine.GetGameName());
			gameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == gameButton) {
						gameManager.SetGameEngine(engine);
						ChangeScene(gameScreen.CreateGameScene(engine));
					}
				}
			});
			hGameButtonBox.getChildren().add(gameButton);
		}
		Button backButton = new Button("back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == backButton) {
						ChangeScene(CreateUserSelectionScene());
					}
				}
			});
		
		BorderPane layout = new BorderPane(hGameButtonBox);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	private Scene CreateGameOverScreen() {
		VBox endGameInfoBox = new VBox();
		endGameInfoBox.getChildren().add(new Text("Game Over"));
		HBox scoreBox = new HBox();
		scoreBox.getChildren().add(new Text("Score: " + this.gameManager.GetScore()));
		endGameInfoBox.getChildren().add(scoreBox);
		
		Button backButton = new Button("back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == backButton) {
						ChangeScene(CreateUserSelectionScene());
					}
				}
			});
		
		BorderPane layout = new BorderPane(endGameInfoBox);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	private Scene CreateLeaderBoardScreen(Integer gameId) {
		VBox leaderBoardBox = new VBox();
		GameLeaderBoard leaderboard = this.gameManager.GetLeaderboard(gameId);
		List<Entry<Integer, Integer>> userScorePairs = leaderboard.GetSortedLeaderBoard();
		
		for (Entry<Integer, Integer> userScorePair : userScorePairs) {
			User user = this.gameManager.GetUser(userScorePair.getKey());
			leaderBoardBox.getChildren().add(new Text(user.GetName() + ": " + userScorePair.getValue()));
		}
		

		Button backButton = new Button("back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == backButton) {
						ChangeScene(CreateGameLeaderBoardScreen());
					}
				}
			});
		
		BorderPane layout = new BorderPane(leaderBoardBox);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	private Scene CreateGameLeaderBoardScreen() {
		HBox gameLeaderBoardBox = new HBox();
		gameLeaderBoardBox.getChildren().add(new Text("Select a game"));

		for (GameEngine engine : gameManager.GetAllGameEngines()) {
			Button gameButton = new Button();
			gameButton.setText(engine.GetGameName());
			gameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == gameButton) {
						ChangeScene(CreateLeaderBoardScreen(engine.GetGameId()));
					}
				}
			});
			gameLeaderBoardBox.getChildren().add(gameButton);
		}

		Button backButton = new Button("back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == backButton) {
						ChangeScene(CreateUserSelectionScene());
					}
				}
			});
		
		BorderPane layout = new BorderPane(gameLeaderBoardBox);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void handle(ActionEvent arg0) { //possibly move this to an nested class
		if (arg0.getSource() == this.eventFireButton) {
			this.ChangeScene(CreateGameOverScreen()); 
		}
		
	}
}

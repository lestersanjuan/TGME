package application;

import GameModules.Board;
import GameModules.GameEngine;
import GameModules.GameManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Main extends Application implements EventHandler<ActionEvent>{
	private GameManager gameManager;
	private Stage window;
	private Button eventFireButton;
	private GameScreen gameScreen;
	
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
		
		this.gameScreen = new GameScreen(WIDTH, HEIGHT, gameManager, eventFireButton);
		
		try {
			
			primaryStage.setTitle("TGME");
			
			Scene userSelectionScene = CreateUserSelectionScene();
			primaryStage.setScene(CreateGameSelectionScene());
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
		
		BorderPane layout = new BorderPane(hUserSelectionBox);
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
	
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void handle(ActionEvent arg0) {
		if (arg0.getSource() == this.eventFireButton) {
			//this.ChangeScene(null); //Set to game over screen
		}
		
	}
}

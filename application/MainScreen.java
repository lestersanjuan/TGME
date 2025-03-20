package application;

import java.util.ArrayList;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MainScreen extends Application {
	private GameManager gameManager;
	private Stage window;
	private Button eventFireButton;
	private static Map<GameEngine, GenericGameScreen> gameEngineScreenMap;
	
	private static Integer WIDTH = 600;
	private static Integer HEIGHT = 800;
	
	
	@Override
	public void start(Stage primaryStage) {
		this.window = primaryStage;
		
		this.eventFireButton = new Button();
		this.eventFireButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() == eventFireButton) {
					ChangeScene(CreateGameOverScreen()); 
					gameManager.ResetCurrentEngine();
				}
			}
		}); 

		this.gameManager = new GameManager();
		for (GenericGameScreen gameScreen : gameEngineScreenMap.values()) {
			gameScreen.SetWidth(WIDTH);
			gameScreen.SetHeight(HEIGHT);
			gameScreen.SetGameManager(gameManager);
			gameScreen.SetEndGameHandler(eventFireButton);
		}
		
		this.gameManager.AddGameEngines(new ArrayList<GameEngine>(gameEngineScreenMap.keySet()));
		this.gameManager.AddUser("Player1", 1);
		this.gameManager.AddUser("Player2", 2);
		
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
		Text headerText = new Text("User Selection");
		headerText.setFont(Font.font(24));	
		VBox vUserInputBox = new VBox();
		HBox hUserSelectionBox = new HBox();
		hUserSelectionBox.getChildren().add(new Text("Enter userId"));
		TextField userInput = new TextField();
		hUserSelectionBox.getChildren().add(userInput);

		Text outputText = new Text("");
		
		Button submitButton = new Button("submit");
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == submitButton) {
						try {		
							
							Integer userId = Integer.parseInt(userInput.getText());
							if (gameManager.IsUser(userId)) {
								gameManager.SetCurrentUser(userId);
								ChangeScene(CreateGameSelectionScene());
							} else {
								outputText.setText("Not a valid id, enter your user id");
							}
							
						} catch (NumberFormatException e){
							outputText.setText("Not a valid id, enter your numerical id");
						}
					}
					
				}
			});
		
		
		hUserSelectionBox.getChildren().add(submitButton);
		vUserInputBox.getChildren().addAll(hUserSelectionBox, outputText);
		
		HBox buttonHBox = new HBox();
		Button leaderBoardButton = new Button("leaderboards");
		leaderBoardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() == leaderBoardButton) {
					ChangeScene(CreateGameLeaderBoardScreen());
				}
			}
		});
		Button newUserButton = new Button("new user");
		newUserButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() == newUserButton) {
					ChangeScene(CreateNewUserScreen());
				}
			}
		});
		buttonHBox.getChildren().addAll(leaderBoardButton, newUserButton);
		
		BorderPane layout = new BorderPane(vUserInputBox);
		layout.setTop(headerText);
		layout.setBottom(buttonHBox);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	
	private Scene CreateGameSelectionScene() {
		Text headerText = new Text("Game Selection");
		headerText.setFont(Font.font(24));	
		VBox gameScreenBox = new VBox();
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
						Scene gameScene = gameEngineScreenMap.get(engine).CreateGameScene(engine);
						ChangeScene(gameScene);
					}
				}
			});
			hGameButtonBox.getChildren().add(gameButton);
		}
		gameScreenBox.getChildren().add(hGameButtonBox);
		
		User currentUser = gameManager.GetCurrentUser();
		
		Button profileButton = new Button("view " + currentUser.GetName() + "\'s profile");
		profileButton.setOnAction( new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == profileButton) {
						ChangeScene(CreateUserProfileScreen(currentUser));
					}
				}
			});
		
		gameScreenBox.getChildren().add(profileButton);
		
		Button backButton = new Button("back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == backButton) {
						ChangeScene(CreateUserSelectionScene());
					}
				}
			});
		
		BorderPane layout = new BorderPane(gameScreenBox);
		layout.setTop(headerText);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	private Scene CreateUserProfileScreen(User user) {	
		Text headerText = new Text("Profile");
		headerText.setFont(Font.font(24));
		VBox vUserInfoBox = new VBox();
		vUserInfoBox.getChildren().add(new Text("Username: " + user.GetName()));
		vUserInfoBox.getChildren().add(new Text("User id: " + user.GetUserId()));
		String lastPlayedGame = user.GetData("LastGame");
		vUserInfoBox.getChildren().add(new Text("Last played game: " + lastPlayedGame));
		vUserInfoBox.getChildren().add(new Text("View leaderboards to see your highscores!"));
		
		Button backButton = new Button("back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == backButton) {
						ChangeScene(CreateGameSelectionScene());
					}
				}
			});
		
		BorderPane layout = new BorderPane(vUserInfoBox);
		layout.setTop(headerText);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	private Scene CreateNewUserScreen() {	
		Text headerText = new Text("New User Creator");
		headerText.setFont(Font.font(24));
		VBox vUserInfoBox = new VBox();
		HBox userNameBox = new HBox();
		userNameBox.getChildren().add(new Text("Enter a username: "));
		
		TextField usernameTextField = new TextField();
		userNameBox.getChildren().add(usernameTextField);
		
		HBox userIdBox = new HBox();
		userNameBox.getChildren().add(new Text("Enter a userId: "));
		
		TextField userIdTextField = new TextField();
		userNameBox.getChildren().add(userIdTextField);
		Text outputText = new Text("");
		
		
		Button submit = new Button("submit");
		
		submit.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == submit) {
						try {		
							Integer newId = Integer.parseInt(userIdTextField.getText());
							if (gameManager.GetUser(newId) != null) {
								outputText.setText("Id is already taken");
							} else {
								gameManager.AddUser(usernameTextField.getText(), newId);
								gameManager.SetCurrentUser(newId);
								ChangeScene(CreateGameSelectionScene());
							}
							
						} catch (NumberFormatException e){
							outputText.setText("Not a valid id, enter your numerical id");
						}
						
					} 
				}
			});
		
		vUserInfoBox.getChildren().addAll(userNameBox, userIdBox, submit, outputText);
		
		Button backButton = new Button("back");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == backButton) {
						ChangeScene(CreateUserSelectionScene());
					}
				}
			});
		
		BorderPane layout = new BorderPane(vUserInfoBox);
		layout.setTop(headerText);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	private Scene CreateGameOverScreen() {
		VBox endGameInfoBox = new VBox();
		Text gameOverText = new Text("Game Over");
		gameOverText.setFont(Font.font(64));
		endGameInfoBox.getChildren().add(gameOverText);
		HBox scoreBox = new HBox();
		Text scoreText = new Text("Score: " + this.gameManager.GetScore());
		scoreText.setFont(Font.font(24));
		scoreBox.getChildren().add(scoreText);
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
	
	private Scene CreateLeaderBoardScreen(GameEngine gameEngine) {
		Text leaderBoardText = new Text(gameEngine.GetGameName() + " Leaderboards");
		leaderBoardText.setFont(Font.font(24));
		VBox leaderBoardBox = new VBox();
		GameLeaderBoard leaderboard = this.gameManager.GetLeaderboard(gameEngine.GetGameId());
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
		layout.setTop(leaderBoardText);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	private Scene CreateGameLeaderBoardScreen() {
		HBox gameLeaderBoardBox = new HBox();
		gameLeaderBoardBox.getChildren().add(new Text("Select a game to view its leaderboard"));

		for (GameEngine engine : gameManager.GetAllGameEngines()) {
			Button gameButton = new Button();
			gameButton.setText(engine.GetGameName());
			gameButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (event.getSource() == gameButton) {
						ChangeScene(CreateLeaderBoardScreen(engine));
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
		
		Text leaderBoardText = new Text("Leaderboards");
		leaderBoardText.setFont(Font.font(24));
		
		BorderPane layout = new BorderPane(gameLeaderBoardBox);
		layout.setTop(leaderBoardText);
		layout.setBottom(backButton);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}

	public static void StartScreen(Map<GameEngine, GenericGameScreen> argGameEngineScreenMap, Integer width, Integer height) {
		gameEngineScreenMap = argGameEngineScreenMap;
		WIDTH = width;
		HEIGHT = height;
		launch();
	}
}

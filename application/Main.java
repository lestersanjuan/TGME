package application;
	
import java.util.HashMap;
import java.util.Map;

import GameModules.Board;
import GameModules.GameEngine;
import GameModules.GameManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
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


public class Main extends Application {
	private GameManager gameManager;
	private Stage window;
	private final Integer WIDTH = 400;
	private final Integer HEIGHT = 600;
	
	private VBox testBox;
	private BorderPane testPane;
	
	@Override
	public void start(Stage primaryStage) {
		this.window = primaryStage;
		this.gameManager = new GameManager();
		this.gameManager.AddUser("Player1", 1);
		this.gameManager.AddUser("Player2", 2);
		
		try {
			
			
			primaryStage.setTitle("TGME");
			
			Scene userSelectionScene = CreateUserSelectionScene();
			//StackPane layout = new StackPane(hGameButtonBox);
			primaryStage.setScene(CreateGameScene(gameManager.GetGameEngine(1)));
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
						System.out.println(engine.GetGameName());
					}
				}
			});
			hGameButtonBox.getChildren().add(gameButton);
		}
		BorderPane layout = new BorderPane(hGameButtonBox);
		Scene scene = new Scene(layout,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	private VBox GetBoard(Board board) { //Interfaced
		testBox = new VBox();
		testBox.setSpacing(5); 
		for (int row = 0; row < board.GetHeight(); row ++) {
			HBox tileRow = new HBox();
			tileRow.setSpacing(5); 
			for (int col = 0; col < board.GetWidth(); col ++) {
				Rectangle rectangle = new Rectangle();
				rectangle.setWidth(20.0f); 
				rectangle.setHeight(20.0f); 
				rectangle.setFill(Color.hsb(50, 1, 1));	
				tileRow.getChildren().add(rectangle);
			}
			testBox.getChildren().add(tileRow);
		}
		
		return testBox;
	}
	
	private VBox TestGetBoard(Board board) { //Interfaced
		VBox vBox = new VBox();
		vBox.setSpacing(5); 
		for (int row = 0; row < board.GetHeight(); row ++) {
			HBox tileRow = new HBox();
			tileRow.setSpacing(5); 
			for (int col = 0; col < board.GetWidth(); col ++) {
				Rectangle rectangle = new Rectangle();
				rectangle.setWidth(20.0f); 
				rectangle.setHeight(20.0f); 
				rectangle.setFill(Color.hsb(10, 1, 1));	
				tileRow.getChildren().add(rectangle);
			}
			vBox.getChildren().add(tileRow);
		}
		return vBox;
	}
	
	private Scene CreateGameScene(GameEngine gameEngine) {

		testPane = new BorderPane();
		testPane.getChildren().add(GetBoard(gameEngine.GetBoardState()));
		testPane.setBottom(CreateControlPanel(gameEngine));
		Scene scene = new Scene(testPane,WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		return scene;
	}
	
	private VBox CreateControlPanel (GameEngine gameEngine) { //Should be an interface implementation
		VBox controlBox = new VBox();
		HBox controls1 = new HBox();
		HBox controls2 = new HBox();
		
		controlBox.getChildren().add(controls1);
		controlBox.getChildren().add(controls2);
		
		Button rotateButton = new Button("rotate");
		
		Button downButton = new Button("down");
		controls1.getChildren().add(rotateButton);
		controls1.getChildren().add(downButton);
		
		Button leftButton = new Button("left");
		
		Button rightButton = new Button("right");
		controls2.getChildren().add(leftButton);
		controls2.getChildren().add(rightButton);
		
		EventHandler<ActionEvent> controlHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() == rotateButton) {
					gameEngine.Action("rotate");
					testPane.getChildren().remove(testBox);
					testPane.getChildren().add(TestGetBoard(gameManager.GetBoardState()));
				}
				else if (event.getSource() == downButton) {
					gameEngine.Action("down");
				}
				else if (event.getSource() == leftButton) {
					gameEngine.Action("left");
				}
				else if (event.getSource() == rightButton) {
					gameEngine.Action("right");
				}
			}
		};

		rotateButton.setOnAction(controlHandler);
		downButton.setOnAction(controlHandler);
		leftButton.setOnAction(controlHandler);
		rotateButton.setOnAction(controlHandler);
		
		return controlBox;
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

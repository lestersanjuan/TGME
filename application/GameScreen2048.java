package application;

import GameModules.Board;
import GameModules.GameEngine;
import GameModules.GameManager;
import GameModules.ITile;
import GameModules.Board.Spot;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameScreen2048 extends GenericGameScreen{
	public GameScreen2048(Integer width, Integer height, GameManager gameManager, Button endGameHandler) {
		super (width, height, gameManager, endGameHandler);
	}
	
	
	protected VBox GetBoard(Board board) { //Interfaced, use the board state first before trying to get by coords. Rn it is for the columns game
		boardBox = new VBox();
		boardBox.setSpacing(5); 
		for (int row = 0; row < board.GetHeight(); row ++) {
			HBox tileRow = new HBox();
			tileRow.setSpacing(5); 
			for (int col = 0; col < board.GetWidth(); col ++) {
				ITile curTile = board.GetSpot(row, col).GetFirstTile();
				
				StackPane tilePane = new StackPane();
				Rectangle rectangle = new Rectangle();
				rectangle.setWidth(50.0f); 
				rectangle.setHeight(50.0f); 
				rectangle.setFill(Color.RED);

				Text value = new Text(curTile.GetValue());
				value.setFill(Color.BLACK);
				
				tilePane.getChildren().addAll(rectangle, value);
				
				
				tileRow.getChildren().add(tilePane);
			}
			boardBox.getChildren().add(tileRow);
		}
		
		return boardBox;
	}
	
	public Scene CreateGameScene(GameEngine gameEngine) {

		gamePane = new BorderPane();
		gamePane.getChildren().add(GetBoard(gameEngine.GetBoardState()));
		gamePane.setBottom(CreateControlPanel(gameEngine));
		Scene scene = new Scene(gamePane, this.width, this.height);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		return scene;
	}
	
	protected VBox CreateControlPanel (GameEngine gameEngine) { //Should be an interface implementation
		VBox controlBox = new VBox();
		HBox controls1 = new HBox();
		HBox controls2 = new HBox();
		
		controlBox.getChildren().add(controls1);
		controlBox.getChildren().add(controls2);
		
		Button upButton = new Button("up");
		
		Button downButton = new Button("down");
		controls1.getChildren().add(upButton);
		controls1.getChildren().add(downButton);
		
		Button leftButton = new Button("left");
		
		Button rightButton = new Button("right");
		controls2.getChildren().add(leftButton);
		controls2.getChildren().add(rightButton);
		
		EventHandler<ActionEvent> controlHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() == downButton) {
					gameEngine.Action("down");
				}
				else if (event.getSource() == upButton) {
					gameEngine.Action("up");
				}
				else if (event.getSource() == leftButton) {
					gameEngine.Action("left");
				}
				else if (event.getSource() == rightButton) {
					gameEngine.Action("right");
				}
				
				if (event.getSource().getClass() == Button.class) {
					CheckGameOver();
					UpdateGuiBoard();
				}
			}
		};

		upButton.setOnAction(controlHandler);
		downButton.setOnAction(controlHandler);
		leftButton.setOnAction(controlHandler);
		rightButton.setOnAction(controlHandler);
		
		return controlBox;
		
	}
}

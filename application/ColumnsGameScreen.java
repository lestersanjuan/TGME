package application;

import GameModules.Board;
import GameModules.Board.Spot;
import GameModules.GameEngine;
import GameModules.GameManager;
import GameModules.ITile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColumnsGameScreen extends GenericGameScreen{
	public ColumnsGameScreen(Integer width, Integer height, GameManager gameManager, Button endGameHandler) {
		super (width, height, gameManager, endGameHandler);
	}
	
	
	protected VBox GetBoard(Board board) { //Interfaced, use the board state first before trying to get by coords. Rn it is for the columns game
		boardBox = new VBox();
		boardBox.setSpacing(5); 
		for (int row = 0; row < board.GetHeight(); row ++) {
			HBox tileRow = new HBox();
			tileRow.setSpacing(5); 
			for (int col = 0; col < board.GetWidth(); col ++) {
				Spot curSpot = board.GetSpot(row, col);
				
				Rectangle rectangle = new Rectangle();
				rectangle.setWidth(20.0f); 
				rectangle.setHeight(20.0f); 
				if (curSpot.IsEmpty()) {

					rectangle.setFill(Color.GRAY);	
				}
				else {
					ITile curTile = curSpot.GetFirstTile();
					String value = curTile.GetValue();
					if (value.equals("O")) {
						rectangle.setFill(Color.ORANGE);
					}
					else if (value.equals("R")) {
						rectangle.setFill(Color.RED);
					}
					else if (value.equals("G")) {
						rectangle.setFill(Color.GRAY);
					}
					else if (value.equals("B")) {
						rectangle.setFill(Color.BLUE);
					}
					else if (value.equals("Y")) {
						rectangle.setFill(Color.YELLOW);
					}
					else if (value.equals("P")) {
						rectangle.setFill(Color.PURPLE);
					}
				}
				tileRow.getChildren().add(rectangle);
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
				if (event.getSource() == downButton) {
					gameEngine.Action("s");
				}
				else if (event.getSource() == leftButton) {
					gameEngine.Action("a");
				}
				else if (event.getSource() == rightButton) {
					gameEngine.Action("d");
				}
				
				if (event.getSource().getClass() == Button.class) {
					CheckGameOver();
					UpdateGuiBoard();
				}
			}
		};

		rotateButton.setOnAction(controlHandler);
		downButton.setOnAction(controlHandler);
		leftButton.setOnAction(controlHandler);
		rotateButton.setOnAction(controlHandler);
		
		return controlBox;
		
	}
}

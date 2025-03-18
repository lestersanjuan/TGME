package application;

import GameModules.Board;
import GameModules.GameEngine;
import GameModules.ITile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameScreen2048 extends GenericGameScreen{
	protected VBox GetBoard(Board board) { //Interfaced, use the board state first before trying to get by coords. Rn it is for the columns game
		boardBox = new VBox();
		boardBox.setSpacing(5); 
		for (int row = 0; row < board.GetHeight(); row ++) {
			HBox tileRow = new HBox();
			tileRow.setSpacing(5); 
			for (int col = 0; col < board.GetWidth(); col ++) {
				ITile curTile = board.GetSpot(row, col).GetFirstTile();
				
				StackPane tilePane = new StackPane();
				Float tileSideLength = 120.0f;
				
				Rectangle rectangle = new Rectangle();
				rectangle.setWidth(tileSideLength); 
				rectangle.setHeight(tileSideLength); 
				rectangle.setFill(Color.TAN);

				Text value = new Text(curTile.GetValue());
				value.setFill(Color.BLACK);
				value.setFont(Font.font(24));
				
				tilePane.getChildren().addAll(rectangle, value);
				
				
				tileRow.getChildren().add(tilePane);
			}
			boardBox.getChildren().add(tileRow);
		}
		
		return boardBox;
	}
	
	public void EditScene(Scene gameScene, GameEngine gameEngine) {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				if(key.getCode().equals(KeyCode.W)) {
					gameEngine.Action("up");
				}
				else if(key.getCode()==KeyCode.S) {
					gameEngine.Action("down");
				}
				else if(key.getCode()==KeyCode.A) {
					gameEngine.Action("left");
				}
				else if(key.getCode()==KeyCode.D) {
					gameEngine.Action("right");
				}

				CheckGameOver();
				UpdateGuiBoard();
			}
		});
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
		
		double buttonSideLength = 50;
		
		upButton.setMinWidth(buttonSideLength);
		upButton.setMinHeight(buttonSideLength);
		downButton.setMinWidth(buttonSideLength);
		downButton.setMinHeight(buttonSideLength);
		leftButton.setMinWidth(buttonSideLength);
		leftButton.setMinHeight(buttonSideLength);
		rightButton.setMinWidth(buttonSideLength);
		rightButton.setMinHeight(buttonSideLength);
		
		return controlBox;
		
	}
}

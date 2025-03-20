package application;

import GameModules.Board;
import GameModules.Board.Spot;
import GameModules.GameEngine;
import GameModules.ITile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColumnsGameScreen extends GenericGameScreen{
	protected VBox GetBoard(Board board) {
		boardBox = new VBox();
		boardBox.setSpacing(5); 
		for (int row = 0; row < board.GetHeight(); row ++) {
			HBox tileRow = new HBox();
			tileRow.setSpacing(5); 
			for (int col = 0; col < board.GetWidth(); col ++) {
				Spot curSpot = board.GetSpot(row, col);
				Float tileSideLength = 45.0f;
				
				Rectangle rectangle = new Rectangle();
				rectangle.setWidth(tileSideLength); 
				rectangle.setHeight(tileSideLength); 
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
						rectangle.setFill(Color.GREEN);
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

	public void EditScene(Scene scene, GameEngine gameEngine) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				if(key.getCode().equals(KeyCode.S)) {
					gameEngine.Action("enter");
				}
				else if(key.getCode()==KeyCode.W) {
					gameEngine.Action("s");
				}
				else if(key.getCode()==KeyCode.A) {
					gameEngine.Action("a");
				}
				else if(key.getCode()==KeyCode.D) {
					gameEngine.Action("d");
				}

				CheckGameOver();
				UpdateGuiBoard();
			}
		});
	}
	
	protected VBox CreateControlPanel (GameEngine gameEngine) {
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
					gameEngine.Action("s");
				}
				else if (event.getSource() == leftButton) {
					gameEngine.Action("a");
				}
				else if (event.getSource() == rightButton) {
					gameEngine.Action("d");
				}
				else if (event.getSource() == downButton) {
					gameEngine.Action("down");
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
		rightButton.setOnAction(controlHandler);

		double buttonSideLength = 50;
		rotateButton.setMinWidth(buttonSideLength);
		rotateButton.setMinHeight(buttonSideLength);
		downButton.setMinWidth(buttonSideLength);
		downButton.setMinHeight(buttonSideLength);
		leftButton.setMinWidth(buttonSideLength);
		leftButton.setMinHeight(buttonSideLength);
		rightButton.setMinWidth(buttonSideLength);
		rightButton.setMinHeight(buttonSideLength);
		
		return controlBox;
		
	}
}

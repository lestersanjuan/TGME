package application;

import java.util.HashMap;
import java.util.Map;

import GameModules.GameEngine;
import Games.ColumnsGameEngine2pINP;
import Games.TwentyFortyEightGameEngine;

public class programMain {

	public static void main(String[] args) {
		GameEngine columnsEngine = new ColumnsGameEngine2pINP(1);
		GameEngine gameEngine2048 = new TwentyFortyEightGameEngine(2);
		//GameEngine sampleGameEngine = new SampleGameEngine("Game2", 3);
		GenericGameScreen columnsScreen = new ColumnsGameScreen();
		GenericGameScreen screen2048 = new GameScreen2048();
		Map<GameEngine, GenericGameScreen> gameEngineScreenMap = new HashMap<GameEngine, GenericGameScreen>();
		gameEngineScreenMap.put(columnsEngine, columnsScreen);
		gameEngineScreenMap.put(gameEngine2048, screen2048);
		MainScreen.StartScreen(gameEngineScreenMap, 600, 800);
	}

}

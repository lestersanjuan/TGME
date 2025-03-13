package GameEngine;
import java.util.HashMap;
import java.util.Map;

public abstract class User {
	protected String name;
	protected Map<String, String> data;
	protected Integer userId;
	
	public User(String name) {
		this.name = name;
		this.data = new HashMap<String, String>();
	}
	
	public String ListScore(String gameId) {
		return this.data.get(gameId);
	}
	
	public void SetScore(String gameId, String score) {
		this.data.put(gameId, score);
	}
	
	public String GetName() {
		return this.name;
	}
}
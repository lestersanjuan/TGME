import java.util.HashMap;
import java.util.Map;

public abstract class User {
	private String name;
	private Map<String, String> data;
	
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
}

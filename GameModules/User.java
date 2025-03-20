package GameModules;
import java.util.HashMap;
import java.util.Map;

public class User {
	protected String name;
	protected Map<String, String> data;
	protected Integer userId;
	
	public User(String name, Integer id) {
		this.name = name;
		this.userId = id;
		this.data = new HashMap<String, String>();
		this.data.put("LastGame", "You haven't played a game yet!");
	}
	
	public String ListScore(String gameId) {
		return this.data.get(gameId);
	}
	
	public void SetScore(String gameId, String score) {
		this.data.put(gameId, score);
	}
	
	public void SetData(String key, String data) {
		this.data.put(key, data);
	}
	
	public String GetData(String key) {
		return this.data.get(key);
	}
	
	public String GetName() {
		return this.name;
	}
	
	public Integer GetUserId() {
		return this.userId;
	}
}

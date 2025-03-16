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
	
	public Integer GetUserId() {
		return this.userId;
	}
}

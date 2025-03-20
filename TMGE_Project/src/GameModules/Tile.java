package GameModules;
import java.util.HashMap;
import java.util.Map;

public class Tile implements ITile{
	private String currentValue;
	private Map<String, String> data;
	
	public Tile (String value) {
		this.currentValue = value;
		this.data = new HashMap<String, String>();
	}

	@Override
	public String GetValue() {
		return this.currentValue;
	}

	@Override
	public void SetValue(String value) {
		this.currentValue = value;
	}

	@Override
	public String GetData(String key) {
		return this.data.get(key);
	}

	@Override
	public void SetData(String key, String value) {
		this.data.put(key, value);
	}

	@Override
	public void RemoveData(String key) {
		this.data.remove(key);
	}
}

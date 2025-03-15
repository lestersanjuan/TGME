package GameModules;
import java.util.Map;

public interface ITile {
	// public List<Integer> GetPosition();
	// public void SetPosition(List<Integer> coords);
	public String GetValue();
	public void SetValue(String value);
	public String GetData(String key);
	public void SetData(String key, String value);
	public void RemoveData(String key);
}

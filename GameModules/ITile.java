package GameModules;

public interface ITile {
	public String GetValue();
	public void SetValue(String value);
	public String GetData(String key);
	public void SetData(String key, String value);
	public void RemoveData(String key);
}

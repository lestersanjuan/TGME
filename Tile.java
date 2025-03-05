import java.util.Map;

public class Tile implements ITile{
	private String value;

	@Override
	public String GetValue() {
		return this.value;
	}

	@Override
	public void SetValue(String value) {
		this.value = value;
	}
}

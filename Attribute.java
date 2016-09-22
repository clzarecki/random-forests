import java.util.ArrayList;
import java.util.List;


public class Attribute {
	
	public String name;
	public boolean isReal;
	public List<String> values;
	
	/**
	 * Real attribute constructor
	 * @param name attribute name
	 */
	public Attribute(String name) {
		super();
		this.name = name;
		this.isReal = true;
		this.values = new ArrayList<String>();
	}
	
	/**
	 * Discrete attribute constructor
	 * @param name attribute name
	 * @param attrValues values the attribute takes
	 */
	public Attribute(String name, List<String> attrValues) {
		super();
		this.name = name;
		this.isReal = false;
		this.values = attrValues;
	}
	
	public boolean equals(Attribute attr) {
		return name.equals(attr.name);
	}

}

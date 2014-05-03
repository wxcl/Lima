package cpnv.jav1.lima;

public class Gear extends Article {

	// CPNV Gear for the Lima App
	// Author:	X. Carrel
	// Date: May 2014

	private String _size; // SMXL text format "XL", "M", ...
	
	// ================================== Constructors ==========================================
	
	// Default constructor
	public Gear() {
		super();
		_size = "U"; // Default value "Unique"
	}
	
	public Gear(String name, int number, String resp, String supplier, double price, double VAT, String size) {
		super (name, number, resp, supplier, price, VAT);
		if ((size.length() > 3) || (size.length() < 1)) // Invalid size code
			_size = "U"; // Default value "Unique"
		else
			_size = size;
	}
	
	public Gear(Gear original){
		super((Article)original);
		_size = original.getSize();
	}

	// ================================== Getter / Setter ==========================================
	
	public String getSize() {
		return _size;
	}

	public void setSize(String size) {
		if ((size.length() > 3) || (size.length() < 1)) // Invalid size code
			_size = "U"; // Default value "Unique"
		else
			_size = size;
	}

	// ================================== Other public methods ==========================================
	
	public String dump() {
		return super.dump()+"-"+_size;
	}

}

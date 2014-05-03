package cpnv.jav1.lima;

public class Book extends Article {

	// CPNV Books for the Lima App
	// Author:	X. Carrel
	// Date: May 2014
	
	private long _isbn; // ISBN number, must be > 1000000 (otherwise set to 0)
	
	// ================================== Constructors ==========================================
	
	// Default constructor
	public Book() {
		super();
		_isbn = 0;
	}
	
	// Main constructor
	public Book(String name, int number, String resp, String supplier, double price, double VAT, long isbn) {
		super (name, number, resp, supplier, price, VAT);
		if (isbn < 100000) // ISBN number, must be > 1000000 (otherwise set to 0)
			_isbn = 0;
		else
			_isbn = isbn;
	}
	
	// Duplication constructor
	public Book(Book original){
		super((Article)original);
		_isbn = original.getISBN();
	}

	// ================================== Getter / Setter ==========================================
	
	public long getISBN() {
		return _isbn;
	}

	public void setISBN(long isbn) {
		if (isbn < 100000) // ISBN number, must be > 1000000 (otherwise set to 0)
			_isbn = 0;
		else
			_isbn = isbn;
	}
	
	// ================================== Other public methods ==========================================
	
	public String dump() {
		return super.dump()+"-"+_isbn;
	}

}

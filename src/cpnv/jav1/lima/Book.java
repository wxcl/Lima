package cpnv.jav1.lima;

public class Book extends Article {
	
	private int _isbn;
	
	public Book()
	{
		super();
		_isbn = -1;
	}
	
	public int getISBN()
	{
		return _isbn;
	}

	public void setISBN (int isbn)
	{
		_isbn = isbn;
	}
	
	public String dump()
	{
		return super.dump() + "\nISBN:" + String.valueOf(_isbn);
	}
}

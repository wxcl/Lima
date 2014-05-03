package cpnv.jav1.lima;

import java.lang.reflect.Field;

public class Article {

	// CPNV Article for the Lima App
	// Author:	X. Carrel
	// Date: May 2014
	
	private String _name;
	private int _number; // CPNV Article number
	private String _resp; // Person in charge of this article
	private String _supplier;
	private double _price;
	private double _VAT; // = swiss "TVA"
	
	// ================================== Constructors ==========================================
	
	public Article ()
	// Default constructor
	{
		_name = "(à définir)";
		_number = 0;
		_resp = "(à définir)";
		_supplier = "(à définir)";
		_price = 0.0;
		_VAT = 0.0;
	}
	
	public Article (String name, int number, String resp, String supplier, double price, double VAT)
	// Main constructor
	{
		if (name.length() < 3) // All names must be at least 3 characters long
			_name = "(à définir)";
		else
			_name = name;
		
		if (number < 0)
			_number = 0;
		else
			_number = number;
		
		if (resp.length() < 3) // All names must be at least 3 characters long
			_resp = "(à définir)";
		else
			_resp = resp;
		
		if (supplier.length() < 3) // All names must be at least 3 characters long
			_supplier = "(à définir)";
		else
			_supplier = supplier;
		
		if (price < 0.0)
			_price = 0;
		else
			_price = price;

		if (VAT < 0.0)
			_VAT = 0;
		else
			_VAT = VAT;
	}
	
	// Duplication constructor
	public Article (Article original)
	{
		_name = original.getName();
		_number = original.getNumber()+1; // Pick the next number
		_resp = original.getResp();
		_supplier = original.getSupplier();
		_price = original.getPrice();
		_VAT = original.getVAT();
	}
	
	// ================================== Getter / Setter ==========================================
	
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		if (name.length() < 3) // All names must be at least 3 characters long
			_name = "(à définir)";
		else
			_name = name;
	}

	public int getNumber()
	{
		return _number;
	}

	public void setNumber(int number)
	{
		if (number < 0) // All numbers must be positive
			_number = 0;
		else
			_number = number;
	}

	public String getResp()
	{
		return _resp;
	}

	public void setResp(String resp)
	{
		if (resp.length() < 3) // All names must be at least 3 characters long
			_resp = "(à définir)";
		else
			_resp = resp;
	}
	
	public String getSupplier() {
		return _supplier;
	}

	public void setSupplier(String supplier) {
		if (supplier.length() < 3) // All names must be at least 3 characters long
			_supplier = "(à définir)";
		else
			_supplier = supplier;
	}

	public double getPrice() {
		return _price;
	}

	public void setPrice(double price) {
		if (price < 0.0) // All numbers must be positive
			_price = 0;
		else
			_price = price;
	}

	public double getVAT() {
		return _VAT;
	}

	public void setVAT(double VAT) {
		if (VAT < 0.0) // All numbers must be positive
			_VAT = 0;
		else
			_VAT = VAT;
	}

	// ================================== Other public methods ==========================================
	
	public String dump()
	// Dumps content according to requested format
	{
		return _name+"-"+_number+"-"+_resp+"-"+_supplier+"-"+_price+"-"+_VAT;
	}
}

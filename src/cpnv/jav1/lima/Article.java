package cpnv.jav1.lima;

public class Article {
	int _number;
	String _resp;
	
	public Article ()
	{
		_number = -1;
		_resp = "?";
	}
	
	public Article (int number, String resp)
	{
		_number = number;
		_resp = resp;
	}
	
	public int getNumber()
	{
		return _number;
	}

	public void setNumber(int number)
	{
		_number = number;
	}

	public String getResp()
	{
		return _resp;
	}

	public void setResp(String resp)
	{
		_resp = resp;
	}
	
	public String dump()
	{
		return "Numro d'article: "+_number+"\nResponsable:"+_resp;
	}
}

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.net.URLEncoder;

/*
 Dados[Nome] = "Joaoddf"
 Dados[Idade] = "23"
 * 
 * 
 */
public class ObjectoGeral {

	private Map<String,String> Dados;
	private String Nome;
	
	//---------------------------------
	public ObjectoGeral()
	{
		Nome = "??";
		Dados = new HashMap<String,String>();
	}
	//---------------------------------
	public ObjectoGeral(String _nome)
	{
		Nome = _nome;
		Dados = new HashMap<String,String>();
	}
	//---------------------------------
	public void Add(String key, String value)
	{
		Dados.put(key, value);
	}	
	//---------------------------------
	public void SetNome(String _nome)
	{
		Nome = _nome;
	}
	//-------------------------------
	public String GetNome()
	{
		return Nome;
	}
	//---------------------------------
	public Map<String,String> GetMap()
	{
		return Dados;
	}
	//---------------------------------
	public String Get(String key)
	{
		if (Dados.containsKey(key))
			return Dados.get(key);
		return null;
	}
	//---------------------------------
	public void Mostrar()
	{
		Uteis.MSG("NOME-->"+Nome);
		Set<String> Lista_Keys = Dados.keySet();
		for (String key : Lista_Keys)
			Uteis.MSG("\t"+key+"-->["+Dados.get(key)+"]");
	}
	//---------------------------------
	public String ToUrl()
	{
		StringBuilder result = new StringBuilder();
	    boolean first = true;
	    for(Map.Entry<String, String> entry : Dados.entrySet())
	    {
	        if (first)
	             first = false;
	        else
	             result.append("&");
	        try 
	        {
	             result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	             result.append("=");
	             result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

	        } 
	        catch (UnsupportedEncodingException e) 
	        {
	                e.printStackTrace();
	        }
	    }
	    return result.toString();	
	}
	//---------------------------------
	public String ToXML()
	{
		//return "UM DIA FAZER!";
		String XML = "";
		XML += "<DADOS>";
			XML += "<REGISTO>";
				XML += "<DATA>"+"2020-02-02"+"</DATA>";
				XML += "<NCAR>"+"4"+"</NCAR>";
			XML += "</REGISTO>";

		XML += "</DADOS>";
		return XML;
	}
	//---------------------------------
	
	
	
	
	
	
	
	
	
	
	//-------------------------------	
	public void ParserDoKortiniG(Map<String,String> DadosObjGeral)
	
	{	
		String chave,valor;
		for (Map.Entry<String,String> entry : DadosObjGeral.entrySet())   
		{
			chave = entry.getKey();
			valor = entry.getValue();
			//fazer o parser para injetar no mapa bidimensional!
			//System.out.println("/n" +chave + valor);
		}
	}
	
	//---------------------------------
	
}

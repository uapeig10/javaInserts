import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.json.JSONArray;
import org.json.JSONObject;

public class API_Servidor {

	String Base_Url;
	private List<ObjectoGeral> Lista_Last_Values;
	private List<ObjectoGeral> Lista_radars;
	//private List<ObjectoGeral> Lista_Parking;
	private List<ObjectoGeral> Lista_radars_events_region;
	private List<ObjectoGeral> Lista_radars_events_measurement;
	 
	//---------------------------------
	public API_Servidor(String _base_url)
	{
		Base_Url = _base_url;
	//	Lista_Last_Values = new ArrayList<ObjectoGeral>();
		Lista_radars = new ArrayList<ObjectoGeral>();
		Lista_radars_events_region = new ArrayList<ObjectoGeral>();
	}
	//---------------------------------
	public List<ObjectoGeral> Get_Lista_radars() 
	{
		// TODO Auto-generated method stub
		return Lista_radars;
	}
	//---------------------------------
	public List<ObjectoGeral> Get_Lista_Last_Values()
	{
		return Lista_Last_Values;
	}
	
	//---------------------------------
	
	public List<ObjectoGeral> Get_Lista_radars_events_region()
	{
		return Lista_radars_events_region;
	}
	
	//---------------------------------
	private String Executar(String Servico, ObjectoGeral Parametros)
	{
		//String Parametros = "?timeInterval=5d&order=asc&limit=10";
		//URL url = new URL(Servidor+"/parking"+Parametros);  // https://pasmo.es.av.it.pt/api/parking/latestValues
		//URL url = new URL(Servidor+"/parking/301168"+Parametros);  // https://pasmo.es.av.it.pt/api/parking/latestValues
	
		HttpURLConnection con = null;
		BufferedReader in = null;
		StringBuffer Resposta = null;
		try
		{
			URL url;
			if (Parametros == null)
				url = new URL(Base_Url+Servico);  // https://pasmo.es.av.it.pt/api/parking/latestValues
			else
			{
				String parm_url = Parametros.ToUrl();
				Uteis.MSG("parm_url = " + parm_url);
				url = new URL(Base_Url+Servico+"?"+parm_url);  // https://pasmo.es.av.it.pt/api/parking/latestValues
				System.out.println(url);
			}
			
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			Resposta = new StringBuffer();
			while ((inputLine = in.readLine()) != null) 
			{
				Resposta.append(inputLine);
				//    Uteis.MSG(inputLine);
			}
		}
		catch(Exception Ex)
		{
			Uteis.MSG("ERRO= "+Ex.getMessage());
		}
		finally
		{
			try { if (in != null)  in.close();} catch(Exception Ex) {  }
			try { if (con != null) con.disconnect(); } catch(Exception Ex) {  }
		}
		return Resposta.toString();
	}	
	//---------------------------------
	public boolean ParserJSON_latestValues(String json)
	{
		Uteis.MSG_Debug("\tINICIO:"+Thread.currentThread().getStackTrace()[1].toString()); 
		if (json == null) return false;
		try
		{
			JSONArray jsonarray = new JSONArray(json);
			int TAM = jsonarray.length();
			Uteis.MSG("TAM = " + TAM);
			Uteis.MSG(json);
			Lista_Last_Values.clear();
			for (int i = 0; i < TAM; i++)
			{
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				String sensor_id = ""+jsonobject.getInt("sensor_id");				
				String status = ""+jsonobject.getInt("status");				
				String timestamp = jsonobject.getString("timestamp");
				ObjectoGeral Obj = new ObjectoGeral("Sensor-"+sensor_id);
				Obj.Add("sensor_id", sensor_id);
				Obj.Add("status", status);
				Obj.Add("timestamp", timestamp);
				Obj.Mostrar();		
				Lista_Last_Values.add(Obj);
			}		
		}
		catch(Exception Ex)
		{
			Uteis.MSG_Debug("ERRO Parser: " + Ex.getMessage());
			return false;
		}
		return true;
	}
	//-----------------------------------------------------
	
	//---------------------------------
	public String Executar_latestValues()
	{
		Uteis.MSG_Debug(Thread.currentThread().getStackTrace()[1].toString()); 
		String RES = Executar("/parking/latestValues", null);	
		boolean res_parser = ParserJSON_latestValues(RES);
		if (res_parser)
			Uteis.MSG("Parser com Sucesso!");
		else
			Uteis.MSG("Parser INSUCESSO!");
		return RES;	
	}
	//---------------------------------
	public String Executar_parking()
	{
		Uteis.MSG_Debug(Thread.currentThread().getStackTrace()[1].toString()); 
		ObjectoGeral Parametros = new ObjectoGeral();
		Parametros.Add("timeInterval", "5m");
		Parametros.Add("order", "asc");
		Parametros.Add("limit", "10");
		//Parametros.Mostrar();
		String RES = Executar("/parking", Parametros);		
		return RES;	
	}
	//---------------------------------
	public String Executar_parking_sensor_ID(String sensorID)
	{
		Uteis.MSG_Debug(Thread.currentThread().getStackTrace()[1].toString()); 
		ObjectoGeral Parametros = new ObjectoGeral();
		Parametros.Add("sensorID", sensorID);
		Parametros.Add("timeInterval", "5d");
		Parametros.Add("order", "asc");
		Parametros.Add("limit", "10");
		//Parametros.Mostrar();
		String RES = Executar("/parking/"+sensorID, Parametros);		
		return RES;	
	}
	//---------------------------------
	public String Executar_availableSensors()
	{
		Uteis.MSG_Debug(Thread.currentThread().getStackTrace()[1].toString()); 
		ObjectoGeral Parametros = new ObjectoGeral();
		Parametros.Add("state", "active");
		//Parametros.Add("measurent", "[speed:80][class:1]");
		//Parametros.Mostrar();
		String RES = Executar("/parking/availableSensors", Parametros);		
		return RES;	
	}
	//--------------------------------
	public boolean ParserJSON_radars(String json)
	{
		Uteis.MSG_Debug("\tINICIO:"+Thread.currentThread().getStackTrace()[1].toString()); 
		if (json == null) return false;
		try
		{
			Uteis.MSG("ParserJSON_radars= "+json);
			JSONArray jsonarray = new JSONArray(json);
			int TAM = jsonarray.length();
			
			Uteis.MSG("TAM = " + TAM);
			Uteis.MSG("Entrou");
			Lista_radars.clear();
			
			for (int i = 0; i < TAM; i++)
			{	
				Uteis.MSG("Entrou1");
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				
				
				String radarID = ""+jsonobject.getInt("radarID");	
				//Uteis.MSG("radar ID ="+ radarID);
				
				String objID = ""+jsonobject.getInt("objID");				
				String radarlat = ""+jsonobject.getFloat("radarlat");
				String radarlon = ""+jsonobject.getFloat("radarlon");
				String radarazm = ""+jsonobject.getInt("radarazm");
				String timestamp = ""+jsonobject.getString("timestamp");
				String xSpeed = ""+jsonobject.getInt("xSpeed");
				String ySpeed = ""+jsonobject.getInt("ySpeed");
				String xPoint = ""+jsonobject.getInt("xPoint");
				String yPoint = ""+jsonobject.getInt("yPoint");
				String oLength = ""+jsonobject.getInt("oLength");
				
				
				ObjectoGeral Obj = new ObjectoGeral("radar"+ radarID);
				
				Obj.Add("radarID", radarID);	
				
				Obj.Add("objID", objID);
				Obj.Add("radarlat", radarlat);
				Obj.Add("radarlon", radarlon);
				Obj.Add("radarazm", radarazm);
				Obj.Add("timestamp", timestamp);
				Obj.Add("xSpeed", xSpeed);
				Obj.Add("ySpeed", ySpeed);
				Obj.Add("xPoint", xPoint);
				Obj.Add("yPoint", yPoint);
				Obj.Add("oLength",oLength);
				
				Obj.Mostrar();		
				Lista_radars.add(Obj);
			}		
		}
		catch(Exception Ex)
		{
			Uteis.MSG_Debug("ParserJSON_radars: ERRO Parser: " + Ex.getMessage());
			return false;
		}
		return true;
	}
//---------------------------------------------------	
	public String Executar_radars()
	{
		Uteis.MSG_Debug(Thread.currentThread().getStackTrace()[1].toString()); 
		ObjectoGeral Parametros = new ObjectoGeral();
		Parametros.Add("timeInterval", "5d");
		Parametros.Add("order","asc");
		//Parametros.Mostrar();
		Parametros.Add("limit", "100");
		
		String RES = Executar("/radars", Parametros);	
		//Uteis.MSG("Executar_radars:RES =["+ RES + "]");
		boolean res_parser = ParserJSON_radars(RES);
		Uteis.MSG(""+res_parser);
		if (res_parser)
			Uteis.MSG("Executar_radars: Parser com Sucesso!");
		else
			Uteis.MSG("Executar_radars: Parser INSUCESSO!");
		return RES;	
	}
//-------------------------------------------
/*	public boolean ParserJSON_radars_events_region(String json)
	{
		Uteis.MSG_Debug("\tINICIO:"+Thread.currentThread().getStackTrace()[1].toString()); 
		if (json == null) return false;
		try
		{
			Uteis.MSG("ParserJSON_radars_events_region= "+json);
			JSONArray jsonarray = new JSONArray(json);
			int TAM = jsonarray.length();
			
			Uteis.MSG("TAM = " + TAM);
			//Uteis.MSG("Entrou");
			Lista_radars_events_region.clear();
			
			for (int i = 0; i < TAM; i++)
			{	
				//Uteis.MSG("Entrou1");
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				
				
				
				String number_of_cars = ""+jsonobject.getInt("number_of_cars");	
				String timestamp = ""+jsonobject.getString("timestamp");	
			
				
				
				ObjectoGeral Obj = new ObjectoGeral("radar");
				
				Obj.Add("number_of_cars",number_of_cars);
				Obj.Add("timestamp", timestamp);
				
				Obj.Mostrar();		
				

			}		
		}
		catch(Exception Ex)
		{
			Uteis.MSG_Debug("ParserJSON_radars_events_region: ERRO Parser: " + Ex.getMessage());
			return false;
		}
		return true;
	}*/
	//-------------------------------------------------
	public boolean ParserJSON_radars_events_region(String region,String json, ObjectoDadosBid ODB,String event)
	{
		Uteis.MSG_Debug("\tINICIO:"+Thread.currentThread().getStackTrace()[1].toString()); 
		if (json == null) return false;
		System.out.println(event + region);
		try
		{
			Uteis.MSG("ParserJSON_radars_events_region= "+json);
			JSONArray jsonarray = new JSONArray(json);
			int TAM = jsonarray.length();
			
			Uteis.MSG("TAM = " + TAM);
			//Uteis.MSG("Entrou");
			Lista_radars_events_region.clear();
			
			//Map<int,int> Dados  = new HashMap<String,String>();
			   Iterator<Object> iterator = jsonarray.iterator();
			   String timestamp = "oo";
			   int number_of_cars = 0;
			   int contador = 0;
		        while (iterator.hasNext()) 
		        {	
		        	ObjectoGeral ObjT = new ObjectoGeral("radars_events_time");
		        	//ObjectoGeral ObjC = new ObjectoGeral("radars_events_cars");
		            JSONObject jsonObject = (JSONObject) iterator.next();
		            Set<String> l_key = jsonObject.keySet();
		            for (String key : l_key) 
		            {	
		            	//System.out.println("contador = " + contador);
		            	//contador++;
		            	if(key.compareTo("timestamp") == 0) 
		            		{
	            				timestamp = jsonObject.getString(key).toString();
		            			//System.out.printf("vem ai um timestamp:" + timestamp+"\n");
	            				//secalhar na usar key e guardar numa variável?
	            				//ObjT.Add(key, timestamp);
	            				//ObjT.Mostrar();
	            				Lista_radars_events_region.add(ObjT);
		            		}
		            	
		            	if(key.compareTo("number_of_cars") == 0) 
		                	{	
		            			//System.out.printf("vem ai carro\n %d",jsonObject.getInt(key));
		                		number_of_cars = jsonObject.getInt(key);
		                	//	System.out.println(number_of_cars);
		                	//	System.out.printf("vem ai ncarros:" + number_of_cars+"\n");  
		                		//ObjT.Add(key, ""+number_of_cars);
		                		//ObjT.Mostrar();
		                		Lista_radars_events_region.add(ObjT);
		                	}
		            	
		            }
		          //  System.out.println(timestamp + number_of_cars);
		            if(event == "cars_in")
		            	ODB.Add(region,timestamp, number_of_cars);
		            if(event=="cars_out")
		            	ODB.Add1(region,timestamp,number_of_cars);	
		           
		        }
		      //  ODB.Mostrar();

		    }
		catch(Exception Ex)
		{
			Uteis.MSG_Debug("ParserJSON_radars_events_region: ERRO Parser: " + Ex.getMessage());
			return false;
		}
		return true;
	}
	//----------------------------------------------------
	
	//----------------------------------------------------
	public String Executar_radars_events_region(String region, String event) 
	{
		Uteis.MSG_Debug(Thread.currentThread().getStackTrace()[1].toString()); 
		ObjectoGeral Parametros = new ObjectoGeral();
		ObjectoDadosBid ODB = new ObjectoDadosBid(5);
		
		
		
		Parametros.Add("initialDate", "2020-05-26T00:00:00Z");
		Parametros.Add("finalDate", "2020-05-27T00:00:00Z");
		Parametros.Add("groupby", "300");
		String RES = Executar("/radars/events/"+region+"/"+event, Parametros);
		
		
		//Uteis.MSG("Executar_radars:RES =["+ RES + "]");
		boolean res_parser = ParserJSON_radars_events_region(region,RES,ODB,event);
		Uteis.MSG(""+res_parser);
		//ODB.SendDataPHP(10,1);
		if (res_parser)
			Uteis.MSG("Executar_radars: Parser com Sucesso!");
		else
			Uteis.MSG("Executar_radars: Parser INSUCESSO!");
		return RES;	
		
	}
//---------------------------------------
	public String Executar_radars_events_measurements(String region,String acao) 
	{
		Uteis.MSG_Debug(Thread.currentThread().getStackTrace()[1].toString()); 
		ObjectoGeral Parametros = new ObjectoGeral();
		ObjectoDadosBid ODB = new ObjectoDadosBid(5);
		
		
		
		Parametros.Add("initialDate", "2020-05-26T00:00:00Z");
		Parametros.Add("finalDate", "2020-05-27T00:00:00Z");
		Parametros.Add("measurement", "class");
		String RES = Executar("/radars/"+region+"/"+acao,Parametros);
	
		
		
		
		//Uteis.MSG("Executar_radars:RES =["+ RES + "]");
		boolean res_parser = ParserJSON_radars_events_measurement(region,RES,ODB);
		Uteis.MSG(""+res_parser);
		//ODB.SendDataPHP(10,1);
		if (res_parser)
			Uteis.MSG("Executar_radars: Parser com Sucesso!");
		else
			Uteis.MSG("Executar_radars: Parser INSUCESSO!");
		return RES;	
		
	}
	
	public boolean ParserJSON_radars_events_measurement(String region,String json, ObjectoDadosBid ODB)
	{
		Uteis.MSG_Debug("\tINICIO:"+Thread.currentThread().getStackTrace()[1].toString()); 
		if (json == null) return false;
		System.out.println(region);
		try
		{
			Uteis.MSG("ParserJSON_radars_events_measurement= "+json);
			JSONArray jsonarray = new JSONArray(json);
			JSONArray jsonA;
			JSONObject x ;
			int TAM = jsonarray.length();
			int contador = 0;
			int TAMmeasures= 0;
			
			Uteis.MSG("TAM = " + TAM);
			Iterator<Object> iterator = jsonarray.iterator();
			Map<String, ObjectoGeral> Dados_FRANK = new HashMap<String, ObjectoGeral>();
	 		while(iterator.hasNext())
			{	
	 			Dados_FRANK.clear();
				String timestamp="?";
				JSONObject measurements= new JSONObject();
				String measurement = "oo";
				String radar="oo";
				String teste ="";
				int teste1= 0;
				//System.out.println("contador=" + ++contador);
	            JSONObject jsonObject = (JSONObject) iterator.next();
	            ObjectoGeral Obj = null;
	            Set<String> l_key = jsonObject.keySet();
	            
	            for (String key : l_key) 
	            {	
	            	//System.out.println("key="+ key );
	            	//System.out.println("contador=" + ++contador);
	            	if(key.compareTo("timestamp")==0) 
	            	{
	            		timestamp= jsonObject.getString("timestamp");
	            		//System.out.println("timestamp="+ timestamp );
	            		if (Obj != null)
	            		{
	            			Obj.SetNome(region);
	            			Dados_FRANK.put(timestamp, Obj); // Adicionma para o Caldeiro!
	            		}
	            	}	
	            	if(key.compareTo("device")==0)
	            		{
	            			radar= jsonObject.getString("device");
	            			//System.out.println("radar =" + radar );	            			
	            		}
	            	if(key.compareTo("measurement")==0) 
	            		{
	            			measurements=jsonObject.getJSONObject("measurement");
	            			Obj = new ObjectoGeral();
	            			Set<String> ListaKeys = measurements.keySet();
		            		for(String KeyMeas :ListaKeys)
		            		{
		            			teste1 = measurements.getInt(KeyMeas);
		            			//System.out.println(KeyMeas+ "=" +teste1 );
		            			//System.out.println("radar=" + radar+"timestamp=" + timestamp + KeyMeas + "=" +teste1);
		            			Obj.Add(KeyMeas, ""+teste1);
		            			//SendToPHP(region,KeyMeas,teste1); 
		            			//SendToPHP(radar,timestamp,KeyMeas,teste1);
		            			
		            			//System.out.println("aqui" +Dados_FRANK.);
		            		}
		            		
            		
	            		}
	            	SendPHPDadosFranK(Dados_FRANK);
	            		//System.out.println("radar=" + radar+"timestamp=" + timestamp );
	            }            	
	            	
	            System.out.println("//------------------------");	
		 		//MostrarDadosFrank(Dados_FRANK);
		 	}
	 		TesteDadosFranK(Dados_FRANK);
	            

	            //System.out.println("//------------------------");
	            	//if(key.compareTo("measurement")==0) measurement=jsonObject.getString("measurement");
	            	
	            	//System.out.println(radar + "\n" + measurements+"\n"+ timestamp+"\n");
	            	
	        
			    
			//Uteis.MSG("Entrou");
		//	Lista_radars_events_region.clear();
			
			//Map<int,int> Dados  = new HashMap<String,String>();
			  
		        	
		        
			  /* for(int i=0; i<jsonarray.length(); i++){
				    JSONObject obj = jsonarray.getJSONObject(i);
				    
				    System.out.println("\n"+obj);
				}*/
		            
		          //  System.out.println(timestamp + number_of_cars);
		            //	ODB.AddMeas(region,timestamp,measurement);	
		           
		        
		            	//ODB.Mostrar();

		    }
		catch(Exception Ex)
		{
			Uteis.MSG_Debug("ParserJSON_radars_events_measerument: ERRO Parser: " + Ex.getMessage());
			return false;
		}
		return true;
	}
	//---------------
	private long Converter(String timestamp)
	{
		Instant teste = Instant.parse( timestamp );
		long sec = teste.getEpochSecond();
		//System.out.println("teste da data:" + sec );
		return sec;
	}
	//--------------------
	public void SendToPHP(String region,String timestamp,String Key,int value)
	{	
		try {
			URL url = new URL("http://fjunior.f2mobile.eu/cmd_bdados2.php");
			long longsec = Converter(timestamp);
			String postData = "region=" +region+ "&timestamp=" +longsec+"&type=" +Key+ "&value=" +value;
			
			//String postData = "value=1";
			System.out.println(postData);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", Integer.toString(postData.length()));
			conn.setUseCaches(false);

			try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
				dos.writeBytes(postData);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//----------------
	public void MostrarDadosFrank(Map<String, ObjectoGeral> Dados)
	{
		Set<String> Lista_Keys = Dados.keySet();
		for (String key : Lista_Keys)
		{	
			System.out.println(key);
			ObjectoGeral OBJ = Dados.get(key);
			//Uteis.MSG(key);
			
			OBJ.Mostrar();
		}
	}
	//------------------------------
	public void SendPHPDadosFranK(Map<String, ObjectoGeral> Dados1)
	{
		Set<String> Lista_Keys = Dados1.keySet();
		String timestamp ="?";
		String type ="?";
		String radar= "?";
		int value = -1;
		for (String key : Lista_Keys)	
		{	
			 timestamp=key;
			 ObjectoGeral Obj = new ObjectoGeral();
			 Obj = Dados1.get(key);
			 Set<String> Lista_Keys1 = Obj.GetMap().keySet();
			 for(String Key :Lista_Keys1)
			 {	
				radar = Obj.GetNome();
				type = Key;
				value = Integer.parseInt(Obj.Get(Key));
				SendToPHP(radar,timestamp,type,value);
			 }
		}
	}
//-------------------------
	public void TesteDadosFranK(Map<String, ObjectoGeral> Dados1)
	{
		Set<String> Lista_Keys = Dados1.keySet();
		String timestamp ="?";
		String type ="?";
		String radar= "?";
		int bikes = 0;
		int cars = 0;
		int trucks = 0;
		int value = -1;
		
		for (String key : Lista_Keys)	
		{	
			 timestamp=key;
			 ObjectoGeral Obj = new ObjectoGeral();
			 Obj = Dados1.get(key);
			 Set<String> Lista_Keys1 = Obj.GetMap().keySet();
			 for(String Key :Lista_Keys1)
			 {	
				radar = Obj.GetNome();
				type = Key;
				value = Integer.parseInt(Obj.Get(Key));
				if(type=="bikes") bikes += value;
				if(type=="cars") cars += value;
				if(type=="truck") trucks += value;
				System.out.println("bikes=" + bikes+"cars=" + cars+ "trucks=" +trucks );
			 }
		}
	}
	
	public void DadosFrank(Map<String, ObjectoGeral> Dados)
	{
		Set<String> Lista_Keys = Dados.keySet();
		for (String key : Lista_Keys)
		{
			ObjectoGeral OBJ = Dados.get(key);
			Uteis.MSG(key);
			OBJ.Mostrar();
		}
	}
	
	
	
	
	
	
	public String Executar_PHP()
	{	
		//String Parametros = "?timeInterval=5d&order=asc&limit=10";
		//URL url = new URL(Servidor+"/parking"+Parametros);  // https://pasmo.es.av.it.pt/api/parking/latestValues
		//URL url = new URL(Servidor+"/parking/301168"+Parametros);  // https://pasmo.es.av.it.pt/api/parking/latestValues
	
		HttpURLConnection con = null;
		BufferedReader in = null;
		StringBuffer Resposta = null;
		try
		{
			URL url;
			/*if (Parametros == null)
				url = new URL(Base_Url+Servico);  // https://pasmo.es.av.it.pt/api/parking/latestValues
			else
			{
				String parm_url = Parametros.ToUrl();
				Uteis.MSG("parm_url = " + parm_url);
				url = new URL(Base_Url+Servico+"?"+parm_url);  // https://pasmo.es.av.it.pt/api/parking/latestValues
			}*/
			url = new URL("http://fjunior.f2mobile.eu/teste.php");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			Resposta = new StringBuffer();
			while ((inputLine = in.readLine()) != null) 
			{
				Resposta.append(inputLine);
				//    Uteis.MSG(inputLine);
			}
		}
		catch(Exception Ex)
		{
			Uteis.MSG("ERRO= "+Ex.getMessage());
		}
		finally
		{
			try { if (in != null)  in.close();} catch(Exception Ex) {  }
			try { if (con != null) con.disconnect(); } catch(Exception Ex) {  }
		}
		return Resposta.toString();	}
	

	//---------------------------------
	//---------------------------------
	//---------------------------------
}

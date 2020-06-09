import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class ObjectoDadosBid 
{	
	//map<Data(dia),map<hora(15min),nºcarros>> DadosBid
	SortedMap<Long,Integer> DadosBid;
	SortedMap<Long,Integer>	DadosBidMeas;
	int INTERVALO_SEG;
	long TimeStamp_Inicio;
	boolean Primeira_vez = true;

	public ObjectoDadosBid(int Interv)
	{
		DadosBid = new TreeMap<Long,Integer>();
		INTERVALO_SEG = Interv * 60;
	}
	public int GetDados(String Data,int Hora)
	{
		return 0;
	}
	

	public void Add(String region,String timestamp, int number_of_cars )
	{
		//System.out.printf("olaolaola");
		// tratar! o timespan
		long sec = ParserObj(timestamp)/INTERVALO_SEG;
		//String segundoskey = "" +sec;
		Long segundoskey = sec;
		if (DadosBid.containsKey(segundoskey))
		{
			DadosBid.put(segundoskey, DadosBid.get(segundoskey) + number_of_cars);
			SendDataPHP(region,segundoskey, DadosBid.get(segundoskey) + number_of_cars);
		}	
		else {
			DadosBid.put(segundoskey, number_of_cars);
			SendDataPHP(region,segundoskey,  number_of_cars);
		}
		}
	
	public void AddMeas(String region,String timestamp, int measurement)
	{
		//System.out.printf("olaolaola");
		// tratar! o timespan
		long sec = ParserObj(timestamp)/INTERVALO_SEG;
		//String segundoskey = "" +sec;
		Long segundoskey = sec;
		if (DadosBid.containsKey(segundoskey))
		{
			DadosBidMeas.put(segundoskey, DadosBid.get(segundoskey) + measurement);
			//SendDataPHPmeas(region,segundoskey, DadosBid.get(segundoskey) + measurement);
		}	
		else {
			DadosBidMeas.put(segundoskey, measurement);
			//SendDataPHPmeas(region,segundoskey,  measurement);
		}
		}
	
	private long ParserObj(String timestamp)
	{
		Instant teste = Instant.parse( timestamp );
		long sec = teste.getEpochSecond();
		//System.out.println("teste da data:" + sec );
		return sec;
	}
	public void Mostrar()
	{
		
		Set<Long> Lista_Keys = DadosBid.keySet();
		Date x;
		/*
		Uteis.MSG("\tNumero Keys = [" + Lista_Keys.size()+"]");
		Instant teste = Instant.parse( "2020-04-14T12:00:00Z" );
		long sec = teste.getEpochSecond();		
		Uteis.MSG_Debug("DATA:"+new Date(sec*1000));
		*/
		for (Long key : Lista_Keys)
		{	
			long var = key; //Integer.parseInt(key);
			x =  new Date(var*1000*INTERVALO_SEG);
			Uteis.MSG("\t"+x+"-->["+DadosBid.get(key)+"]");
		}	
	}
	
	public void MostrarMeas()
	{
		
		Set<Long> Lista_Keys = DadosBid.keySet();
		Date x;
		/*
		Uteis.MSG("\tNumero Keys = [" + Lista_Keys.size()+"]");
		Instant teste = Instant.parse( "2020-04-14T12:00:00Z" );
		long sec = teste.getEpochSecond();		
		Uteis.MSG_Debug("DATA:"+new Date(sec*1000));
		*/
		for (Long key : Lista_Keys)
		{	
			long var = key; //Integer.parseInt(key);
			x =  new Date(var*1000*INTERVALO_SEG);
			Uteis.MSG("\t"+x+"-->["+DadosBidMeas.get(key)+"]");
		}	
	}
	
	public void Add1(String region,String timestamp,int number_of_cars)
	{
		//System.out.printf("olaolaola");
		// tratar! o timespan
		long sec = ParserObj(timestamp)/INTERVALO_SEG;
		//String segundoskey = "" +sec;
		Long segundoskey = sec;
		if (DadosBid.containsKey(segundoskey))
		{
			DadosBid.put(segundoskey, DadosBid.get(segundoskey) + number_of_cars);
			SendDataPHP1(region,segundoskey, DadosBid.get(segundoskey) + number_of_cars);
		}	
		else {
			DadosBid.put(segundoskey, number_of_cars);
			SendDataPHP1(region,segundoskey,  number_of_cars);
		}
	}
	
	
	public int GetDados(long t)
	{
		return 0;
	}
	
	public int GetDadosSoData(long t)
	{	
		long Key =(t-TimeStamp_Inicio)/INTERVALO_SEG;
		if (DadosBid.containsKey(Key))
		{
			int v1 = DadosBid.get(Key); 
		}
		
		return 0;
	}
	
	public void SendDataPHP(String region,long Key,int value) 
	{	
		try {
			URL url = new URL("http://fjunior.f2mobile.eu/cmd_bdados.php");
			String postData = "value=" + Key +"&xaxa=" +value +"&radarID=" +region ;
			
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
	
	public void SendDataPHP1(String region,long Key,int value) 
	{	
		try {
			URL url = new URL("http://fjunior.f2mobile.eu/cmd_bdados1.php");
			String postData = "value=" + Key +"&xaxa=" +value +"&radarID=" +region ;
			
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
	
	public void SendDataPHPmeas(String region,long Key,String value) 
	{	
		try {
			URL url = new URL("http://fjunior.f2mobile.eu/cmd_bdados1.php");
			String postData = "value=" + Key +"&measurement=" +value +"&radarID=" +region ;
			
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
	
	public void MapaToXml()
	{
		
	}
	
}	    
		/* URL url = new URL("http://g.php"); // URL to your application
		    Map<String,Object> params = new LinkedHashMap<>();
		    params.put("value", 5); // All parameters, also easy
		    params.put("id", 17);

		    StringBuilder postData = new StringBuilder();
		    // POST as urlencoded is basically key-value pairs, as with GET
		    // This creates key=value&key=value&... pairs
		    for (Map.Entry<String,Object> param : params.entrySet()) {
		        if (postData.length() != 0) postData.append('&');
		        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
		        postData.append('=');
		        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		    }

		    // Convert string to byte array, as it should be sent
		    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		    // Connect, easy
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    // Tell server that this is POST and in which format is the data
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		    conn.setDoOutput(true);
		    conn.getOutputStream().write(postDataBytes);

		    // This gets the output from your server
		    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		    for (int c; (c = in.read()) >= 0;)
		        System.out.print((char)c);*/
	//---------------------------------
		

		    // This gets the output from your server
		  // Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		  //  for (int c; (c = in.read()) >= 0;)
		   //     System.out.print((char)c);
		
	//-----------------	---------------
		/*for (Map.Entry mapElement : DadosBid.entrySet()) 
		 { 
	            String key = (String)mapElement.getKey(); 
	  
	            // Add some bonus marks 
	            // to all the students and print it 
	            int value = ((int)mapElement.getValue() + 10); 
	  
	            System.out.println(key + " : " + value); 
		 } */  
	


/*
X = Objeto();
Uteis.Msg("Passo 1");
if (X == null) return -1;
Y = X.Build(3);

Y.Run()
new Objeto().Build(3).run();
*/
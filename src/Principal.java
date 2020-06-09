import java.applet.Applet;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Principal 
{

	public void Experiencias_Servidor()
	{
		API_Servidor Servidor = new API_Servidor("https://pasmo.es.av.it.pt/api");
		
		/*String RESP = Servidor.Executar_latestValues();
		List<ObjectoGeral> LLV = Servidor.Get_Lista_Last_Values();*/
		String RESP1 = Servidor.Executar_radars();
		List<ObjectoGeral> LR = Servidor.Get_Lista_radars();
		
		//Uteis.MSG("RESP=" + RESP);
		
		
		//RESP = Servidor.Executar_parking();
		//Uteis.MSG("RESP=" + RESP);	
		
		//RESP = Servidor.Executar_parking_sensor_ID("300466");
		//Uteis.MSG("RESP=" + RESP);	
			
		//RESP = Servidor.Executar_availableSensors();
		//Uteis.MSG("RESP=" + RESP);	
	}
	
	//---------------------------------
	public static void main(String[] args) throws Exception 
	{		
		API_Servidor Servidor = new API_Servidor("https://pasmo.es.av.it.pt/api");
		
		//String RESP = Servidor.Executar_latestValues();
		//List<ObjectoGeral> LLV = Servidor.Get_Lista_Last_Values();
		//String RESP1 = Servidor.Executar_radars();
		//List<ObjectoGeral> LR = Servidor.Get_Lista_radars();
		
		
		String RESP2 = Servidor.Executar_radars_events_region("ponte","cars_in");
		String RESP3 = Servidor.Executar_radars_events_region("barra", "cars_in");
		String RESP4 = Servidor.Executar_radars_events_region("costa_nova", "cars_in");
		String RESP5 = Servidor.Executar_radars_events_region("dunaMeio", "cars_in");
		String RESP6 = Servidor.Executar_radars_events_region("riaAtiva", "cars_in");
		String RESP2a = Servidor.Executar_radars_events_region("ponte","cars_out");
		String RESP3a = Servidor.Executar_radars_events_region("barra", "cars_out");
		String RESP4a = Servidor.Executar_radars_events_region("costa_nova", "cars_out");
		String RESP5a = Servidor.Executar_radars_events_region("dunaMeio", "cars_out");
		String RESP6a = Servidor.Executar_radars_events_region("riaAtiva", "cars_out");
		
		String RESPMeas = Servidor.Executar_radars_events_measurements("ponte","class");
		//String RESPMeas1 = Servidor.Executar_radars_events_measurements("barra","class");
		String RESPMeas2 = Servidor.Executar_radars_events_measurements("duna_meio","class");
		String RESPMeas4 = Servidor.Executar_radars_events_measurements("riaAtiva","class");
		//List<ObjectoGeral> LRER = Servidor.Get_Lista_radars_events_region();
		//String teste = Servidor.Executar_PHP();
		//System.out.println("Resultado =" + teste);
		
	
		
		//Uteis.MSG("RESP=" + RESP);
		
		
		//RESP = Servidor.Executar_parking();
		//Uteis.MSG("RESP=" + RESP);	
		
		//RESP = Servidor.Executar_parking_sensor_ID("300466");
		//Uteis.MSG("RESP=" + RESP);	
			
		//RESP = Servidor.Executar_availableSensors();
		//Uteis.MSG("RESP=" + RESP);			
	}
	
	//---------------------------------
	//---------------------------------
	//---------------------------------
	//---------------------------------
	//---------------------------------
	//---------------------------------
	//---------------------------------

	

}





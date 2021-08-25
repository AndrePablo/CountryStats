import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/*
 * Authors: Ashwin, Byron, Ashvinder
 */


//Class handles making API calls when prompted by the Model class 
//Store corresponding API call ID for each analysis metric possible 

public class RenderingServer {
	
	private Vector<Vector<String>> metricAndIndicator = new Vector<Vector<String>>();
	
	//Create analysis name and API call ID tuples upon construction 
	
	public RenderingServer() {
		metricAndIndicator.add(addMetricAndIndicator("Population", "SP.POP.TOTL"));
		metricAndIndicator.add(addMetricAndIndicator("Ratio of Hospital Beds", "SH.MED.BEDS.ZS"));
		metricAndIndicator.add(addMetricAndIndicator("Current Health Expenditure", "SH.XPD.CHEX.PC.CD"));	
		metricAndIndicator.add(addMetricAndIndicator("Mortality", "SP.DYN.IMRT.IN"));
		metricAndIndicator.add(addMetricAndIndicator("Maternal mortality ratio" ,"SH.STA.MMRT"));
		metricAndIndicator.add(addMetricAndIndicator("GDP", "NY.GDP.MKTP.CD"));
		metricAndIndicator.add(addMetricAndIndicator("Unemployment", "SL.UEM.TOTL.ZS"));
		metricAndIndicator.add(addMetricAndIndicator("Male Unemployment", "SL.UEM.TOTL.MA.ZS"));
		metricAndIndicator.add(addMetricAndIndicator("Female Unemployment", "SL.UEM.TOTL.FE.ZS"));

	}
	
	//Helper function to add tuples of analysis names and API call IDs
	
	private Vector<String> addMetricAndIndicator(String metric, String indicator) {
		Vector<String> v = new Vector<String>();
		v.add(metric);
		v.add(indicator);
		return v;
	}
	
	//Retrieve the API call ID for a given analysis metric 
	
	private String getIndicatorForMetric(String metric) {
		Iterator<Vector<String>> iter = metricAndIndicator.iterator();
		while(iter.hasNext()) {
			Vector<String> v = iter.next();
			if(v.elementAt(0).equals(metric)) {
				return v.elementAt(1);
			}
		}
		return "null";
	}
	
	//Call API and return collected data 
	
	public Vector<Double> getData(String metric, String country, String startYear, String endYear) {
		String urlString = "http://api.worldbank.org/v2/country/" + country + "/indicator/" + getIndicatorForMetric(metric) + "?date=" + startYear + ":" + endYear + "&format=json";
		double dataPerYear = 0;
		Vector<Double> collectedData = new Vector<Double>();

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			System.out.println("connected");
			int responsecode = conn.getResponseCode();
			System.out.println(Integer.toString(responsecode));
			if (responsecode == 200) {
				System.out.println("reading");
				String inline = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					inline += sc.nextLine();
				}
				sc.close();
				
				//Collected API data in form of JSON arrays, must be parsed and stored into a vector 
				
				JsonArray jsonArray = new JsonParser().parse(inline).getAsJsonArray();
				int size = jsonArray.size();
				int sizeOfResults = jsonArray.get(1).getAsJsonArray().size();
				int year;
				
				//Vectors push recent elements; therefore iterate in reverse to add all elements in order 
				
				for (int i = sizeOfResults - 1; i >= 0; i--) {
					year = jsonArray.get(1).getAsJsonArray().get(i).getAsJsonObject().get("date").getAsInt();
					if (jsonArray.get(1).getAsJsonArray().get(i).getAsJsonObject().get("value").isJsonNull())
						dataPerYear = 0;
					else
						dataPerYear = jsonArray.get(1).getAsJsonArray().get(i).getAsJsonObject().get("value").getAsDouble();
					collectedData.add((double) dataPerYear);
				}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
		return collectedData;
	
	}
}




import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/*
 * Authors: Ashwin, Ashvinder
 */


//Model class implements logic required to call data processing instructions and generate required views for MainUI
//Implements the strategy design pattern to conduct various API calls depending on the selected metric
//Implements functionality of MVC architecture 

public class Model extends Subject{
	
	//Contain access to data recovered from API calls and to AnalysisServer for validating data and RenderingServer for making the API call
	
	private AnalysisServer analysisServer = new AnalysisServer();
	private RenderingServer renderingServer = new RenderingServer();
	private Vector<Double> data = new Vector<Double>();
	private Vector<Double> data2 = new Vector<Double>();
	private Vector<Double> data3 = new Vector<Double>();
	private String startYear = "";
	private String endYear = "";
	private String allMetrics;
	private String firstMetric = "";
	private String secondMetric = "";
	private String thirdMetric = "";
	private String[] splitMetric;
	private double max = 0;
	
	//Call upon analysis server to determine if API call can be made
	//Call API via rendering server, then notify all attached observers (viewers) to update 
	
	public boolean runAnalysis(String metric, String country, String startYear, String endYear, List<String> v) {	
		this.startYear = startYear;
		this.endYear = endYear;
		this.allMetrics = metric;
		
		//Parse single string of all metrics into multiple strings for each metric 
		
		if((this.allMetrics.contains("vs"))&&!(this.allMetrics.contains("&"))) //Detection for two series but not three
		{
			splitMetric = this.allMetrics.split("vs ");
			firstMetric = splitMetric[0];
			secondMetric = splitMetric[1];
		}
		
		else if((this.allMetrics.contains("vs"))&&(this.allMetrics.contains("&"))) // Case of three series requires extra metric handling
		{
			splitMetric = this.allMetrics.split("vs |& ");
			firstMetric = splitMetric[0];
			secondMetric = splitMetric[1];
			thirdMetric = splitMetric[2];
		}
		
		//Check if selected analysis is possible via AnalysisServer
		//If so, call the API using RenderingServer 
		//Handle each possible analysis using a switch statement
		
		if(analysisServer.checkIfCallValid(metric, v, startYear, endYear)) {
			System.out.println("Successful call");
			switch(metric) {
			case "Population":
				data = renderingServer.getData("Population", country, startYear, endYear);
				divideByMillion(data);
				break;
			case "Ratio of Hospital Beds vs Current Health Expenditure":
				data = renderingServer.getData("Ratio of Hospital Beds", country, startYear, endYear);
				data2 = renderingServer.getData("Current Health Expenditure", country, startYear, endYear);
				break;
			case "Mortality":
				data = renderingServer.getData("Mortality", country, startYear, endYear);
				break;
			case "Mortality vs Expenses & Hospital Beds":
				data = renderingServer.getData("Mortality", country, startYear, endYear);
				data2 = renderingServer.getData("Current Health Expenditure", country, startYear, endYear);
				data3 = renderingServer.getData("Ratio of Hospital Beds", country, startYear, endYear);
				break;
			case "Unemployment":
				data = renderingServer.getData("Unemployment", country, startYear, endYear);
				break;
			}

			//Once data is processed, notify all observers of it to render graphs
			
			notifyObservers();
			
			//Indicate to MainUI no problems occurred and data can be displayed, else show error message 
			
			return true;
		}
		else {
			return false;
		}
	}
	
	
	//Model also handles any data retrieval, therefore getters provide data back as needed
	
	public Vector<Double> getDataFirstMetric() {
		return data;
	}
	
	public Vector<Double> getDataSecondMetric() {
		return data2;
	}
	
	public Vector<Double> getDataThirdMetric() {
		return data3;
	}
	
	public String getStartYear() {
		return startYear;
	}
	
	public String getEndYear() {
		return endYear;
	}
	
	public String getAllMetrics() {
		return allMetrics;
	}
	
	public String getFirstMetric() {
		return firstMetric;
	}
	
	public String getSecondMetric() {
		return secondMetric;
	}
	
	public String getThirdMetric() {
		return thirdMetric;
	}
	
	public void divideByMillion(Vector<Double> data) {
		Iterator<Double> dataIter = data.iterator();
		int count  = 0;
		while(dataIter.hasNext()) {
			double currentDataItem = dataIter.next();
			data.set(count, currentDataItem/1000000);
			count++;
		}
	}
	
	public double getMax() {
		return max;
	}
	
	public void getMaxOfData() {
		Iterator iter = data.iterator();
		while(iter.hasNext()) {
			double a = (double) iter.next();
			if(a > max)
				max = a;
		}
	}
}


import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/*
 * Authors: Ashwin, Byron, Ashvinder
 */

//Class to determine what types of viewers are allowed for each analysis metric 

public class AnalysisServer {
	
	private Vector<Vector<String>> allowedTypes = new Vector<Vector<String>>();
	
	//Constructor and helper method to manually configure allowable viewers per each metric 
	
	public AnalysisServer() {
		allowedTypes.add(addTypes("Population", "Scatter Chart"));
		allowedTypes.add(addTypes("Population", "Report"));
		allowedTypes.add(addTypes("Population", "Line Chart"));
		allowedTypes.add(addTypes("Population", "Bar Chart"));
		allowedTypes.add(addTypes("Mortality", "Scatter Chart"));
		allowedTypes.add(addTypes("Mortality", "Report"));
		allowedTypes.add(addTypes("Mortality", "Line Chart"));
		allowedTypes.add(addTypes("Ratio of Hospital Beds vs Current Health Expenditure", "Line Chart"));
		allowedTypes.add(addTypes("Ratio of Hospital Beds vs Current Health Expenditure", "Bar Chart"));
		allowedTypes.add(addTypes("Ratio of Hospital Beds vs Current Health Expenditure", "Scatter Chart"));
		allowedTypes.add(addTypes("Ratio of Hospital Beds vs Current Health Expenditure", "Report"));
		allowedTypes.add(addTypes("Mortality vs Expenses & Hospital Beds", "Report"));
		allowedTypes.add(addTypes("Mortality vs Expenses & Hospital Beds", "Line Chart"));
		allowedTypes.add(addTypes("Mortality vs Expenses & Hospital Beds", "Scatter Chart"));
		allowedTypes.add(addTypes("GDP", "Report"));
		allowedTypes.add(addTypes("GDP", "Scatter Chart"));
		allowedTypes.add(addTypes("GDP", "Line Chart"));
		allowedTypes.add(addTypes("GDP", "Bar Chart"));
		allowedTypes.add(addTypes("Mortality vs Expenses & Hospital Beds", "Report"));
		allowedTypes.add(addTypes("Mortality vs Expenses & Hospital Beds", "Bar Chart"));
		allowedTypes.add(addTypes("Mortality vs Expenses & Hospital Beds", "Line Chart"));
		allowedTypes.add(addTypes("Mortality vs Expenses & Hospital Beds", "Scatter Chart"));
		allowedTypes.add(addTypes("Current Health Expenditure per Capita vs Mortality Rate", "Report"));
		allowedTypes.add(addTypes("Current Health Expenditure per Capita vs Mortality Rate", "Line Chart"));
		allowedTypes.add(addTypes("Current Health Expenditure per Capita vs Mortality Rate", "Bar Chart"));
		allowedTypes.add(addTypes("Current Health Expenditure per Capita vs Mortality Rate", "Scatter Chart"));
      	allowedTypes.add(addTypes("Male vs Female Unemployment", "Pie Chart"));
		allowedTypes.add(addTypes("Unemployment", "Pie Chart"));
	}
	
	//Helper method to add tuples of metric and allowable viewer 
	
	private Vector<String> addTypes(String metric, String view) {
		Vector<String> v = new Vector<String>();
		v.add(metric);
		v.add(view);
		return v;
	}
	
	//Helper function called from Model class to check if user-selected viewers are valid
	//Pass in user-selected data from MainUI and compare against allowed types of analysis dictated by AnalysisServer 
	
	public boolean checkIfCallValid(String metric, List<String> selectedViewers, String startYear, String endYear) {
		
		//Iterate through each tuple of (allowableMetric, viewer)
		
		Iterator<Vector<String>> iter = allowedTypes.iterator();
		while(iter.hasNext()) {
			Vector<String> v = iter.next();
			
			//If user selected metric retrieved from MainUI matches one of the allowable metrics stored in the AnalysisServer
			//Then check if associated user-selected viewer can be used for that metric 
			
			if(metric.equals(v.elementAt(0))) {
				if(selectedViewers.contains(v.elementAt(1))) {
					if(Integer.valueOf(startYear) <= Integer.valueOf(endYear))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
}


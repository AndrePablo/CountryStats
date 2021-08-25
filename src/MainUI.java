import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * Authors: Ashwin, Ashvinder
 */


//MainUI implements the facade design pattern, singleton design pattern and MVC architecture
//Acts as center-piece to drive data-processing through user's selections 
//Displays all results of operations back to user 
//Singleton design pattern ensures only one instance of MainUI window runs at a time 

public class MainUI extends JFrame {
	
	
	private static final long serialVersionUID = 1L;

	//Create instance of Model class and other helper variables used to display things in MainUI 
	
	private static MainUI instance;	
	private Model model = new Model();
	private JLabel showViewers = new JLabel("Active Viewers: ");
	
	//Variable used to track which viewers are currently in use
	//Necessary for analysis server to determine allowable graphs for each analysis metric before any API calls are made 
	//Thread-safe list variant to avoid MultiThreadedException() when updating active viewer list 
		
	private List<String> activeViewers = new CopyOnWriteArrayList<String>();
		
	//Form instances of each viewer, but do not populate any with data yet 
	
	private BarViewer bar;
	private ScatterViewer scatter;
	private LineViewer line;
	private PieViewer pie;
	private ReportViewer report;
	
	//public getInstance() method calls the same instance of MainUI when needed to implement singleton design pattern 
	
	public static MainUI getInstance() {
		if (instance == null)
			instance = new MainUI();

		return instance;
	}

	
	//Constructor used to determine layout of MainUI, update layout when data ready and to handle button on-click commands 
	
	private MainUI() {
		
		// Set window title
		
		super("Country Statistics");
		
		//Populate both array lists for country names and IDs
		
		addCountryNameAndID();
		
		JLabel chooseCountryLabel = new JLabel("Choose a country: ");
		
		Vector<Vector<String>> countriesNamesAndIDs = addCountryNameAndID();
		Vector<String> countriesNames = getCountries(countriesNamesAndIDs);		
		JComboBox<String> countriesList = new JComboBox<String>(countriesNames);
		
		//Add start and end years drop downs 
		
		JLabel from = new JLabel("From");
		JLabel to = new JLabel("To");
		Vector<String> years = new Vector<String>();
		for (int i = 2021; i >= 1990; i--) {
			years.add("" + i);
		}
		JComboBox<String> fromList = new JComboBox<String>(years);
		JComboBox<String> toList = new JComboBox<String>(years);

		JButton darkMode = new JButton("Dark Mode");
		JButton lightMode = new JButton("Light Mode");

		//Configure elements in top of layout 
		
		JPanel north = new JPanel();
		north.add(chooseCountryLabel);
		north.add(countriesList);
		north.add(from);
		north.add(fromList);
		north.add(to);
		north.add(toList);
		north.add(showViewers);
		north.add(darkMode);
		north.add(lightMode);

		
		//Add buttons and drop-downs for selecting analysis types, and viewers 
		
		JButton clear = new JButton("Clear Data");
		JButton recalculate = new JButton("Recalculate");
				
		JLabel viewsLabel = new JLabel("Available Views: ");

		Vector<String> viewsNames = new Vector<String>();
		viewsNames.add("Pie Chart");
		viewsNames.add("Line Chart");
		viewsNames.add("Bar Chart");
		viewsNames.add("Scatter Chart");
		viewsNames.add("Report");
		JComboBox<String> viewsList = new JComboBox<String>(viewsNames);
		JButton addView = new JButton("+");
		JButton removeView = new JButton("-");

		JLabel methodLabel = new JLabel("Choose analysis method: ");

		Vector<String> methodsNames = new Vector<String>();
		methodsNames.add("Mortality");
		methodsNames.add("Mortality vs Expenses & Hospital Beds");
		methodsNames.add("Ratio of Hospital Beds vs Current Health Expenditure");
		methodsNames.add("Population");
      	methodsNames.add("Unemployment");

		JComboBox<String> methodsList = new JComboBox<String>(methodsNames);

		//Configure elements in top of layout 
		
		JPanel south = new JPanel();
		south.add(viewsLabel);
		south.add(viewsList);
		south.add(addView);
		south.add(removeView);

		south.add(methodLabel);
		south.add(methodsList);
		south.add(recalculate);
		south.add(clear);
		
		//Show selected (active) viewers 
		
		JPanel east = new JPanel();
		
		JPanel west = new JPanel();

		getContentPane().add(north, BorderLayout.NORTH);
		getContentPane().add(east, BorderLayout.EAST);
		getContentPane().add(south, BorderLayout.SOUTH);
		getContentPane().add(west, BorderLayout.WEST);
		
		//Add on-click button ActionListeners to execute other commands 
		//For the "clear" button, trigger detaching any existing observers and deleting shown graphs on MainUI 
		
		
		//Set foreground and background colors corresponding to user input for dark mode or light mode
		darkMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				chooseCountryLabel.setForeground(Color.yellow);
				from.setForeground(Color.yellow);
				to.setForeground(Color.yellow);
				methodLabel.setForeground(Color.yellow);
				viewsLabel.setForeground(Color.yellow);
				showViewers.setForeground(Color.yellow);
				
				east.setBackground(Color.black);
				west.setBackground(Color.black);
				north.setBackground(Color.black);
				south.setBackground(Color.black);

			}
		});
		
		lightMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				chooseCountryLabel.setForeground(Color.DARK_GRAY);
				from.setForeground(Color.DARK_GRAY);
				to.setForeground(Color.DARK_GRAY);
				methodLabel.setForeground(Color.DARK_GRAY);
				viewsLabel.setForeground(Color.DARK_GRAY);
				showViewers.setForeground(Color.DARK_GRAY);
				
				east.setBackground(Color.white);
				west.setBackground(Color.white);
				north.setBackground(Color.white);
				south.setBackground(Color.white);

			}
		});
		
			
		//"Clear" button will call remove on each existing viewer to detach it, then delete the viewer from the main frame
		
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(scatter != null) {
					removeViewer("Scatter Chart");
					west.remove(scatter.getChart());
					scatter.remove();
				}
				if(bar != null) 
				{
					removeViewer("Bar Chart");
					west.remove(bar.getChart());
					bar.remove();
				}
				if(line != null) 
				{
					removeViewer("Line Chart");
					east.remove(line.getChart());
					line.remove();
				}
				if(pie != null) 
				{
					removeViewer("Pie Chart");
					west.remove(pie.getChart());
					pie.remove();
				}
				if(report != null) 
				{
					removeViewer("Report");
					east.remove(report.getChart());
					report.remove();
				}
			}
		});
		
		//For the "recalculate" button, determine which viewers user has selected
		//Attach each selected viewer and retrieve data using processing functions from Model class
		
		recalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Attach selected viewers by accessing activeViewers list  
				
				if(activeViewers.contains("Bar Chart")) {
					bar = BarViewer.getInstance(model);
				}
				if(activeViewers.contains("Scatter Chart")) {
					scatter = ScatterViewer.getInstance(model);
				}
				if(activeViewers.contains("Line Chart")) {
					line = LineViewer.getInstance(model);
				}
				if(activeViewers.contains("Pie Chart")) {
					pie = PieViewer.getInstance(model);
				}
				if(activeViewers.contains("Report")) {
					report = ReportViewer.getInstance(model);
				}
				
				//If there are any issues with user's selections, then display error message, else begin making API calls and waiting for rendered views to display 
				
				if(model.runAnalysis(String.valueOf(methodsList.getSelectedItem()), getIdForCountry(String.valueOf(countriesList.getSelectedItem()), countriesNamesAndIDs), String.valueOf(fromList.getSelectedItem()), String.valueOf(toList.getSelectedItem()), activeViewers)) {
					
					//Only add viewers when they do not already exist
					//Therefore must check if each viewer is active beforehand to prevent duplicate viewers from rendering
					
					if(activeViewers.contains("Bar Chart") && !bar.isActive()) {
						west.add(bar.getChart());
					}
					if(activeViewers.contains("Scatter Chart") && !scatter.isActive()) {
						west.add(scatter.getChart());
					}
					if(activeViewers.contains("Line Chart") && !line.isActive()) {
						east.add(line.getChart());
					}
					if(activeViewers.contains("Pie Chart") && !pie.isActive()) {
						west.add(pie.getChart());
					}
					if(activeViewers.contains("Report") && !report.isActive()) {
						east.add(report.getChart());
					}
				}
				else {
					JOptionPane p = new JOptionPane();
					p.showMessageDialog(new JFrame(), "Error in selections");
				}
			}
		});
		
		//Add viewer to list of active viewers 
		
		addView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addViewer(String.valueOf(viewsList.getSelectedItem()));
				
			}
		});

		//Remove viewer from list of active viewers 
		
		removeView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeViewer(String.valueOf(viewsList.getSelectedItem()));
				
			}
		});
		
	}
	
	//Helper function to display active viewers to UI 
	
	public void updateShownViewers() {
		String views = "";
		Iterator<String> iter = activeViewers.iterator();
		while(iter.hasNext()) {
			String v = iter.next();
			views += v + ", ";
		}
		showViewers.setText("Active Viewers: " + views);
	}
	
	//Manually add valid country name and corresponding ID for API call
	//Return all countries when called 
	
	public Vector<Vector<String>> addCountryNameAndID() {
		Vector<Vector<String>> all = new Vector<Vector<String>>();
		all.add(addCountryIdPair("Aruba", "ABW"));	
		all.add(addCountryIdPair("Afghanistan", "AGO"));		
		all.add(addCountryIdPair("Albania", "ALB"));		
		all.add(addCountryIdPair("Argentina", "ARG"));		
		all.add(addCountryIdPair("Armenia", "ARM"));		
		all.add(addCountryIdPair("Australia", "AUS"));		
		all.add(addCountryIdPair("Austria", "AUT"));		
		all.add(addCountryIdPair("Azerbaijan", "AZE"));
      	all.add(addCountryIdPair("Bangladesh", "BGD"));	
		all.add(addCountryIdPair("Belgium", "BEL"));	
		all.add(addCountryIdPair("Bosnia and Herzegovina", "BIH"));	
		all.add(addCountryIdPair("Brazil", "BRA"));	
		all.add(addCountryIdPair("Bulgaria", "BGR"));	
		all.add(addCountryIdPair("Cambodia", "KHM"));	
		all.add(addCountryIdPair("Cameroon", "CMR"));
		all.add(addCountryIdPair("Canada", "CAN"));
     	all.add(addCountryIdPair("Chad", "TCD"));	
		all.add(addCountryIdPair("China", "CHN"));	
		all.add(addCountryIdPair("Croatia", "HRV"));	
		all.add(addCountryIdPair("Cuba", "CUB"));	
		all.add(addCountryIdPair("Denmark", "DNK"));	
		all.add(addCountryIdPair("Ecuador", "ECU"));	
		all.add(addCountryIdPair("Egypt", "EGY"));	
		all.add(addCountryIdPair("El Salvador", "SLV"));	
		all.add(addCountryIdPair("Ethiopia", "ETH"));	
		all.add(addCountryIdPair("Finland", "FIN"));	
		all.add(addCountryIdPair("France", "FRA"));	
		all.add(addCountryIdPair("Georgia", "GEO"));	
		all.add(addCountryIdPair("Germany", "DEU"));	
		all.add(addCountryIdPair("Ghana", "GHA"));	
		all.add(addCountryIdPair("Greece", "GRC"));	
		all.add(addCountryIdPair("Greenland", "GRL"));	
		all.add(addCountryIdPair("Guatemala", "GTM"));	
		all.add(addCountryIdPair("Haiti", "HTI"));	
		all.add(addCountryIdPair("Honduras", "HND"));	
		all.add(addCountryIdPair("Hong Kong", "HKG"));	
		all.add(addCountryIdPair("Hungary", "HUN"));	
		all.add(addCountryIdPair("Iceland", "ISL"));	
		all.add(addCountryIdPair("India", "IND"));	
		all.add(addCountryIdPair("Indonesia", "IDN"));	
		all.add(addCountryIdPair("Iran (Islamic Republic of)", "IRN"));	
		all.add(addCountryIdPair("Iraq", "IRQ"));	
		all.add(addCountryIdPair("Ireland", "IRL"));	
		all.add(addCountryIdPair("Israel", "ISR"));	
		all.add(addCountryIdPair("Italy", "ITA"));	
		all.add(addCountryIdPair("Jamaica", "JAM"));	
		all.add(addCountryIdPair("Japan", "JPN"));	
		all.add(addCountryIdPair("Jordan", "JOR"));	
		all.add(addCountryIdPair("Kazakhstan", "KAZ"));	
		all.add(addCountryIdPair("Kenya", "KEN"));	
		all.add(addCountryIdPair("Kuwait", "KWT"));	
		all.add(addCountryIdPair("Kyrgyzstan", "KGZ"));	
		all.add(addCountryIdPair("Lebanon", "LBN"));	
		all.add(addCountryIdPair("Libya", "LBY"));	
		all.add(addCountryIdPair("Lithuania", "LTU"));	
		all.add(addCountryIdPair("Luxembourg", "LUX"));	
		all.add(addCountryIdPair("Malaysia", "MYS"));
		all.add(addCountryIdPair("Mexico", "MEX"));
		all.add(addCountryIdPair("Monaco", "MCO"));
		all.add(addCountryIdPair("Morocco", "MAR"));
		all.add(addCountryIdPair("Myanmar", "MMR"));
		all.add(addCountryIdPair("Nepal", "NPL"));
		all.add(addCountryIdPair("Netherlands", "NLD"));
		all.add(addCountryIdPair("New Zealand", "NZL"));
		all.add(addCountryIdPair("North Korea", "PRK"));
		all.add(addCountryIdPair("Norway", "NOR"));
		all.add(addCountryIdPair("Oman", "OMN"));
		all.add(addCountryIdPair("Pakistan","PAK"));
		all.add(addCountryIdPair("Phillipines", "PHL"));
		all.add(addCountryIdPair("Poland", "POL"));
		all.add(addCountryIdPair("Portugal", "PRT"));
		all.add(addCountryIdPair("Puerto Rico", "PRI"));
		all.add(addCountryIdPair("Qatar", "QAT"));
		all.add(addCountryIdPair("Russia", "RUS"));
		all.add(addCountryIdPair("Romania", "ROU"));
		all.add(addCountryIdPair("Senegal", "SEN"));
		all.add(addCountryIdPair("Singapore", "SGP"));
		all.add(addCountryIdPair("Saudi Arabia", "SAU"));
		all.add(addCountryIdPair("Serbia", "SRB"));
		all.add(addCountryIdPair("Slovenia", "SVN"));
		all.add(addCountryIdPair("South Korea", "KOR"));
		all.add(addCountryIdPair("Spain", "ESP"));
		all.add(addCountryIdPair("Syria", "SYR"));
		all.add(addCountryIdPair("Sweden", "SWE"));
		all.add(addCountryIdPair("Switzerland", "CHE"));
		all.add(addCountryIdPair("Taiwan", "TWN"));
		all.add(addCountryIdPair("Thailand", "THA"));
		all.add(addCountryIdPair("Uganda", "UGA"));
		all.add(addCountryIdPair("Ukraine", "UKR"));
		all.add(addCountryIdPair("United States of America", "USA"));
		all.add(addCountryIdPair("Uruguay", "URY"));
		all.add(addCountryIdPair("Yemen", "YEM"));
		all.add(addCountryIdPair("Zimbabwe", "ZWE"));
		return all;
	}
	
	//Populate array list containing country names for drop-down
	//Return list when called 
	
	public Vector<String> getCountries(Vector<Vector<String>> countries) {
		Iterator<Vector<String>> allCountries = countries.iterator();
		
		Vector<String> countriesOnly = new Vector<String>();
		
		while(allCountries.hasNext()) {
			countriesOnly.add(allCountries.next().firstElement());
		}
		
		return countriesOnly;
	}
	
	//Populate, return array list containing country names and IDs, can add tuples of form: (country, id)
	
	public Vector<String> addCountryIdPair(String country, String id) {
		Vector<String> list = new Vector<String>();
		list.add(country);
		list.add(id);
		return list;
	}

	//For API calls, retrieve ID for selected country by accessing elements of countriesNamesAndIDs vector 
	
	public String getIdForCountry(String country, Vector<Vector<String>> allCountries) {
		Iterator<Vector<String>> iter = allCountries.iterator();
		while(iter.hasNext()) {
			Vector<String> countryTuple = iter.next(); 
			if(countryTuple.firstElement().equals(country)) {
				return countryTuple.elementAt(1);
			}
		}
		return "null";
	}
	
	//When adding active viewers, if a viewer already exists in activeViewers list, then return
	//Retrieve viewer to be added from drop-down in MainUI 
	//Else add the viewer to activeViewers list 
	//Call updateShownViewers() to display results to MainUI
	
	public void addViewer(String viewer) {
		Iterator<String> activeViewsIter = activeViewers.iterator();
		while(activeViewsIter.hasNext()) {
			String view = activeViewsIter.next();
			if(view.equals(viewer)) {
				return;
			}
		}
		activeViewers.add(viewer);
		updateShownViewers();
	}
	
	//When removing active viewers, if a viewer already exists in activeViewers list, then determine its position in activeViewers and delete it 
	//Else if entire list traversed and view does not exist, then there is nothing to remove 
	//Call updateShownViewers() to display results to MainUI
	
	public void removeViewer(String viewer) {
		Iterator<String> activeViewsIter = activeViewers.iterator();
		int count = 0;
		while(activeViewsIter.hasNext()) {
			String view = activeViewsIter.next();
			if(view.equals(viewer)) {
				activeViewers.remove(count);
			}
			count++;
		}
		updateShownViewers();
	}
}




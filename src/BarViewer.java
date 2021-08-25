import org.jfree.chart.ChartPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/*
 * Authors: Ashwin, Byron, Ashvinder
 */


//Class used to render bar graphs to display on MainUI 
//Uses observer design pattern to ensure graphs rendered only when data from successful API call available 
//Uses singleton design pattern to ensure only a single bar graph is created at a time 

public class BarViewer implements Observer, Viewer {
	
	private ChartPanel chart;
	private Model m;
	private static BarViewer instance = null;
	private boolean isActive = false;
	
	//Attach as observer to Model class (Subject)
	//Keep constructor to class private, so it must be made through getInstance method  
	
	private BarViewer(Model subject) {
		m = subject;
		m.attach(this);
	}
	
	//Singleton design pattern ensures only single instance of BarViewer exists at a time 
	
	public static BarViewer getInstance(Model subject) {
		if(instance == null) {
			instance = new BarViewer(subject);
		}
		return instance;
	}
	
	//Must detach BarViewer from list of observers when user clears data for new selection 
	//Must delete reference to existing instance so new bar graphs can be constructed for subsequent API calls
	
	public void remove() {
		m.detach(this);
		instance = null;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	//If class that implemented Subject and called notifyObservers() matches local instance, then can assume correct Subject reference
	//If that is the case, call render using data from class implementing Subject 
	
	@Override
	public void update(Subject changedSubject) {
		if(changedSubject.equals(m)) {
			render();
		}
	}
	
	//Function determines number of datasets available and plots each one accordingly 
	
	public void render() {
		
		int start = Integer.valueOf(m.getStartYear());
		
		//Contain up to two dataset objects to plot two or more sets of data with very different scales 
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		
		//Contain API data called from Model class into Vector variables 
		
		Vector<Double> data1 = m.getDataFirstMetric();
		Vector<Double> data2 = m.getDataSecondMetric();	
		Vector<Double> data3 = m.getDataThirdMetric();

		//Number of categories plotted depends on which vectors are empty or full
		
		//Case where only one category of data available 
		
		if(data2.isEmpty() && !data1.isEmpty() && data3.isEmpty()) {
			String year = m.getStartYear();
			Iterator<Double> iterData1 = data1.iterator();

			//Use iterator on vector to populate dataset object
			
			while(iterData1.hasNext()) {
				dataset.setValue(iterData1.next(), m.getAllMetrics(), year);
				start++;
				year = Integer.toString(start);
			}

			//Configure bar graph 
			
			CategoryPlot plot = new CategoryPlot();
			BarRenderer renderer = new BarRenderer();

			plot.setDataset(0, dataset);
			plot.setRenderer(0, renderer);
			CategoryAxis domainAxis = new CategoryAxis("Year");
			plot.setDomainAxis(domainAxis);
			plot.setRangeAxis(new NumberAxis(m.getAllMetrics()));

			//Map range of values from data into y-axis 
			
			plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis

			//Create bar graph and store it in a local variable 
			if(m.getAllMetrics().equals("Population")) {
				JFreeChart barChart = new JFreeChart(m.getAllMetrics() + " (In Millions) Over Time",
						new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
				chart = new ChartPanel(barChart);
				chart.setPreferredSize(new Dimension(400, 300));
				chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
				chart.setBackground(Color.white);
			}
			else {
				JFreeChart barChart = new JFreeChart(m.getAllMetrics() + " Over Time",
						new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
				chart = new ChartPanel(barChart);
				chart.setPreferredSize(new Dimension(400, 300));
				chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
				chart.setBackground(Color.white);
			}
			
			JFreeChart barChart = new JFreeChart(m.getAllMetrics() + " Over Time",
					new Font("Serif", java.awt.Font.BOLD, 18), plot, true);			
		}
		
		//Case where two categories of data available 
		
		else if(!data1.isEmpty() && !data2.isEmpty() && data3.isEmpty()) {
			BarRenderer renderer2 = new BarRenderer();
			
			String year = m.getStartYear();
			Iterator<Double> iterData1 = data1.iterator();
			Iterator<Double> iterData2 = data2.iterator();

			while(iterData1.hasNext()) {
				dataset.setValue(iterData1.next(), m.getAllMetrics(), year);
				dataset2.setValue(iterData2.next(), m.getFirstMetric(), year);
				start++;
				year = Integer.toString(start);
			}

			CategoryPlot plot = new CategoryPlot();
			BarRenderer renderer = new BarRenderer();

			plot.setDataset(0, dataset);
			plot.setRenderer(0, renderer);
			CategoryAxis domainAxis = new CategoryAxis("Year");
			plot.setDomainAxis(domainAxis);
			plot.setRangeAxis(new NumberAxis(m.getAllMetrics()));
			plot.setDataset(1,dataset2);
			plot.setRenderer(1,renderer2);
			plot.setRangeAxis(1, new NumberAxis(m.getAllMetrics()));
		
			plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis
			plot.mapDatasetToRangeAxis(1,1);// 2nd dataset to 1st y-axis
			JFreeChart barChart = new JFreeChart(m.getAllMetrics() + " Over Time",
					new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

			chart = new ChartPanel(barChart);
			chart.setPreferredSize(new Dimension(600, 400));
			chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			chart.setBackground(Color.white);
		}
		
		//Case where three categories of data available 
		
		else if(!data1.isEmpty() && !data2.isEmpty() && !data3.isEmpty()) {
			BarRenderer renderer1 = new BarRenderer();
			BarRenderer renderer2 = new BarRenderer();
			String year = m.getStartYear();
			Iterator<Double> iterData1 = data1.iterator();
			Iterator<Double> iterData2 = data2.iterator();
			Iterator<Double> iterData3 = data3.iterator();
			
			while(iterData1.hasNext()) {
				dataset.setValue(iterData1.next(), m.getFirstMetric(), year);
				dataset2.setValue(iterData2.next(), m.getSecondMetric(), year);
				dataset2.setValue(iterData3.next(), m.getThirdMetric(), year);
				start++;
				year = Integer.toString(start);
			}
			
			CategoryPlot plot = new CategoryPlot();
			
			plot.setDataset(0, dataset);
			plot.setRenderer(0, renderer1);
			CategoryAxis domainAxis = new CategoryAxis("year");
			plot.setDomainAxis(domainAxis);
			plot.setRangeAxis(new NumberAxis(m.getFirstMetric()));
			
		
			plot.setDataset(1, dataset2);
			plot.setRenderer(1, renderer2);
			plot.setRangeAxis(1, new NumberAxis(m.getSecondMetric()));
			plot.mapDatasetToRangeAxis(0,  0);
			plot.mapDatasetToRangeAxis(1,  1);
			
			JFreeChart barChart = new JFreeChart(m.getAllMetrics() + " Over Time",
					new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

			chart = new ChartPanel(barChart);
			chart.setPreferredSize(new Dimension(600, 400));
			chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			chart.setBackground(Color.white);
		}
	}

	//Return chart when processed back to main UI for viewing
	
		public ChartPanel getChart() {
			isActive = true;
			return chart;
		}
}



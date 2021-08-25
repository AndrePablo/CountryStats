import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;

/*
 * Authors: Ashwin, Byron, Ashvinder
 */


//Class used to render line graphs to display on MainUI 
//Uses observer design pattern to ensure graphs rendered only when data from successful API call available 
//Uses singleton design pattern to ensure only a single bar graph is created at a time 
//Functionality of observer and singleton design patterns similar to BarViewer 

public class LineViewer implements Observer, Viewer
{
	private ChartPanel chart;
	private Model m;
	private boolean isActive = false;
	private static LineViewer instance = null;

	public LineViewer(Model subject) 
	{
		m = subject;
		m.attach(this);
	}
	
	public static LineViewer getInstance(Model subject) {
		if(instance == null) {
			instance = new LineViewer(subject);
		}
		return instance;
	}
	
	public void remove() {
		m.detach(this);
		instance = null;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	//Function used to display different line charts to MainUI 
	
	public void render()
	{
		Vector<Double> data1 = m.getDataFirstMetric();
		Vector<Double> data2 = m.getDataSecondMetric();
		Vector<Double> data3 = m.getDataThirdMetric();
		
		int start = Integer.valueOf(m.getStartYear());
		
		Iterator<Double> iterData1 = data1.iterator();
		
		//Add series to store data values for each metric, starting with first metric 
		
		TimeSeries seriesData1 = new TimeSeries(m.getFirstMetric());
    	XYItemRenderer itemRenderer = new XYLineAndShapeRenderer(true,true);
		XYPlot plot = new XYPlot();

		//Case where one category of data plotted 
				
        if(data2.isEmpty() && data3.isEmpty() && !data1.isEmpty()) 
        {
        	while(iterData1.hasNext()) 
    		{
    			seriesData1.add(new Year(start),iterData1.next());
    			start++;
    		}    		
        	
        	TimeSeriesCollection dataset = new TimeSeriesCollection();
    		dataset.addSeries(seriesData1);
    		
    		plot.setDataset(0, dataset);
    		plot.setRenderer(0, itemRenderer);
    		DateAxis domainAxis = new DateAxis("Year ");
    		plot.setDomainAxis(domainAxis);
    		plot.setRangeAxis(new NumberAxis(m.getFirstMetric()));
    		
    		plot.mapDatasetToRangeAxis(0, 0);
    		
    		if(m.getAllMetrics().equals("Population")) {
    			JFreeChart lineChart = new JFreeChart(m.getAllMetrics() + " (In Millions) Over Time",
        				new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
        		chart = new ChartPanel(lineChart);

        		chart.setPreferredSize(new Dimension(600, 480));
        		chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        		chart.setBackground(Color.white);
    		}
    		else {
    			JFreeChart lineChart = new JFreeChart(m.getAllMetrics() + " Over Time",
        				new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

        		chart = new ChartPanel(lineChart);

        		chart.setPreferredSize(new Dimension(600, 480));
        		chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        		chart.setBackground(Color.white);
    		}
    		
        }
		
        //Case where two categories of data plotted 
        
        else if(!data1.isEmpty() && !data2.isEmpty() && data3.isEmpty()) 
        {
			Iterator<Double> iterData2 = data2.iterator();
			TimeSeries seriesData2 = new TimeSeries(m.getSecondMetric()); 
	    	XYItemRenderer itemRenderer2 = new XYLineAndShapeRenderer(true,true);
	    	
			while(iterData1.hasNext()) 
    		{
    			seriesData1.add(new Year(start),iterData1.next());
    			seriesData2.add(new Year(start),iterData2.next());
    			start++;
    		}
    		    		
    		TimeSeriesCollection dataset = new TimeSeriesCollection();
    		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
    		dataset.addSeries(seriesData1);
    		dataset2.addSeries(seriesData2);
    		
    		plot.setDataset(0, dataset);
    		plot.setRenderer(0, itemRenderer);
    		DateAxis domainAxis = new DateAxis("Year ");
    		plot.setDomainAxis(domainAxis);
    		plot.setRangeAxis(new NumberAxis(m.getFirstMetric()));
    		
			plot.setDataset(1,dataset2);
			plot.setRenderer(1,itemRenderer2);
			plot.setRangeAxis(1, new NumberAxis(m.getSecondMetric()));

			//Map each dataset to a unique y-axis 
			
			plot.mapDatasetToRangeAxis(0, 0);
			plot.mapDatasetToRangeAxis(1,1);
			
			JFreeChart lineChart = new JFreeChart(m.getAllMetrics() + " Over Time",
					new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
    		
			chart = new ChartPanel(lineChart);
    		chart.setPreferredSize(new Dimension(600, 480));
    		chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    		chart.setBackground(Color.white);
        }
        
        //Case where three categories of data plotted 
        
        else if(!data1.isEmpty() && !data2.isEmpty() && !data3.isEmpty())
        {
        	Iterator<Double> iterData2 = data2.iterator();
        	Iterator<Double> iterData3 = data3.iterator();
			TimeSeries seriesData2 = new TimeSeries(m.getSecondMetric()); 
			TimeSeries seriesData3 = new TimeSeries(m.getThirdMetric()); 
			XYItemRenderer itemRenderer2 = new XYLineAndShapeRenderer(true,true);
			XYItemRenderer itemRenderer3 = new XYLineAndShapeRenderer(true,true);

			while(iterData1.hasNext()) 
    		{
    			seriesData1.add(new Year(start),iterData1.next());
    			seriesData2.add(new Year(start),iterData2.next());
    			seriesData3.add(new Year(start),iterData3.next());
    			start++;
    		}

			TimeSeriesCollection dataset = new TimeSeriesCollection();
    		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
    		TimeSeriesCollection dataset3 = new TimeSeriesCollection();

    		dataset.addSeries(seriesData1);
    		dataset2.addSeries(seriesData2);
    		dataset3.addSeries(seriesData3);

    		
    		plot.setDataset(0, dataset);
    		plot.setRenderer(0, itemRenderer);
    		DateAxis domainAxis = new DateAxis("Year ");
    		plot.setDomainAxis(domainAxis);
    		plot.setRangeAxis(new NumberAxis(m.getFirstMetric()));
    		
			plot.setDataset(1,dataset2);
			plot.setRenderer(1,itemRenderer2);
			plot.setRangeAxis(1, new NumberAxis(m.getSecondMetric()));

			plot.setDataset(2,dataset3);
			plot.setRenderer(2,itemRenderer3);
			plot.setRangeAxis(2, new NumberAxis(m.getThirdMetric()));

			//Map each dataset to a unique y-axis 
			
			plot.mapDatasetToRangeAxis(0, 0);
			plot.mapDatasetToRangeAxis(1,1);
			plot.mapDatasetToRangeAxis(2,2);

			JFreeChart lineChart = new JFreeChart(m.getAllMetrics() + " Over Time",
					new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
    		
			chart = new ChartPanel(lineChart);
    		chart.setPreferredSize(new Dimension(600, 480));
    		chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    		chart.setBackground(Color.white);        
    		}
		
	}
	
	@Override
	public void update(Subject changedSubject)
	{
		if(changedSubject.equals(m)) {
			render();
		}		
	}
	
	public ChartPanel getChart() {
		return chart;
	}
}


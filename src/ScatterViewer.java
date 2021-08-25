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
 * Authors: Ashvinder
 */


//Class used to render scatter graphs to display on MainUI 
//Uses observer design pattern to ensure graphs rendered only when data from successful API call available 
//Uses singleton design pattern to ensure only a single scatter graph is created at a time 

public class ScatterViewer implements Observer, Viewer 
{
	private ChartPanel chart;
	private Model m;
	private static ScatterViewer instance = null;
	private boolean isActive = false;
	
	private ScatterViewer(Model subject) {
		m = subject;
		m.attach(this);
	}
	
	public void remove() {
		m.detach(this);
		instance = null;
	}
	
	public static ScatterViewer getInstance(Model subject) {
		if(instance == null) {
			instance = new ScatterViewer(subject);
		}
		return instance;
	}
	
	@Override
	public void update(Subject changedSubject) {
		if(changedSubject.equals(m)) {
			render();
		}
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	//Works similarly to LineViewer except for some modification to XYLineAndShapeRenderer to create scatter graph 
	
	public void render() 
	{
		
		Vector<Double> data1 = m.getDataFirstMetric();
		Vector<Double> data2 = m.getDataSecondMetric();
		Vector<Double> data3 = m.getDataThirdMetric();
		int start = Integer.valueOf(m.getStartYear());
		TimeSeries series1 = new TimeSeries(m.getAllMetrics());
		Iterator<Double> iterData1 = data1.iterator();
		XYPlot plot = new XYPlot();
    	XYItemRenderer itemRenderer = new XYLineAndShapeRenderer(false,true);
		
		if(data2.isEmpty() && data3.isEmpty() && !data1.isEmpty()) 
		{
			while(iterData1.hasNext()) 
			{
				series1.add(new Year(start), iterData1.next());
				start++;
			}
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(series1);
			
			plot = new XYPlot();
			
			plot.setDataset(0, dataset);
			plot.setRenderer(0, itemRenderer);
			DateAxis domainAxis = new DateAxis("Year");
			plot.setDomainAxis(domainAxis);
			plot.setRangeAxis(new NumberAxis(m.getAllMetrics()));

			
			plot.mapDatasetToRangeAxis(0, 0);
			if(m.getAllMetrics().equals("Population")) {
				JFreeChart scatterChart = new JFreeChart(m.getAllMetrics() + " (In Millions) Over Time",
						new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
			
				chart = new ChartPanel(scatterChart);
				chart.setPreferredSize(new Dimension(350, 325));
				chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
				chart.setBackground(Color.white);
			}
			else {
				JFreeChart scatterChart = new JFreeChart(m.getAllMetrics() + " Over Time",
						new Font("Serif", java.awt.Font.BOLD, 18), plot, true);

				chart = new ChartPanel(scatterChart);
				chart.setPreferredSize(new Dimension(350, 325));
				chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
				chart.setBackground(Color.white);
			}
			
		}
		
		else if(!data1.isEmpty() && !data2.isEmpty() && data3.isEmpty()) //Case of two datasets active
        {
			Iterator<Double> iterData2 = data2.iterator();
			TimeSeries series2 = new TimeSeries(m.getSecondMetric()); 
	    	XYItemRenderer itemRenderer2 = new XYLineAndShapeRenderer(false,true);
	    	
			while(iterData1.hasNext()) 
    		{
    			series1.add(new Year(start),iterData1.next());
    			series2.add(new Year(start),iterData2.next());
    			start++;
    		}
    		    		
    		TimeSeriesCollection dataset = new TimeSeriesCollection();
    		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
    		dataset.addSeries(series1);
    		dataset2.addSeries(series2);
    		
    		plot.setDataset(0, dataset);
    		plot.setRenderer(0, itemRenderer);
    		DateAxis domainAxis = new DateAxis("Year ");
    		plot.setDomainAxis(domainAxis);
    		plot.setRangeAxis(new NumberAxis(m.getFirstMetric()));
    		
			plot.setDataset(1,dataset2);
			plot.setRenderer(1,itemRenderer2);
			plot.setRangeAxis(1, new NumberAxis(m.getSecondMetric()));

			plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis
			plot.mapDatasetToRangeAxis(1,1);// 2nd dataset to 1st y-axis
			
			JFreeChart lineChart = new JFreeChart(m.getAllMetrics() + " Over Time",
					new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
    		
			chart = new ChartPanel(lineChart);
    		chart.setPreferredSize(new Dimension(600, 480));
    		chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    		chart.setBackground(Color.white);
        }
        
        else if(!data1.isEmpty() && !data2.isEmpty() && !data3.isEmpty())
        {
        	Iterator<Double> iterData2 = data2.iterator();
        	Iterator<Double> iterData3 = data3.iterator();
			TimeSeries series2 = new TimeSeries(m.getSecondMetric()); 
			TimeSeries series3 = new TimeSeries(m.getThirdMetric()); 
			XYItemRenderer itemRenderer2 = new XYLineAndShapeRenderer(false,true);
			XYItemRenderer itemRenderer3 = new XYLineAndShapeRenderer(false,true);

			while(iterData1.hasNext()) 
    		{
    			series1.add(new Year(start),iterData1.next());
    			series2.add(new Year(start),iterData2.next());
    			series3.add(new Year(start),iterData3.next());
    			start++;
    		}

			TimeSeriesCollection dataset = new TimeSeriesCollection();
    		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
    		TimeSeriesCollection dataset3 = new TimeSeriesCollection();

    		dataset.addSeries(series1);
    		dataset2.addSeries(series2);
    		dataset3.addSeries(series3);

    		
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

			
			plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis
			plot.mapDatasetToRangeAxis(1,1);// 2nd dataset to 1st y-axis
			plot.mapDatasetToRangeAxis(2,2);// 3rd dataset to 1st y-axis

			JFreeChart lineChart = new JFreeChart(m.getAllMetrics() + " Over Time",
					new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
    		
			chart = new ChartPanel(lineChart);
    		chart.setPreferredSize(new Dimension(600, 480));
    		chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    		chart.setBackground(Color.white);        }
		
		
		
	}
	
	//Return chart when processed back to main UI for viewing 
	public ChartPanel getChart() {
		isActive = true;
		return chart;
	}
}


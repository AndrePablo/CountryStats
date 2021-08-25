import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/*
 * Authors: Ashvinder
 */


//Class used to render pie graphs to display on MainUI 
//Uses observer design pattern to ensure graphs rendered only when data from successful API call available 
//Uses singleton design pattern to ensure only a single pie graph is created at a time 


public class PieViewer implements Observer, Viewer
{
	private ChartPanel chart;
	private Model m;
	private boolean isActive = false;
	private static PieViewer instance = null;

	public PieViewer(Model subject) 
	{
		m = subject;
		m.attach(this);
	}
	
	public static PieViewer getInstance(Model subject) {
		if(instance == null) {
			instance = new PieViewer(subject);
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
	
	public void render()
	{
		Vector<Double> data = m.getDataFirstMetric();
		Iterator<Double> iterData = data.iterator();
	    double value;
	    
		DefaultPieDataset dataset = new DefaultPieDataset( );

		while(iterData.hasNext())
		{
			value = iterData.next();
			dataset.setValue("Unemployed", (value));
			dataset.setValue("Employed", (100-value));
		}
		
		JFreeChart pieChart = ChartFactory.createPieChart(m.getAllMetrics() + " in " + m.getStartYear(),dataset,true,true,false);
		
		chart = new ChartPanel(pieChart);

		chart.setPreferredSize(new Dimension(350, 325));
		chart.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chart.setBackground(Color.white);
	}
	
	@Override
	public void update(Subject changedSubject) 
	{
		if(changedSubject.equals(m)) 
		{
			render();
		}
	}
	
	public ChartPanel getChart() 
	{
		return chart;
	}
}



import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * Authors: Asvinder
 */


//Class used to render reports to display on MainUI 
//Uses observer design pattern to ensure reports rendered only when data from successful API call available 
//Uses singleton design pattern to ensure only a single report is created at a time 


public class ReportViewer implements Observer, Viewer
{
	private JScrollPane outputScrollPane;
	private Model m;
	private boolean isActive = false;
	private static ReportViewer instance = null;

	public ReportViewer(Model subject) 
	{
		m = subject;
		m.attach(this);
	}
	
	public static ReportViewer getInstance(Model subject) {
		if(instance == null) {
			instance = new ReportViewer(subject);
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
		Vector<Double> data1 = m.getDataFirstMetric();
		Vector<Double> data2 = m.getDataSecondMetric();
		Vector<Double> data3 = m.getDataThirdMetric();
		Iterator<Double> iterData1 = data1.iterator();
		int start = Integer.valueOf(m.getStartYear());
		String year = m.getStartYear();
		
		String message1, title;
		
		if(data2.isEmpty() && data3.isEmpty() && !data1.isEmpty())
		{
			message1 = " ";
			if(m.getAllMetrics().equals("Population")) {
				title = m.getAllMetrics() + " (In Millions) ==============================\n" + "Year " + year + ":\n";
			}
			else {
				title = m.getAllMetrics() + "==============================\n" + "Year " + year + ":\n";
			}
			
			while(iterData1.hasNext())
			{
				message1 = message1.concat("\t").concat(m.getAllMetrics()).concat(" => " + iterData1.next()).concat("\n");
				start++;
				year = Integer.toString(start);
				if(iterData1.hasNext())
					message1 = message1.concat("Year ").concat(year).concat(":\n");			
			}
			
			JTextArea report = new JTextArea();
			report.setEditable(false);
//			report.setPreferredSize(new Dimension(400, 300));
			report.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			report.setBackground(Color.white);
			
			report.append(title + message1);
			outputScrollPane = new JScrollPane(report, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			outputScrollPane.setPreferredSize(new Dimension(400,300));
		}
		
		else if(!data1.isEmpty() && !data2.isEmpty() && data3.isEmpty()) //Case of two datasets active
		{
			message1 = " ";
			Iterator<Double> iterData2 = data2.iterator();

			title = m.getAllMetrics() + "==============================\n" + "Year " + year + ":\n";
			
			while(iterData1.hasNext())
			{
				message1 = message1.concat("\t").concat(m.getFirstMetric()).concat(" => " + iterData1.next()).concat("\n");
				message1 = message1.concat("\t").concat(m.getSecondMetric()).concat(" => " + iterData2.next()).concat("\n");
				start++;
				year = Integer.toString(start);
				if(iterData1.hasNext())
					message1 = message1.concat("Year ").concat(year).concat(":\n");
			}
			
			JTextArea report = new JTextArea();
			report.setEditable(false);
//			report.setPreferredSize(new Dimension(400, 300));
			report.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			report.setBackground(Color.white);
			
			report.append(title + message1);
			outputScrollPane = new JScrollPane(report, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			outputScrollPane.setPreferredSize(new Dimension(400,300));
			
		}
		
		else if(!data1.isEmpty() && !data2.isEmpty() && !data3.isEmpty())
		{
			message1 = " ";
			Iterator<Double> iterData2 = data2.iterator();
			Iterator<Double> iterData3 = data3.iterator();

			title = m.getAllMetrics() + "==============================\n" + "Year " + year + ":\n";

			
			while(iterData1.hasNext())
			{
				message1 = message1.concat("\t").concat(m.getFirstMetric()).concat(" => " + iterData1.next()).concat("\n");
				message1 = message1.concat("\t").concat(m.getSecondMetric()).concat(" => " + iterData2.next()).concat("\n");
				message1 = message1.concat("\t").concat(m.getThirdMetric()).concat(" => " + iterData3.next()).concat("\n");
				start++;
				year = Integer.toString(start);
				if(iterData1.hasNext())
					message1 = message1.concat("Year ").concat(year).concat(":\n");
			}
			
			JTextArea report = new JTextArea();
			report.setEditable(false);
//			report.setPreferredSize(new Dimension(400, 300));
			report.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			report.setBackground(Color.white);
			
			report.append(title + message1);
			outputScrollPane = new JScrollPane(report, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			outputScrollPane.setPreferredSize(new Dimension(400,300));
		}	
	}
	
	@Override
	public void update(Subject changedSubject) 
	{
		if(changedSubject.equals(m)) 
		{
			render();
		}
	}
	
	public JScrollPane getChart() 
	{
		return outputScrollPane;
	}
}



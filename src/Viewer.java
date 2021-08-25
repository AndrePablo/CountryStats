//Define Viewer interface LineViewer, BarViewer, ScatterViewer, ReportViewer and PieViewer implement

/*
 * Authors: Ashvinder
 */


public interface Viewer 
{
	public void remove();
	public boolean isActive();
	public void render();
}


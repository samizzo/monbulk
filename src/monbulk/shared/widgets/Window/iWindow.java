package monbulk.shared.widgets.Window;

import com.google.gwt.user.client.ui.HasWidgets;

public interface iWindow {

	
	public String getWindowName();
	public void setWindowName(String Name);
	public void Shrink();
	public void Maximise();
	public void setManager(WindowManager winManager);
	public void setWindowSize(int width, int height); 
	public void setProperties(WindowProperties wp);
	public void setOwner(Object Owner);
	public HasWidgets getChild();
	public void init();
	public void close();
	public void hide();
	public void show();
}

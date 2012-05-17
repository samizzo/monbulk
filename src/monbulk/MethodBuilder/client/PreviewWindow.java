package monbulk.MethodBuilder.client;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

import monbulk.shared.widgets.Window.IWindow;
import monbulk.shared.widgets.Window.OkCancelWindow;

public class PreviewWindow extends OkCancelWindow implements IWindow {

	private StringBuilder formattedText;
	
	public enum SupportedFormats{XML,TCL};
	private SupportedFormats selectedFormat;	
	
	protected ScrollPanel _framingPanel;
	protected HTMLPanel _PreviewPanel;
	public PreviewWindow(String windowId, String windowTitle) {
		super(windowId, windowTitle);
		
		_framingPanel = new ScrollPanel();
		this.setWidget(_framingPanel);
		
		
	}
	public void loadPreview(StringBuilder tmpList,PreviewWindow.SupportedFormats inputFormat)
	{
		formattedText = tmpList;
		selectedFormat = inputFormat;
		_PreviewPanel = new HTMLPanel(formattedText.toString());
		_framingPanel.add(_PreviewPanel);
	}

}

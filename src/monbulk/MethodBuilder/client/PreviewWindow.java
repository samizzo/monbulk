package monbulk.MethodBuilder.client;

import com.google.gwt.user.client.ui.HTML;
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
	public PreviewWindow() {
		super("MethodPreviewWindow", "Preview Method Data");
		this.setWidth("600px");
		this.m_windowSettings.resizable=true;
		this.m_windowSettings.minWidth=300;
		this.m_windowSettings.width=600;
		this.m_windowSettings.minHeight=400;
		this.m_windowSettings.height=600;
		_framingPanel = new ScrollPanel();
		//this.setWidget(_framingPanel);
		m_contentPanel.add(_framingPanel);
		_PreviewPanel = new HTMLPanel("No text to render");
		_framingPanel.add(_PreviewPanel);
		
		
	}
	public void loadPreview(StringBuilder tmpList,PreviewWindow.SupportedFormats inputFormat)
	{
		formattedText = tmpList;
		selectedFormat = inputFormat;
		_PreviewPanel.clear();
		HTML output = new HTML();
		output.setHTML(tmpList.toString());
		_PreviewPanel.add(output);
		
		
		
	}

}

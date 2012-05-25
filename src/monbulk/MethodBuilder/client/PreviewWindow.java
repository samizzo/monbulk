package monbulk.MethodBuilder.client;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;

import monbulk.shared.Services.MethodService;
import monbulk.shared.Services.ServiceRegistry;
import monbulk.shared.Services.MethodService.MethodUpdateHandler;
import monbulk.shared.util.MonbulkEnums.ServiceNames;
import monbulk.shared.widgets.Window.IWindow;
import monbulk.shared.widgets.Window.OkCancelWindow;

public class PreviewWindow extends OkCancelWindow implements IWindow, MethodUpdateHandler {

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
		_PreviewPanel = new HTMLPanel("");
		_framingPanel.add(_PreviewPanel);
		
		
	}
	public void loadPreview(StringBuilder tmpList,PreviewWindow.SupportedFormats inputFormat)
	{
		formattedText = tmpList;
		selectedFormat = inputFormat;
		_PreviewPanel.clear();
		TextArea tmpArea = new TextArea();
		tmpArea.setHeight("600px");
		tmpArea.setWidth("600px");
		//HTML output = new HTML();
		//output.setHTML(tmpList.toString());
		tmpArea.setText(formattedText.toString());
		_PreviewPanel.add(tmpArea);
		
		
		
	}
	public void loadPreview(String tmpList,PreviewWindow.SupportedFormats inputFormat)
	{
		
		selectedFormat = inputFormat;
		_PreviewPanel.clear();
		
		Button tmpButton = new Button();
		tmpButton.setText("SAVE");
		final TextArea tmpArea = new TextArea();
		tmpArea.setHeight("600px");
		tmpArea.setWidth("600px");
		//HTML output = new HTML();
		//output.setHTML(tmpList.toString());
		tmpList = tmpList.replace("<method>", "");
		tmpList = tmpList.replace("</method>", "");
		tmpArea.setText(tmpList);
		final MethodUpdateHandler tmpHandle = this;
		tmpButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				try
				{
					MethodService tmpSvc = MethodService.get();
					if(tmpSvc != null)
					{
						tmpSvc.createOrUpdate(tmpArea.getText(), tmpHandle);
							
					}
					else
					{
						throw new ServiceRegistry.ServiceNotFoundException(ServiceNames.Methods);
					}
				}
				catch (ServiceRegistry.ServiceNotFoundException e)
				{
					GWT.log("Couldn't find Method service");
				}
				
			}
			
		});
		
		Label Guide = new Label();
		Guide.setText("You can append the xml of the method below. Click save to upload the method.");
		_PreviewPanel.add(Guide);
		_PreviewPanel.add(tmpButton);
		_PreviewPanel.add(tmpArea);
		
		
		
	}
	@Override
	public void onUpdateMethod(String response) {
		AbsolutePanel _w_response = new AbsolutePanel();
		Label _r_text = new Label();
		if(response!=null)
		{
			_r_text.setText("Success: Your method has been created with ID: " + response);
		}
		else
		{
			if(response=="")
			{
				_r_text.setText("Success: Your method has been updated");
			}
		}
		_w_response.add(_r_text);
		this._PreviewPanel.clear();
		this._PreviewPanel.add(_w_response);
		
	}

}

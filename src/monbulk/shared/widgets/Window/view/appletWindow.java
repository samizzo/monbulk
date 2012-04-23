package monbulk.shared.widgets.Window.view;

import monbulk.client.event.WindowEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DialogBox;

public class appletWindow extends DialogBox
{
	private static appletWindowUiBinder uiBinder = GWT
			.create(appletWindowUiBinder.class);

	interface appletWindowUiBinder extends UiBinder<Widget, appletWindow> {	}

	private String m_id;
	private int m_minWidth;
	private int m_minHeight;
	private HandlerManager m_eventBus;

	public appletWindow(String title, String id, HandlerManager eventBus, Widget widget)
	{
		super(false, false, new appletWindowCaption(title, eventBus));
		appletWindowCaption c = (appletWindowCaption)getCaption();
		c.setParentApplet(this);

		m_id = id;
		m_eventBus = eventBus;
		
		if (widget == null)
		{
			widget = uiBinder.createAndBindUi(this);
		}

		setWidget(widget);
	}

	public void setMinSize(int minWidth, int minHeight)
	{
		m_minWidth = minWidth;
		m_minHeight = minHeight;
	}
	
    public String getId()
    {
    	return m_id;
    }
    
    public int getMinWidth()
    {
    	return m_minWidth;
    }
    
    public int getMinHeight()
    {
    	return m_minHeight;
    }
    
    protected void beginDragging(MouseDownEvent event)
    {
    	super.beginDragging(event);
    	m_eventBus.fireEvent(new WindowEvent(getId(), WindowEvent.EventType.ActivateWindow));
    }
}

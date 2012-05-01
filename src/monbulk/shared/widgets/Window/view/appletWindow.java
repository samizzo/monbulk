package monbulk.shared.widgets.Window.view;

import monbulk.client.event.WindowEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DialogBox;

public class appletWindow extends DialogBox
{
	private static appletWindowUiBinder uiBinder = GWT.create(appletWindowUiBinder.class);
	interface appletWindowUiBinder extends UiBinder<Widget, appletWindow> {	}

	private boolean m_isMaximised = false;
	private int m_prevWidth, m_prevHeight;
	private int m_prevX, m_prevY;
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
    
    public void maximise()
    {
    	int newWidth, newHeight;
    	int newX, newY;
		Widget w = getWidget();

		if (m_isMaximised)
		{
			// Restore dimensions.
			newWidth = m_prevWidth;
			newHeight = m_prevHeight;
			newX = m_prevX;
			newY = m_prevY;
		}
		else
		{
			// Maximise window.
			m_prevWidth = w.getOffsetWidth();
			m_prevHeight = w.getOffsetHeight();
			m_prevX = getPopupLeft();
			m_prevY = getPopupTop();

			newWidth = Window.getClientWidth() - (getOffsetWidth() - w.getOffsetWidth());
			newHeight = Window.getClientHeight() - (getOffsetHeight() - w.getOffsetHeight());
			newX = newY = 0;
		}

		boolean wasMaximised = m_isMaximised;
		w.setSize(newWidth + "px", newHeight + "px");
		setPopupPosition(newX, newY);
		m_isMaximised = !wasMaximised;

		if (m_isMaximised)
		{
			// Ensure the window is centred.
			center();
		}
    }
    
    // Returns true if we actually resized the height.
    public boolean resizeHeight(int deltay)
    {
    	int newHeight = getOffsetHeight() + deltay;
    	if (newHeight > getMinHeight())
    	{
			// Update the height of our wrapped widget.
			Widget widget = getWidget();
			newHeight = widget.getOffsetHeight() + deltay;
			widget.setHeight(newHeight + "px");
			
			// Window is no longer maximised.
			m_isMaximised = false;
			return true;
    	}
    	
    	return false;
    }
    
    // Returns true if we actually resized the width.
    public boolean resizeWidth(int deltax)
    {
		int newWidth = getOffsetWidth() + deltax;
    	if (newWidth > getMinWidth())
    	{
			// Update the width of our wrapped widget.
			Widget widget = getWidget();
			newWidth = widget.getOffsetWidth() + deltax;
			widget.setWidth(newWidth + "px");
			
			// Window is no longer maximised.
			m_isMaximised = false;
			return true;
    	}
    	
    	return false;
    }
}

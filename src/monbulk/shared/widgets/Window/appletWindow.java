package monbulk.shared.widgets.Window;

import monbulk.client.event.WindowEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DialogBox;
import monbulk.client.desktop.Desktop;

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

		m_id = id;
		m_eventBus = eventBus;
		
		if (widget == null)
		{
			widget = uiBinder.createAndBindUi(this);
		}

		setWidget(widget);
		
		c.setParentApplet(this);
	}
	public void setGlass(Boolean setGlass)
	{
		this.setGlassEnabled(setGlass);
		this.setGlassStyleName("glassPanel");
	}
	public void setTitle(String title)
	{
		appletWindowCaption c = (appletWindowCaption)getCaption();
		c.setText(title);
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
		// FIXME: This causes an ActivateWindow event to be fired
		// when we are closing the window, which means we do a
		// bringToFront just before closing, which means m_zindex
		// in Desktop is out of sync (i.e. will increase continually).
    	m_eventBus.fireEvent(new WindowEvent(getId(), WindowEvent.EventType.ActivateWindow));
    }
    
    /**
     * Resizes this window to fill the available space in the desktop.
     */
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

			int topNavHeight = Desktop.get().getTopNavHeight();
			newWidth = Window.getClientWidth() - (getOffsetWidth() - w.getOffsetWidth());
			newHeight = Window.getClientHeight() - (getOffsetHeight() - w.getOffsetHeight()) - topNavHeight;
			newX = 0;
			newY = topNavHeight;
		}

		boolean wasMaximised = m_isMaximised;
		w.setSize(newWidth + "px", newHeight + "px");
		setPopupPosition(newX, newY);
		m_isMaximised = !wasMaximised;
    }
    
    /**
     * Resizes the height of the window.  Returns true if we actually resized
     * the height.
     * @param deltay
     * @return
     */
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
    
    /**
     * Resizes the width of the window.  Returns true if we actually resized
     * the width.
     * @param deltax
     * @return
     */
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
    
    public WindowSettings getWindowSettings()
    {
    	Widget widget = getWidget();
    	if (widget instanceof IWindow)
    	{
    		IWindow window = (IWindow)widget;
    		return window.getWindowSettings();
    	}
    	
    	return null;
    }
    
	protected void onPreviewNativeEvent(NativePreviewEvent event)
	{
		Widget widget = getWidget();
		if (widget instanceof IWindow)
		{
			IWindow window = (IWindow)widget;
			window.onPreviewNativeEvent(event);
		}
	}
}

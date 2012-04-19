package monbulk.shared.widgets.Window.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.RootPanel;

import monbulk.shared.widgets.Window.WindowManager;
import monbulk.shared.widgets.Window.WindowProperties;
import monbulk.shared.widgets.Window.iWindow;

public class appletWindow extends DialogBox implements NativePreviewHandler, iWindow
{
	private static appletWindowUiBinder uiBinder = GWT
			.create(appletWindowUiBinder.class);

	interface appletWindowUiBinder extends UiBinder<Widget, appletWindow> {	}

    public String WindowTitle;
    private HandlerManager eventBus;
	private boolean m_activatedResize = false;
	private boolean m_resizeWidth = false;
	private boolean m_resizeHeight = false;
	private int m_lastMouseX;
	private int m_lastMouseY;
	private int m_minWidth;
	private int m_minHeight;
    
    @UiField
    HTMLPanel AppletContainer;
    
	public appletWindow(String tmpTitle, HandlerManager evntBus)
	{
		this(tmpTitle, evntBus, null);
	}

	public appletWindow(String title, HandlerManager eventBus, Widget widget)
	{
		super(new appletWindowCaption(title, eventBus));
		this.WindowTitle = title;
		this.eventBus = eventBus;
		
		if (widget == null)
		{
			widget = uiBinder.createAndBindUi(this);
		}

		setWidget(widget);
		
		Event.addNativePreviewHandler(this);
	}

	public void setMinSize(int minWidth, int minHeight)
	{
		m_minWidth = minWidth;
		m_minHeight = minHeight;
	}
	
	public HasWidgets getContainer()
	{
		return AppletContainer;
	}

	@Override
	public void onPreviewNativeEvent(NativePreviewEvent nativeEvent)
	{
		if (nativeEvent.getNativeEvent() instanceof Event)
		{
			Event event = (Event)nativeEvent.getNativeEvent();

			int eventType = event.getTypeInt();
			EventTarget target = event.getEventTarget();
			if (Element.is(target))
			{
				Element element = Element.as(target);
				if (element.getClassName().equals("dialogBottomRight"))
				{
					RootPanel.get().addStyleName("globalMoveCursor");

					if (eventType == Event.ONMOUSEDOWN)
					{
						// Mouse is over the resize grip so prepare for resizing.
						m_activatedResize = true;
						m_resizeWidth = m_resizeHeight = true;
						m_lastMouseX = event.getClientX();
						m_lastMouseY = event.getClientY();
						nativeEvent.cancel();
					}
				}
				else
				{
					RootPanel.get().removeStyleName("globalMoveCursor");
				}
			}
	
			if (m_activatedResize)
			{
				RootPanel.get().addStyleName("globalMoveCursor");

				switch (eventType)
				{
					case Event.ONMOUSEUP:
					{
						RootPanel.get().removeStyleName("globalMoveCursor");
						m_activatedResize = false;
						nativeEvent.cancel();
						break;
					}
					
					case Event.ONMOUSEMOVE:
					{
						int x = event.getClientX();
						int y = event.getClientY();
						int deltax = x - m_lastMouseX;
						int deltay = y - m_lastMouseY;
						m_lastMouseX = x;
						m_lastMouseY = y;
	
						boolean doResize = false;
						
						// If we aren't resizing the width (because we previously tried to
						// resize to less than the minimum), then we can resize again once
						// the cursor is past the edge of the panel.
						if (m_resizeWidth || (x > getAbsoluteLeft() + getOffsetWidth()))
						{
							int newWidth = getOffsetWidth() + deltax;
							if (newWidth > m_minWidth)
							{
								m_resizeWidth = true;
								setWidth(newWidth + "px");
								
								// Update the width of our wrapped widget.
								Widget w = getWidget();
								newWidth = w.getOffsetWidth() + deltax;
								w.setWidth(newWidth + "px");
								doResize = true;
							}
							else
							{
								m_resizeWidth = false;
							}
						}

						// If we aren't resizing the height (because we previously tried to
						// resize to less than the minimum), then we can resize again once
						// the cursor is past the bottom of the panel.
						if (m_resizeHeight || (y > getAbsoluteTop() + getOffsetHeight()))
						{
							int newHeight = getOffsetHeight() + deltay;						
							if (newHeight > m_minHeight)
							{
								m_resizeHeight = true;
								setHeight(newHeight + "px");

								// Update the width of our wrapped widget.
								Widget w = getWidget();
								newHeight = w.getOffsetHeight() + deltay;
								w.setHeight(newHeight + "px");
								doResize = true;
							}
							else
							{
								m_resizeHeight = false;
							}
						}
						
						if (doResize)
						{
							// Inform our wrapped widget that we resized.
							Widget w = getWidget();
							if (w instanceof RequiresResize)
							{
								((RequiresResize)w).onResize();
							}
						}
						
						nativeEvent.cancel();
						break;
					}
				}
			}
		}
	}

	@Override
	public String getWindowName() {
		// TODO Auto-generated method stub
		return this.getWindowName();
	}

	@Override
	public void setWindowName(String Name) {
		// TODO Auto-generated method stub
		this.setWindowName(Name);
	}

	@Override
	public void Shrink() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Maximise() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setManager(WindowManager winManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWindowSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProperties(WindowProperties wp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOwner(Object Owner) {
		DialogBox tmp = new DialogBox();
		
		//Not required yet
		
	}

	@Override
	public HasWidgets getChild() {
		// TODO Auto-generated method stub
		return  AppletContainer;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		this.hide();
		
	}
}

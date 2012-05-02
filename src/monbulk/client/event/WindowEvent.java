package monbulk.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class WindowEvent extends GwtEvent<WindowEventHandler>
{
	public enum EventType
	{
		ActivateWindow,
		CloseWindow,
		MaximiseWindow,
		Ok,
		Cancel,
	}
	
	public static Type<WindowEventHandler> TYPE = new Type<WindowEventHandler>();

	private String m_windowId;
	private EventType m_eventType;

	public WindowEvent(String windowId, EventType eventType)
	{
		m_windowId = windowId;
		m_eventType = eventType;
	}

	public String getWindowId()
	{
		return m_windowId;
	}
	
	public EventType getEventType()
	{
		return m_eventType;
	}
	 
	@Override
	public Type<WindowEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	@Override
	protected void dispatch(WindowEventHandler handler)
	{
		handler.onWindowEvent(this);
	}
}

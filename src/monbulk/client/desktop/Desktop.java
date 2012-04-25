package monbulk.client.desktop;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;

import monbulk.client.event.WindowEvent;
import monbulk.client.event.WindowEventHandler;
import monbulk.shared.widgets.Window.view.appletWindow;

public class Desktop extends Composite implements WindowEventHandler, NativePreviewHandler
{
	private static DesktopUiBinder uiBinder = GWT.create(DesktopUiBinder.class);
	interface DesktopUiBinder extends UiBinder<Widget, Desktop> { }

	@UiField VerticalPanel m_buttons;
	@UiField Label m_time;

	// TODO: Use a SimpleEventBus instead of HandlerManager.
	private HandlerManager m_eventBus = new HandlerManager(null);
	private HashMap<String, Launcher> m_windows = new HashMap<String, Launcher>();
	private Launcher m_resizingLauncher = null;
	private boolean m_activatedResize = false;
	private boolean m_resizeWidth = false;
	private boolean m_resizeHeight = false;
	private int m_lastMouseX;
	private int m_lastMouseY;

	private class Launcher implements ClickHandler
	{
		public PushButton m_button;
		public appletWindow m_applet;
		public String m_windowId;
		public String m_title;
		private boolean m_firstShow = true;
		
		public Launcher(String windowId, String title, Widget widget)
		{
			m_windowId = windowId;
			m_title = title;
			createAppletWindow(widget);
			createButton();
		}

		private void createAppletWindow(Widget widget)
		{
			appletWindow newWindow = new appletWindow(m_title, m_windowId, m_eventBus, widget);
			// HACK: So we can identify our window when processing native events.
			newWindow.getElement().setId("appletWindowId-" + m_windowId);
			newWindow.setStyleName("appWindow-Dialog");
			int width = 1000;
			int height = 670;
			newWindow.setHeight("100%");
			newWindow.setWidth("100%");
			//newWindow.setWidth(width + "px");
			//	newWindow.setHeight(height + "px");
			newWindow.setMinSize(width, height);
	
			// HACK: I would have preferred to do this in the ui.xml by setting
			// the width/height to 100% but I can't get it to work.
			//width -= 50;
			//height -= 100;
			//widget.setSize(width + "px", height + "px");
	
			newWindow.hide();
			m_applet = newWindow;
		}
		
		private void createButton()
		{
			m_button = new PushButton();
			m_button.setText(m_title);
			m_button.setStyleName(m_windowId + "Button");
			m_buttons.add(m_button);
			m_button.addClickHandler(this);
		}
		
		public void onClick(ClickEvent event)
		{
			if (m_firstShow)
			{
				m_applet.center();
				m_firstShow = false;
			}
			else if (!m_applet.isShowing())
			{
				m_applet.show();
			}

			bringToFront(this);
		}
	}

	public Desktop(RootPanel rootPanel)
	{
		initWidget(uiBinder.createAndBindUi(this));
		rootPanel.add(this);
		Event.addNativePreviewHandler(this);
		m_eventBus.addHandler(WindowEvent.TYPE, this);
		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand()
		{
			public boolean execute()
			{
				Date now = new Date();
				DateTimeFormat format = DateTimeFormat.getFormat("h:mm a");
				String time = format.format(now);
				m_time.setText(time);
				return true;
			}
		},
		1000);
	}
	
	// Registers a new window with the desktop environment.
	// windowId should only use alphanumeric characters.
	public void registerWindow(String windowId, String title, Composite window) throws Exception
	{
		if (m_windows.containsKey(windowId))
		{
			throw new Exception("Window '" + windowId + "' has already been registered!");
		}

		Launcher launcher = new Launcher(windowId, title, window);
		m_windows.put(windowId, launcher);
	}
	
	public void onWindowEvent(WindowEvent event)
	{
		String windowId = event.getWindowId();
		if (m_windows.containsKey(windowId))
		{
			Launcher launcher = m_windows.get(windowId);

			switch (event.getEventType())
			{
				case ActivateWindow:
				{
					bringToFront(launcher);
					break; 
				}
				
				case CloseWindow:
				{
					launcher.m_applet.hide();
					break;
				}
				
				case MaximiseWindow:
				{
					launcher.m_applet.maximise();
					break;
				}
			}
		}
	}

	public HandlerManager getEventBus()
	{
		return m_eventBus;
	}

	// Handle native events so we can perform window resizing.	
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

				Launcher launcher = m_resizingLauncher;
				if (launcher == null)
				{
					// If we aren't currently resizing a window, then find
					// the window this event is for.
					for (Launcher l : m_windows.values())
					{
						Element e = element;
						String name = "appletWindowId-" + l.m_windowId;
						while (e != null)
						{
							if (e.getId().equals(name))
							{
								launcher = l;
								break;
							}
		
							e = e.getParentElement();
						}
						
						if (launcher != null)
						{
							break;
						}
					}
				}

				if (launcher != null)
				{
					boolean overResizeGrip = element.getClassName().equals("dialogBottomRight");
					if (overResizeGrip)
					{
						// Mouse is over a window, we are currently not resizing a window,
						// and the mouse is over the resize grip.
						RootPanel.get().addStyleName("globalMoveCursor");
	
						if (eventType == Event.ONMOUSEDOWN)
						{ 
							// Mouse is down so prepare for resizing.
							m_resizingLauncher = launcher;
							m_activatedResize = true;
							m_resizeWidth = m_resizeHeight = true;
							m_lastMouseX = event.getClientX();
							m_lastMouseY = event.getClientY();
							bringToFront(launcher);
							nativeEvent.cancel();
						}
					}
					else
					{
						// Not over a resize grip and not currently resizing so remove cursor.
						RootPanel.get().removeStyleName("globalMoveCursor");
					}
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
						m_resizingLauncher = null;
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
	
						appletWindow w = m_resizingLauncher.m_applet;
						
						// If we aren't resizing the width (because we previously tried to
						// resize to less than the minimum), then we can resize again once
						// the cursor is past the edge of the panel.
						if (m_resizeWidth || (x > w.getAbsoluteLeft() + w.getOffsetWidth()))
						{
							m_resizeWidth = w.resizeWidth(deltax);
						}

						// If we aren't resizing the height (because we previously tried to
						// resize to less than the minimum), then we can resize again once
						// the cursor is past the bottom of the panel.
						if (m_resizeHeight || (y > w.getAbsoluteTop() + w.getOffsetHeight()))
						{
							m_resizeHeight = w.resizeHeight(deltay);
						}
						
						break;
					}
				}

				// NOTE: I think we only need to do a preventDefault, which
				// stops the browser from selecting text when resizing a
				// window, but just in case I'm doing all through.
				nativeEvent.cancel();
				nativeEvent.getNativeEvent().stopPropagation();
				nativeEvent.getNativeEvent().preventDefault();
			}
		}
	}
	
	private void bringToFront(Launcher launcher)
	{
		for (Launcher l : m_windows.values())
		{
			l.m_applet.removeStyleName("foregroundWindow");
		}
		
		launcher.m_applet.addStyleName("foregroundWindow");
	}
}

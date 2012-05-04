package monbulk.client.desktop;

import java.util.Date;
import java.util.HashMap;

import arc.mf.client.RemoteServer;

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
import com.google.gwt.user.client.ui.HTMLPanel;
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
import com.google.gwt.dom.client.Node;

import monbulk.client.event.WindowEvent;
import monbulk.client.event.WindowEventHandler;
import monbulk.shared.widgets.Window.*;

public class Desktop extends Composite implements WindowEventHandler, NativePreviewHandler
{
	private static Desktop s_instance = null;

	private static DesktopUiBinder uiBinder = GWT.create(DesktopUiBinder.class);
	interface DesktopUiBinder extends UiBinder<Widget, Desktop> { }

	@UiField VerticalPanel m_buttons;
	@UiField Label m_time;
	@UiField Label m_username;
	@UiField HTMLPanel m_topNav;

	// TODO: Use a SimpleEventBus instead of HandlerManager.
	private HandlerManager m_eventBus = new HandlerManager(null);
	private HashMap<String, Window> m_windows = new HashMap<String, Window>();
	private Window m_resizingWindow = null;
	private boolean m_activatedResize = false;
	private boolean m_resizeWidth = false;
	private boolean m_resizeHeight = false;
	private int m_lastMouseX;
	private int m_lastMouseY;
	private Window m_modalWindow;
	private int m_zindex = 100;

	// FIXME: This should be rolled into appletWindow.
	private class Window implements ClickHandler
	{
		public PushButton m_button;
		public appletWindow m_applet;
		private boolean m_firstShow = true;
		public WindowSettings m_windowSettings;
		
		public Window(IWindow widget)
		{
			m_windowSettings = widget.getWindowSettings();
			createAppletWindow((Widget)widget);

			if (m_windowSettings.createDesktopButton)
			{
				createButton();
			}
		}
		
		private boolean patchBottomRightElement(Element element)
		{
			String className = element.getClassName();
			if (className != null && className.equals("dialogBottomRight"))
			{
				element.replaceClassName("dialogBottomRight", "dialogBottomRightNoResize");
				return true;
			}
			else
			{
				int count = element.getChildCount();
				for (int i = 0; i < count; i++)
				{
					Node n = element.getChild(i);
					if (n instanceof Element)
					{
						if (patchBottomRightElement((Element)n))
						{
							return true;
						}
					}
				}

				return false;
			}
		}

		private void createAppletWindow(Widget widget)
		{
			WindowSettings ws = m_windowSettings;
			if (ws.width == -1 || ws.height == -1)
			{
				String width = ws.width == -1 ? "100%" : (ws.width + "px");
				String height = ws.height == -1 ? "100%" : (ws.height + "px");
				widget.setSize(width, height);
			}
			else
			{
				widget.setPixelSize(ws.width, ws.height);
			}

			appletWindow newWindow = new appletWindow(ws.windowTitle, ws.windowId, m_eventBus, widget);
			newWindow.setModal(ws.modal);

			// HACK: So we can identify our window when processing native events.
			newWindow.getElement().setId("appletWindowId-" + ws.windowId);
			newWindow.setStyleName("appWindow-Dialog");
			if (!ws.resizable)
			{
				patchBottomRightElement(newWindow.getElement());
			}
			newWindow.setHeight("100%");
			newWindow.setWidth("100%");
			newWindow.setMinSize(ws.minWidth, ws.minHeight);
			newWindow.hide();
			m_applet = newWindow;
		}
		
		private void createButton()
		{
			WindowSettings ws = m_windowSettings;
			m_button = new PushButton();
			m_button.setText(ws.windowTitle);
			m_button.setStyleName(ws.windowId + "Button");
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

	public Desktop(RootPanel rootPanel) throws Exception
	{
		if (s_instance != null)
		{
			throw new Exception("Desktop instance already found!  There should only be one Desktop instance!");
		}
		
		s_instance = this;

		initWidget(uiBinder.createAndBindUi(this));
		rootPanel.add(this);
		Event.addNativePreviewHandler(this);
		m_eventBus.addHandler(WindowEvent.TYPE, this);
		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand()
		{
			public boolean execute()
			{
				Date now = new Date();
				DateTimeFormat format = DateTimeFormat.getFormat("E h:mm a");
				String time = format.format(now);
				m_time.setText(time);
				return true;
			}
		},
		1000);
		
		String domain = RemoteServer.domain();
		String user = RemoteServer.user() + ((domain != null && domain.length() > 0) ? (" / " + domain) : "");
		m_username.setText(user);
	}

	// Returns the Desktop instance.
	public static Desktop get()
	{
		return s_instance;
	}
	
	// Shows the specified window.
	public IWindow show(String windowId, boolean centre)
	{
		Window w = findWindow(windowId);
		if (w != null)
		{
			if (w.m_windowSettings.modal)
			{
				m_modalWindow = w;
			}

			w.m_applet.show();
			bringToFront(w);
			
			if (centre)
			{
				w.m_applet.center();
			}
			
			return (IWindow)w.m_applet.getWidget();
		}
		
		return null;
	}
	
	public IWindow getWindow(String windowId)
	{
		Window w = findWindow(windowId);
		if (w != null)
		{
			return (IWindow)w.m_applet.getWidget();
		}
		
		return null;
	}
	
	// Hides the specified window.
	public void hide(String windowId)
	{
		Window w = findWindow(windowId);
		if (w != null)
		{
			hide(w);
		}
	}
	
	private void hide(Window window)
	{
		IWindow w = (IWindow)window.m_applet.getWidget();
		if (w instanceof IWindow.HideHandler)
		{
			IWindow.HideHandler handler = (IWindow.HideHandler)w;
			handler.onHide();
		}

		window.m_applet.hide();

		if (m_modalWindow == window)
		{
			m_modalWindow = null;
		}

		window.m_applet.removeStyleName("foregroundWindow");
		
		if (window.m_windowSettings.modal)
		{
			m_zindex--;
		}
	}
	
	// Registers a new window with the desktop environment.
	// The specified IWindow instance should also be a widget.
	public void registerWindow(IWindow widget) throws Exception
	{
		WindowSettings ws = widget.getWindowSettings();

		if (!(widget instanceof Widget))
		{
			throw new Exception("Window '" + ws.windowId + "' is not a widget!"); 
		}
		
		if (m_windows.containsKey(ws.windowId))
		{
			throw new Exception("Window '" + ws.windowId + "' has already been registered!");
		}

		Window window = new Window(widget);
		m_windows.put(ws.windowId, window);
	}
	
	public void onWindowEvent(WindowEvent event)
	{
		String windowId = event.getWindowId();
		if (m_windows.containsKey(windowId))
		{
			Window window = m_windows.get(windowId);

			switch (event.getEventType())
			{
				case ActivateWindow:
				{
					bringToFront(window);
					break; 
				}
				
				case CloseWindow:
				{
					hide(window);
					break;
				}
				
				case MaximiseWindow:
				{
					window.m_applet.maximise();
					break;
				}
			}
		}
	}

	public HandlerManager getEventBus()
	{
		return m_eventBus;
	}
	
	public int getTopNavHeight()
	{
		return m_topNav.getOffsetHeight();
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

				Window window = m_resizingWindow;
				if (window == null)
				{
					// If we aren't currently resizing a window, then find
					// the window this event is for.
					for (Window w : m_windows.values())
					{
						Element e = element;
						String name = "appletWindowId-" + w.m_windowSettings.windowId;
						while (e != null)
						{
							if (e.getId().equals(name))
							{
								window = w;
								break;
							}
		
							e = e.getParentElement();
						}
						
						if (window != null)
						{
							break;
						}
					}
				}

				boolean overResizeGrip = element.getClassName().equals("dialogBottomRight");
				if (window != null && overResizeGrip && (m_modalWindow == null || m_modalWindow == window) && window.m_windowSettings.resizable)
				{
					// Mouse is over a window, we are currently not resizing a window,
					// and the mouse is over the resize grip.
					RootPanel.get().addStyleName("globalMoveCursor");

					if (eventType == Event.ONMOUSEDOWN)
					{ 
						// Mouse is down so prepare for resizing.
						m_resizingWindow = window;
						m_activatedResize = true;
						m_resizeWidth = m_resizeHeight = true;
						m_lastMouseX = event.getClientX();
						m_lastMouseY = event.getClientY();
						bringToFront(window);
						nativeEvent.cancel();
					}
				}
				else
				{
					// Not over a resize grip and not currently resizing so remove cursor.
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
						m_resizingWindow = null;
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
	
						appletWindow w = m_resizingWindow.m_applet;
						
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
	
	private void bringToFront(Window window)
	{
		if (window.m_windowSettings.modal)
		{
			m_zindex++;
			window.m_applet.getElement().getStyle().setZIndex(m_zindex);
		}
		else
		{
			for (Window w : m_windows.values())
			{
				w.m_applet.removeStyleName("foregroundWindow");
			}
		}

		window.m_applet.addStyleName("foregroundWindow");
	}
	
	private Window findWindow(String windowId)
	{
		for (Window w : m_windows.values())
		{
			if (w.m_windowSettings.windowId.equals(windowId))
			{
				return w;
			}
		}
		
		return null;
	}
}

package monbulk.client.desktop;

import java.util.HashMap;

import monbulk.client.event.CloseWindowEvent;
import monbulk.shared.widgets.Window.view.appletWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;

import monbulk.client.event.CloseWindowEventHandler;

public class Desktop extends Composite implements CloseWindowEventHandler
{
	private static DesktopUiBinder uiBinder = GWT.create(DesktopUiBinder.class);
	interface DesktopUiBinder extends UiBinder<Widget, Desktop> { }

	@UiField VerticalPanel m_buttons;
	
	// TODO: Use a SimpleEventBus instead of HandlerManager.
	private HandlerManager m_eventBus = new HandlerManager(null);

	private class Launcher implements ClickHandler
	{
		public PushButton m_button;
		public appletWindow m_applet;
		public String m_tokenName;
		public String m_title;
		private boolean m_firstShow = true;
		
		public Launcher(String tokenName, String title, Widget widget)
		{
			m_tokenName = tokenName;
			m_title = title;
			createAppletWindow(widget);
			createButton();
		}

		private void createAppletWindow(Widget widget)
		{
			appletWindow newWindow = new appletWindow(m_title, m_tokenName, m_eventBus, widget);
			newWindow.setStyleName("appWindow-Dialog");
			newWindow.setModal(true);
			int width = 1000;
			int height = 670;
			newWindow.setWidth(width + "px");
			newWindow.setHeight(height + "px");
			newWindow.setMinSize(width, height);
	
			// HACK: I would have preferred to do this in the ui.xml by setting
			// the width/height to 100% but I can't get it to work.
			width -= 50;
			height -= 100;
			widget.setSize(width + "px", height + "px");
	
			newWindow.hide();
			m_applet = newWindow;
		}
		
		private void createButton()
		{
			m_button = new PushButton();
			m_button.setText(m_title);
			m_button.setStyleName(m_tokenName + "Button");
			m_buttons.add(m_button);
			m_button.addClickHandler(this);
		}
		
		public void onClick(ClickEvent event)
		{
			if (m_firstShow)
			{
				m_applet.center();
			}
			else
			{
				m_applet.show();
			}
		}
	}

	private HashMap<String, Launcher> m_windows = new HashMap<String, Launcher>();

	public Desktop(RootPanel rootPanel)
	{
		initWidget(uiBinder.createAndBindUi(this));
		rootPanel.add(this);
		
		m_eventBus.addHandler(CloseWindowEvent.TYPE, this);
	}
	
	// Registers a new window with the desktop environment.
	// tokenName should only use alphanumeric characters.
	public void registerWindow(String tokenName, String title, Composite window) throws Exception
	{
		if (m_windows.containsKey(tokenName))
		{
			throw new Exception("Window '" + tokenName + "' has already been registered!");
		}

		Launcher launcher = new Launcher(tokenName, title, window);
		m_windows.put(tokenName, launcher);
	}
	
	public void onCloseWindow(CloseWindowEvent event)
	{
		String token = event.getId();
		if (m_windows.containsKey(token))
		{
			Launcher launcher = m_windows.get(token);
			launcher.m_applet.hide();
		}
	}
	
	public HandlerManager getEventBus()
	{
		return m_eventBus;
	}
}

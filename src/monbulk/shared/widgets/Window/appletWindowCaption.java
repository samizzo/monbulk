package monbulk.shared.widgets.Window;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.DialogBox.Caption;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.InlineLabel;

import monbulk.client.event.WindowEvent;

public class appletWindowCaption extends Composite implements Caption
{
	private static appletWindowCaptionUiBinder uiBinder = GWT.create(appletWindowCaptionUiBinder.class);

	interface appletWindowCaptionUiBinder extends UiBinder<Widget, appletWindowCaption> { }

    private HandlerManager m_eventBus;
    private appletWindow m_parentWindow;

    @UiField PushButton m_close;
    @UiField PushButton m_maximise;
    @UiField InlineLabel m_title;

    public void setParentApplet(appletWindow parent)
    {
		m_parentWindow = parent;
		WindowSettings ws = m_parentWindow.getWindowSettings();
		if (ws != null)
		{
			m_maximise.setVisible(ws.resizable);
		}
    }
    
	public appletWindowCaption(String title, HandlerManager eventBus)
	{	
		m_eventBus = eventBus;
    	initWidget(uiBinder.createAndBindUi(this));
    	m_title.setText(title);
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler)
	{
		return null;
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler)
	{
		return null;
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler)
	{
		return null;
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler)
	{
		return null;
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler)
	{
		return null;
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler)
	{
		return null;
	}

	@Override
	public String getHTML()
	{
		return null;
	}

	@Override
	public void setHTML(String html)
	{
	}

	@Override
	public String getText()
	{
		return m_title.getText();
	}

	@Override
	public void setText(String text)
	{
		m_title.setText(text);
	}

	@Override
	public void setHTML(SafeHtml html)
	{
	}

	@UiHandler("m_close")
	public void onCloseClick(ClickEvent e)
	{
		m_eventBus.fireEvent(new WindowEvent(m_parentWindow.getId(), WindowEvent.EventType.CloseWindow));
	}
	
	@UiHandler("m_maximise")
	public void onMaximiseClick(ClickEvent event)
	{
		WindowSettings ws = m_parentWindow.getWindowSettings();
		if (ws != null && ws.resizable)
		{
			m_eventBus.fireEvent(new WindowEvent(m_parentWindow.getId(), WindowEvent.EventType.MaximiseWindow));
		}
	}
}

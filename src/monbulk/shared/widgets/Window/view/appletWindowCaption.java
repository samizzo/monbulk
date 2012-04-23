package monbulk.shared.widgets.Window.view;

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

    private HandlerManager eventBus;
    private appletWindow m_parentWindow;

    @UiField 
	PushButton btnClose;
    
    @UiField
    InlineLabel WindowTitle;

    public void setParentApplet(appletWindow parent)
    {
		m_parentWindow = parent;
    }
    
	public appletWindowCaption(String Title, HandlerManager evntBus)
	{	
		this.eventBus = evntBus;
		
    	initWidget(uiBinder.createAndBindUi(this));
    	this.WindowTitle.setText(Title);
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
		return null;
	}

	@Override
	public void setText(String text)
	{
	}

	@Override
	public void setHTML(SafeHtml html)
	{
	}

	@UiHandler("btnClose")
	public void onClick(ClickEvent e)
	{
		eventBus.fireEvent(new WindowEvent(m_parentWindow.getId(), WindowEvent.EventType.CloseWindow));
	}
}

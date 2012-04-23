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

import monbulk.client.event.CloseWindowEvent;

public class appletWindowCaption extends Composite implements Caption {

	private static appletWindowCaptionUiBinder uiBinder = GWT
			.create(appletWindowCaptionUiBinder.class);

	interface appletWindowCaptionUiBinder extends
			UiBinder<Widget, appletWindowCaption> {
	}
    private HandlerManager eventBus;
    
    @UiField 
	PushButton btnClose;
    
    @UiField
    InlineLabel WindowTitle;
    
    private appletWindow m_parentWindow;
    
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
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHTML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHTML(String html) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHTML(SafeHtml html) {
		// TODO Auto-generated method stub
		
	}
	@UiHandler("btnClose")
	public void onClick(ClickEvent e)
	{
		eventBus.fireEvent(new CloseWindowEvent(m_parentWindow.getTokenName()));
	}
}

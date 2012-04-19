package daris.Monbulk.shared.widgets.Window.view;

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

import daris.Monbulk.MethodBuilder.client.event.ChangeWindowEvent;
import daris.Monbulk.client.event.CloseWindowEvent;

public class appletWindowCaption extends Composite implements Caption {

	private static appletWindowCaptionUiBinder uiBinder = GWT
			.create(appletWindowCaptionUiBinder.class);

	interface appletWindowCaptionUiBinder extends
			UiBinder<Widget, appletWindowCaption> {
	}
    private String strTitle;
    private HandlerManager eventBus;
    
    @UiField 
	PushButton btnClose;
    
    @UiField
    InlineLabel WindowTitle;
    
	public appletWindowCaption(String Title,HandlerManager evntBus) {
		
		this.strTitle = Title;
		this.eventBus = evntBus;
		
		//this.WindowTitle.setText(Title);
		
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
		// FIXME: This is a dirty hack.  This class shouldn't care what the title
		// of the window is.
		if (strTitle != "Method Builder" && strTitle != "Metadata Editor")
		{
			//Needs to be more elegant for Subwindows not registered - or else we register!!!
			eventBus.fireEvent(new  ChangeWindowEvent(strTitle,"Close"));
		}
		else
		{
			eventBus.fireEvent(new CloseWindowEvent(strTitle));
			
		}
		
	}
}

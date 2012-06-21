package monbulk.MethodBuilder.client.view;

import monbulk.shared.Architecture.ISearchController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

// FIXME: Not sure why this extends Composite, because the SearchWidget instance itself isn't used anywhere.
public class SearchWidget extends Composite implements ClickHandler {

		private PushButton _btnSearch;
		private final TextBox _tbSearchText;
		private HTMLPanel _Filters;
		private HorizontalPanel _HeaderWidget;
		private final ISearchController _Controller;
		public SearchWidget(ISearchController tmpControl) {
		this._Controller = tmpControl;
		this._tbSearchText = new TextBox();
		this._tbSearchText.setStyleName("tbSearchBox");
		try
		{
		 this._HeaderWidget = new HorizontalPanel();
		 this._Filters = new HTMLPanel("");
		_btnSearch = new PushButton();
		_btnSearch.setStyleName("btnSearch");
		
		
		this._tbSearchText.addChangeHandler(new ChangeHandler()
		{

			@Override
			public void onChange(ChangeEvent event) {
				
				//Dispatch a SearchEvent
				_Controller.Filter(_tbSearchText.getValue());
				
			}
			
		});
		this._tbSearchText.addKeyUpHandler(new KeyUpHandler(){

						@Override
			public void onKeyUp(KeyUpEvent event) {
				int keyCode = event.getNativeKeyCode();
				if ((keyCode >= 'a' && keyCode <= 'z') ||
					(keyCode >= 'A' && keyCode <= 'Z' ) ||
					(keyCode == '.') ||
					(keyCode == KeyCodes.KEY_BACKSPACE) ||
					(keyCode == KeyCodes.KEY_DELETE))
				{
					_Controller.Filter(_tbSearchText.getValue());
				}
				
			}
			
		});
		this._btnSearch.addClickHandler(this);
		_HeaderWidget = new HorizontalPanel();
		this._HeaderWidget.add(this._btnSearch);
		this._HeaderWidget.add(this._tbSearchText);
		
		this._tbSearchText.setWidth("140px");
		_HeaderWidget.setHeight("35px");
		_HeaderWidget.setStyleName("searchWidget");
		}
		catch(Exception ex)
		{
			GWT.log("Error Occurs @ SearchWidget.Construct" + ex.getMessage() + ex.getCause());
		}
		
	}
	public Widget getHeaderWidget()
	{
		return this._HeaderWidget.asWidget();
	}
	/**
	 * Not Implemented - should return the filters widget
	 * 
	 * @return Widget 	Returns the widget for search filters
	 */
	public Widget getBodyWidget()
	{
		return null;
	}
	@Override
	public void onClick(ClickEvent event) {
		
		_Controller.Filter(_tbSearchText.getValue());
	}

}

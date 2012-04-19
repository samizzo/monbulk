package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import monbulk.MethodBuilder.client.event.MenuChangeEvent;
import monbulk.MethodBuilder.shared.MenuCommand;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoMethodComplete;
import monbulk.shared.Services.MethodService;
import monbulk.shared.Services.ServiceRegistry;
import monbulk.shared.util.MonbulkEnums;
import monbulk.shared.view.iMenuWidget;

public class MethodList extends Composite implements iMenuWidget, MethodService.MethodServiceHandler {

	private static MethodListUiBinder uiBinder = GWT
			.create(MethodListUiBinder.class);

	interface MethodListUiBinder extends UiBinder<Widget, MethodList> {
	}
	private final HandlerManager eventBus;
	private String ActiveClassName;
	private String PassiveClassName;
	
	@UiField
	MenuBar _MethodList;
	
	@UiField
	PushButton _Newbutton;
	
	public MethodList(HandlerManager eBus) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = eBus;
		
		try
		{
			MethodService service = (MethodService)ServiceRegistry.getService(MonbulkEnums.ServiceNames.Methods);
			service.getMethodList(this);
		}
		catch (ServiceRegistry.ServiceNotFoundException e)
		{
			GWT.log("Couldn't find Method service");
		}
		_Newbutton.setText("Add New Method");
	}
	public MethodList(HandlerManager tmpEvent, String menuClassName, String activeClassName, String pClassName)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = tmpEvent;
		this._MethodList.setStyleName(menuClassName);
		this.ActiveClassName = activeClassName;
		this.PassiveClassName = pClassName;
		try
		{
			MethodService service = (MethodService)ServiceRegistry.getService(MonbulkEnums.ServiceNames.Methods);
			service.getMethodList(this);
		}
		catch (ServiceRegistry.ServiceNotFoundException e)
		{
			GWT.log("Couldn't find metadata service");
		}
		this._MethodList.setSize("137px", "400px");
		
		
	}


	@UiHandler("_Newbutton")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

	public void setText(String text) {
		_Newbutton.setText(text);
	}

	public String getText() {
		return _Newbutton.getText();
	}

	@Override
	public HasWidgets getBaseWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActiveMenu(String activeItem) {
		//this._MethodList.
		//Need to extend MenuBar to allow for search and removal of MenuItem 
	}

	@Override
	public void populateItems(List<String> tmpArray) {
		
		Iterator<String> it = tmpArray.iterator();
		while(it.hasNext())
		{
			//What this should be is a 2d array with a string for the text and a string for the command to call
			//this.addItem(new MenuItem());
			String MenuText = it.next();
			MenuCommand tmpCommand = new MenuCommand(MenuText, eventBus);
			MenuItem tmpButton = new MenuItem(MenuText,tmpCommand);
			tmpButton.setText(MenuText);
			
			ClickHandler tmpHandler = new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					
					eventBus.fireEvent(new MenuChangeEvent("" + event.hashCode()));
					
				}
			};
			
			MenuItem tmpItem = new MenuItem(MenuText,tmpCommand);
			tmpItem.setStyleName(this.PassiveClassName);
			this._MethodList.addItem(tmpButton);
			
		}
		
	}
	protected void OnClickMenu(ClickEvent e)
	{
		
	}
	@Override
	public void onReadMethodList(ArrayList<pojoMethod> arrMethods) {
		Iterator<pojoMethod> it = arrMethods.iterator();
		//arrMethods.v
		while(it.hasNext())
		{
			//What this should be is a 2d array with a string for the text and a string for the command to call
			//this.addItem(new MenuItem());
			pojoMethod tmpMethod = it.next();
			MenuCommand tmpCommand = new MenuCommand(tmpMethod.getMethodID(), eventBus);
			MenuItem tmpItem= new MenuItem(tmpMethod.getFieldVale(pojoMethod.MethodNameField),tmpCommand);
			
			tmpItem.setStyleName(this.PassiveClassName);
			this._MethodList.addItem(tmpItem);
			
		}
		
	}
	@Override
	public void onReadMethod(pojoMethodComplete method) {
		// TODO Auto-generated method stub
		
	}


}

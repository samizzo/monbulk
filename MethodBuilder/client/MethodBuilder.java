package daris.Monbulk.MethodBuilder.client;

import java.util.ArrayList;
import java.util.List;

import arc.gui.gwt.widget.ContainerWidget;
import arc.gui.gwt.widget.window.Window;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import daris.Monbulk.MethodBuilder.client.event.MenuChangeEvent;
import daris.Monbulk.MethodBuilder.client.event.MenuChangeEventHandler;
import daris.Monbulk.MethodBuilder.client.presenter.MethodCreatorPresenter;
import daris.Monbulk.MethodBuilder.client.view.MethodBuilderMaster;
import daris.Monbulk.MethodBuilder.client.view.MethodBuilderMenu;
import daris.Monbulk.MethodBuilder.client.view.MethodList;


import daris.Monbulk.shared.Architecture.IPresenter.DockedPresenter;
import daris.Monbulk.shared.Services.MetadataService;
import daris.Monbulk.shared.Services.MethodService;
import daris.Monbulk.shared.Services.ServiceRegistry;
import daris.Monbulk.shared.util.MonbulkEnums;
import daris.Monbulk.shared.view.ILayoutWidget;
import daris.Monbulk.shared.view.iMenuWidget;
import daris.Monbulk.shared.widgets.Window.view.appletWindow;
import daris.Monbulk.shared.baseAppletManager;
import daris.Monbulk.shared.iAppletManager;

public class MethodBuilder extends baseAppletManager implements iAppletManager {
	private ILayoutWidget LayoutPanel;
	private iMenuWidget AppletMenu;
	private DockedPresenter CurrentPresenter;
	private String CurrentState;
	
	public MethodBuilder(HandlerManager evtBus,HasWidgets ParentContainer)
	{
		super(evtBus, ParentContainer);
		
		//Could be set in a Constructor - you select from any master file so layout can be dynamic
		//May look at applying device specific layouts via an XML file 
		MethodBuilderMaster tmpPanel = new MethodBuilderMaster();
		
		this.SetLayout((Widget)tmpPanel);
		
		
		//setMenu - Sets the Items for the menu - can be bound to a DataItem
		this.setMenu();
		
		//Binds Events
		bind();		
	}

	// Constructor that takes a gwt HandlerManager and an arc ContainerWidget.
	public MethodBuilder(HandlerManager evtBus, ContainerWidget MfluxPanel)
	{
		super(evtBus, null);
		super.setWindowContainer(MfluxPanel);
		MethodBuilderMaster tmpPanel = new MethodBuilderMaster();
		LayoutPanel = (ILayoutWidget) tmpPanel;
		if(this.getWindowContainer()!=null)
		{
			this.getWindowContainer().add((Widget)tmpPanel);
		}
		
		this.SetChildContainer(tmpPanel.getBodyContainer());
		
		//setMenu - Sets the Items for the menu - can be bound to a DataItem
		this.setMenu();
		
		//Binds Events
		bind();
	}

	//We should also be able to launch from a position in History...
	//Which means knowing the presenter state and the model being used
	@Override
	public void ChangeAppletState(String initState)
	{
		if(!initState.equals(this.CurrentState))
		{
			doMenuTransition(initState);
			CleanPanels();
			
			if(initState.equals("InitialiseMethodBuilder"))
			{
				this.CurrentState="Create Method";
				this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus);
				this.CurrentPresenter.go(this.LayoutPanel.getBodyContainer(),this.LayoutPanel.getDockContainer(),this.LayoutPanel.getNavigationContainer());
			}
			else if(initState.equals("Create Method"))
			{
				///Basically we need to provide Panels for adding widgets to
				//And we need to launch a Presenter to control with a DefaultState 
				//
					this.CurrentState="Create Method";
					this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus);
					this.CurrentPresenter.go(this.LayoutPanel.getBodyContainer(),this.LayoutPanel.getDockContainer(),this.LayoutPanel.getNavigationContainer());
			
			}
			else if(initState.equals("Edit Methods"))
			{
				this.CurrentState="Edit Methods";
			}
			else if(initState.equals("Save"))
			{
				
			}
			else if(initState.equals("Publish"))
			{
				
			}
			else
			{
				this.CurrentState="Edit Method";
				this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus);
				this.CurrentPresenter.go(this.LayoutPanel.getBodyContainer(),this.LayoutPanel.getDockContainer(),this.LayoutPanel.getNavigationContainer());
				//this.CurrentPresenter.
			}
		}
		
	}
	public void CleanPanels()
	{
		this.LayoutPanel.getBodyContainer().clear();
		this.LayoutPanel.getDockContainer().clear();
	}
	@Override
	public void ChangeAppletState()
	{
		ChangeAppletState("Create Method");
	}
	private void bind() {
	    
	    eventBus.addHandler(MenuChangeEvent.TYPE,
	    		new MenuChangeEventHandler(){
	    			public void onMenuChange(MenuChangeEvent event)
	    			{
	    				 ChangeAppletState(event.getId());
	    			}

	    		}
	    );	       
   }
	private void doMenuTransition(String ID)
	{
		AppletMenu.setActiveMenu(ID);
	}
	
	@Override
	public Widget getShrunkWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Widget getMenuWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Widget getDesktopIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMenu() {
		
		try
		{
			this.AppletMenu = (iMenuWidget) new MethodList(this.eventBus,"WindowMenu","ActiveTab","gwt-MenuItem");
		//List<String> MenuItems =  new ArrayList<String>();
		//MenuItems.add("Create Method");
		//MenuItems.add("Edit Methods");
		//AppletMenu.populateItems(MenuItems);
			LayoutPanel.getMenuContainer().add((Widget) AppletMenu);
		}
		catch(Exception ex)
		{
			GWT.log("Something is breaking",ex);
		}
		return;
	}

	@Override
	public void SetLayout(Widget Layout) {
		// TODO Auto-generated method stub
		LayoutPanel = (ILayoutWidget) Layout;
		if(this.getParentContainer()!=null)
		{
			this.getParentContainer().add((Widget)LayoutPanel);
		}
		
		this.SetChildContainer(LayoutPanel.getBodyContainer());
			
	
	}
	public void show(Window owner)
	{
		/* TODO need to implement Arcitecta FIX {
	if (_showing) {
		_win.close();
	}
	WindowProperties wp = new WindowProperties();
	wp.setModal(false);
	wp.setTitle("Method Builder");
	wp.setCanBeResized(true);
	wp.setCanBeClosed(true);
	wp.setCanBeMoved(true);
	wp.setOwnerWindow(owner);
	wp.setSize(1000, 800);
	wp.setCenterInPage(true);
	_win = Window.create(wp);
	_win.addCloseListener(new WindowCloseListener() {

		@Override
		public void closed(Window w) {
			_showing = false;
		}
	});
	Basically I need to make an extension panel with hasWIdgets and BaseWidget!!!
	AbsolutePanel tmpPanel = new AbsolutePanel();
	tmpPanel.add(this.LayoutPanel.asWidget());
	_win.setContent(tmpPanel);
	_win.centerInPage();
	_win.show();
	_showing = true;*/
	  appletWindow newWindow = new appletWindow("Method Name",this.eventBus);
	  newWindow.setModal(true);
	  newWindow.setWidth("1000px");
	  newWindow.show();
	  newWindow.center();
	  newWindow.setStyleName("appWindow-Dialog");
	  newWindow.getContainer().add(this.LayoutPanel.asWidget());
	  
	  //Bizarre Bug which tells me the widgets parent does not implement hasWidgets
	  //this.LayoutPanel.
	  this.ChangeAppletState();
	  
	}
}

package monbulk.MethodBuilder.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import monbulk.MethodBuilder.client.event.MenuChangeEvent;
import monbulk.MethodBuilder.client.event.MenuChangeEventHandler;
import monbulk.MethodBuilder.client.presenter.MethodCreatorPresenter;
import monbulk.MethodBuilder.client.view.MethodBuilderMaster;
import monbulk.MethodBuilder.client.view.MethodList;

import monbulk.shared.Architecture.IPresenter.DockedPresenter;
import monbulk.shared.view.iMenuWidget;
import monbulk.shared.widgets.Window.*;
import com.google.gwt.user.client.ui.ResizeComposite;

public class MethodBuilder extends ResizeComposite implements IWindow
{
	private iMenuWidget AppletMenu;
	private DockedPresenter CurrentPresenter;
	private String CurrentState;
	private HandlerManager eventBus;
	private WindowSettings m_windowSettings;
	
	public MethodBuilder(HandlerManager evtBus)
	{
		m_windowSettings = new WindowSettings();
		m_windowSettings.width = 1200;
		m_windowSettings.height = 800;
		m_windowSettings.minWidth = 1200;
		m_windowSettings.minHeight = 800;
		m_windowSettings.windowId = "MethodBuilder";
		m_windowSettings.windowTitle = "Method Builder";

		eventBus = evtBus;
		
		MethodBuilderMaster m = new MethodBuilderMaster();
		initWidget(m);
		setMenu();
		ChangeAppletState();
		
		//Binds Events
		bind();		
	}

	//We should also be able to launch from a position in History...
	//Which means knowing the presenter state and the model being used

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
				this.CurrentPresenter.go(getBodyContainer(),getDockContainer(),getNavigationContainer());
			}
			else if(initState.equals("Create Method"))
			{
				///Basically we need to provide Panels for adding widgets to
				//And we need to launch a Presenter to control with a DefaultState 
				//
					this.CurrentState="Create Method";
					this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus);
					this.CurrentPresenter.go(getBodyContainer(),getDockContainer(),getNavigationContainer());
			
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
			else if(initState.contains("Edit"))
			{
				this.CurrentState="Edit Method";
				
				String ID = initState.replace("Edit:", "");
				MethodCreatorPresenter mcp = new MethodCreatorPresenter(this.eventBus, ID);
				
				this.CurrentPresenter = mcp;
				
				this.CurrentPresenter.go(getBodyContainer(),getDockContainer(),getNavigationContainer());
				
				//this.CurrentPresenter.
			}
		}
		
	}
	public void CleanPanels()
	{
		this.getBodyContainer().clear();
		this.getDockContainer().clear();
	}
	
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
	
	public void setMenu()
	{
		try
		{
			AppletMenu = (iMenuWidget) new MethodList(this.eventBus,"WindowMenu","ActiveTab","gwt-MenuItem");
			getMenuContainer().add((Widget) AppletMenu);
		}
		catch(Exception ex)
		{
			GWT.log("Something is breaking",ex);
		}
		return;
	}
	
	private HasWidgets getBodyContainer()
	{
		MethodBuilderMaster m = (MethodBuilderMaster)getWidget();
		return m.getBodyContainer();
	}
	
	private HasWidgets getMenuContainer()
	{
		MethodBuilderMaster m = (MethodBuilderMaster)getWidget();
		return m.getMenuContainer();
	}
	
	private HasWidgets getDockContainer()
	{
		MethodBuilderMaster m = (MethodBuilderMaster)getWidget();
		return m.getDockContainer();
	}
	
	private HasWidgets getNavigationContainer()
	{
		MethodBuilderMaster m = (MethodBuilderMaster)getWidget();
		return m.getNavigationContainer();
	}
	
	public WindowSettings getWindowSettings()
	{
		return m_windowSettings;
	}
}

package monbulk.MethodBuilder.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import monbulk.MethodBuilder.client.event.MenuChangeEvent;
import monbulk.MethodBuilder.client.event.MenuChangeEventHandler;
import monbulk.MethodBuilder.client.presenter.MethodCreatorPresenter;
import monbulk.MethodBuilder.client.view.MethodBuilderMaster;
import monbulk.MethodBuilder.client.view.MethodList;

import monbulk.client.desktop.Desktop;
import monbulk.shared.Architecture.IPresenter.DockedPresenter;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Events.DragEvent;
import monbulk.shared.Events.DragEventHandler;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Services.Dictionary;
import monbulk.shared.Services.DictionaryService.GetDictionaryHandler;
import monbulk.shared.util.GWTLogger;
import monbulk.shared.view.iMenuWidget;
import monbulk.shared.widgets.Window.*;
import com.google.gwt.user.client.ui.ResizeComposite;

public class MethodBuilder extends ResizeComposite implements IWindow,GetDictionaryHandler
{
	private iMenuWidget AppletMenu;
	private FormPresenter CurrentPresenter;
	private String CurrentState;
	private HandlerManager eventBus;
	private WindowSettings m_windowSettings;
	private String loadedMethodID;
	private String loadedMethodName;
	private Boolean isConfirmed;
	public enum PreLoadedLists{STUDY_TYPES,DICOM_MODALITY,METADATA}
	private HashMap<PreLoadedLists,ArrayList<String>> loadedLists;
	
	public MethodBuilder(HandlerManager evtBus)
	{
		
		
		m_windowSettings = new WindowSettings();
		m_windowSettings.width = 1025;
		m_windowSettings.height = 700;
		m_windowSettings.minWidth = 700;
		m_windowSettings.minHeight = 680;
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
	private void loadMethod(String ID, String Name)
	{
		this.loadedMethodID = ID;
		this.loadedMethodName = Name;
	}
	//We should also be able to launch from a position in History...
	//Which means knowing the presenter state and the model being used

	private Boolean checkMethodState(String initState)
	{
		Desktop d = Desktop.get();
		final PreviewWindow m = (PreviewWindow )d.getWindow("MethodPreviewWindow");
		if(this.CurrentPresenter!=null)
		{
			if(!initState.equals("InitialiseMethodBuilder"))
			{
				if(this.CurrentPresenter.isModified())
				{
					if(m.getShouldConfirmStatus())
					{
						m.confirmStateChange(initState, this.loadedMethodID);
						d.show(m, true);
						return true;
					}
					else
					{
						m.setShouldConfirmStatus(true);
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		
	}
	public void ChangeAppletState(String initState)
	{
		
		if(!initState.equals(this.CurrentState))
		{
			//doMenuTransition(initState);
			if(initState.equals("Refresh"))
			{
				CleanPanels();
				MethodList tmpMenu = (MethodList) this.AppletMenu;
				tmpMenu.refreshList();
				this.CurrentState="Create Method";
				if(this.loadedMethodID=="")
				{
					this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus);
					this.AppletMenu.setActiveMenu("");
				}
				else
				{
					this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus,this.loadedMethodID);
				}
				this.CurrentPresenter.go(getBodyContainer(),getNavigationContainer());
				return;
			}
			else if(!this.checkMethodState(initState))
			{
				CleanPanels();
			
				if(initState.equals("InitialiseMethodBuilder"))
				{
					this.CurrentState="Create Method";
					this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus);
					this.CurrentPresenter.go(getBodyContainer(),getNavigationContainer());
				}
				else if(initState.equals("Create Method"))
				{
					///Basically we need to provide Panels for adding widgets to
					//And we need to launch a Presenter to control with a DefaultState 
					//
						this.CurrentState="Create Method";
						this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus);
						this.CurrentPresenter.go(getBodyContainer(),getNavigationContainer());
						this.AppletMenu.setActiveMenu("");
				}
				
				
				else if(initState.equals("NewMethod"))
				{
					///Basically we need to provide Panels for adding widgets to
					//And we need to launch a Presenter to control with a DefaultState 
					//
						this.CurrentState="Create Method";
						this.CurrentPresenter = new MethodCreatorPresenter(this.eventBus);
						this.CurrentPresenter.go(getBodyContainer(),getNavigationContainer());
						this.AppletMenu.setActiveMenu("");
				
				}
				else if(initState.equals("Edit Methods"))
				{
					this.CurrentState="Edit Methods";
				}
			
				else if(initState.contains("Edit"))
				{
					this.CurrentState="Edit Method";
					try
					{
						//String ID = initState.replace("Edit:", "");
						MethodCreatorPresenter mcp = new MethodCreatorPresenter(this.eventBus, this.loadedMethodID);
						this.CurrentPresenter = mcp;
						this.CurrentPresenter.go(getBodyContainer(),getNavigationContainer());
					}
					catch(Exception ex)
					{
						GWTLogger.Log("Error occurs @ MethodBuilder.ChangeAppletState:", "MethodBuilder", "ChangeAppletState", "121");
					}
					//this.CurrentPresenter.
				}
				this.isConfirmed=false;
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
	    				loadedMethodID = event.getNewID();
	    				 ChangeAppletState(event.getId());
	    			}
	    		}
	    );	     
	    eventBus.addHandler(DragEvent.TYPE,
	    		new DragEventHandler(){
	    			@Override
					public void onDrag(DragEvent event) {
						//this.loadedMethodID = event.getPojo().getFieldVale(pojoMethod.MethodIDField);
						//this.loadedMethodID = event.getPojo().getFieldVale(pojoMethod.MethodIDField);
	    				if(event.getId()=="NewMethod")
	    				{
	    					ChangeAppletState("NewMethod");
	    				}
	    				else
	    				{
	    					loadMethod(event.getPojo().getFieldVale(pojoMethod.MethodIDField),event.getName());
	    					ChangeAppletState(event.getId());
	    				}
					}
	    		}
	    );
	    
   }

	public void setMenu()
	{
		try
		{
			AppletMenu = (iMenuWidget) new MethodList(this.eventBus);//,"WindowMenu","ActiveTab","gwt-MenuItem");
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
	@Override
	public void onGetDictionary(Dictionary dictionary) {
		// TODO Auto-generated method stub
		
	}

	public void onPreviewNativeEvent(NativePreviewEvent event)
	{
	}
}

package monbulk.client;

import java.util.ArrayList;
import java.util.Iterator;

import arc.gui.gwt.widget.panel.AbsolutePanel;
import arc.gui.gwt.widget.window.WindowManager;
import arc.gui.window.WindowProperties;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import monbulk.MethodBuilder.client.MethodBuilder;
import monbulk.MetadataEditor.MetadataEditor;
import monbulk.client.event.CloseWindowEvent;
import monbulk.client.event.CloseWindowEventHandler;
import monbulk.client.event.OpenWindowEvent;
import monbulk.client.event.OpenWindowEventHandler;
import monbulk.client.presenter.DesktopPresenter;
import monbulk.client.presenter.Presenter;
import monbulk.client.view.MonbulkDesktop;
import monbulk.shared.widgets.Window.view.appletWindow;

public class appManager implements Presenter, ValueChangeHandler<String>{
	  private final HandlerManager eventBus;
	  private HasWidgets container;
	  private final ArrayList<appletWindow> winManager;
	  public static final int MAX_WINDOWS = 25;
	  private MonbulkDesktop myDesktop = null;
	  public enum WindowState {OPEN,CLOSE,SHRINK,EXPAND,DRAG};
	  private MethodBuilder myMethodBuilder;
	  private AbsolutePanel arcContainer = null;
	  
	  public appManager(HandlerManager eventBus) {
		    this.eventBus = eventBus;
		    bind();
		    winManager = new ArrayList<appletWindow>();
		    arcContainer = new AbsolutePanel();
		    arcContainer.setWidth("100%");
		    arcContainer.setHeight("100%");
		  }

	  private void bind() {
		    History.addValueChangeHandler(this);

		    eventBus.addHandler(OpenWindowEvent.TYPE,
		        new OpenWindowEventHandler() {
		          public void onOpenWindow(OpenWindowEvent event) {
		        	 
		        	  doChangeWindow(event.getId(),WindowState.OPEN);
		        	  
		          }
		        });
		    eventBus.addHandler(CloseWindowEvent.TYPE, 
		    		new CloseWindowEventHandler(){
		    			public void onCloseWindow(CloseWindowEvent event){
		    				doChangeWindow(event.getId(),WindowState.CLOSE);
		    			}   	
		    		});
		       
	  }
	
	  private appletWindow CreateAppletWindow(String name, Widget widget)
	  {
		  appletWindow newWindow = new appletWindow(name, eventBus, widget);
		  newWindow.setStyleName("appWindow-Dialog");
		  newWindow.setModal(true);
		  int width = 1000;
		  int height = 670;
		  newWindow.setWidth(width + "px");
		  newWindow.setHeight(height + "px");
		  newWindow.setMinSize(width, height);
		  newWindow.center();
		  return newWindow;
	  }
	
	  private void LaunchWindow(String WindowName)
	  {
		  // TODO: These window name strings appear in a few places.
		  // They should be defined in one place and linked to the
		  // class they are associated with (e.g. via some kind of adapter
		  // or interface which can be registered with the appManager).

		  if(WindowName == "Method Builder")
		  {
			  appletWindow newWindow = CreateAppletWindow(WindowName, null);
			  // HACK: Override the width here because the method builder
			  // doesn't correctly resize its contents.
			  newWindow.setWidth("1200px");
			  newWindow.setMinSize(1200, newWindow.getOffsetHeight());
			  winManager.add(newWindow);
			  myMethodBuilder = new MethodBuilder(this.eventBus, newWindow.getContainer());
			  myMethodBuilder.ChangeAppletState();
		  }
		  else if (WindowName == "Metadata Editor")
		  {
			  MetadataEditor metadataEditor = new MetadataEditor(eventBus);
			  appletWindow window = CreateAppletWindow(WindowName, metadataEditor);
			  // HACK: I would have preferred to do this in the ui.xml by setting
			  // the width/height to 100% but I can't get it to work.
			  int width = window.getOffsetWidth() - 50;
			  int height = window.getOffsetHeight() - 100;
			  metadataEditor.setSize(width + "px", height + "px");
			  winManager.add(window);
		  }
	  }

	  private void doChangeWindow(String id,WindowState Change)
	  {
		  Iterator<appletWindow> e = winManager.iterator();
		  Boolean winExists = false;
		  while(e.hasNext())
		  {
			  appletWindow tmpWindow = (appletWindow)e.next();
			  if(tmpWindow.WindowTitle == id)
			  {
				  switch(Change)
				  {
				  case OPEN:
					  tmpWindow.show();
					  winExists= true;  
					  break;
				  case CLOSE:
				  	  tmpWindow.hide();
				  	  winExists= true;
				  	  return;
				  }
				  break;
			  }
			  
		  }

		  if(!winExists)
		  {
			  
			  if(History.getToken().equals(id))
			  {
				  LaunchWindow(id);
			  }
			  else
			  {
				  
				  History.newItem(id);
			  }
			  //so Normally we would call a factory but as time is precious- we will just use MethodBuilder
		  }
	  }

	  public void onValueChange(ValueChangeEvent<String> event) {
		    String token = event.getValue();
		    
		    if(token=="Desktop")
		    {
		    	 if(myDesktop==null)
				 {
		    		 myDesktop = new MonbulkDesktop();
				 }
		    	 new DesktopPresenter(eventBus,myDesktop).go(container);
		    }
		    else if (token == "Metadata Editor" || token == "Method Builder")
		    {
		    	LaunchWindow(token);
		    }
		    else
		    {
		    	 if(myDesktop==null)
				 {
		    		 myDesktop = new MonbulkDesktop();
				 }
		    	 new DesktopPresenter(eventBus,myDesktop).go(container);
		    }
	  }
	@Override
	public void go(HasWidgets container) {
		// 
		this.container = container;
		
		    if ("".equals(History.getToken())) {
		      History.newItem("Desktop");
		    }
		    else
		    {
		    	History.fireCurrentHistoryState();
		    }
		    
	}
}

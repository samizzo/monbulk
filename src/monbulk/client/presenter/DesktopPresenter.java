package monbulk.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import monbulk.client.event.OpenWindowEvent;
import monbulk.client.view.iDesktopView;


public class DesktopPresenter implements Presenter, iDesktopView.Presenter{
	 public interface Display {
		    HasClickHandlers getLauncherButton();
		    Widget asWidget();
		  }
	private final HandlerManager eventBus;
	private final iDesktopView view;
	
	public DesktopPresenter(HandlerManager eventBus, iDesktopView display)
	{
		 this.eventBus = eventBus;
		 this.view = display;
		 this.view.setPresenter(this);
	}
	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub
		container.clear();
	    container.add(view.asWidget());
	    
	}
	public void bind() {
	 
	    
	  }
	@Override
	public void onLauncherClicked(String ClickedLabel) {
		// TODO Auto-generated method stub
		
		eventBus.fireEvent(new OpenWindowEvent(ClickedLabel));
	}
}

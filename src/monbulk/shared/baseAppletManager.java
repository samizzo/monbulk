package monbulk.shared;

import java.util.ArrayList;

import arc.gui.gwt.widget.ContainerWidget;
import arc.gui.gwt.widget.window.Window;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import monbulk.client.presenter.Presenter;

public class baseAppletManager implements iAppletManager{

	private ArrayList<Presenter> allPresenters;
	protected final HandlerManager eventBus;
	private HasWidgets ParentContainer;
	private ContainerWidget ParentWindow;
	private HasWidgets ChildContainer;

	public baseAppletManager(HandlerManager tmpManager, HasWidgets PrntContainer)
	{
		eventBus = tmpManager;
		allPresenters = new ArrayList<Presenter>();
		ParentContainer = PrntContainer;
		ParentWindow = null;
	}

	public ContainerWidget getWindowContainer()
	{
		return this.ParentWindow;
	}

	public void setWindowContainer(ContainerWidget tmpPanel)
	{
		this.ParentWindow = tmpPanel;
	}
		
    public void setParentContainer(HasWidgets tmpContainer)
    {
    	ParentContainer = tmpContainer;
    }
    
    public HasWidgets getParentContainer()
    {
    	return (HasWidgets) ParentContainer;
    }
    
    public void SetChildContainer(HasWidgets tmpContainer)
    {
    	ChildContainer = tmpContainer;
    }

    public void setParent(HasWidgets PContainer)
    {
    	this.ParentContainer = PContainer;
    }
    
    public HasWidgets GetChildContainer()
    {
    	return ChildContainer;
    }
    
    public void addPresenter(Presenter tmpPresenter)
    {
    	allPresenters.add(tmpPresenter);
    }
    
    public void show(Window owner)
    {
    	
    }
    
    //We should also fire general events here TODO
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void SetLayout(Widget Layout) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void ChangeAppletState(String newState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void ChangeAppletState() {
		// TODO Auto-generated method stub
		
	}
}

package monbulk.MethodBuilder.client.view;

import java.util.Iterator;
import java.util.List;


import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.view.iMenuWidget;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;


public class MethodBuilderMenu extends MenuBar implements iMenuWidget {

	
	private HandlerManager eventBus;
	private String ActiveClassName;
	private String PassiveClassName;
	
	public MethodBuilderMenu(HandlerManager tmpEvent, String menuClassName, String activeClassName, String pClassName)
	{
		super(true);
		eventBus = tmpEvent;
		this.setStyleName(menuClassName);
		this.ActiveClassName = activeClassName;
		this.PassiveClassName = pClassName; 
		
	}

	@Override
	public HasWidgets getBaseWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActiveMenu(String activeItem) {
		// TODO Auto-generated method stub
		Iterator<MenuItem> tmpIt = this.getItems().iterator();
		
		while(tmpIt.hasNext())
		{
			MenuItem tmpItem = tmpIt.next();
			if(tmpItem != null)
			{
				if(tmpItem.getStyleName().equals(this.ActiveClassName))
				{
					tmpItem.setStyleName(this.PassiveClassName);
				}
							
				if(tmpItem.getText().equals(activeItem))
				{
					tmpItem.setStyleName(this.ActiveClassName);
				}
			}
		}
	}

	@Override
	public void populateItems(List<String> tmpArray) {
		// TODO Auto-generated method stub
		
		Iterator<String> it = tmpArray.iterator();
		while(it.hasNext())
		{
			//What this should be is a 2d array with a string for the text and a string for the command to call
			//this.addItem(new MenuItem());
			/*String MenuText = it.next();
			MenuCommand tmpCommand = new MenuCommand(MenuText, eventBus); 
			//MenuCommand tmpCommand = new MenuCommand(MenuText, eventBus);
			MenuItem tmpItem = new MenuItem(MenuText,tmpCommand);
			tmpItem.setStyleName(this.PassiveClassName);
			this.addItem(tmpItem);*/
			
		}
	}

	@Override
	public void setPresenter(IPresenter tmpPresenter) {
		// TODO Auto-generated method stub
		
	}
}

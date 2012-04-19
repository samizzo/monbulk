package monbulk.MethodBuilder.client.view;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.view.ILayoutWidget;


public class MethodBuilderMaster extends Composite implements ILayoutWidget, HasWidgets {

	private static MethodBuilderMasterUiBinder uiBinder = GWT
			.create(MethodBuilderMasterUiBinder.class);

	interface MethodBuilderMasterUiBinder extends
			UiBinder<Widget, MethodBuilderMaster> {
	}
	@UiField
    HTMLPanel MenuPanel;
	
	@UiField
    HTMLPanel DockPanel;
	
	@UiField
    HTMLPanel BodyPanel;
	
	@UiField
    HTMLPanel _container;
	
	@UiField 
	HTMLPanel NavigationsPanel;
	
	public MethodBuilderMaster() {
		initWidget(uiBinder.createAndBindUi(this));
	}
    
	@Override
	public HasWidgets getBodyContainer() {
		// TODO Auto-generated method stub
		return (HasWidgets)BodyPanel;
	}

	@Override
	public HasWidgets getMenuContainer() {
		// TODO Auto-generated method stub
		return MenuPanel;
	}
	
	@Override
	public HasWidgets getNavigationContainer() {
		// TODO Auto-generated method stub
		return NavigationsPanel;
	}
	@Override
	public HasWidgets getDockContainer() {
		// TODO Auto-generated method stub
		return DockPanel;
	}
	@Override
	public Widget asWidget()
	{
		return this._container;
	}

	@Override
	public void add(Widget w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return this._container.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		// TODO Auto-generated method stub
		if(this.remove(w))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
}

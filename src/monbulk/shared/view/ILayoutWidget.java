package daris.Monbulk.shared.view;


import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface ILayoutWidget {
	public HasWidgets getBodyContainer();
	public HasWidgets getMenuContainer();
	public HasWidgets getDockContainer();
	public HasWidgets getNavigationContainer();
	public Widget asWidget();
	
}

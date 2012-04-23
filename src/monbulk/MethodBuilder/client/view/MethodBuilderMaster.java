package monbulk.MethodBuilder.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class MethodBuilderMaster extends ResizeComposite
{
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
	HTMLPanel NavigationsPanel;
	
	public MethodBuilderMaster() {
		initWidget(uiBinder.createAndBindUi(this));
	}
    
	public HasWidgets getBodyContainer() {
		// TODO Auto-generated method stub
		return (HasWidgets)BodyPanel;
	}

	public HasWidgets getMenuContainer() {
		// TODO Auto-generated method stub
		return MenuPanel;
	}
	
	public HasWidgets getNavigationContainer() {
		// TODO Auto-generated method stub
		return NavigationsPanel;
	}

	public HasWidgets getDockContainer() {
		// TODO Auto-generated method stub
		return DockPanel;
	}
}

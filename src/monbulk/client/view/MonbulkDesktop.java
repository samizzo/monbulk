package monbulk.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import monbulk.client.view.iDesktopView;

public class MonbulkDesktop extends Composite implements iDesktopView {

	private static MonbulkDesktopUiBinder uiBinder = GWT
			.create(MonbulkDesktopUiBinder.class);
	
	private Presenter presenter;
	
	interface MonbulkDesktopUiBinder extends UiBinder<Widget, MonbulkDesktop> {
	}
	@UiField 
	PushButton btnMethodApp;
	
	@UiField
	PushButton btnArcMethodApp;
	
	@UiField
	PushButton btnMetadataEditor;

	@UiField
	HTMLPanel ArcContainer;
	
	public MonbulkDesktop() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("btnMetadataEditor")
	public void onMetadataEditorClick(ClickEvent a_event)
	{
		if (presenter != null)
		{
			presenter.onLauncherClicked("Metadata Editor");
		}
	}

	@UiHandler("btnMethodApp")
	public void onClick(ClickEvent e) {
	    
		if (presenter != null) {
		      presenter.onLauncherClicked("Method Builder");
		    }
	  }
	@UiHandler("btnArcMethodApp")
	public void onClick1(ClickEvent e) {
	    
		if (presenter != null) {
		      presenter.onLauncherClicked("Arc Method Builder");
		    }
	  }

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub
		this.presenter = presenter;
		
	}
	public HasWidgets getContainer()
	{
		return this.ArcContainer;
	}

}

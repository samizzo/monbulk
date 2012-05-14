package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView;


public class AppletStateNavigation extends Composite implements IView {

	private static AppletStateNavigationUiBinder uiBinder = GWT
			.create(AppletStateNavigationUiBinder.class);

	interface AppletStateNavigationUiBinder extends
			UiBinder<Widget, AppletStateNavigation> {
	}
	FormPresenter Presenter;
	
	@UiField
	PushButton btnSave;
	
	@UiField
	PushButton btnPublish;
	
	@UiField
	PushButton btnCancel;
	
	public AppletStateNavigation() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		this.Presenter = (FormPresenter)presenter;
		
	}

	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub
		
	}
	@UiHandler("btnSave")
	public void onClick(ClickEvent e)
	{
		this.Presenter.FormComplete("Navigation", "Save");
	}
	@UiHandler("btnCancel")
	public void onClick1(ClickEvent e)
	{
		this.Presenter.FormComplete("Navigation", "Cancel");
	}
	@UiHandler("btnPublish")
	public void onClick2(ClickEvent e)
	{
		this.Presenter.FormComplete("Navigation", "Publish");
	}
	

}

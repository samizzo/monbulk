package daris.Monbulk.client.view;

import com.google.gwt.user.client.ui.Widget;


public interface iDesktopView {
	public interface Presenter {
	    void onLauncherClicked(String ClickedLabel);
	 }
	 Widget asWidget();
	 void setPresenter(Presenter presenter);
}

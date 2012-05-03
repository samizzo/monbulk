package monbulk.shared.view;

import java.util.List;

import monbulk.shared.Architecture.IPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface iMenuWidget {
	public HasWidgets getBaseWidget();
	public Widget asWidget();
	public void setActiveMenu(String activeItem);
	public void populateItems(List<String> tmpArray);
	public void setPresenter(IPresenter tmpPresenter);
}

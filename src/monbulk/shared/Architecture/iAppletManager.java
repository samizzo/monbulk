package daris.Monbulk.shared.Architecture;

import com.google.gwt.user.client.ui.Widget;

public interface iAppletManager {
	public Widget getShrunkWidget();
	public Widget getMenuWidget();
	public Widget getDesktopIcon();
	public void setMenu();
	public void SetLayout(Widget Layout);
	public void ChangeAppletState(String newState);
	public void ChangeAppletState();
	
}

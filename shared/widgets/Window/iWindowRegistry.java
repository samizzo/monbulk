package daris.Monbulk.shared.widgets.Window;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;

import daris.Monbulk.shared.util.MonbulkEnums.WindowTypes;

public interface iWindowRegistry {
	public iWindow createWindow(WindowTypes WindowType,String WindowName);
	public iWindow createWindow(WindowTypes WindowType,WindowProperties wp);
	public iWindow createWindow(WindowTypes WindowType,WindowProperties wp,HandlerManager eventBus);
	public iWindow createWindow(WindowTypes WindowType,WindowProperties wp, HandlerManager eventBus, Widget w);
}

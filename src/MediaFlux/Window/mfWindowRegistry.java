package daris.Monbulk.MediaFlux.Window;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;

import daris.Monbulk.shared.util.MonbulkEnums.WindowTypes;
import daris.Monbulk.shared.widgets.Window.WindowProperties;
import daris.Monbulk.shared.widgets.Window.iWindow;
import daris.Monbulk.shared.widgets.Window.iWindowRegistry;

public class mfWindowRegistry implements iWindowRegistry{

	@Override
	public iWindow createWindow(WindowTypes WindowType,String WindowName) {
		switch(WindowType)
		{
		case DEFAULT:
			iWindow tmpItem = new MFWindow(WindowName);
			return tmpItem;
		default:
			iWindow tmpItem2 = new MFWindow(WindowName);
			return tmpItem2;
		}
	}

	@Override
	public iWindow createWindow(WindowTypes WindowType, WindowProperties wp) {
		// TODO Auto-generated method stub
		switch(WindowType)
		{
		case DEFAULT:
			iWindow tmpItem = new MFWindow(wp.get_WindowName(),wp);
			return tmpItem;
		default:
			iWindow tmpItem2 = new MFWindow(wp.get_WindowName(),wp);
			return tmpItem2;
		}
	}

	@Override
	public iWindow createWindow(WindowTypes WindowType, WindowProperties wp,
			HandlerManager eventBus) {
		switch(WindowType)
		{
		case DEFAULT:
			iWindow tmpItem = new MFWindow(wp.get_WindowName(),wp);
			return tmpItem;
		default:
			iWindow tmpItem2 = new MFWindow(wp.get_WindowName(),wp);
			return tmpItem2;
		}
	}

	@Override
	public iWindow createWindow(WindowTypes WindowType, WindowProperties wp,
			HandlerManager eventBus, Widget w) {
		switch(WindowType)
		{
		case DEFAULT:
			iWindow tmpItem = new MFWindow(wp.get_WindowName(),wp);
			return tmpItem;
		default:
			iWindow tmpItem2 = new MFWindow(wp.get_WindowName(),wp);
			return tmpItem2;
		}
	}

}

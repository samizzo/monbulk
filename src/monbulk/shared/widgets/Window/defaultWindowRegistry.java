package monbulk.shared.widgets.Window;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.util.MonbulkEnums.WindowTypes;
import monbulk.shared.widgets.Window.view.appletWindow;

public class defaultWindowRegistry implements iWindowRegistry {


	@Override
	public iWindow createWindow(WindowTypes WindowType,String WindowName) {
		// TODO Auto-generated method stub
		switch(WindowType)
		{
			case DEFAULT:
				iWindow tmpWin = new appletWindow(WindowName,null,null);
				return tmpWin;
			default:
				iWindow tmpWin1 = new appletWindow(WindowName,null);
				return tmpWin1;
		}
		
	}

	@Override
	public iWindow createWindow(WindowTypes WindowType, WindowProperties wp) {
		// TODO Auto-generated method stub
		switch(WindowType)
		{
			case DEFAULT:
				iWindow tmpWin = new appletWindow(wp.get_WindowName(),null,null);
				tmpWin.setProperties(wp);
				return tmpWin;
			default:
				iWindow tmpWin1 = new appletWindow(wp.get_WindowName(),null,null);
				tmpWin1.setProperties(wp);
				return tmpWin1;
		}
		
	}

	@Override
	public iWindow createWindow(WindowTypes WindowType, WindowProperties wp,
			HandlerManager eventBus) {
		switch(WindowType)
		{
			case DEFAULT:
				iWindow tmpWin = new appletWindow(wp.get_WindowName(),eventBus);
				tmpWin.setProperties(wp);
				return tmpWin;
			default:
				iWindow tmpWin1 = new appletWindow(wp.get_WindowName(),eventBus);
				tmpWin1.setProperties(wp);
				return tmpWin1;
		}
		
	}

	@Override
	public iWindow createWindow(WindowTypes WindowType, WindowProperties wp,
			HandlerManager eventBus, Widget w) {
		switch(WindowType)
		{
			case DEFAULT:
				iWindow tmpWin = new appletWindow(wp.get_WindowName(),eventBus,w);
				tmpWin.setProperties(wp);
				return tmpWin;
			default:
				iWindow tmpWin1 = new appletWindow(wp.get_WindowName(),eventBus,w);
				tmpWin1.setProperties(wp);
				return tmpWin1;
		}
	}

}

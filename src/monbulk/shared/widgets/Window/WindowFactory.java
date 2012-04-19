package monbulk.shared.widgets.Window;

import com.google.gwt.event.shared.HandlerManager;

import monbulk.shared.util.MonbulkEnums.WindowStates;
import monbulk.shared.util.MonbulkEnums.WindowTypes;

/*
 * Class: WindowFactory
 * Author: Andrew Glenn
 * Date: 21/3/2012
 * 
 * Creates a Singleton instance of Window - relative to what platforms has been registered
 * 1. Mediaflux Platform
 * 2. GWT Platform
 * 3. Smart GWT platform
 * 
 * Effectiviely it must create the window, register it into the manager (to handle events) and perform the wdiget conversions
 */
public final class WindowFactory {

	private static iWindowRegistry _WinRegister;
	public static void registerPlatfrom(iWindowRegistry platformRegistrar)
	{
		_WinRegister = platformRegistrar;
	}
	public static iWindow Create(WindowTypes Type,String WindowName)
	{
		iWindow newWin = _WinRegister.createWindow(Type,WindowName);
		WindowManager.AddWindow(newWin, WindowStates.OPEN_DEFAULT);
		newWin.setManager(WindowManager.getInstance());
		return newWin;
	}
	public static iWindow Create(WindowTypes Type,String WindowName,monbulk.shared.widgets.Window.WindowProperties wp)
	{
		iWindow newWin = _WinRegister.createWindow(Type,wp);
		WindowManager.AddWindow(newWin, WindowStates.OPEN_DEFAULT);
		newWin.setManager(WindowManager.getInstance());
		return newWin;
	}
	public static iWindow Create(WindowTypes Type,String WindowName,monbulk.shared.widgets.Window.WindowProperties wp,HandlerManager eventBus)
	{
		iWindow newWin = _WinRegister.createWindow(Type,wp);
		WindowManager.AddWindow(newWin, WindowStates.OPEN_DEFAULT);
		newWin.setManager(WindowManager.getInstance());
		return newWin;
	}
	
}

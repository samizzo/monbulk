package monbulk.shared.widgets.Window;

import java.util.ArrayList;
import java.util.Iterator;

import monbulk.shared.util.MonbulkEnums.WindowStates;

/*
 * Class: WindowManager
 * Created by: Andrew Glenn
 * Date: 21/3/2012
 * 
 * WindowManager  keeps a reference to all windows loaded. When a Window is loaded it should check if it already exists and relaunch.
 * It should also handle external events such as close/ shrink/expand
 */

public final class WindowManager {

	private static WindowManager _instance = new WindowManager();
	
	private static final ArrayList<WindowReference> _WindowList = new ArrayList<WindowReference>();
	
	public static WindowManager getInstance()
	{
		return _instance;
	}
	public static void AddWindow(iWindow newWin,WindowStates winState)
	{
		
		WindowReference tmpWin = new WindowReference(newWin);
		tmpWin.setState(winState);
		_WindowList.add(tmpWin);
	}
	public static WindowStates getStateForWindowName(String WindowName)
	{
		Iterator<WindowReference> i = _WindowList.iterator();
		while(i.hasNext())
		{
			WindowReference tmpItem = i.next();
			if(tmpItem._win.getWindowName() == WindowName)
			{
				return tmpItem.getState();
			}
		}
		return null;
		
	}
	public static iWindow getWindowForWindowName(String WindowName)
	{
		Iterator<WindowReference> i = _WindowList.iterator();
		while(i.hasNext())
		{
			WindowReference tmpItem = i.next();
			if(tmpItem._win.getWindowName() == WindowName)
			{
				return tmpItem._win;
			}
		}
		return null;
		
	}
}

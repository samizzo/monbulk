package daris.Monbulk.shared.widgets.Window;

import daris.Monbulk.shared.util.MonbulkEnums.WindowStates;

public class WindowReference
{
	public final iWindow _win;
	private WindowStates _windowState;
	
	public WindowReference(iWindow Window)
	{
		this._win= Window;
	}
	public void setState(WindowStates state)
	{
		this._windowState = state;
	}
	public WindowStates getState()
	{
		return _windowState;
	}
}

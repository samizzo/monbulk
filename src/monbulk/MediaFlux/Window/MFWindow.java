package daris.Monbulk.MediaFlux.Window;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;

import arc.gui.gwt.widget.panel.AbsolutePanel;
import arc.gui.gwt.widget.window.Window;
import arc.gui.window.WindowProperties;
import daris.Monbulk.shared.widgets.Window.WindowManager;
import daris.Monbulk.shared.widgets.Window.iWindow;


public class MFWindow implements iWindow {

	private Window _win;
	private AbsolutePanel _mfContainer;
	private HTMLPanel _gwtContainer;
	private String _windowName;
	private WindowProperties _wp;
	public MFWindow(String WindowName)
	{
		_gwtContainer = new HTMLPanel("");
		_mfContainer = new AbsolutePanel();
		_mfContainer.add(_gwtContainer, true);
		_wp = new WindowProperties();
		_wp.setModal(false);
		_wp.setTitle(WindowName);
		_wp.setCanBeResized(true);
		_wp.setCanBeClosed(true);
		_wp.setCanBeMoved(true);
		_wp.setPosition(0, 0);
		_wp.setGlassOpacity(0.7);
		//_win = Window.create(_mfContainer,_wp);
		_windowName = WindowName;
	}
	public MFWindow(String WindowName,daris.Monbulk.shared.widgets.Window.WindowProperties wp)
	{
		_gwtContainer = new HTMLPanel("");
		_mfContainer = new AbsolutePanel();
		_mfContainer.add(_gwtContainer, true);
		this.setProperties(wp);
		_wp.setTitle(WindowName);
		//_win = Window.create(_mfContainer,_wp);
		_windowName = WindowName;
		
	}
	@Override
	public String getWindowName() {
		// TODO Auto-generated method stub
		return this._windowName;
	}

	@Override
	public void setWindowName(String Name) {
		this._windowName = Name;
		
	}
	

	@Override
	public void Shrink() {
		// TODO Auto-generated method stub
		this._win.resizeTo(20, 100);
	}

	@Override
	public void Maximise() {
		// TODO Auto-generated methis._thod stub
		this._win.expandToFitParent();
	}

	@Override
	public void setManager(WindowManager winManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWindowSize(int width, int height) {
		// TODO Auto-generated method stub
		_wp.setSize(width, height);
	}
	@Override
	public void setOwner(Object Owner) {
		// TODO Auto-generated method stub
		_wp.setOwnerWindow((arc.gui.window.Window) Owner);
	}
	@Override
	public HasWidgets getChild() {
		// TODO Auto-generated method stub
		return _gwtContainer;
	}
	@Override
	public void init() {
		_win = Window.create(_mfContainer,_wp);
		
	}
	@Override
	public void setProperties(
			daris.Monbulk.shared.widgets.Window.WindowProperties wp) {
		
		// TODO Auto-generated method stub
		_wp.setModal(wp.get_isModal());
		_wp.setCanBeResized(wp.get_canResize());
		_wp.setCanBeClosed(true);
		_wp.setCanBeMoved(wp.get_canMove());
		_wp.setPosition(wp.get_x(), wp.get_y());
		_wp.setGlassOpacity(0.7);
	}
	@Override
	public void close() {
		if(!this._win.equals(null))
		{
			this._win.closeIfOK();
		}
		
	}
	@Override
	public void hide() {
	
		if(!this._win.equals(null))
		{
			this._win.hide();
		}
		
	}
	@Override
	public void show() {
		if(!this._win.equals(null))
		{
			this._win.show();
		}
		
	}


}

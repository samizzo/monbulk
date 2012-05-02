package monbulk.shared.widgets.Window;

public interface IWindow
{
	public interface HideHandler
	{
		public abstract void onHide();
	};
	
	public abstract WindowSettings getWindowSettings();
}

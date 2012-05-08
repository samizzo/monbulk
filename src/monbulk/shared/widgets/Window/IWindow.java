package monbulk.shared.widgets.Window;

public interface IWindow
{
	public interface HideHandler
	{
		public abstract void onHide();
	};
	
	public interface ShowHandler
	{
		public abstract void onShow();
	}
	
	public abstract WindowSettings getWindowSettings();
}

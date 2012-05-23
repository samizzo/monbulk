package monbulk.shared.widgets.Window;

import com.google.gwt.user.client.Event.NativePreviewEvent;

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
	public abstract void onPreviewNativeEvent(NativePreviewEvent event);
}

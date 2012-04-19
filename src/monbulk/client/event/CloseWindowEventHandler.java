package monbulk.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface CloseWindowEventHandler extends EventHandler {
	void onCloseWindow(CloseWindowEvent event);
}

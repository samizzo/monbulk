package monbulk.shared.widgets.Window;

import monbulk.client.desktop.Desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.Event.NativePreviewEvent;

/**
 * A Monbulk window with a panel for adding content and "ok" and "cancel"
 * buttons.  "ok" and "cancel" close the window and fire an event. 
 */
public class OkCancelWindow extends Composite implements IWindow, IWindow.HideHandler, IWindow.ShowHandler
{
	private static OkCancelWindowUiBinder uiBinder = GWT.create(OkCancelWindowUiBinder.class);
	interface OkCancelWindowUiBinder extends UiBinder<Widget, OkCancelWindow> {	}

	public interface OkCancelHandler
	{
		public enum Event
		{
			Ok,
			Cancel
		};
		
		public abstract void onOkCancelClicked(Event eventType);
	};

	/**
	 * Validation handler.Validates data in this dialog box.  This callback is
	 * called when 'ok' is pressed.  If it returns false the window will not
	 * be hidden.  
	 */
	public interface ValidateHandler
	{
		public abstract boolean validate();
	};
	
	protected @UiField Button m_ok;
	protected @UiField Button m_cancel;
	protected @UiField HTMLPanel m_contentPanel;
	protected WindowSettings m_windowSettings;
	private OkCancelHandler m_okCancelHandler;
	private ValidateHandler m_validateHandler;
	
	/**
	 * Constructs a new OkCancelWindow with the specified window id
	 * and window title and the default settings (modal, non-resizable,
	 * no desktop button).
	 * @param windowId
	 * @param windowTitle
	 */
	public OkCancelWindow(String windowId, String windowTitle)
	{
		m_windowSettings = new WindowSettings();
		m_windowSettings.width = 400;
		m_windowSettings.height = 400;
		m_windowSettings.modal = true;
		m_windowSettings.createDesktopButton = false;
		m_windowSettings.resizable = false;
		m_windowSettings.windowId = windowId;
		m_windowSettings.windowTitle = windowTitle;
		init(m_windowSettings);
	}

	/**
	 * Constructs a new OkCancelWindow with the specified WindowSettings
	 * object.
	 * @param windowSettings
	 */
	public OkCancelWindow(WindowSettings windowSettings)
	{
		init(windowSettings);
	}
	
	private void init(WindowSettings windowSettings)
	{
		m_windowSettings = windowSettings;
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * Returns the window settings for this window.
	 * @return
	 */
	public WindowSettings getWindowSettings()
	{
		return m_windowSettings;
	}
	
	/**
	 * Sets the OkCancelHandler.  The handler will be cleared
	 * when the window is hidden.
	 * @param handler
	 */
	public void setOkCancelHandler(OkCancelHandler handler)
	{
		m_okCancelHandler = handler;
	}
	
	/**
	 * Sets the ValidateHandler.
	 * @param handler
	 */
	public void setValidateHandler(ValidateHandler handler)
	{
		m_validateHandler = handler;
	}
	
	@UiHandler("m_ok")
	protected void onOkClicked(ClickEvent event)
	{
		if (m_validateHandler == null || m_validateHandler.validate())
		{
			onOk();
			hide(OkCancelHandler.Event.Ok);
		}
	}

	@UiHandler("m_cancel")
	protected void onCancelClicked(ClickEvent event)
	{
		onCancel();
		hide(OkCancelHandler.Event.Cancel);
	}
	
	protected void onOk()
	{
	}
	
	protected void onCancel()
	{
	}
	
	/**
	 * Hides this window and triggers any attached OkCancelHandler.
	 */
	private void hide(OkCancelHandler.Event event)
	{
		if (m_okCancelHandler != null)
		{
			m_okCancelHandler.onOkCancelClicked(event);
			m_okCancelHandler = null;
		}

		Desktop d = Desktop.get();
		d.hide(m_windowSettings.windowId);
	}
	
	/**
	 * Called whenever the window is hidden.
	 */
	public void onHide()
	{
	}
	
	/**
	 * Called whenever the window is shown.
	 */
	public void onShow()
	{
	}
	
	public void onPreviewNativeEvent(NativePreviewEvent event)
	{
		if (event.getTypeInt() == Event.ONKEYDOWN)
		{
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE)
			{
				onCancelClicked(null);
			}
		}
	}
}

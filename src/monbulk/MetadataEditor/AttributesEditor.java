package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Composite;
import monbulk.shared.widgets.Window.*;

public class AttributesEditor extends Composite implements IWindow
{
	private static AttributesEditorUiBinder uiBinder = GWT.create(AttributesEditorUiBinder.class);
	interface AttributesEditorUiBinder extends UiBinder<Widget, AttributesEditor> {	}

	private WindowSettings m_windowSettings;

	public AttributesEditor()
	{
		m_windowSettings = new WindowSettings();
		m_windowSettings.width = 300;
		m_windowSettings.height = 300;
		m_windowSettings.modal = true;
		m_windowSettings.createDesktopButton = false;
		m_windowSettings.resizable = false;
		m_windowSettings.windowId = "AttributesEditor";
		m_windowSettings.windowTitle = "Attributes";

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public WindowSettings getWindowSettings()
	{
		return m_windowSettings;
	}
}

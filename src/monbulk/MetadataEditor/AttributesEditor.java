package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickEvent;

import monbulk.client.desktop.Desktop;
import monbulk.client.event.WindowEvent;
import monbulk.shared.widgets.Window.*;
import monbulk.shared.Services.Metadata;

public class AttributesEditor extends Composite implements IWindow, IWindow.HideHandler
{
	private static AttributesEditorUiBinder uiBinder = GWT.create(AttributesEditorUiBinder.class);
	interface AttributesEditorUiBinder extends UiBinder<Widget, AttributesEditor> {	}

	@UiField ElementProperties m_elementProperties;
	@UiField Button m_ok;
	@UiField Button m_cancel;

	private WindowSettings m_windowSettings;

	public AttributesEditor()
	{
		m_windowSettings = new WindowSettings();
		m_windowSettings.width = -1;
		m_windowSettings.height = -1;
		m_windowSettings.modal = true;
		m_windowSettings.createDesktopButton = false;
		m_windowSettings.resizable = false;
		m_windowSettings.windowId = "AttributesEditor";
		m_windowSettings.windowTitle = "Attributes";

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void onHide()
	{
		m_elementProperties.setElement(null, false);
	}
	
	public WindowSettings getWindowSettings()
	{
		return m_windowSettings;
	}
	
	public void setElement(Metadata.Element element)
	{
		m_elementProperties.setElement(element, false);
	}
	
	@UiHandler("m_ok")
	void onOkClicked(ClickEvent event)
	{
		m_elementProperties.updateCurrentElement();
		hide(WindowEvent.EventType.Ok);
	}

	@UiHandler("m_cancel")
	void onCancelClicked(ClickEvent event)
	{
		hide(WindowEvent.EventType.Cancel);
	}
	
	private void hide(WindowEvent.EventType eventType)
	{
		Desktop d = Desktop.get();
		d.getEventBus().fireEvent(new WindowEvent(m_windowSettings.windowId, eventType));
		d.hide(m_windowSettings.windowId);
	}
}

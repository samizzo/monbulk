package monbulk.MetadataEditor;

import monbulk.client.desktop.Desktop;
import monbulk.client.event.WindowEvent;
import monbulk.shared.widgets.Window.IWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import monbulk.shared.widgets.Window.WindowSettings;

public class MetadataSelect extends Composite implements IWindow
{
	private static MetadataSelectUiBinder uiBinder = GWT.create(MetadataSelectUiBinder.class);
	interface MetadataSelectUiBinder extends UiBinder<Widget, MetadataSelect> {	}

	private WindowSettings m_windowSettings;

	@UiField Button m_ok;
	@UiField Button m_cancel;
	@UiField MetadataList m_metadataList;

	public MetadataSelect()
	{
		m_windowSettings = new WindowSettings();
		m_windowSettings.width = 400;
		m_windowSettings.height = 400;
		m_windowSettings.modal = true;
		m_windowSettings.createDesktopButton = false;
		m_windowSettings.resizable = false;
		m_windowSettings.windowId = "MetadataSelect";
		m_windowSettings.windowTitle = "Metadata";

		initWidget(uiBinder.createAndBindUi(this));
		
		m_ok.setEnabled(false);
		m_metadataList.setHandler(new MetadataList.Handler()
		{
			public void onMetadataSelected(String metadataName)
			{
				m_ok.setEnabled(true);
			}
			
			public void onRefreshList()
			{
				m_ok.setEnabled(false);
			}
			
			public void onRemoveMetadata(String metadataName)
			{
				// Remove button is hidden.
			}
		});
	}

	public WindowSettings getWindowSettings()
	{
		return m_windowSettings;
	}
	
	public String getSelectedMetadataName()
	{
		return m_metadataList.getSelectedMetadataName();
	}
	
	@UiHandler("m_ok")
	void onOkClicked(ClickEvent event)
	{
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

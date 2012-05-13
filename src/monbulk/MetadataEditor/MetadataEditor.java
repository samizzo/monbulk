package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import monbulk.shared.Services.*;
import monbulk.shared.Services.MetadataService.*;
import monbulk.shared.widgets.Window.*;

public class MetadataEditor extends ResizeComposite implements IWindow
{
	interface MetadataEditorUiBinder extends UiBinder<Widget, MetadataEditor> { }
	private static MetadataEditorUiBinder s_uiBinder = GWT.create(MetadataEditorUiBinder.class);

	@UiField MetadataList m_metadataList;
	@UiField MetadataProperties m_metadataProperties;
	
	private WindowSettings m_windowSettings;
	
	public MetadataEditor()
	{
		m_windowSettings = new WindowSettings();
		m_windowSettings.width = 1000;
		m_windowSettings.height = 678;
		m_windowSettings.minWidth = 1000;
		m_windowSettings.minHeight = 678;
		m_windowSettings.windowId = "MetadataEditor";
		m_windowSettings.windowTitle = "Metadata Editor"; 

		Widget widget = s_uiBinder.createAndBindUi(this);
		initWidget(widget);

		m_metadataList.setHandler(new MetadataList.Handler()
		{
			public void onRefreshList()
			{
				m_metadataProperties.clear();
			}

			public void onMetadataSelected(String metadataName)
			{
				MetadataService service = MetadataService.get();
				if (service != null)
				{
					service.getMetadata(metadataName, new GetMetadataHandler()
					{
						// Callback for reading a specific metadata object.
						public void onGetMetadata(Metadata metadata)
						{
							// If the currently selected metadata doesn't match
							// the one we kicked off this service call for, then
							// just ignore the result.												
							String selected = m_metadataList.getSelectedMetadataName();
							if (metadata.getName().equals(selected))
							{
								m_metadataProperties.setMetadata(metadata);

								// Now make sure the metadata list box has focus.
								m_metadataList.setFocus(true);
							}
						}
					});
				}
			}
			
			public void onRemoveMetadata(String metadataName)
			{
				m_metadataProperties.clear();
			}
		});
		
		m_metadataList.addSaveHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				Metadata metadata = m_metadataProperties.getMetadata();
				MetadataService service = MetadataService.get();
				if (service != null)
				{
					service.updateMetadata(metadata, new UpdateMetadataHandler()
					{
						public void onUpdateMetadata(Metadata metadata, boolean success)
						{
							GWT.log("updated metadata: " + success);
						}
					});
				}
			}
		});
	}
	
	public WindowSettings getWindowSettings()
	{
		return m_windowSettings;
	}
}

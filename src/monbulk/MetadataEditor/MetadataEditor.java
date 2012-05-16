package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
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
			
			public void onNewMetadata()
			{
				// Create a new, empty metadata object.
				m_metadataProperties.clear();
				m_metadataProperties.setMetadata(new Metadata("", "", ""));
				m_metadataProperties.setNameFocus();
				m_metadataList.clearSelection();
			}
		});
		
		m_metadataList.addSaveHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				final Metadata metadata = m_metadataProperties.getMetadata();
				final MetadataService service = MetadataService.get();
				if (service != null)
				{
					final String oldName = m_metadataList.getSelectedMetadataName();
					final String newName = metadata.getName();
					
					// If the metadata name hasn't changed or it's a new metadta we can update immediately.
					if (oldName == null || oldName.length() == 0 || oldName.equals(newName))
					{
						updateMetadata(metadata);
					}
					else
					{
						// The name has changed, so we need to rename before
						// updating.  First check if the new name already exists.
						service.metadataExists(newName, new MetadataExistsHandler()
						{
							public void onMetadataExists(String metadataName, boolean exists)
							{
								if (exists)
								{
									// New metadata name already exists, so don't allow the user to save yet.
									Window.alert("A metadata with the name '" + metadataName + "' already exists.  Please use a different name.");
									m_metadataProperties.setNameFocus();
								}
								else
								{
									// New name doesn't exist so rename the metadata.
									service.renameMetadata(oldName, newName, new RenameMetadataHandler()
									{
										public void onRenameMetadata()
										{
											updateMetadata(metadata);
										}
									});
								}
							}
						});
					}
				}
			}
		});
	}
	
	private void updateMetadata(final Metadata metadata)
	{
		MetadataService service = MetadataService.get();
		service.updateMetadata(metadata, new UpdateMetadataHandler()
		{
			public void onUpdateMetadata(Metadata m)
			{
				// Refresh the list so it has the new name.
				m_metadataList.refresh(m.getName());
			}
		});
	}
	
	public WindowSettings getWindowSettings()
	{
		return m_windowSettings;
	}
}

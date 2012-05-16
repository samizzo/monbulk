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
	
	private enum State
	{
		None,
		WaitingForRefresh,
		WaitingForSelect,
		WaitingForNew,
	};

	private boolean m_cancelSelect = false;
	private State m_state = State.None;
	private String m_metadataNameToSelect;
	
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
				m_state = State.WaitingForRefresh;
				checkModified();
			}

			public boolean onMetadataSelected(String metadataName)
			{
				m_cancelSelect = false;
				m_state = State.WaitingForSelect;
				m_metadataNameToSelect = metadataName;
				checkModified();
				return !m_cancelSelect;
			}
			
			public void onRemoveMetadata(String metadataName)
			{
				m_metadataProperties.clear();
			}
			
			public void onNewMetadata()
			{
				m_state = State.WaitingForNew;
				checkModified();
			}
		});
		
		m_metadataList.addSaveHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				saveMetadata();
			}
		});
	}
	
	private void updateMetadata(final Metadata metadata, final boolean refresh)
	{
		MetadataService service = MetadataService.get();
		service.updateMetadata(metadata, new UpdateMetadataHandler()
		{
			public void onUpdateMetadata(Metadata m)
			{
				m.clearModified();

				// Refresh the list so it has the new name.
				if (refresh)
				{
					// If we are already waiting to select metadata,
					// we now need to refresh the list because the
					// name of the previous metadata changed.  Thus
					// we need to select the metadata we are waiting
					// to select, instead of the one we just changed.
					String select = m.getName();
					if (m_state == State.WaitingForSelect)
					{
						select = m_metadataNameToSelect;

						// Override the state, since we don't want to
						// do an unnecessary action (the refresh call
						// will do this for us).
						m_state = State.None;
					}

					m_metadataList.refresh(select);
				}

				performAction();
			}
		});
	}
	
	private void performAction()
	{
		if (m_state == State.WaitingForRefresh)
		{
			m_metadataProperties.clear();
		}
		else if (m_state == State.WaitingForNew)
		{
			// Create a new, empty metadata object.
			m_metadataProperties.clear();
			m_metadataProperties.setMetadata(new Metadata("", "", ""));
			m_metadataProperties.setNameFocus();
			m_metadataList.clearSelection();
		}
		else if (m_state == State.WaitingForSelect)
		{
			MetadataService service = MetadataService.get();
			if (service != null)
			{
				service.getMetadata(m_metadataNameToSelect, new GetMetadataHandler()
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

		m_state = State.None;
	}

	private void saveMetadata()
	{
		final Metadata metadata = m_metadataProperties.getMetadata();
		final MetadataService service = MetadataService.get();
		if (service != null)
		{
			final String oldName = m_metadataList.getSelectedMetadataName();
			final String newName = metadata.getName();
			
			if (newName.length() == 0)
			{
				// No name given.
				Window.alert("The metadata has no name.  Please enter a name for this metadata.");
				m_metadataProperties.setNameFocus();
				m_cancelSelect = true;
				return;
			}

			boolean nameIsSame = oldName.equals(newName);
			if (oldName.length() == 0 || nameIsSame)
			{
				// If the metadata name hasn't changed or it's a new metadata
				// we can update immediately.
				updateMetadata(metadata, !nameIsSame);
			}
			else
			{
				// The name has changed so we need to rename before
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
							
							// Ensure the metadata we are working with is selected.
							m_metadataList.setSelectedMetadata(oldName);
						}
						else
						{
							// New name doesn't exist so rename the metadata.
							service.renameMetadata(oldName, newName, new RenameMetadataHandler()
							{
								public void onRenameMetadata()
								{
									updateMetadata(metadata, true);
								}
							});
						}
					}
				});
			}
		}
	}

	/**
	 * Checks if the currently selected metadata has been modified,
	 * and if so prompts the user to save.
	 */
	private void checkModified()
	{
		Metadata m = m_metadataProperties.getMetadata();
		if (m != null && m.getIsModified() && Window.confirm("The metadata '" + m.getName() + "' has been modified.  Do you want to save it?"))
		{
			saveMetadata();
		}
		else
		{
			performAction();
		}
	}
	
	public WindowSettings getWindowSettings()
	{
		return m_windowSettings;
	}
}

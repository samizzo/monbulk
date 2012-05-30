package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import monbulk.MetadataEditor.MetadataList.Handler;
import monbulk.client.Monbulk;
import monbulk.client.Roles;
import monbulk.client.Settings;
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
		WaitingForClone,
		WaitingForFromTemplate,
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

		User user = Monbulk.getUser();
		
		// Only user with CREATE or ADMIN roles can create/clone and edit.
		boolean canCreate = user.hasRole(Roles.MetadataEditor.CREATE) || user.hasRole(Roles.MetadataEditor.ADMIN);
		m_metadataList.setShowNew(canCreate);
		m_metadataList.setShowClone(canCreate);
		m_metadataList.setShowFromTemplate(canCreate);
		m_metadataProperties.setReadOnly(!canCreate);

		// Only user with ADMIN role can remove.
		boolean canRemove = user.hasRole(Roles.MetadataEditor.ADMIN);
		m_metadataList.setShowRemove(canRemove);
		
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
			
			public void onNewMetadata(Handler.NewType type)
			{
				switch (type)
				{
					case Clone:
					{
						m_state = State.WaitingForClone;
						break;
					}
					
					case New:
					{
						m_state = State.WaitingForNew;
						break;
					}
					
					case FromTemplate:
					{
						m_state = State.WaitingForFromTemplate;
						break;
					}
				}
				
				checkModified();
			}
		});
		
		m_metadataProperties.addSaveHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				saveMetadata(false);
			}
		});
		
		m_metadataProperties.addSaveTemplateHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				saveMetadata(true);
			}
		});
	}
	
	private void updateMetadata(final Metadata metadata, final boolean refresh, final boolean template, final String name)
	{
		MetadataService service = MetadataService.get();
		metadata.setName(name);
		service.updateMetadata(metadata, new UpdateMetadataHandler()
		{
			public void onUpdateMetadata(Metadata m)
			{
				m.clearModified();

				String msg = "The " + (template ? "template" : "metadata") + " '" + name + "' was saved."; 
				Window.alert(msg);

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
				
				m_metadataProperties.setSaveEnabled(false);

				performAction();
			}
		});
	}
	
	private void performAction()
	{
		m_metadataList.removeDummyItem();

		if (m_state == State.WaitingForRefresh)
		{
			m_metadataProperties.clear();
		}
		else if (m_state == State.WaitingForNew || m_state == State.WaitingForClone || m_state == State.WaitingForFromTemplate)
		{
			// Create a new, empty metadata object.
			Metadata m = (m_state == State.WaitingForClone || m_state == State.WaitingForFromTemplate) ? m_metadataProperties.getMetadata() : new Metadata("", "", "");
			if (m_state == State.WaitingForFromTemplate)
			{
				String name = m.getName();

				// Strip off ".template."
				int index = name.indexOf(".template.");
				assert(index >= 0);
				name = name.substring(0, index) + name.substring(index + 9);
				m.setName(name);
			}

			m.clearModified();
			m_metadataProperties.clear();
			m_metadataProperties.setMetadata(m);
			m_metadataProperties.setNameFocus();
			m_metadataList.clearSelection();
			m_metadataList.addDummyItem();
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

	/**
	 * Saves the current metadata.  If 'template' is true or
	 * the name is in the template namespace, the metadata
	 * will be saved as a template.
	 * @param template
	 */
	private void saveMetadata(final boolean template)
	{
		final Metadata metadata = m_metadataProperties.getMetadata();
		final MetadataService service = MetadataService.get();
		if (service != null)
		{
			final String oldName = m_metadataList.getSelectedMetadataName();
			String name = metadata.getName();

			Settings settings = Monbulk.getSettings();
			String defaultNs = settings.getDefaultNamespace();

			// Strip off default namespace.
			if (name.startsWith(defaultNs + "."))
			{
				name = name.substring(defaultNs.length() + 1);
			}

			final boolean isTemplate = name.startsWith("template.");

			// Strip off template namespace.
			if (isTemplate)
			{
				name = name.substring(9);
			}

			// Reconstruct the full namespace (it may have changed, e.g.
			// if we are saving a metadata as a template).
			String ns = defaultNs + (template || isTemplate ? ".template." : ".");
			name = ns + name;

			final String newName = name;
			final String type = template || isTemplate ? "template" : "metadata"; 

			if (newName.length() == 0 || newName.equals(ns))
			{
				// No name given.
				Window.alert("The " + type + " has no name.  Please enter a name for this " + type + ".");
				m_metadataProperties.setNameFocus();
				m_cancelSelect = true;
				return;
			}

			if (oldName.equals(newName))
			{
				// If the metadata name hasn't changed we can update immediately.
				updateMetadata(metadata, false, template || isTemplate, newName);
			}
			else
			{
				// The name has changed or it's a new metadata, so we may
				// need to rename before updating.  First check if the new name
				// already exists.
				service.metadataExists(newName, new MetadataExistsHandler()
				{
					public void onMetadataExists(String metadataName, boolean exists)
					{
						if (exists)
						{
							// New metadata name already exists, so don't allow the user to save yet.
							String msg = "A " + type + " with the name '" + metadataName + "' already exists.  Please use a different name.";
							Window.alert(msg);
							m_metadataProperties.setNameFocus();
							
							// Ensure the metadata we are working with is selected.
							m_metadataList.setSelectedMetadata(oldName);
						}
						else
						{
							// New name doesn't exist.
							if (template || oldName.length() == 0 || oldName.equals("<new metadata>"))
							{
								// Saving existing metadata as a template, or
								// it's a new metadata, so just update, don't
								// rename first.
								updateMetadata(metadata, true, template, newName);
							}
							else
							{
								// Rename the metadata first before updating.
								service.renameMetadata(oldName, newName, new RenameMetadataHandler()
								{
									public void onRenameMetadata()
									{
										updateMetadata(metadata, true, template || isTemplate, newName);
									}
								});
							}
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
			saveMetadata(false);
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
	
	public void onPreviewNativeEvent(NativePreviewEvent event)
	{
	}
}

package monbulk.MetadataEditor;

import java.util.List;

import monbulk.shared.Services.Metadata;
import monbulk.shared.Services.MetadataService;
import monbulk.shared.Services.MetadataService.CreateMetadataHandler;
import monbulk.shared.Services.MetadataService.DestroyMetadataHandler;
import monbulk.shared.Services.MetadataService.GetMetadataTypesHandler;
import monbulk.shared.Services.MetadataService.MetadataExistsHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class MetadataList extends Composite implements KeyUpHandler, KeyDownHandler
{
	public interface Handler
	{
		public void onMetadataSelected(String metadataName);
		public void onRefreshList();
		public void onRemoveMetadata(String metadataName);
	};

	private static MetadataListUiBinder uiBinder = GWT.create(MetadataListUiBinder.class);
	interface MetadataListUiBinder extends UiBinder<Widget, MetadataList> { }

	protected List<String> m_metadataTypes = null;
	private Handler m_handler = null;
	private String m_newMetadataName = null;
	private Boolean m_newMetadataExists = false;

	@UiField HTMLPanel m_buttonsPanel;
	@UiField Button m_refreshList;
	@UiField Button m_removeMetadata;
	@UiField Button m_newMetadata;
	@UiField Button m_saveMetadata;
	@UiField ListBox m_metadataListBox;
	@UiField TextBox m_filterTextBox;

	//Extended aglenn 6/5/12
	@UiField LayoutPanel m_LayoutPanel;
	
	// Item to select after metadata list is populated.
	private String m_itemToSelect = "";

	public MetadataList()
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		m_filterTextBox.addKeyUpHandler(this);
		m_filterTextBox.addKeyDownHandler(this);
		m_filterTextBox.setText("");
		
		populateListBox();
	}
	
	/**
	 * If 'showRefresh' is true the refresh button will be visible.
	 * @param showRefresh
	 */
	public void setShowRefresh(boolean showRefresh)
	{
		m_refreshList.setVisible(showRefresh);
	}
	
	/**
	 * if 'showRemove' is true the remove button will be visible.
	 * @param showRemove
	 */
	public void setShowRemove(boolean showRemove)
	{
		m_removeMetadata.setVisible(showRemove);
	}
	
	/**
	 * If 'showNew' is true the new button will be visible.
	 * @param showNew
	 */
	public void setShowNew(boolean showNew)
	{
		m_newMetadata.setVisible(showNew);
	}

	/**
	 * If 'showSave' is true the save button will be visible.
	 * @param showSave
	 */
	public void setShowSave(boolean showSave)
	{
		m_saveMetadata.setVisible(showSave);
	}
	
	/**
	 * Sets the metadata list handler.
	 * @param handler
	 */
	public void setHandler(Handler handler)
	{
		m_handler = handler;
	}
	
	/**
	 * Returns the panel that contains the buttons (can be used to add
	 * custom buttons to the control).
	 * @return
	 */
	public HTMLPanel getButtonsPanel()
	{
		return m_buttonsPanel;
	}

	/**
	 * Returns the selected metadata name, or empty string if none
	 * is selected.
	 * @return
	 */
	public String getSelectedMetadataName()
	{
		int index = m_metadataListBox.getSelectedIndex();
		return index >= 0 ? m_metadataListBox.getItemText(index) : "";
	}
	
	/**
	 * Sets the listbox focus state to 'focus'.
	 * @param focus
	 */
	public void setFocus(boolean focus)
	{
		m_metadataListBox.setFocus(focus);
	}

	/**
	 * Removes the specified metadata from the list.
	 * @param metadata
	 */
	public void remove(String metadata)
	{
		int newIndex = 0;
		int count = m_metadataListBox.getItemCount();
		for (int i = 0; i < count; i++)
		{
			String foo = m_metadataListBox.getItemText(i);
			if (foo.equals(metadata))
			{
				newIndex = i < (count - 1) ? i : (count - 2);
				m_metadataListBox.removeItem(i);
				break;
			}
		}
		
		// Remove from list.
		m_metadataTypes.remove(metadata);
		
		// Select next item in list.
		selectMetadata(newIndex);
	}

	/**
	 * Refreshes the metadata list.  If 'selection' is not an empty
	 * string, the metadata named 'selection' will be selected when
	 * the list has finished populating.
	 * @param selection
	 */
	public void refresh(String selection)
	{
		m_itemToSelect = selection;
		populateListBox();
		m_filterTextBox.setText("");
	}
	
	/**
	 * Adds a handler to the save button.
	 * @param event
	 */
	public void addSaveHandler(ClickHandler handler)
	{
		m_saveMetadata.addClickHandler(handler);
	}

	public void onKeyDown(KeyDownEvent event)
	{
		if (event.getSource() == m_filterTextBox)
		{
			// If user presses down arrow in filter text box, they
			// automatically start scrolling through the metadata list.
			if (event.isDownArrow())
			{
				m_metadataListBox.setFocus(true);
			}
		}
	}

	public void onKeyUp(KeyUpEvent event)
	{
		if (event.getSource() == m_filterTextBox)
		{
			int keyCode = event.getNativeKeyCode();
			if ((keyCode >= 'a' && keyCode <= 'z') ||
				(keyCode >= 'A' && keyCode <= 'Z' ) ||
				(keyCode == '.') ||
				(keyCode == KeyCodes.KEY_BACKSPACE) ||
				(keyCode == KeyCodes.KEY_DELETE))
			{
				// Filter list using filter text.
				String filterText = m_filterTextBox.getText();
				m_metadataListBox.clear();
				if (m_metadataTypes != null)
				{
					for (int i = 0; i < m_metadataTypes.size(); i++)
					{
						String m = m_metadataTypes.get(i);
						if (m.indexOf(filterText) >= 0 || filterText.length() == 0)
						{
							m_metadataListBox.addItem(m);
						}
					}
				}
			}
		}
	}

	public void populateListBox()
	{
		m_metadataListBox.clear();

		MetadataService service = MetadataService.get();
		if (service != null)
		{
			service.getMetadataTypes(new GetMetadataTypesHandler()
			{
				// Callback for reading a list of metadata.
				public void onGetMetadataTypes(List<String> types)
				{
					m_metadataTypes = types;
					
					if (types != null)
					{
						// Select the first item by default.
						int selectionIndex = types.size() > 0 ? 0 : -1;

						// Add all items.						
						for (int i = 0; i < types.size(); i++)
						{
							String name = types.get(i);
							m_metadataListBox.addItem(name);
							if (name.equals(m_itemToSelect))
							{
								// We've found the item we need to select.
								selectionIndex = i;
							}
						}
						
						if (selectionIndex != -1)
						{
							selectMetadata(selectionIndex);
							setFocus(true);
						}
					}
				}
			});
		}
	}

	private void selectMetadata(int index)
	{
		m_metadataListBox.setSelectedIndex(index);
		onMetadataSelected(null);
		m_itemToSelect = "";
	}
	
	@UiHandler("m_metadataListBox")
	protected void onMetadataSelected(ChangeEvent event)
	{
		if (m_handler != null)
		{
			int index = m_metadataListBox.getSelectedIndex();
			String selected = m_metadataListBox.getValue(index);
			m_handler.onMetadataSelected(selected);
		}
	}
	
	@UiHandler("m_refreshList")
	protected void onRefreshList(ClickEvent event)
	{
		refresh("");

		if (m_handler != null)
		{
			m_handler.onRefreshList();
		}
	}
	
	@UiHandler("m_removeMetadata")
	protected void onRemoveMetadata(ClickEvent event)
	{
		String selected = getSelectedMetadataName();

		if (selected.length() > 0)
		{
			String msg = "Are you sure you wish to remove '" + selected + "'?";
			if (Window.confirm(msg))
			{
				if (m_handler != null)
				{
					m_handler.onRemoveMetadata(selected);
				}

				// Call service to destroy metadata.
				MetadataService service = MetadataService.get();
				service.destroyMetadata(selected, new DestroyMetadataHandler()
				{
					public void onDestroyMetadata(String name, boolean success)
					{
						if (success)
						{
							// Metadata was successfully destroyed, so refresh our list.
							remove(name);
						}
					}
				});
			}
		}
	}
	
	@UiHandler("m_newMetadata")
	protected void onNewMetadata(ClickEvent event)
	{
		String msg = m_newMetadataExists ? "Metadata already exists!  Please enter a new name" : "Please enter a name";
		m_newMetadataName = Window.prompt(msg, "new metadata");

		if (m_newMetadataName != null && m_newMetadataName.length() > 0)
		{
			// Check if this metadata name exists.
			final MetadataService service = MetadataService.get();
			if (service != null)
			{
				service.metadataExists(m_newMetadataName, new MetadataExistsHandler()
				{
					// Callback for reading a specific metadata object.
					public void onMetadataExists(String metadataName, boolean exists)
					{
						if (exists)
						{
							// New metadata name already exists.  Ask the user to try again.
							m_newMetadataExists = true;
							onNewMetadata(null);
						}
						else
						{
							// New metadata name doesn't already exist.
							m_newMetadataExists = false;
							createNewMetadata(metadataName);
						}
					}
				});
			}
		}
	}
	
	private void createNewMetadata(String name)
	{
		MetadataService service = MetadataService.get();
		service.createMetadata(name, new CreateMetadataHandler()
		{
			public void onCreateMetadata(Metadata metadata, boolean success)
			{
				// Refresh list and select the new metadata.
				refresh(metadata.getName());
			}
		});
	}
	/**
	 * Added by aglenn
	 * @param shouldHide
	 * @since 196
	 */
	protected void hideListBox(Boolean shouldHide)
	{
		this.m_metadataListBox.setVisible(shouldHide);
	}
	/**
	 * Added by aglenn
	 * @return LayoutPanel the baseLayout for this widget
	 * @since 196
	 */
	protected LayoutPanel getLayout()
	{
		return m_LayoutPanel;
	}
	/**
	 * Added by aglenn
	 * @return String Value of the filter TextBox
	 * 196
	 */
	protected String getTextBoxValue()
	{
		return this.m_filterTextBox.getValue();
	}
	/**
	 * HACK - Need to shuffle widgets down after add as LayoutPanel.insert doesn't seem to be working with UIBinder 
	 * @param w	A widget to add into a layer
	 */
	protected void addWidget(Widget w)
	{
		this.m_LayoutPanel.add(w);
		//this.m_LayoutPanel.setWidgetVerticalPosition(w, Layout.Alignment.BEGIN);
		this.m_LayoutPanel.setWidgetTopHeight(w, 30, Style.Unit.PX, 510, Style.Unit.PX);
		//this.m_LayoutPanel.setWidgetTopHeight(this.m_filterTextBox, 0, Style.Unit.PX, 30, Style.Unit.PX);
		this.m_LayoutPanel.setWidgetTopHeight(this.m_buttonsPanel, 545, Style.Unit.PX, 24, Style.Unit.PX);
		
	}
}

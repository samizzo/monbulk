package monbulk.MetadataEditor;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

import monbulk.shared.Services.*;
import monbulk.shared.Services.Metadata.ElementTypes;
import monbulk.shared.Services.MetadataService.*;

public class MetadataEditor extends ResizeComposite implements KeyUpHandler, KeyDownHandler, SelectionHandler<TreeItem>, CommonElementPanel.ChangeTypeHandler
{
	interface MetadataEditorUiBinder extends UiBinder<Widget, MetadataEditor> { }
	private static MetadataEditorUiBinder s_uiBinder = GWT.create(MetadataEditorUiBinder.class);

	@UiField ListBox m_metadataListBox;
	@UiField TextBox m_filterTextBox;
	@UiField TextBox m_name;
	@UiField TextBox m_label;
	@UiField TextBox m_description;
	@UiField Button m_refreshList;
	@UiField Tree m_elementsTree;
	@UiField Button m_removeMetadata;
	@UiField Button m_newMetadata;
	@UiField VerticalPanel m_properties;
	@UiField Button m_addElement;
	@UiField Button m_removeElement;
	
	private List<String> m_metadataTypes = null;
	private ArrayList<ElementPanel> m_availablePanels = new ArrayList<ElementPanel>();
	private ArrayList<ElementPanel> m_elementPanels = new ArrayList<ElementPanel>();
	private TreeItem m_selectedElement = null;

	private String m_newMetadataName = null;
	private Boolean m_newMetadataExists = false;
	
	private Metadata m_selectedMetadata = null;
	
	// Item to select after metadata list is populated.
	private String m_itemToSelect = "";
	
	public MetadataEditor()
	{
		Widget widget = s_uiBinder.createAndBindUi(this);
		initWidget(widget);

		m_filterTextBox.addKeyUpHandler(this);
		m_filterTextBox.addKeyDownHandler(this);
		m_filterTextBox.setText("");

		m_elementsTree.addSelectionHandler(this);
		
		populateListBox();
		
		// Register all available element panels.
		m_availablePanels.add(new CommonElementPanel(this));
		m_availablePanels.add(new EnumerationElementPanel());
		m_availablePanels.add(new StringElementPanel());
		m_availablePanels.add(new DateElementPanel());
		m_availablePanels.add(new NumberElementPanel());
	}

	private void populateListBox()
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
						// Add all items.
						int selectionIndex = -1;
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
	
	private TreeItem createTreeItem(String name, Metadata.Element element, TreeItem root)
	{
		TreeItem treeItem = new TreeItem(name);
		treeItem.addStyleName("itemHighlight");
		if (root != null)
		{
			treeItem.addStyleName("noItemHighlight");
		}
		treeItem.setUserObject(element);
		return treeItem;
	}
	
	private void addElementTreeItem(TreeItem root, TreeItem newItem)
	{
		if (root != null)
		{
			root.addItem(newItem);
		}
		else
		{
			m_elementsTree.addItem(newItem);
		}
	}
	
	private TreeItem populateElementTree(TreeItem root, ArrayList<Metadata.Element> elements, Metadata.Element searchElement)
	{
		TreeItem result = null;

		for (Metadata.Element e : elements)
		{
			TreeItem treeItem = createTreeItem(e.getAttribute("name", ""), e, root);
			result = e == searchElement ? treeItem : result;

			if (e instanceof Metadata.DocumentElement)
			{
				Metadata.DocumentElement doc = (Metadata.DocumentElement)e;
				TreeItem r = populateElementTree(treeItem, doc.getElements(), searchElement);
				result = r != null ? r : result;
			}

			addElementTreeItem(root, treeItem);
		}
		
		return result;
	}

	@UiHandler("m_metadataListBox")
	void onMetadataSelected(ChangeEvent event)
	{
		MetadataService service = MetadataService.get();
		if (service != null)
		{
			updateSelectedElement();

			final int index = m_metadataListBox.getSelectedIndex();
			String selected = m_metadataListBox.getValue(index);
			service.getMetadata(selected, new GetMetadataHandler()
			{
				// Callback for reading a specific metadata object.
				public void onGetMetadata(Metadata metadata)
				{
					// If the currently selected metadata doesn't match
					// the one we kicked off this service call for, then
					// just ignore the result.												
					if (m_metadataListBox.getSelectedIndex() == index)
					{
						clearMetadataFields();
						Metadata m = metadata;
						m_selectedMetadata = m;
						m_addElement.setEnabled(true);
						m_name.setText(m.getName());
						m_label.setText(m.getLabel());
						m_description.setText(m.getDescription());
						populateElementTree(null, m.getElements(), null);
						if (m_elementsTree.getItemCount() > 0)
						{
							// Select the first item in the tree automatically.
							TreeItem treeItem = m_elementsTree.getItem(0);
							m_elementsTree.setSelectedItem(treeItem, true);
							
							// Now make sure the metadata list box has focus.
							m_metadataListBox.setFocus(true);
						}
					}
				}
			});
		}
	}

	// -------------------------------------------
	// Button handlers
	// -------------------------------------------

	@UiHandler("m_refreshList")
	void onRefreshList(ClickEvent event)
	{
		populateListBox();
		clearAllFields();
	}
	
	@UiHandler("m_removeMetadata")
	void onRemoveMetadata(ClickEvent event)
	{
		int index = m_metadataListBox.getSelectedIndex();
		if (index >= 0)
		{
			String name = m_metadataListBox.getItemText(index);
			String msg = "Are you sure you wish to remove '" + name + "'?";
			if (Window.confirm(msg))
			{
				// Call service to destroy metadata.
				MetadataService service = MetadataService.get();
				service.destroyMetadata(name, new DestroyMetadataHandler()
				{
					public void onDestroyMetadata(String name, boolean success)
					{
						if (success)
						{
							// Metadata was successfully destroyed, so refresh our list.
							removeMetadataFromList(name);
						}
					}
				});
			}
		}
	}
	
	private void removeMetadataFromList(String name)
	{
		int newIndex = 0;

		// Remove from list box.
		int count = m_metadataListBox.getItemCount();
		for (int i = 0; i < count; i++)
		{
			String foo = m_metadataListBox.getItemText(i);
			if (foo.equals(name))
			{
				newIndex = i < (count - 1) ? i : (count - 2);
				m_metadataListBox.removeItem(i);
				break;
			}
		}
		
		// Remove from list.
		m_metadataTypes.remove(name);
		
		// Select next item in list.
		selectMetadata(newIndex);
	}
	
	@UiHandler("m_newMetadata")
	void onNewMetadata(ClickEvent event)
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
				m_itemToSelect = metadata.getName();
				onRefreshList(null);
			}
		});
	}
	
	@UiHandler("m_addElement")
	public void onAddElementClicked(ClickEvent event)
	{
		try
		{
			Metadata.Element newElement = Metadata.createElement("string", "New element", "New element description");
			Metadata.Element selectedElement = m_selectedElement != null ? (Metadata.Element)m_selectedElement.getUserObject() : null;
			
			// If there is a selected element and it's a document element pull it out.
			Metadata.DocumentElement docElement = selectedElement != null && selectedElement.getType() == ElementTypes.Document ? (Metadata.DocumentElement)selectedElement : null;
			
			// If there is a doc element, add the new element to it, otherwise add
			// it to the metadata.
			ArrayList<Metadata.Element> elements = docElement != null ? docElement.getElements() : m_selectedMetadata.getElements();
			elements.add(newElement);

			// If the selected item is a doc element then use it as the parent. 
			TreeItem parentTreeItem = docElement != null ? m_selectedElement : null;

			// Add a new tree item and select it.
			TreeItem newTreeItem = createTreeItem(newElement.getAttribute("name", ""), newElement, parentTreeItem);
			addElementTreeItem(parentTreeItem, newTreeItem);
			m_elementsTree.setSelectedItem(newTreeItem);
			m_elementsTree.ensureSelectedItemVisible();
			
			// HACK: We have to set the item selected again to
			// really make sure it is visible because of this bug:
			// http://code.google.com/p/google-web-toolkit/issues/detail?id=1783
			m_elementsTree.setSelectedItem(newTreeItem);
		}
		catch (Exception e)
		{
			GWT.log(e.toString());
		}
	}
	
	@UiHandler("m_removeElement")
	public void onRemoveElementClicked(ClickEvent event)
	{
		Metadata.Element element = (Metadata.Element)m_selectedElement.getUserObject();
		Metadata.DocumentElement parent = element.getParent();

		// Remove from the metadata object itself, or the parent.
		ArrayList<Metadata.Element> elements = parent == null ? m_selectedMetadata.getElements() : parent.getElements();
		elements.remove(element);

		// Find the index of the item we are removing.
		TreeItem parentItem = m_selectedElement.getParentItem();
		int index = parentItem != null ? parentItem.getChildIndex(m_selectedElement) : getTreeItemIndex(m_selectedElement);

		if (parentItem == null)
		{
			// Remove from the tree itself.
			m_elementsTree.removeItem(m_selectedElement);
		}
		else
		{
			// Remove from its parent.
			parentItem.removeItem(m_selectedElement);
		}

		clearElements();

		// Select the next sibling or parent item.
		if (index >= 0)
		{
			if (parentItem == null)
			{
				// No parent, so select the next item in the root of the tree.
				int count = m_elementsTree.getItemCount();
				if (count > 0)
				{
					// Clamp the index.
					if (index >= count) 
					{
						index = count - 1;
					}

					TreeItem item = m_elementsTree.getItem(index);
					m_elementsTree.setSelectedItem(item);
				}
			}
			else
			{
				// Parent exists, so select the next child of the parent.
				int count = parentItem.getChildCount();
				
				// If there are no more children we will select the parent.
				TreeItem item = parentItem;
				if (count > 0)
				{
					// Clamp the index.
					if (index >= count)
					{
						index = count - 1;
					}

					item = parentItem.getChild(index);
				}
				
				m_elementsTree.setSelectedItem(item);
			}
		}
	}

	// -------------------------------------------
	// End of button handlers
	// -------------------------------------------

	private void clearAllFields()
	{
		m_filterTextBox.setText("");
		clearMetadataFields();
	}
	
	private void clearElements()
	{
		m_elementPanels.clear();
		m_properties.clear();
		m_selectedElement = null;
		m_removeElement.setEnabled(false);
	}

	private void clearMetadataFields()
	{
		m_selectedMetadata = null;
		m_addElement.setEnabled(false);
		m_name.setText("");
		m_label.setText("");
		m_description.setText("");
		m_elementsTree.clear();
		clearElements();
	}
	
	private void addElementPanel(ElementPanel panel)
	{
		m_properties.add(panel);
		m_elementPanels.add(panel);
	}
	
	private int getTreeItemIndex(TreeItem item)
	{
		int count = m_elementsTree.getItemCount();
		for (int i = 0; i < count; i++)
		{
			if (m_elementsTree.getItem(i) == item)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public void onChangeType(Metadata.Element element, String newType)
	{
		// User has changed the type of this element.
		
		// Create new element from old.
		Metadata.Element newElement = null;
		try
		{
			Metadata.ElementTypes t = Metadata.ElementTypes.valueOf(newType);
			newElement = Metadata.createElement(t.getMetaName(), element.getAttribute("name", ""), element.getDescription());
		}
		catch (Exception e)
		{
			GWT.log(e.toString());
			return;
		}

		Metadata.DocumentElement docParent = element.getParent();
		ArrayList<Metadata.Element> elements = docParent == null ? m_selectedMetadata.getElements() : docParent.getElements();
		newElement.setParent(docParent);
		
		int oldIndex = elements.indexOf(element);
		elements.remove(element);
		elements.add(oldIndex, newElement);

		// Refresh the tree.
		m_properties.clear();
		TreeItem parent = m_selectedElement.getParentItem();
		int index = parent != null ? parent.getChildIndex(m_selectedElement) : getTreeItemIndex(m_selectedElement);
		
		if (index >= 0)
		{
			TreeItem newItem = createTreeItem(newElement.getAttribute("name", ""), newElement, parent);

			if (parent != null)
			{
				parent.removeItem(m_selectedElement);
				parent.insertItem(index, newItem);
			}
			else
			{
				m_elementsTree.removeItem(m_selectedElement);
				m_elementsTree.insertItem(index, newItem);
			}

			m_elementsTree.setSelectedItem(newItem, true);
		}
	}
	
	// Updates the currently selected element from the user's settings
	// on the panel.
	private void updateSelectedElement()
	{
		if (m_selectedElement != null)
		{
			Object o = m_selectedElement.getUserObject();
			if (o != null)
			{
				Metadata.Element element = (Metadata.Element)o;
				for (ElementPanel e : m_elementPanels)
				{
					e.update(element);
				}
			}
		}
	}
	
	// Tree view selection handler.
	public void onSelection(SelectionEvent<TreeItem> event)
	{
		// Update metadata from user settings and remove highlight from
		// previously selected item.
		if (m_selectedElement != null)
		{
			updateSelectedElement();
			m_selectedElement.removeStyleName("itemSelected");
		}

		// Add highlight to newly selected item.
		TreeItem selectedItem = event.getSelectedItem();
		selectedItem.addStyleName("itemSelected");

		clearElements();
		m_selectedElement = selectedItem;
		m_removeElement.setEnabled(true);

		Object o = selectedItem.getUserObject();
		if (o != null)
		{
			final Metadata.Element element = (Metadata.Element)o;

			// Add the appropriate panels.
			for (ElementPanel e : m_availablePanels)
			{
				if (e.getType() == Metadata.ElementTypes.All ||
					e.getType().isSame(element.getType()))
				{
					addElementPanel(e);
				}
			}

			// Update the panels with the element's data.
			for (ElementPanel e : m_elementPanels)
			{
				e.set(element);
			}
		}
	}
}

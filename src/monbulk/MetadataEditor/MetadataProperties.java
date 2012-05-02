package monbulk.MetadataEditor;

import java.util.ArrayList;

import monbulk.shared.Services.Metadata;
import monbulk.shared.Services.MetadataService;
import monbulk.shared.Services.Metadata.ElementTypes;
import monbulk.shared.Services.MetadataService.GetMetadataHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

public class MetadataProperties extends Composite implements SelectionHandler<TreeItem>, CommonElementPanel.ChangeTypeHandler
{
	private static MetadataPropertiesUiBinder uiBinder = GWT.create(MetadataPropertiesUiBinder.class);
	interface MetadataPropertiesUiBinder extends UiBinder<Widget, MetadataProperties> { }

	@UiField TextBox m_name;
	@UiField TextBox m_label;
	@UiField TextBox m_description;
	@UiField Tree m_elementsTree;
	@UiField ElementProperties m_elementProperties;
	@UiField Button m_addElement;
	@UiField Button m_removeElement;
	@UiField HTMLPanel m_addRemovePanel;
	@UiField CaptionPanel m_elements;

	private TreeItem m_selectedElement = null;
	private Metadata m_metadata = null;

	public MetadataProperties()
	{
		initWidget(uiBinder.createAndBindUi(this));
		m_elementsTree.addSelectionHandler(this);
		m_elementProperties.setChangeTypeHandler(this);
	}

	public void setReadOnly(boolean readOnly)
	{
		// FIXME: Currently this only works if you set MetadataProperties to
		// read-only.  If you try to set it back to not read-only the add/remove
		// panel won't be re-added.  We don't need this functionality at the moment
		// so I haven't implemented it.
		if (readOnly)
		{
			m_addRemovePanel.removeFromParent();
			float height = 175 + 29;
			LayoutPanel p = (LayoutPanel)getWidget();
			p.setWidgetTopHeight(m_elements, 126, Unit.PX, height, Unit.PX);
		}
		
		m_name.setEnabled(!readOnly);
		m_label.setEnabled(!readOnly);
		m_description.setEnabled(!readOnly);
		m_elementProperties.setReadOnly(readOnly);
	}

	public void setMetadata(String name)
	{
		MetadataService service = MetadataService.get();
		if (service != null)
		{
			service.getMetadata(name, new GetMetadataHandler()
			{
				// Callback for reading a specific metadata object.
				public void onGetMetadata(Metadata metadata)
				{
					setMetadata(metadata);
				}
			});
		}
	}

	public void setMetadata(Metadata metadata)
	{
		m_elementProperties.updateCurrentElement();
		
		clear();
		m_metadata = metadata;
		m_addElement.setEnabled(true);
		m_name.setText(metadata.getName());
		m_label.setText(metadata.getLabel());
		m_description.setText(metadata.getDescription());
		populateElementTree(null, metadata.getElements(), null);
		if (m_elementsTree.getItemCount() > 0)
		{
			// Select the first item in the tree automatically.
			TreeItem treeItem = m_elementsTree.getItem(0);
			m_elementsTree.setSelectedItem(treeItem, true);
		}
	}

	public void clear()
	{
		m_metadata = null;
		m_addElement.setEnabled(false);
		m_name.setText("");
		m_label.setText("");
		m_description.setText("");
		m_elementsTree.clear();
		clearElements();
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
			if (e.getType().isVisible())
			{
				TreeItem treeItem = createTreeItem(e.getSetting("name", ""), e, root);
				result = e == searchElement ? treeItem : result;
	
				if (e instanceof Metadata.DocumentElement)
				{
					Metadata.DocumentElement doc = (Metadata.DocumentElement)e;
					TreeItem r = populateElementTree(treeItem, doc.getElements(), searchElement);
					result = r != null ? r : result;
				}
	
				addElementTreeItem(root, treeItem);
			}
		}
		
		return result;
	}

	// -------------------------------------------
	// Button handlers
	// -------------------------------------------

	@UiHandler("m_addElement")
	public void onAddElementClicked(ClickEvent event)
	{
		try
		{
			Metadata.Element newElement = Metadata.createElement("string", "New element", "New element description", false);
			Metadata.Element selectedElement = m_selectedElement != null ? (Metadata.Element)m_selectedElement.getUserObject() : null;
			
			// If there is a selected element and it's a document element pull it out.
			Metadata.DocumentElement docElement = selectedElement != null && selectedElement.getType() == ElementTypes.Document ? (Metadata.DocumentElement)selectedElement : null;
			
			// If there is a doc element, add the new element to it, otherwise add
			// it to the metadata.
			ArrayList<Metadata.Element> elements = docElement != null ? docElement.getElements() : m_metadata.getElements();
			elements.add(newElement);

			// If the selected item is a doc element then use it as the parent. 
			TreeItem parentTreeItem = docElement != null ? m_selectedElement : null;

			// Add a new tree item and select it.
			TreeItem newTreeItem = createTreeItem(newElement.getSetting("name", ""), newElement, parentTreeItem);
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
		ArrayList<Metadata.Element> elements = parent == null ? m_metadata.getElements() : parent.getElements();
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

	private void clearElements()
	{
		m_elementProperties.setElement(null, true);
		m_selectedElement = null;
		m_removeElement.setEnabled(false);
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
			newElement = Metadata.createElement(t.getMetaName(), element.getSetting("name", ""), element.getDescription(), false);
		}
		catch (Exception e)
		{
			GWT.log(e.toString());
			return;
		}

		Metadata.DocumentElement docParent = element.getParent();
		ArrayList<Metadata.Element> elements = docParent == null ? m_metadata.getElements() : docParent.getElements();
		newElement.setParent(docParent);
		
		int oldIndex = elements.indexOf(element);
		elements.remove(element);
		elements.add(oldIndex, newElement);

		// Refresh the tree.
		TreeItem parent = m_selectedElement.getParentItem();
		int index = parent != null ? parent.getChildIndex(m_selectedElement) : getTreeItemIndex(m_selectedElement);
		
		if (index >= 0)
		{
			TreeItem newItem = createTreeItem(newElement.getSetting("name", ""), newElement, parent);

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
	
	// Tree view selection handler.
	public void onSelection(SelectionEvent<TreeItem> event)
	{
		// Update metadata from user settings and remove highlight from
		// previously selected item.
		if (m_selectedElement != null)
		{
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
			Metadata.Element element = (Metadata.Element)o;
			m_elementProperties.setElement(element, true);
		}
	}
}

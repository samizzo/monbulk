package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;

import monbulk.shared.Services.Metadata;
import monbulk.shared.widgets.Window.WindowSettings;
import monbulk.shared.widgets.Window.OkCancelWindow.OkCancelHandler;
import monbulk.client.desktop.Desktop;

/*
 * TODO: Tidy this up so it mirrors the metadata properties.  The editor
 * should work with a clone of the attribute.
 */

public class AttributesPanel extends ElementPanel implements OkCancelHandler, CommonElementPanel.ChangeTypeHandler
{
	private static AttributesPanelUiBinder uiBinder = GWT.create(AttributesPanelUiBinder.class);
	interface AttributesPanelUiBinder extends UiBinder<Widget, AttributesPanel> { }

	@UiField ListBox m_attributes;
	@UiField Button m_add;
	@UiField Button m_remove;
	@UiField Button m_edit;
	
	private Metadata.Element m_editAttribute;
	private Metadata.Element m_newAttribute;
	private boolean m_addNewElement = false;
	private boolean m_typeChanged = false;
	private ElementEditor m_elementEditor;

	public AttributesPanel() throws Exception
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		// Register our own element editor window.
		m_elementEditor = new ElementEditor(false);
		m_elementEditor.setChangeTypeHandler(this);
		WindowSettings w = m_elementEditor.getWindowSettings();
		w.windowId = "ElementEditor-Attributes";
		w.windowTitle = "Attribute";
		Desktop.get().registerWindow(m_elementEditor);
	}

	public void set(Metadata.Element element)
	{
		super.set(element);
		m_attributes.clear();
		
		int numAttributes = element.getNumAttributes();
		for (int i = 0; i < numAttributes; i++)
		{
			Metadata.Element attribute = element.getAttribute(i);
			String name = attribute.getName();
			if (name.length() > 0)
			{
				m_attributes.addItem(name);
			}
		}
		
		setButtonState(false);
	}

	public void update(Metadata.Element element)
	{
	}

	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.Attribute;
	}
	
	@UiHandler("m_attributes")
	void onAttributeSelected(ChangeEvent event)
	{
		setButtonState(true);
	}
	
	private void showEditor(Metadata.Element element, boolean addNewElement)
	{
		// Show the attributes editor.
		
		Desktop d = Desktop.get();

		m_typeChanged = false;
		m_editAttribute = element;
		m_addNewElement = addNewElement;

		m_elementEditor.setOkCancelHandler(this);
		m_elementEditor.setMetadataElement(element);
		d.show(m_elementEditor, true);
	}

	@UiHandler("m_edit")
	void onEditClicked(ClickEvent event)
	{
		int index = m_attributes.getSelectedIndex();
		int numAttributes = m_element.getNumAttributes();
		if (index >= 0 && index < numAttributes) 
		{
			// Show the editor and set the element to edit.
			Metadata.Element element = m_element.getAttribute(index);
			showEditor(element, false);
		}
	}
	
	@UiHandler("m_remove")
	void onRemoveClicked(ClickEvent event)
	{
		int index = m_attributes.getSelectedIndex();
		int numAttributes = m_element.getNumAttributes();
		if (index >= 0 && index < numAttributes)
		{
			m_element.removeAttribute(index);
			m_attributes.removeItem(index);
			
			index = index < numAttributes ? index : (numAttributes - 1);
			if (index >= 0)
			{
				m_attributes.setSelectedIndex(index);
			}
			else
			{
				setButtonState(false);
			}
		}
	}
	
	@UiHandler("m_add")
	void onAddClicked(ClickEvent event)
	{
		try
		{
			Metadata.Element element = Metadata.createElement("String", "attribute", "An attribute", true);
			showEditor(element, true);
		}
		catch (Exception e)
		{
			GWT.log(e.toString());
		}
	}
	
	private void setButtonState(boolean enabled)
	{
		m_edit.setEnabled(enabled);
		m_remove.setEnabled(enabled);
	}
	
	public void onOkCancelClicked(OkCancelHandler.Event event)
	{
		switch (event)
		{
			case Ok:
			{
				if (m_addNewElement)
				{
					// Adding a new element, so add it to the list and the
					// parent element.
					String name = m_editAttribute.getName();
					if (name.length() > 0)
					{
						m_attributes.addItem(name);
						m_element.addAttribute(m_editAttribute);
					}
				}
				else
				{
				 	if (m_typeChanged)
				 	{
						// Editing an attribute and it changed type, so replace
						// the old element with the new one.
						m_element.replaceAttribute(m_editAttribute, m_newAttribute);
						m_editAttribute = m_newAttribute;
				 	}
				 	
				 	// Update the name in the list box.
				 	int selected = m_attributes.getSelectedIndex();
				 	m_attributes.setItemText(selected, m_editAttribute.getName());
				}
				break;
			}
			
			case Cancel:
			{
				break;
			}
		}
		
		m_newAttribute = m_editAttribute = null;
	}
	
	// We need to process a change of type event by creating a new element
	// and giving it to the owning metadata.
	// FIXME: This should be in the attributes editor not here.
	public void onChangeType(Metadata.Element element, String newType)
	{
		// Create new element from old and give it to the editor.
		try
		{
			Metadata.ElementTypes t = Metadata.ElementTypes.valueOf(newType);
			Metadata.Element newAttribute = Metadata.createElement(t.getTypeName(), element.getName(), element.getDescription(), true);
			if (m_addNewElement)
			{
				// Adding a new attribute so just overwrite the
				// previous m_editAttribute.
				m_editAttribute = m_newAttribute = newAttribute;
			}
			else
			{
				// Changing the type of an existing attribute so
				// we need to remember the previous attribute.
				m_newAttribute = newAttribute;
			}

			m_elementEditor.setMetadataElement(m_newAttribute);
			m_typeChanged = true;
		}
		catch (Exception e)
		{
			GWT.log(e.toString());
			return;
		}
	}
}

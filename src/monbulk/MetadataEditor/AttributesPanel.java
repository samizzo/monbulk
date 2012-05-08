package monbulk.MetadataEditor;

import java.util.ArrayList;

import monbulk.shared.Services.Metadata;
import monbulk.client.desktop.Desktop;
import monbulk.client.event.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;

public class AttributesPanel extends ElementPanel implements WindowEventHandler
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

	public AttributesPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void set(Metadata.Element element)
	{
		super.set(element);
		m_attributes.clear();
		
		for (Metadata.Element e : element.getAttributes())
		{
			String name = e.getSetting("name", "");
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
		Desktop d = Desktop.get();

		// Listen for window events so we can process ok/cancel buttons.
		d.getEventBus().addHandler(WindowEvent.TYPE, this);

		m_typeChanged = false;
		m_editAttribute = element;
		m_addNewElement = addNewElement;

		final AttributesEditor editor = (AttributesEditor)d.getWindow("AttributesEditor");
		editor.setElement(element);
		editor.setChangeTypeHandler(new CommonElementPanel.ChangeTypeHandler()
		{
			public void onChangeType(Metadata.Element element, String newType)		
			{
				// Create new element from old and give it to the editor.
				try
				{
					Metadata.ElementTypes t = Metadata.ElementTypes.valueOf(newType);
					Metadata.Element newAttribute = Metadata.createElement(t.getMetaName(), element.getSetting("name", ""), element.getDescription(), true);
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

					editor.setElement(m_newAttribute);
					m_typeChanged = true;
				}
				catch (Exception e)
				{
					GWT.log(e.toString());
					return;
				}

			}
		});
		d.show("AttributesEditor", true);
	}

	@UiHandler("m_edit")
	void onEditClicked(ClickEvent event)
	{
		int index = m_attributes.getSelectedIndex();
		ArrayList<Metadata.Element> attributes = m_element.getAttributes();
		if (index >= 0 && index < attributes.size()) 
		{
			// Show the editor and set the element to edit.
			Metadata.Element element = attributes.get(index);
			showEditor(element, false);
		}
	}
	
	@UiHandler("m_remove")
	void onRemoveClicked(ClickEvent event)
	{
		int index = m_attributes.getSelectedIndex();
		ArrayList<Metadata.Element> attributes = m_element.getAttributes();
		if (index >= 0 && index < attributes.size())
		{
			attributes.remove(index);
			m_attributes.removeItem(index);
			
			index = index < attributes.size() ? index : (attributes.size() - 1);
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
	
	public void onWindowEvent(WindowEvent event)
	{
		if (event.getWindowId().equals("AttributesEditor"))
		{
			Desktop.get().getEventBus().removeHandler(WindowEvent.TYPE, this);

			switch (event.getEventType())
			{
				case Ok:
				{
					ArrayList<Metadata.Element> attributes = m_element.getAttributes();

					if (m_addNewElement)
					{
						// Adding a new element, so add it to the list and the
						// parent element.
						String name = m_editAttribute.getSetting("name", "");
						if (name.length() > 0)
						{
							m_attributes.addItem(name);
							attributes.add(m_editAttribute);
						}
					}
					else
					{
					 	if (m_typeChanged)
					 	{
							// Editing an attribute and it changed type, so replace
							// the old element with the new one.
							int index = attributes.indexOf(m_editAttribute);
							attributes.set(index, m_newAttribute);
							m_editAttribute = m_newAttribute;
					 	}
					 	
					 	// Update the name in the list box.
					 	int selected = m_attributes.getSelectedIndex();
					 	m_attributes.setItemText(selected, m_editAttribute.getSetting("name", ""));
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
	}
}

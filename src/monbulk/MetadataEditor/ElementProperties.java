package monbulk.MetadataEditor;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.KeyUpHandler;
import monbulk.shared.Services.Metadata;

public class ElementProperties extends Composite
{
	private static ElementPropertiesUiBinder uiBinder = GWT.create(ElementPropertiesUiBinder.class);
	interface ElementPropertiesUiBinder extends UiBinder<Widget, ElementProperties> { }

	@UiField VerticalPanel m_properties;
	
	private Metadata.Element m_element;
	private ArrayList<ElementPanel> m_availablePanels = new ArrayList<ElementPanel>();
	private ArrayList<ElementPanel> m_elementPanels = new ArrayList<ElementPanel>();
	
	public ElementProperties(boolean canHaveAttributes) throws Exception
	{
		// Register all available element panels.
		m_availablePanels.add(new CommonElementPanel());
		m_availablePanels.add(new EnumerationElementPanel());
		m_availablePanels.add(new StringElementPanel());
		m_availablePanels.add(new DateElementPanel());
		m_availablePanels.add(new NumberElementPanel());
		if (canHaveAttributes)
		{
			m_availablePanels.add(new AttributesPanel());
		}
		m_availablePanels.add(new DocumentElementPanel());

		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private CommonElementPanel getCommonElementPanel()
	{
		for (ElementPanel p : m_availablePanels)
		{
			if (p instanceof CommonElementPanel)
			{
				CommonElementPanel panel = (CommonElementPanel)p;
				return panel;
			}
		}
		
		return null;
	}
	
	public void setNameFocus()
	{
		CommonElementPanel p = getCommonElementPanel();
		p.setNameFocus();
	}
	
	public void addNameKeyUpHandler(KeyUpHandler handler)
	{
		CommonElementPanel p = getCommonElementPanel();
		p.addNameKeyUpHandler(handler);
	}
	
	public String getName()
	{
		CommonElementPanel p = getCommonElementPanel();
		return p.getName();
	}

	public void setChangeTypeHandler(CommonElementPanel.ChangeTypeHandler handler)
	{
		CommonElementPanel p = getCommonElementPanel();
		if (p != null)
		{
			p.setChangeTypeHandler(handler);
		}
	}

	public Metadata.Element getMetadataElement()
	{
		return m_element;
	}
	
	public void setMetadataElement(Metadata.Element element, boolean updateCurrent)
	{
		clear(updateCurrent);

		m_element = element;
		
		if (element != null)
		{
			// Add the appropriate panels for this element.
			for (ElementPanel e : m_availablePanels)
			{
				if (e.getType() == Metadata.ElementTypes.All ||
					e.getType().isSame(element.getType()) ||
					(e.getType() == Metadata.ElementTypes.Attribute && element.canHaveAttributes()))
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

	// Sets all element panels to read-only.
	public void setReadOnly(boolean readOnly)
	{
		for (ElementPanel e : m_availablePanels)
		{
			e.setReadOnly(readOnly);
		}
	}
	
	// Removes all element panels.
	public void clear(boolean updateCurrent)
	{
		if (updateCurrent)
		{
			updateCurrentElement();
		}

		m_element = null;
		m_elementPanels.clear();
		m_properties.clear();
	}
	
	/**
	 *  Updates the currently selected element from the user's settings
	 *  on the panels.
	 */
	public void updateCurrentElement()
	{
		if (m_element != null)
		{
			for (ElementPanel e : m_elementPanels)
			{
				e.update(m_element);
			}
		}
	}
	
	private void addElementPanel(ElementPanel panel)
	{
		m_properties.add(panel);
		m_elementPanels.add(panel);
	}
}

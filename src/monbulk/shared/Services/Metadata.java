package monbulk.shared.Services;

import java.util.List;
import arc.mf.client.xml.*;

public class Metadata
{
	public interface ModifiedHandler
	{
		public void onModified();
	}

	private DocumentElement m_rootElement = new DocumentElement("root", "root", Element.ElementTypes.Document);
	private ModifiedHandler m_modifiedHandler = null;
	
	public Metadata(String name, String description, String label)
	{
		setName(name);
		setDescription(description);
		setLabel(label);
		m_rootElement.setMetadata(this);
	}

	/**
	 * Returns true if the metadata has been modified.
	 * @return
	 */
	public boolean getIsModified()
	{
		return m_rootElement.getIsModified();
	}

	/**
	 * Sets the name of this metadata.
	 * @param name
	 */
	public void setName(String name)
	{
		m_rootElement.setSetting("name", name);
	}
	
	/**
	 * Returns the name of this metadata.
	 * @return
	 */
	public String getName()
	{
		return m_rootElement.getName();
	}
	
	/**
	 * Sets the description of this metadata.
	 * @param description
	 */
	public void setDescription(String description)
	{
		m_rootElement.setDescription(description);
	}
	
	/**
	 * Returns the description of this metadata.
	 * @return
	 */
	public String getDescription()
	{
		return m_rootElement.getDescription();
	}
	
	/**
	 * Sets the label for this metadata.
	 * @param label
	 */
	public void setLabel(String label)
	{
		m_rootElement.setSetting("label", label);
	}
	
	/**
	 * Returns the label for this metadata.
	 * @return
	 */
	public String getLabel()
	{
		return m_rootElement.getSetting("label", "");
	}
	
	/**
	 * Returns the root element for this metadata.  The root element is a
	 * DocumentElement but is not itself saved; it acts as a container
	 * for the root-level elements.
	 * @return
	 */
	public DocumentElement getRootElement()
	{
		return m_rootElement;
	}
	
	/**
	 * Clears the modified flag for this metadata and all its elements.
	 */
	public void clearModified()
	{
		m_rootElement.clearModified();
	}
	
	/**
	 * Create a new metadata object from the specified MF xml definition.
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public static Metadata create(XmlElement element) throws Exception
	{
		XmlElement type = element.element("type");
		Metadata metadata = new Metadata(type.value("@name"), type.value("description"), type.value("label"));
		parseMetadata(metadata.getRootElement(), type.elements("definition/element"), metadata);
		metadata.clearModified();	// Some things during creation set the modified flag.
		return metadata;
	}
	
	private static void parseMetadata(DocumentElement parent, List<XmlElement> xmlElements, Metadata metadata) throws Exception
	{
		if (xmlElements == null)
		{
			return;
		}

		for (XmlElement e : xmlElements)
		{
			Element element = Element.createElement(e);
			parent.addChild(element);
			element.setMetadata(metadata);
			if (element instanceof DocumentElement)
			{
				DocumentElement docElement = (DocumentElement)element;
				parseMetadata(docElement, e.elements("element"), metadata);
			}
		}
	}
	
	/**
	 * Returns an xml representation of this metadata.
	 * @return
	 */
	public String getXml()
	{
		XmlStringWriter x = new XmlStringWriter();
		x.add("type", getName());

		String label = getLabel();
		if (label != null && label.length() > 0)
		{
			x.add("label", label);
		}
		
		String description = getDescription();
		if (description != null && description.length() > 0)
		{
			x.add("description", description);
		}
		
		x.push("definition");
		int numChildren = m_rootElement.getNumChildren();
		for (int i = 0; i < numChildren; i++)
		{
			Element e = m_rootElement.getChild(i);
			e.addXml(x);

			// This feels a bit messy.  Pop should probably be in addXml()
			// but derived metadata elements need to call up to the parent
			// class to add common xml/attributes, so the parent can't pop.
			x.pop(); 
		}
		x.pop();

		return x.document();
	}

	/**
	 * Sets the modified handler for this metadata object.
	 * @param handler
	 */
	public void setModifiedHandler(ModifiedHandler handler)
	{
		m_modifiedHandler = handler;
	}
	
	/**
	 * Calls any attached modified handler.
	 */
	public void onModified()
	{
		if (m_modifiedHandler != null)
		{
			m_modifiedHandler.onModified();
		}
	}
}
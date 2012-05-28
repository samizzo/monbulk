package monbulk.shared.Services;

import java.util.ArrayList;

import arc.mf.client.xml.XmlElement;
import arc.mf.client.xml.XmlStringWriter;

import com.google.gwt.core.client.GWT;


/**
 * A document element has a number of child elements.
 */
public class DocumentElement extends Element
{
	public enum ReferenceType
	{
		Document,
		Element;
		
		public static ReferenceType parse(String value) throws Exception
		{
			if (value != null)
			{
				for (ReferenceType r : ReferenceType.values())
				{
					if (r.toString().equalsIgnoreCase(value))
					{
						return r;
					}
				}
			}
			
			throw new Exception("Unknown reference type '" + value + "'");
		}
	};

	private ArrayList<Element> m_children = new ArrayList<Element>();
	private boolean m_isReference = false;
	private ReferenceType m_referenceType;
	private String m_referenceName;
	private String m_referenceValue;

	/**
	 * Copy constructor.  Creates a new DocumentElement from an existing element.
	 * @param element
	 */
	protected DocumentElement(DocumentElement element)
	{
		super(element);
		
		for (Element e : element.m_children)
		{
			Element child = e.clone();
			m_children.add(child);
			child.m_parent = this;
		}

		m_isReference = element.m_isReference;
		m_referenceType = element.m_referenceType;
		m_referenceName = element.m_referenceName;
		m_referenceValue = element.m_referenceValue;
	}
	
	protected DocumentElement(String name, String description, Element.ElementTypes type)
	{
		super(type, name, description, false);
	}

	/**
	 * Returns true if this element or any of its children have been modified.
	 * @return
	 */
	public boolean getIsModified()
	{
		for (Element e : m_children)
		{
			if (e.getIsModified())
			{
				return true;
			}
		}
		
		return super.getIsModified();
	}
	
	/**
	 * Clears the modified flag for any child elements.
	 */
	public void clearModified()
	{
		super.clearModified();
		
		for (Element e : m_children)
		{
			e.clearModified();
		}
	}
	
	/**
	 * Replaces an element in this DocumentElement.
	 * @param oldElement
	 * @param newElement
	 */
	public void replaceChild(Element oldElement, Element newElement)
	{
		int index = m_children.indexOf(oldElement);
		if (index >= 0)
		{
			m_children.set(index, newElement);
			newElement.m_parent = this;
			onModified();
		}
	}

	/**
	 * Returns the number of children in this DocumentElement.
	 * @return
	 */
	public int getNumChildren()
	{
		return m_children.size();
	}
	
	/**
	 * Returns a specific child of this DocumentElement.
	 * @param index
	 * @return
	 */
	public Element getChild(int index)
	{
		return m_children.get(index);
	}
	
	/**
	 * Removes a specified child from this DocumentElement.
	 * @param element
	 */
	public void removeChild(Element element)
	{
		m_children.remove(element);
		onModified();
	}
	
	public void addChild(Element element)
	{
		m_children.add(element);
		element.m_parent = this;
		element.onModified();
		onModified();
	}
	
	/**
	 * Returns false.  DocumentElement type cannot have attributes.
	 */
	public boolean canHaveAttributes()
	{
		return false;
	}

	public void setFromXmlElement(XmlElement xmlElement)
	{
		super.setFromXmlElement(xmlElement);
		
		XmlElement reference = xmlElement.element("reference");
		if (reference != null)
		{
			try
			{
				m_referenceType = ReferenceType.parse(reference.value("@type"));
				m_referenceName = reference.value("@name");
				if (m_referenceName == null)
				{
					m_referenceName = "";
				}
				m_referenceValue = reference.value("value");
				m_isReference = true;

				// It's a DocumentElement but it's a reference so we override the type. 
				m_type = Element.ElementTypes.Reference;
			}
			catch (Exception e)
			{
				GWT.log("Bad reference: " + e.toString());
			}
		}
	}
	
	public String getReferenceName()
	{
		return m_referenceName;
	}
	
	public void setReferenceName(String referenceName)
	{
		m_referenceName = referenceName;
		onModified();
	}
	
	public ReferenceType getReferenceType()
	{
		return m_referenceType;
	}
	
	public void setReferenceType(ReferenceType referenceType)
	{
		m_referenceType = referenceType;
		onModified();
	}
	
	public String getReferenceValue()
	{
		return m_referenceValue;
	}
	
	public void setReferenceValue(String referenceValue)
	{
		m_referenceValue = referenceValue;
		onModified();
	}
	
	public Element clone()
	{
		return new DocumentElement(this);
	}
	
	/**
	 * Append an xml description of this element to the XmlStringWriter.
	 * @param w
	 */
	public void addXml(XmlStringWriter w)
	{
		super.addXml(w);

		if (m_type == Element.ElementTypes.Reference)
		{
			// Add reference info.
			String[] attributes = new String[4];
			int index = 0;
			attributes[index++] = "name";
			attributes[index++] = getReferenceName();
			attributes[index++] = "type";
			attributes[index++] = getReferenceType().toString().toLowerCase();
			w.push("reference", attributes);
			{
				w.add("value", getReferenceValue());
			}
			w.pop();
		}
		
		if (m_children != null && m_children.size() > 0)
		{
			// Add children.
			for (Element e : m_children)
			{
				e.addXml(w);
				w.pop();
			}
		}
	}
}

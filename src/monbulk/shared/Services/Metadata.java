package daris.Monbulk.shared.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import arc.mf.client.xml.*;

public class Metadata
{
	public enum ElementTypes
	{
		// These types are unique classes.
		Document("document", true, false),
		Enumeration("enumeration", true, false),

		// These types are generic.
		String("string", true, false),
		Integer("integer", true, true),
		Date("date", true, false),
		Float("float", true, true),
		Double("double", true, true),
		Long("long", true, true),
		CiteableId("citeable-id", true, false),
		Boolean("boolean", true, false),
		Keyword("keyword", true, false),
		EmailAddress("email-address", true, false),
		
		// Special types that aren't visible or selectable byt he user.
		Number("number", false, true),
		All("all", false, false);
		
		private String m_typeName;
		private boolean m_isVisible;
		private boolean m_isNumber;

		ElementTypes(String typeName, boolean isVisible, boolean isNumber)
		{
			m_typeName = typeName;
			m_isVisible = isVisible;
			m_isNumber = isNumber;
		}
		
		public String getMetaName()
		{
			return m_typeName;
		}
		
		public boolean isVisible()
		{
			return m_isVisible;
		}
		
		public boolean isNumber()
		{
			return m_isNumber;
		}
		
		// Returns true if this type is the same as otherType, or if this type
		// is a number type and the other type is also a number type, and vice
		// versa.
		public boolean isSame(ElementTypes otherType)
		{
			return (this == otherType) || (isNumber() && otherType.isNumber());
		}
		
		static ElementTypes fromMetaName(String typeName) throws Exception
		{
			for (ElementTypes t : ElementTypes.values())
			{
				if (t.getMetaName().equalsIgnoreCase(typeName))
				{
					return t;
				}
			}
			
			throw new Exception("Unknown metadata type '" + typeName + "'");
		}
	}
	
	public static Metadata.Element createElement(XmlElement e) throws Exception
	{
		String typeName = e.value("@type");
		String name = e.value("@name");
		String description = e.value("description");
		Metadata.Element element = createElement(typeName, name, description);
		element.setXmlElement(e);
		return element;
	}
	
	public static Metadata.Element createElement(String typeName, String name, String description) throws Exception
	{
		ElementTypes type = ElementTypes.fromMetaName(typeName);

		if (type == ElementTypes.Document)
		{
			return new DocumentElement(name, description);
		}
		else if (type == ElementTypes.Enumeration)
		{
			return new EnumerationElement(name, description);
		}
		else
		{
			return new Element(type, name, description);
		}
	}
	
	public static class Element
	{
		protected String m_description = "";
		protected XmlElement m_xmlElement;
		protected Element m_parent = null;
		protected ElementTypes m_type;
		protected HashMap<String, String> m_attributes = new HashMap<String, String>();
		protected HashMap<String, String> m_restrictions = new HashMap<String, String>();

		public Element(ElementTypes type, String name, String description)
		{
			setAttribute("name", name);
			m_description = description;
			m_type = type;
		}

		public void setRestriction(String name, String value)
		{
			if (value.length() > 0)
			{
				m_restrictions.put(name, value);
			}
			else
			{
				m_restrictions.remove(name);
			}
		}
		
		public String getRestriction(String name, String defaultValue)
		{
			String value = m_restrictions.get(name);
			if (value == null)
			{
				value = defaultValue;
			}
			
			return value;
		}

		public void setAttribute(String name, String value)
		{
			if (value.length() > 0)
			{
				m_attributes.put(name, value);
			}
			else
			{
				m_attributes.remove(name);
			}
		}

		public String getAttribute(String attribute, String defaultValue)
		{
			String value = m_attributes.get(attribute);
			if (value == null)
			{
				value = defaultValue;
			}

			return value;
		}

		public void setXmlElement(XmlElement xmlElement)
		{
			m_xmlElement = xmlElement;
			List<XmlAttribute> attributes = m_xmlElement.attributes();
			for (XmlAttribute a : attributes)
			{
				m_attributes.put(a.name(), a.value());
			}
			
			for (XmlElement e : xmlElement.elements())
			{
				if (e.name().equals("restriction"))
				{
					if (m_type != ElementTypes.Enumeration)
					{
						// Enumerations handle themselves.
						for (XmlElement r : e.elements())
						{
							m_restrictions.put(r.name(), r.value());
						}
					}
				}
			}
		}

		public Element getParent()
		{
			return m_parent;
		}
		
		public void setParent(Element element)
		{
			m_parent = element;
		}
		
		public void setDescription(String description)
		{
			m_description = description;
		}
		
		public String getDescription()
		{
			return m_description;
		}

		public ElementTypes getType()
		{
			return m_type;
		}
	}
	
	// A document has a number of child elements.
	public static class DocumentElement extends Element
	{
		private ArrayList<Element> m_elements = new ArrayList<Element>();

		public DocumentElement(String name, String description)
		{
			super(ElementTypes.Document, name, description);
		}
		
		public ArrayList<Element> getElements()
		{
			return m_elements;
		}
	}
	
	public static class EnumerationElement extends Element
	{
		private ArrayList<String> m_values = new ArrayList<String>();
		private String m_dictionaryName = "";
		
		public EnumerationElement(String name, String description)
		{
			super(ElementTypes.Enumeration, name, description);
		}
		
		// Returns the values of the enum.  Note that this could
		// be empty if the values come from a dictionary.
		public ArrayList<String> getValues()
		{
			return m_values;
		}
		
		public void addValue(String value)
		{
			m_values.add(value);
		}
		
		public void removeValue(String value)
		{
			m_values.remove(value);
		}

		public void setDictionaryName(String name)
		{
			m_dictionaryName = name;
		}
		
		public String getDictionaryName()
		{
			return m_dictionaryName;
		}
		
		public boolean isUsingDictionary()
		{
			return m_dictionaryName != null && m_dictionaryName.length() > 0;
		}
		
		public void setXmlElement(XmlElement element)
		{
			super.setXmlElement(element);
			
			XmlElement restriction = element.element("restriction");
			if (restriction != null)
			{
				String dictionary = restriction.value("dictionary");
				if (dictionary != null && dictionary.length() > 0)
				{
					setDictionaryName(dictionary);
				}
				else
				{
					List<String> values = restriction.values("value");
					m_values.addAll(values);
				}
			}
		}
	}
	
	private ArrayList<Element> m_elements = new ArrayList<Element>();
	private String m_name;
	private String m_description;
	private String m_label;
	
	public Metadata(String name, String description, String label)
	{
		m_name = name;
		m_description = description;
		m_label = label;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public String getDescription()
	{
		return m_description;
	}
	
	public String getLabel()
	{
		return m_label;
	}
	
	public ArrayList<Element> getElements()
	{
		return m_elements;
	}
}
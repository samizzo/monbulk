package monbulk.shared.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import arc.mf.client.xml.*;

public class Metadata
{
	public enum ElementTypes
	{
		// These types are unique classes.
		Document("document", true, false, false),
		Enumeration("enumeration", true, false, true),

		// These types are generic.
		String("string", true, false, true),
		Integer("integer", true, true, true),
		Date("date", true, false, true),
		Float("float", true, true, true),
		Double("double", true, true, true),
		Long("long", true, true, true),
		Boolean("boolean", true, false, false),
		Keyword("keyword", true, false, false),
		
		// Special types that aren't visible or selectable by the user.
		AssetId("asset-id", false, false, false),
		Asset("asset", false, false, false),
		CiteableId("citeable-id", false, false, false),
		EmailAddress("email-address", false, false, false),

		// Special types that aren't real MF types.
		Number("number", false, true, true),
		Attribute("attribute", false, false, false),
		All("all", false, false, false);
		
		private String m_typeName;
		private boolean m_isVisible;
		private boolean m_isNumber;
		private boolean m_useInAttributes;

		ElementTypes(String typeName, boolean isVisible, boolean isNumber, boolean useInAttributes)
		{
			m_typeName = typeName;
			m_isVisible = isVisible;
			m_isNumber = isNumber;
			m_useInAttributes = useInAttributes;
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
		
		public boolean isUseInAttributes()
		{
			return m_useInAttributes;
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
		Metadata.Element element = createElement(typeName, name, description, false);
		element.setXmlElement(e);
		return element;
	}
	
	public static Metadata.Element createElement(String typeName, String name, String description, boolean isAttribute) throws Exception
	{
		ElementTypes type = ElementTypes.fromMetaName(typeName);

		if (type == ElementTypes.Document)
		{
			return new DocumentElement(name, description);
		}
		else if (type == ElementTypes.Enumeration)
		{
			return new EnumerationElement(name, description, isAttribute);
		}
		else
		{
			return new Element(type, name, description, isAttribute);
		}
	}
	
	public static class Element
	{
		protected String m_description = "";
		protected XmlElement m_xmlElement;
		protected DocumentElement m_parent = null;
		protected ElementTypes m_type;
		protected HashMap<String, String> m_settings = new HashMap<String, String>();
		protected HashMap<String, String> m_restrictions = new HashMap<String, String>();
		protected boolean m_isAttribute = false;

		// Attributes on an element are stored as a list of elements,
		// since they are almost exactly the same.
		protected ArrayList<Element> m_attributes = new ArrayList<Element>();

		public Element(ElementTypes type, String name, String description, boolean isAttribute)
		{
			setSetting("name", name);
			m_description = description;
			m_type = type;
			m_isAttribute = isAttribute;
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

		public void setSetting(String name, String value)
		{
			if (value.length() > 0)
			{
				m_settings.put(name, value);
			}
			else
			{
				m_settings.remove(name);
			}
		}

		public String getSetting(String name, String defaultValue)
		{
			String value = m_settings.get(name);
			if (value == null)
			{
				value = defaultValue;
			}

			return value;
		}

		public void setXmlElement(XmlElement xmlElement)
		{
			m_xmlElement = xmlElement;

			if (xmlElement.hasAttributes())
			{
				List<XmlAttribute> attributes = xmlElement.attributes();
				for (XmlAttribute a : attributes)
				{
					m_settings.put(a.name(), a.value());
				}
			}

			if (xmlElement.hasElements())			
			{
				for (XmlElement e : xmlElement.elements())
				{
					if (e.name().equals("restriction"))
					{
						if (m_type != ElementTypes.Enumeration)
						{
							// Enumerations handle themselves.
							if (e.hasElements())
							{
								for (XmlElement r : e.elements())
								{
									m_restrictions.put(r.name(), r.value());
								}
							}
						}
					}
					else if (canHaveAttributes() && e.name().equals("attribute"))
					{
						// Process mf attributes.
						try
						{
							Element element = createElement(e);
							element.m_isAttribute = true;
							m_attributes.add(element);
						}
						catch (Exception exception)
						{
						}
					}
				}
			}
		}

		// TODO: Should parent be the owner Element for attributes?
		public DocumentElement getParent()
		{
			return m_parent;
		}
		
		public void setParent(DocumentElement element)
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
		
		public ArrayList<Element> getAttributes()
		{
			return m_attributes;
		}
		
		public boolean canHaveAttributes()
		{
			return m_type != ElementTypes.Date && !m_isAttribute;
		}
	}
	
	// A document has a number of child elements.
	public static class DocumentElement extends Element
	{
		private ArrayList<Element> m_elements = new ArrayList<Element>();

		public DocumentElement(String name, String description)
		{
			super(ElementTypes.Document, name, description, false);
		}
		
		public ArrayList<Element> getElements()
		{
			return m_elements;
		}
		
		public boolean canHaveAttributes()
		{
			return false;
		}
	}
	
	public static class EnumerationElement extends Element
	{
		private ArrayList<String> m_values = new ArrayList<String>();
		private String m_dictionaryName = "";
		
		public EnumerationElement(String name, String description, boolean isAttribute)
		{
			super(ElementTypes.Enumeration, name, description, isAttribute);
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
		
		public boolean canHaveAttributes()
		{
			return false;
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
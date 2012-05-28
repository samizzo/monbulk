package monbulk.shared.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import monbulk.shared.Services.Element;
import monbulk.shared.util.XmlHelper;
import arc.mf.client.xml.XmlAttribute;
import arc.mf.client.xml.XmlElement;
import arc.mf.client.xml.XmlStringWriter;

/**
 * Base element class for types that don't require anything special.
 */
public class Element
{
	public enum ElementTypes
	{
		// These types are unique classes.
		Document("document", true, false, false),
		Enumeration("enumeration", true, false, true),
		Reference("document", true, false, false),

		// These types are generic.
		String("string", true, false, true),
		Date("date", true, false, true),
		IntegerOld("integer", false, true, true),
		Integer("long", true, true, true),
		FloatOld("float", false, true, true),
		Float("double", true, true, true),
		Boolean("boolean", true, false, false),
		
		// Special types that aren't visible or selectable by the user.
		Keyword("keyword", false, false, false),
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
		
		/**
		 * Returns the type name of this ElementType.  This is the name that
		 * MediaFlux uses to identify types.
		 * @return
		 */
		public String getTypeName()
		{
			return m_typeName;
		}
		
		/**
		 * Returns true if this ElementType is visible to a user.
		 * @return
		 */
		public boolean isVisible()
		{
			return m_isVisible;
		}
		
		/**
		 * Returns true if this ElementType represents a number.
		 * @return
		 */
		public boolean isNumber()
		{
			return m_isNumber;
		}
		
		/**
		 * Returns true if this ElementType can be used in attributes.
		 * @return
		 */
		public boolean isUseInAttributes()
		{
			return m_useInAttributes;
		}
		
		/**
		 * Returns true if this type is the same as otherType, or if this type
		 * is a number type and the other type is also a number type, and vice
		 * versa.
		 * @param otherType
		 * @return
		 */
		public boolean isSame(Element.ElementTypes otherType)
		{
			return (this == otherType) || (isNumber() && otherType.isNumber());
		}
		
		/**
		 * Returns an ElementType given a MediaFlux type name.
		 * @param typeName
		 * @return
		 * @throws Exception
		 */
		public static Element.ElementTypes fromTypeName(String typeName) throws Exception
		{
			for (Element.ElementTypes t : Element.ElementTypes.values())
			{
				if (t.getTypeName().equalsIgnoreCase(typeName))
				{
					return t;
				}
			}
			
			throw new Exception("Unknown metadata type '" + typeName + "'");
		}
	}
	
	/**
	 * Creates a new Metadata Element given a MediaFlux xml definition.
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public static Element createElement(XmlElement e) throws Exception
	{
		String typeName = e.value("@type");
		String name = e.value("@name");
		String description = e.value("description");
		ElementTypes type = ElementTypes.fromTypeName(typeName);

		// HACK: Integers and floats are saved as long and double MF types.
		// When we load integer/float types we convert them internally to
		// long/double.  If the user saves the metadata, they will be updated
		// on the server, otherwise they will be converted each time on load.
		if (type == ElementTypes.IntegerOld)
		{
			type = ElementTypes.Integer;
		}
		else if (type == ElementTypes.FloatOld)
		{
			type = ElementTypes.Float;
		}

		Element element = createElement(type, name, description, false);
		element.setFromXmlElement(e);
		return element;
	}
	
	/**
	 * Creates a new Metadata Element.
	 * @param typeName
	 * @param name
	 * @param description
	 * @param isAttribute
	 * @return
	 * @throws Exception
	 */
	public static Element createElement(Element.ElementTypes type, String name, String description, boolean isAttribute) throws Exception
	{
		if (type == Element.ElementTypes.Document || type == Element.ElementTypes.Reference)
		{
			return new DocumentElement(name, description, type);
		}
		else if (type == Element.ElementTypes.Enumeration)
		{
			return new EnumerationElement(name, description, isAttribute);
		}
		else
		{
			return new Element(type, name, description, isAttribute);
		}
	}
	
	protected DocumentElement m_parent = null;
	protected Element.ElementTypes m_type;
	protected boolean m_isAttribute = false;
	protected HashMap<String, String> m_settings = new HashMap<String, String>();
	protected HashMap<String, String> m_restrictions = new HashMap<String, String>();

	// Attributes on an element are stored as a list of elements,
	// since they are almost exactly the same.
	protected ArrayList<Element> m_attributes = new ArrayList<Element>();
	
	protected boolean m_modified = false;
	protected Metadata m_metadata = null;

	/**
	 * Copy constructor.  Creates a new element from an existing element.
	 * @param element
	 */
	protected Element(Element element)
	{
		m_parent = element.m_parent;
		m_type = element.m_type;

		@SuppressWarnings("unchecked")
		HashMap<String, String> settings = (HashMap<String, String>)element.m_settings.clone();
		m_settings = settings;
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> restrictions = (HashMap<String, String>)element.m_restrictions.clone();
		m_restrictions = restrictions;

		m_isAttribute = element.m_isAttribute;
		
		for (Element e : element.m_attributes)
		{
			m_attributes.add(e.clone());
		}
		
		m_metadata = element.m_metadata;
	}

	protected Element(Element.ElementTypes type, String name, String description, boolean isAttribute)
	{
		m_settings.put("name", name);
		m_settings.put("description", description);
		m_type = type;
		m_isAttribute = isAttribute;
	}
	
	public Element clone()
	{
		return new Element(this);
	}

	/**
	 * Fires the modified event.
	 */
	protected void onModified()
	{
		m_modified = true;
		if (m_metadata != null)
		{
			m_metadata.onModified();
		}
	}

	/**
	 * Returns true if this metadata has been modified.
	 * @return
	 */
	public boolean getIsModified()
	{
		boolean modified = m_modified;

		// Check all attributes on this element.
		for (Element e : m_attributes)
		{
			if (e.getIsModified())
			{
				return true;
			}
		}
		
		return modified;
	}
	
	/**
	 * Clears the modified flag to false.
	 */
	public void clearModified()
	{
		m_modified = false;
		
		// Clear modified flag for all attributes on this element.
		for (Element e : m_attributes)
		{
			e.clearModified();
		}
	}

	/**
	 * Sets the restriction 'name' to 'value'.
	 * @param name
	 * @param value
	 */
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
		
		onModified();
	}
	
	/**
	 * Returns the specified restriction or the default value
	 * if it doesn't exist.
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getRestriction(String name, String defaultValue)
	{
		String value = m_restrictions.get(name);
		if (value == null)
		{
			value = defaultValue;
		}
		
		return value;
	}

	/**
	 * Sets the setting 'name' to 'value'.  Settings end up as xml attributes
	 * so they store various attributes of an element.  Note that the
	 * description of an element is also stored as a setting, however, the
	 * description is actually not saved as an xml attribute but a separate node.
	 * @param name
	 * @param value
	 */
	public void setSetting(String name, String value)
	{
		if (value != null && value.length() > 0)
		{
			m_settings.put(name, value);
		}
		else
		{
			m_settings.remove(name);
		}
		
		onModified();
	}

	/**
	 * Returns the specified setting or the default value
	 * if it doesn't exist.
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getSetting(String name, String defaultValue)
	{
		String value = m_settings.get(name);
		if (value == null)
		{
			value = defaultValue;
		}

		return value;
	}

	public void setFromXmlElement(XmlElement xmlElement)
	{
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
					if (e.hasElements())
					{
						for (XmlElement r : e.elements())
						{
							// HACK: For enumeration types don't add any
							// value restrictions.  The EnumerationElement
							// will add them.
							if (m_type != Element.ElementTypes.Enumeration || !r.name().equals("value"))
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
	
	/**
	 * Sets the parent of this element.  Adds itself to the parent.
	 * @param parent
	 */
	public void setParent(DocumentElement parent)
	{
		parent.addChild(this);
	}
	
	/**
	 * Sets the owner metadata for this element.
	 * @param metadata
	 */
	public void setMetadata(Metadata metadata)
	{
		m_metadata = metadata;
		if (m_modified)
		{
			m_metadata.onModified();
		}
	}
	
	/**
	 * Sets the description of this element.
	 * @param description
	 */
	public void setDescription(String description)
	{
		setSetting("description", description);
	}
	
	/**
	 * Returns the description of this element or empty string if none
	 * has been set.
	 * @return
	 */
	public String getDescription()
	{
		return getSetting("description", "");
	}
	
	/**
	 * Returns the name of this element or empty string if none
	 * has been set.
	 * @return
	 */
	public String getName()
	{
		return getSetting("name", "");
	}

	/**
	 * Returns the type of this element.
	 * @return
	 */
	public Element.ElementTypes getType()
	{
		return m_type;
	}
	
	/**
	 * Returns the number of attributes on this element.
	 * @return
	 */
	public int getNumAttributes()
	{
		return m_attributes.size();
	}

	/**
	 * Returns the specified attribute on this element.
	 * @param index
	 * @return
	 */
	public Element getAttribute(int index)
	{
		return m_attributes.get(index);
	}
	
	/**
	 * Removes the specified attribute from this element.
	 * @param index
	 */
	public void removeAttribute(int index)
	{
		m_attributes.remove(index);
		onModified();
	}
	
	/**
	 * Adds a new attribute to this element.
	 * @param attribute
	 */
	public void addAttribute(Element attribute)
	{
		m_attributes.add(attribute);
		onModified();
	}
	
	/**
	 * Replaces an attribute in this element.
	 * @param oldAttribute
	 * @param newAttribute
	 */
	public void replaceAttribute(Element oldAttribute, Element newAttribute)
	{
		int index = m_attributes.indexOf(oldAttribute);
		if (index >= 0)
		{
			m_attributes.set(index, newAttribute);
			onModified();
		}
	}
	
	public boolean canHaveAttributes()
	{
		return m_type != Element.ElementTypes.Date && !m_isAttribute;
	}
	
	public boolean getIsAttribute()
	{
		return m_isAttribute;
	}
	
	public String toString()
	{
		String name = getSetting("name", "");
		String desc = getSetting("description", "");
		return m_type.toString() + ", " + name + ", " + desc;
	}

	/**
	 * Used by enum element type so it can add its own restriction elements.
	 * @param w
	 */
	protected void addRestrictionsXml(XmlStringWriter w)
	{
	}

	/**
	 * Append an xml description of this element to the XmlStringWriter.
	 * @param w
	 */
	public void addXml(XmlStringWriter w)
	{
		// Make a copy of the settings.
		@SuppressWarnings("unchecked")
		HashMap<String, String> attributes = (HashMap<String, String>)m_settings.clone();
		
		// Remove the "description" entry.  All settings except the
		// description are saved as attributes on the xml element.
		String description = attributes.remove("description");

		// Add the type.
		attributes.put("type", m_type.getTypeName());

		// Add the root element.
		String root = getIsAttribute() ? "attribute" : "element";
		w.push(root, XmlHelper.getAttributesArray(attributes));
		
		if (m_restrictions != null && m_restrictions.size() > 0)
		{
			// Add all restrictions.
			w.push("restriction", new String[] { "base", m_type.getTypeName() });
			{
				Set<Entry<String, String>> r = m_restrictions.entrySet();
				for (Entry<String, String> e : r) 
				{
					String key = e.getKey();
					String value = e.getValue();

					// Ignore the dummy value.
					if (!key.equals("dummy") && !value.equals("dummy"))
					{
						w.add(key, value);
					}
				}
				addRestrictionsXml(w);
			}
			w.pop();
		}
		
		if (description != null && description.length() > 0)
		{
			// Add the description.
			w.add("description", description);
		}
		
		if (m_attributes != null && m_attributes.size() > 0)
		{
			// Add any mf attributes.
			for (Element e : m_attributes)
			{
				e.addXml(w);
				w.pop();
			}
		}
	}
}

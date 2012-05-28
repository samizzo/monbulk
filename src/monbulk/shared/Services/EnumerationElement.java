package monbulk.shared.Services;

import java.util.ArrayList;
import java.util.List;

import arc.mf.client.xml.XmlElement;
import arc.mf.client.xml.XmlStringWriter;

/**
 * An enumeration element is an element that can be one of a set of
 * values.  The values can come from a dictionary or from a specific
 * list of values stored in the element. 
 */
public class EnumerationElement extends Element
{
	private ArrayList<String> m_values = new ArrayList<String>();
	
	/**
	 * Copy constructor.  Creates a new EnumerationElement from an existing element.
	 * @param element
	 */
	protected EnumerationElement(EnumerationElement element)
	{
		super(element);
		
		@SuppressWarnings("unchecked")
		ArrayList<String> values = (ArrayList<String>)element.m_values.clone();
		m_values = values;
	}

	protected EnumerationElement(String name, String description, boolean isAttribute)
	{
		super(Element.ElementTypes.Enumeration, name, description, isAttribute);

		// Dummy entry so there is always a restriction for enumerations.
		m_restrictions.put("dummy", "dummy");
	}

	/**
	 * Adds a new value to this enumeration.
	 * @param value
	 */
	public void addValue(String value)
	{
		m_values.add(value);
		onModified();
	}
	
	/**
	 * Removes a specific value from this enumeration.
	 * @param value
	 */
	public void removeValue(String value)
	{
		m_values.remove(value);
		onModified();
	}
	
	/**
	 * Removes all values from this enumeration.
	 */
	public void clearValues()
	{
		m_values.clear();
		onModified();
	}
	
	/**
	 * Returns the number of values in this enumeration.
	 * @return
	 */
	public int getNumValues()
	{
		return m_values.size();
	}
	
	/**
	 * Returns a specific value from this enumeration.
	 * @param index
	 * @return
	 */
	public String getValue(int index)
	{
		return m_values.get(index);
	}

	/**
	 * Sets the name of a dictionary that this enumeration should
	 * pull its values from.  Note: this doesn't actually access
	 * the dictionary; it merely stores the name.
	 */
	public void setDictionaryName(String name)
	{
		setRestriction("dictionary", name);
	}
	
	/**
	 * Returns the name of the dictionary that this enumeration
	 * references, if set, or empty string if it does not reference
	 * a dictionary.
	 * @return
	 */
	public String getDictionaryName()
	{
		return getRestriction("dictionary", "");
	}
	
	/**
	 * Returns true if this enumeration is referencing a dictionary.
	 * @return
	 */
	public boolean isUsingDictionary()
	{
		String dictionaryName = getDictionaryName();
		return dictionaryName != null && dictionaryName.length() > 0;
	}
	
	public void setFromXmlElement(XmlElement element)
	{
		super.setFromXmlElement(element);
		
		XmlElement restriction = element.element("restriction");
		if (restriction != null)
		{
			String dictionary = restriction.value("dictionary");
			if (dictionary != null && dictionary.length() > 0)
			{
				// TODO: Not needed.  This should be parsed by the parent
				// class when it parses all restrictions.
				setDictionaryName(dictionary);
			}
			else
			{
				List<String> values = restriction.values("value");
				m_values.addAll(values);
			}
		}
	}
	
	/**
	 * Returns false.  Enumerations cannot have attributes.
	 */
	public boolean canHaveAttributes()
	{
		return false;
	}
	
	public Element clone()
	{
		return new EnumerationElement(this);
	}
	
	public void addRestrictionsXml(XmlStringWriter w)
	{
		if (!isUsingDictionary() && m_values != null && m_values.size() > 0)
		{
			for (String v : m_values)
			{
				w.add("value", v);
			}
		}
	}
}

package monbulk.shared.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class XmlHelper
{
	/**
	 * Adds the specified tag and sets its value to 'value'.
	 * @param sb
	 * @param tag
	 * @param value
	 */
	public static void addTagWithValue(StringBuilder sb, String tag, String value)
	{
		sb.append("<");
		sb.append(tag);
		sb.append(">");
		sb.append(value);
		sb.append("</");
		sb.append(tag);
		sb.append(">");
	}

	/**
	 * Returns the specified attributes as an array of strings, where attribute
	 * 'i' is the name and 'i+1' is the value.
	 * @param attributes
	 * @return
	 */
	public static String[] getAttributesArray(HashMap<String, String> attributes)
	{
		String[] attr = new String[attributes.size() << 1];
		Set<Entry<String, String>> entries = attributes.entrySet();
		int i = 0;
		for (Entry<String, String> e : entries)
		{
			attr[i++] = e.getKey();
			attr[i++] = e.getValue();
		}
		return attr;
	}
}

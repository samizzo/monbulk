package monbulk.shared.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;

import monbulk.shared.util.XmlHelper;

public class Dictionary
{
	/**
	 * Represents a single entry in the dictionary.  An entry has a term
	 * and a list of definitions for the term.
	 */
	public static class Entry
	{
		private ArrayList<String> m_definitions = new ArrayList<String>();	// Definitions for this entry.
		private String m_term;

		/**
		 * Constructs a new dictionary entry for the specified term.
		 * @param term
		 */
		public Entry(String term)
		{
			m_term = term;
		}
		
		/**
		 * Returns the collection of definitions for this entry.
		 * @return
		 */
		public ArrayList<String> getDefinitions()
		{
			return m_definitions;
		}
		
		/**
		 * Returns the term.
		 * @return
		 */
		public String getTerm()
		{
			return m_term;
		}
	}
	
	private HashMap<String, Entry> m_entries = new HashMap<String, Entry>();
	private String m_name;
	
	/**
	 * Constructs a new dictionary with the specified name.
	 * @param name
	 */
	public Dictionary(String name)
	{
		m_name = name;
	}

	/**
	 * Returns the collection of entries in this dictionary.
	 * @return
	 */
	public Collection<Entry> getEntries()
	{
		return m_entries.values();
	}
	
	/**
	 * Returns the first definition for the dictionary entry
	 * term 'name', or defaultValue if it doesn't exist.
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getDefinition(String name, String defaultValue)
	{
		Entry e = m_entries.get(name);
		String value = defaultValue;
		if (e != null && e.getDefinitions().size() > 0)
		{
			value = e.getDefinitions().get(0);
		}
		
		return value;
	}

	/**
	 * Returns the name of this dictionary.
	 * @return
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * Adds the specified term to the dictionary.  The term will
	 * have no definitions.
	 * @param term
	 * @return
	 */
	public Entry addEntry(String term)
	{
		Entry entry = new Entry(term);
		m_entries.put(term, entry);
		return entry;
	}

	/**
	 * Adds the specified term to the dictionary, with the given
	 * definitions.
	 * @param term
	 * @param definitions
	 * @return
	 */
	public Entry addEntry(String term, List<String> definitions)
	{
		Entry entry = new Entry(term);
		if (definitions != null)
		{
			entry.getDefinitions().addAll(definitions);
		}
		m_entries.put(term, entry);
		return entry;
	}
	
	/**
	 * Adds the specified dictionary's entries to this dictionary.
	 * @param dictionary
	 */
	public void addDictionary(Dictionary dictionary)
	{
		for (Entry e : dictionary.getEntries())
		{
			addEntry(e.getTerm(), e.getDefinitions());
		}
	}
	
	public String getXmlForEntry(Entry entry)
	{
		StringBuilder sb = new StringBuilder();
		
		// Dictionary name.
		XmlHelper.addTagWithValue(sb, "dictionary", m_name);

		// Definitions.
		for (String d : entry.getDefinitions())
		{
			XmlHelper.addTagWithValue(sb, "definition", d);
		}
		
		// Term.
		XmlHelper.addTagWithValue(sb, "term", entry.getTerm());
		
		return sb.toString();
	}
}

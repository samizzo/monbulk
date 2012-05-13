package monbulk.shared.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;

import monbulk.shared.util.XmlHelper;

public class Dictionary
{
	public static class Entry
	{
		private ArrayList<String> m_definitions = new ArrayList<String>();	// Definitions for this entry.
		private String m_term;
		
		public Entry(String term)
		{
			m_term = term;
		}
		
		public ArrayList<String> getDefinitions()
		{
			return m_definitions;
		}
		
		public String getTerm()
		{
			return m_term;
		}
	}
	
	private HashMap<String, Entry> m_entries = new HashMap<String, Entry>();
	private String m_name;
	
	public Dictionary(String name)
	{
		m_name = name;
	}

	public Collection<Entry> getEntries()
	{
		return m_entries.values();
	}

	public String getName()
	{
		return m_name;
	}

	public Entry addEntry(String term)
	{
		Entry entry = new Entry(term);
		m_entries.put(term, entry);
		return entry;
	}
	
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
	
	// Adds the specified dictionary's entries to this dictionary.
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

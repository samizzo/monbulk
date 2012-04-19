package daris.Monbulk.shared.Services;

import java.util.ArrayList;

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
	
	private ArrayList<Entry> m_entries = new ArrayList<Entry>();
	private String m_name;
	
	public Dictionary(String name)
	{
		m_name = name;
	}

	public ArrayList<Entry> getEntries()
	{
		return m_entries;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public Entry addEntry(String term, String definition)
	{
		Entry entry = new Entry(term);
		entry.getDefinitions().add(definition);
		m_entries.add(entry);
		return entry;
	}
}

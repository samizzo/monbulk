package daris.Monbulk.Desktop.Services;

import java.util.ArrayList;

import daris.Monbulk.shared.Services.Dictionary;
import daris.Monbulk.shared.Services.DictionaryService;

public class MonbulkDictionaryService extends DictionaryService
{	
	public void dictionaryExists(String name, DictionaryExistsHandler handler)
	{
		handler.onDictionaryExists(name, true);
	}
	
	public void getDictionary(String name, GetDictionaryHandler handler)
	{
		Dictionary dictionary = new Dictionary("test");
		dictionary.addEntry("test term", "this is the definition of the test term");
		handler.onGetDictionary(dictionary);
	}
	
	public void getDictionaryList(GetDictionaryListHandler handler)
	{
		ArrayList<String> dictionaries = new ArrayList<String>();
		dictionaries.add("testdictionary");
		handler.onGetDictionaryList(dictionaries);
	}
}

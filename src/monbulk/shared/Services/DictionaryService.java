package monbulk.shared.Services;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;

import monbulk.shared.util.MonbulkEnums;
import monbulk.shared.util.MonbulkEnums.ServiceNames;

public abstract class DictionaryService implements iService
{
	// Convenience function to get a dictionary service.
	public static DictionaryService get()
	{
		try
		{
			DictionaryService service = (DictionaryService)ServiceRegistry.getService(ServiceNames.Dictionary);
			return service;
		}
		catch (ServiceRegistry.ServiceNotFoundException e) 
		{
			GWT.log(e.toString());
		}
		
		return null;
	}

	public interface GetDictionaryHandler
	{
		public void onGetDictionary(Dictionary dictionary);
	}

	public interface DictionaryExistsHandler
	{
		public void onDictionaryExists(String name, boolean exists);
	}
	
	public interface GetDictionaryListHandler
	{
		public void onGetDictionaryList(ArrayList<String> dictionaries);
	}
	
	public interface RemoveDictionaryHandler
	{
		public void onRemoveDictionary(String name);
	}
	
	public interface CreateDictionaryHandler
	{
		public void onCreateDictionary(String name);
	}
	
	public interface AddEntryHandler
	{
		public void onAddEntry(Dictionary dictionary, Dictionary.Entry entry);
	}
	
	public interface AddEntriesHandler
	{
		public void onAddEntries(Dictionary dictionary);
	}

	// Returns whether the specified dictionary exists.
	public abstract void dictionaryExists(String name, DictionaryExistsHandler handler);

	// Removes the specified dictionary.
	public abstract void removeDictionary(String name, RemoveDictionaryHandler handler); 

	// Creates a new dictionary with the specified name.
	public abstract void createDictionary(String name, CreateDictionaryHandler handler);
	
	// Adds all entries in the specified dictionary to the server.
	public abstract void addEntries(Dictionary dictionary, AddEntriesHandler handler);
	
	// Adds the specified entry to the specified dictionary.
	public abstract void addEntry(Dictionary dictionary, Dictionary.Entry entry, AddEntryHandler handler);

	// Returns the specified dictionary.
	public abstract void getDictionary(String name, GetDictionaryHandler handler);

	// Returns a list of all existing dictionaries.
	public abstract void getDictionaryList(GetDictionaryListHandler handler);
	
	public ServiceNames getServiceType()
	{
		return MonbulkEnums.ServiceNames.Dictionary;
	}
}

package daris.Monbulk.shared.Services;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;

import daris.Monbulk.shared.Architecture.IPresenter;
import daris.Monbulk.shared.Architecture.iModel;
import daris.Monbulk.shared.Form.iFormField;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.util.MonbulkEnums;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceActions;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceNames;

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

	// Returns whether the specified dictionary exists.
	public abstract void dictionaryExists(String name, DictionaryExistsHandler handler);

	// Returns the specified dictionary.
	public abstract void getDictionary(String name, GetDictionaryHandler handler);

	// Returns a list of all existing dictionaries.
	public abstract void getDictionaryList(GetDictionaryListHandler handler);

	public ServiceNames getServiceType()
	{
		return MonbulkEnums.ServiceNames.Dictionary;
	}

	// -- Deprecated ------------
	public void sendServiceRequest(ArrayList<iFormField> Parameters, ServiceActions action, iModel responseModel) { }
	public void sendServiceActionRequest(ArrayList<iFormField> Parameters, ServiceActions action, IPresenter responseModel) { }
	public void sendPojoRequest(IPojo Parameters, ServiceActions action, IPresenter responseModel) { }
	public String getServiceName()
	{
		return getServiceType().toString();
	}
}

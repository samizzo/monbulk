package monbulk.MediaFlux.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import com.google.gwt.core.client.GWT;

import arc.mf.client.Output;
import arc.mf.client.ServiceContext;
import arc.mf.client.xml.XmlElement;
import arc.mf.session.ServiceResponseHandler;
import arc.mf.session.Session;

import monbulk.shared.Services.DictionaryService;
import monbulk.shared.Services.Dictionary;

public class MediaFluxDictionaryService extends DictionaryService
{
	private class DictionaryResponseHandler implements ServiceResponseHandler
	{
		private Object m_handler = null;
		private String m_name = "";
		private Dictionary m_dictionary = null;
		private Dictionary.Entry m_entry = null;

		public DictionaryResponseHandler(Object handler)
		{
			m_handler = handler;
		}

		public DictionaryResponseHandler(String name, Object handler)
		{
			this(handler);
			m_name = name;
		}

		public void setDictionary(Dictionary dictionary)
		{
			m_dictionary = dictionary;
		}
		
		public void setEntry(Dictionary.Entry entry)
		{
			m_entry = entry;
		}

		public void processResponse(XmlElement xe, List<Output> outputs) throws Throwable
		{
			if (m_handler instanceof DictionaryExistsHandler)
			{
				DictionaryExistsHandler handler = (DictionaryExistsHandler)m_handler;
				XmlElement existsElement = xe.element("exists");
				String dictionaryName = existsElement.value("@name");
				Boolean exists = Boolean.parseBoolean(existsElement.value());
				handler.onDictionaryExists(dictionaryName, exists);
			}
			else if (m_handler instanceof GetDictionaryHandler)
			{
				GetDictionaryHandler handler = (GetDictionaryHandler)m_handler;
				Dictionary dictionary = new Dictionary(m_name);
				List<XmlElement> entries = xe.elements("entry");
				if (entries != null)
				{
					for (XmlElement e : entries)
					{
						String term = e.value("term");
						List<String> definitions = e.values("definition");
						dictionary.addEntry(term, definitions);
					}
				}

				handler.onGetDictionary(dictionary);
			}
			else if (m_handler instanceof GetDictionaryListHandler)
			{
				GetDictionaryListHandler handler = (GetDictionaryListHandler)m_handler;
				ArrayList<String> names = new ArrayList<String>();
				names.addAll(xe.values("name"));
				handler.onGetDictionaryList(names);
			}
			else if (m_handler instanceof RemoveDictionaryHandler)
			{
				RemoveDictionaryHandler handler = (RemoveDictionaryHandler)m_handler;
				handler.onRemoveDictionary(m_name);
			}
			else if (m_handler instanceof AddEntryHandler) 
			{
				AddEntryHandler handler = (AddEntryHandler)m_handler;
				handler.onAddEntry(m_dictionary, m_entry);
			}
			else if (m_handler instanceof CreateDictionaryHandler)
			{
				CreateDictionaryHandler handler = (CreateDictionaryHandler)m_handler;
				handler.onCreateDictionary(m_name);
			}
			else
			{
				GWT.log("No handler found for dictionary service, ignoring response (" +  xe.toString() + ")");
			}
		}
	}

	public void dictionaryExists(String name, DictionaryExistsHandler handler)
	{
		DictionaryResponseHandler h = new DictionaryResponseHandler(name, handler);
		Session.execute(
				new ServiceContext("MediaFluxDictionaryService.dictionaryExists"),
				"dictionary.exists",
				"<name>" + name + "</name>",
				null,
				0,
				h,
				true);
	}

	public void getDictionary(String name, GetDictionaryHandler handler)
	{
		DictionaryResponseHandler h = new DictionaryResponseHandler(name, handler);
		Session.execute(
				new ServiceContext("MediaFluxDictionaryService.getDictionary"),
				"dictionary.entries.describe",
				"<dictionary>" + name + "</dictionary>",
				null,
				0,
				h,
				true);
	}
	
	public void getDictionaryList(GetDictionaryListHandler handler)
	{
		DictionaryResponseHandler h = new DictionaryResponseHandler(handler);
		Session.execute(
				new ServiceContext("MediaFluxDictionaryService.getDictionaryList"),
				"dictionary.list",
				"",
				null,
				0,
				h,
				true); 
	}
	
	public void removeDictionary(String name, RemoveDictionaryHandler handler)
	{
		DictionaryResponseHandler h = new DictionaryResponseHandler(handler);
		Session.execute(
				new ServiceContext("MediaFluxDictionaryService.removeDictionary"),
				"dictionary.destroy",
				"<name>" + name + "</name>",
				null,
				0,
				h,
				true); 
	}
	
	public void createDictionary(String name, CreateDictionaryHandler handler)
	{
		DictionaryResponseHandler h = new DictionaryResponseHandler(handler);
		Session.execute(
				new ServiceContext("MediaFluxDictionaryService.createDictionary"),
				"dictionary.create",
				"<name>" + name + "</name>",
				null,
				0,
				h,
				true);
	}

	public void addEntry(Dictionary dictionary, Dictionary.Entry entry, AddEntryHandler handler)
	{
		DictionaryResponseHandler h = new DictionaryResponseHandler(handler);
		h.setDictionary(dictionary);
		h.setEntry(entry);

		Session.execute(
				new ServiceContext("MediaFluxDictionaryService.addEntry"),
				"dictionary.entry.add",
				dictionary.getXmlForEntry(entry),
				null,
				0,
				h,
				true);
	}
	
	private class AddEntryHandlerImp implements AddEntryHandler
	{
		private int m_expectedCount = 0;
		private AddEntriesHandler m_handler;
		
		public AddEntryHandlerImp(AddEntriesHandler handler, int numToAdd)
		{
			m_handler = handler;
			m_expectedCount = numToAdd;
		}

		public void onAddEntry(Dictionary dictionary, Dictionary.Entry entry)
		{
			// NOTE: It may seem like there are concurrency issues here
			// because this callback will be called for multiple requests
			// to the server, and the requests may come back at the
			// same time, but JavaScript is single-threaded and the callbacks
			// are processed by the interpreter serially, so we don't need
			// a mutex here.
			m_expectedCount--;
			if (m_expectedCount == 0)
			{
				m_handler.onAddEntries(dictionary);
			}
		}
	}

	public void addEntries(Dictionary dictionary, final AddEntriesHandler handler)
	{
		// TODO: Not sure if firing off a series of requests like this
		// is safe.  Might have to wait for each result back first.
		Collection<Dictionary.Entry> entries = dictionary.getEntries();
		AddEntryHandlerImp h = new AddEntryHandlerImp(handler, entries.size());
		for (Dictionary.Entry e : entries)
		{
			addEntry(dictionary, e, h);
		}
	} 
}

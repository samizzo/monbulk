package daris.Monbulk.MediaFlux.Services;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

import arc.mf.client.Output;
import arc.mf.client.ServiceContext;
import arc.mf.client.ServiceRequest;
import arc.mf.client.xml.XmlElement;
import arc.mf.session.ServiceResponseHandler;
import arc.mf.session.Session;

import daris.Monbulk.shared.Services.DictionaryService;
import daris.Monbulk.shared.Services.Dictionary;

public class MediaFluxDictionaryService extends DictionaryService
{
	private class DictionaryResponseHandler implements ServiceResponseHandler
	{
		private Object m_handler = null;
		private String m_name = "";

		public DictionaryResponseHandler(Object handler)
		{
			m_handler = handler;
		}

		public DictionaryResponseHandler(String name, Object handler)
		{
			this(handler);
			m_name = name;
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
						String definition = e.value("definition");
						dictionary.addEntry(term, definition);
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
			else
			{
				GWT.log("No handler found for dictionary service, ignoring response");
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
}

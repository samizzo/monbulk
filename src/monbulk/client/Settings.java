package monbulk.client;

import monbulk.shared.Services.DictionaryService;
import monbulk.shared.Services.Dictionary;

public class Settings
{
	public interface ReadSettingsHandler
	{
		public void onReadSettings();
	}

	private Dictionary m_dictionary;
	private ReadSettingsHandler m_handler; 

	public Settings(ReadSettingsHandler handler)
	{
		m_handler = handler;
		m_dictionary = new Dictionary("monbulk.settings");

		final DictionaryService service = DictionaryService.get();
		service.dictionaryExists("monbulk.settings", new DictionaryService.DictionaryExistsHandler()
		{
			public void onDictionaryExists(String name, boolean exists)
			{
				if (exists)
				{
					service.getDictionary(name, new DictionaryService.GetDictionaryHandler()
					{
						public void onGetDictionary(Dictionary dictionary)
						{
							m_dictionary = dictionary;
							if (m_handler != null)
							{
								m_handler.onReadSettings();
							}
						}
					});
				}
				else
				{
					if (m_handler != null)
					{
						m_handler.onReadSettings();
					}
				}
			}
		});
	}
	
	public String get(String name, String defaultValue)
	{
		return m_dictionary.getDefinition(name, defaultValue);
	}

	/**
	 * Convenience function to return the default namespace.
	 * @return
	 */
	public String getDefaultNamespace()
	{
		String namespace = get("default_namespace", "monbulk");
		return namespace;
	}
}

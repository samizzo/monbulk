package monbulk.MetadataEditor;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

import monbulk.shared.Services.*;

public class EnumerationElementPanel extends ElementPanel implements ValueChangeHandler<Boolean>
{
	private static EnumerationElementPanelUiBinder uiBinder = GWT.create(EnumerationElementPanelUiBinder.class);

	interface EnumerationElementPanelUiBinder extends UiBinder<Widget, EnumerationElementPanel> { }

	@UiField ListBox m_values;
	@UiField Button m_add;
	@UiField Button m_remove;
	@UiField Button m_saveAsDictionary;
	@UiField ListBox m_dictionaryCombo;
	@UiField CheckBox m_fromDictionary;
	@UiField HTMLPanel m_enumButtons;
	
	boolean m_dictionaryComboPopulated = false;
	boolean m_pendingSetDictionary = false;
	
	public EnumerationElementPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));

		m_fromDictionary.setValue(true);
		setButtonState();

		m_fromDictionary.addValueChangeHandler(this);
		refreshDictionaryCombo();
	}
	
	private void refreshDictionaryCombo()
	{
		// Populate the dictionary name listbox.
		m_dictionaryCombo.clear();
		DictionaryService service = DictionaryService.get();
		if (service != null)
		{
			m_dictionaryComboPopulated = false;
			service.getDictionaryList(new DictionaryService.GetDictionaryListHandler()
			{
				public void onGetDictionaryList(ArrayList<String> dictionaries)
				{
					m_dictionaryComboPopulated = true;
					for (String d : dictionaries)
					{
						m_dictionaryCombo.addItem(d);
					}
					
					if (m_pendingSetDictionary)
					{
						setDictionary();
					}
				}
			});
		}
	}

	// Update the specified metadata element.
	public void update(Metadata.Element element)
	{
		if (element instanceof Metadata.EnumerationElement)
		{
			Metadata.EnumerationElement e = (Metadata.EnumerationElement)element;

			if (m_fromDictionary.getValue())
			{
				// Set the dictionary name.
				int index = m_dictionaryCombo.getSelectedIndex();
				String name = m_dictionaryCombo.getItemText(index);
				e.setDictionaryName(name);
			}
			else
			{
				// Clear the dictionary name and sync the enum values.
				e.setDictionaryName("");
				ArrayList<String> values = e.getValues();
				values.clear();
				for (int i = 0; i < m_values.getItemCount(); i++)
				{
					String value = m_values.getItemText(i);
					values.add(value);
				}
			}
		}
	}

	public void set(Metadata.Element element)
	{
		super.set(element);

		m_values.clear();
		m_remove.setEnabled(false);

		Metadata.EnumerationElement e = (Metadata.EnumerationElement)element;
		if (e.isUsingDictionary())
		{
			// Make sure the dictionary item is selected.
			setDictionary();
		}
		else
		{
			// Entries have been explicitly specified, so add them.
			m_fromDictionary.setValue(false, false);
			populateValues(e.getValues());
			setButtonState();
		}
	}
	
	public void setReadOnly(boolean readOnly)
	{
		super.setReadOnly(readOnly);
		
		if (readOnly)
		{
			// FIXME: Currently this only works if you set this panel to
			// read-only.  If you try to set it back to not read-only the enum
			// buttons panel won't be re-added.  We don't need this
			// functionality at the moment so I haven't implemented it.
			m_enumButtons.removeFromParent();
		}

		m_fromDictionary.setEnabled(!readOnly);
	}
	
	private void populateValues(ArrayList<String> values)
	{
		m_values.clear();
		m_remove.setEnabled(false);
		for (String v : values)
		{
			m_values.addItem(v);
		}
	}

	@UiHandler("m_add")
	void onAddClicked(ClickEvent event)
	{
		// User has added an enum value.
		String newValue = Window.prompt("Enter new value:", "");
		if (newValue != null && newValue.length() > 0)
		{
			m_values.addItem(newValue);
			setButtonState();
		}
	}

	@UiHandler("m_remove")
	void onRemoveClicked(ClickEvent event)
	{
		// User has removed an enum value.
		int selected = m_values.getSelectedIndex();
		if (selected >= 0)
		{
			m_values.removeItem(selected);

			// If there are still items in the list, select the next one,
			// otherwise disable the remove button.
			selected = selected < m_values.getItemCount() ? selected : m_values.getItemCount() - 1;
			if (selected >= 0)
			{
				m_values.setSelectedIndex(selected);
			}
			
			setButtonState();
		}
	}
	
	@UiHandler("m_saveAsDictionary")
	void onSaveAsDictionaryClicked(ClickEvent event)
	{
		// Save the current enum set to a new dictionary.
		final String dictName = Window.prompt("Enter a name for the dictionary:", "");
		if (dictName != null && dictName.length() > 0)
		{
			// Create a new dictionary object.
			final Dictionary d = new Dictionary(dictName);
			int count = m_values.getItemCount();
			for (int i = 0; i < count; i++)
			{
				String value = m_values.getItemText(i);
				d.addEntry(value);
			}

			// Check if the dictionary already exists.
			final DictionaryService service = DictionaryService.get();
			service.dictionaryExists(dictName, new DictionaryService.DictionaryExistsHandler()
			{
				public void onDictionaryExists(String name, boolean exists)
				{
					if (exists)
					{
						// Exists, so prompt the user to either overwrite or append.
						if (Window.confirm("A dictionary called '" + name + "' already exists.  Would you like to add these entries to it?"))
						{
							// Add all entries from d to the existing dictionary on the server.
							addDictionaryEntries(d);
						}
						else if (Window.confirm("Would you like to replace its entries with these entries?"))
						{
							// Remove existing dictionary first.
							service.removeDictionary(name, new DictionaryService.RemoveDictionaryHandler()
							{
								public void onRemoveDictionary(String name)
								{
									// Dictionary was removed, so create a new one from d.
									createDictionary(d);
								}
							});
						}
					}
					else
					{
						// Doesn't exist, so create a new dictionary from d.
						createDictionary(d);
					}
				}
			});
		}
	}
	
	private void addDictionaryEntries(final Dictionary dictionary)
	{
		final DictionaryService service = DictionaryService.get();
		service.addEntries(dictionary, new DictionaryService.AddEntriesHandler()
		{
			public void onAddEntries(Dictionary dictionary)
			{
				// Refresh combo.
				refreshDictionaryCombo();
				m_fromDictionary.setValue(true);
				Metadata.EnumerationElement element = (Metadata.EnumerationElement)m_element;
				element.setDictionaryName(dictionary.getName());
				setDictionary();
			}
		});
	}
	
	private void createDictionary(final Dictionary newDictionary)
	{
		final DictionaryService service = DictionaryService.get();
		service.createDictionary(newDictionary.getName(), new DictionaryService.CreateDictionaryHandler()
		{
			public void onCreateDictionary(String name)
			{
				// Now add all entries.
				addDictionaryEntries(newDictionary);
			}
		}
		);
	}
	
	@UiHandler("m_dictionaryCombo")
	void onDictionaryItemSelected(ChangeEvent event)
	{
		// User has selected a dictionary from the combo box.
		int index = m_dictionaryCombo.getSelectedIndex();
		if (index >= 0)
		{
			String name = m_dictionaryCombo.getItemText(index);
			populateValuesFromDictionary(name);
		}
	}
	
	@UiHandler("m_values")
	void onValueSelected(ChangeEvent event)
	{
		// User selected a value.
		m_remove.setEnabled(true);
	}
	
	private void setDictionary()
	{
		int count = m_dictionaryCombo.getItemCount();
		if (count == 0)
		{
			// Can't set the dictionary yet because the combo
			// is still being initialised.
			m_pendingSetDictionary = true;
		}
		else
		{
			m_pendingSetDictionary = false;
			Metadata.EnumerationElement e = (Metadata.EnumerationElement)m_element;
			String dictionaryName = e.getDictionaryName();
			for (int i = 0; i < count; i++)
			{
				String item = m_dictionaryCombo.getItemText(i);
				if (item.equals(dictionaryName))
				{
					// Set checkbox.
					m_fromDictionary.setValue(true, false);
					setButtonState();
					m_dictionaryCombo.setSelectedIndex(i);
					populateValuesFromDictionary(dictionaryName);
					break;
				}
			}
		}
	}
	
	// Populate the enum listbox from the specified dictionary.
	private void populateValuesFromDictionary(String dictionaryName)
	{
		final DictionaryService service = DictionaryService.get();
		service.dictionaryExists(dictionaryName, new DictionaryService.DictionaryExistsHandler()
		{
			public void onDictionaryExists(String name, boolean exists)
			{
				if (exists)
				{
					// Dictionary exists, so get it and populate the list.
					service.getDictionary(name, new DictionaryService.GetDictionaryHandler()
					{
						public void onGetDictionary(Dictionary dictionary)
						{
							m_values.clear();
							for (Dictionary.Entry e : dictionary.getEntries())
							{
								m_values.addItem(e.getTerm());
							}
						}
					});
				}
				else
				{
					// Doesn't exist so set the checkbox to false.
					m_fromDictionary.setValue(false, false);
					Metadata.EnumerationElement e = (Metadata.EnumerationElement)m_element;
					populateValues(e.getValues());
					setButtonState();
				}
			}
		});
	}

	// Dictionary checkbox value changed handler.	
	public void onValueChange(ValueChangeEvent<Boolean> event)
	{
		m_values.clear();

		// Enable the dictionary if we are using it.
		m_dictionaryCombo.setEnabled(event.getValue() && !m_readOnly);
		
		if (event.getValue())
		{
			// User has checked "From dictionary", so populate
			// the listbox with the dictionary entries.
			String name = m_dictionaryCombo.getItemText(m_dictionaryCombo.getSelectedIndex());
			populateValuesFromDictionary(name);
		}
		else
		{
			// User has unchecked the dictionary box, so restore
			// the enum values from the element itself.
			Metadata.EnumerationElement e = (Metadata.EnumerationElement)m_element;
			populateValues(e.getValues());
		}

		// Disable add/remove if we are using a dictionary as the
		// source of the enum values.
		setButtonState();
	}
	
	private void setButtonState()
	{
		boolean useDictionary = m_fromDictionary.getValue();
		m_dictionaryCombo.setEnabled(useDictionary && !m_readOnly);
		m_add.setVisible(!useDictionary);
		m_remove.setVisible(!useDictionary);
		m_saveAsDictionary.setVisible(!useDictionary);

		if (!useDictionary)
		{
			boolean hasItems = m_values.getItemCount() > 0;
			m_saveAsDictionary.setEnabled(hasItems);
			m_remove.setEnabled(hasItems && m_values.getSelectedIndex() >= 0);
		}
	}
	
	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.Enumeration;
	}
}

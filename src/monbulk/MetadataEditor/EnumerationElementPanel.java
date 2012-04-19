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
	@UiField ListBox m_dictionaryCombo;
	@UiField CheckBox m_fromDictionary;
	
	boolean m_dictionaryComboPopulated = false;
	boolean m_pendingSetDictionary = false;
	
	public EnumerationElementPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));

		m_fromDictionary.setValue(true);
		setButtonState();

		m_fromDictionary.addValueChangeHandler(this);
	
		// Populate the dictionary name listbox.
		DictionaryService service = DictionaryService.get();
		if (service != null)
		{
			service.getDictionaryList(new DictionaryService.GetDictionaryListHandler()
			{
				public void onGetDictionaryList(ArrayList<String> dictionaries)
				{
					m_dictionaryComboPopulated = true;
					m_dictionaryCombo.clear();
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
			setButtonState();
			populateValues(e.getValues());
		}
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
			else
			{
				m_remove.setEnabled(false);
			}
		}
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
					setButtonState();
				}
			}
		});
	}

	// Dictionary checkbox value changed handler.	
	public void onValueChange(ValueChangeEvent<Boolean> event)
	{
		// Disable add/remove if we are using a dictionary as the
		// source of the enum values.
		setButtonState();
		
		// Enable the dictionary if we are using it.
		m_dictionaryCombo.setEnabled(event.getValue());
		
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
	}
	
	private void setButtonState()
	{
		boolean useDictionary = m_fromDictionary.getValue();
		m_dictionaryCombo.setEnabled(useDictionary);
		m_add.setVisible(!useDictionary);
		m_remove.setVisible(!useDictionary);
	}
	
	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.Enumeration;
	}
}

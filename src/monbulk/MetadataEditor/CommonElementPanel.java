package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.event.dom.client.ChangeEvent;

import monbulk.shared.Services.*;

public class CommonElementPanel extends ElementPanel
{
	public interface ChangeTypeHandler
	{
		public void onChangeType(Metadata.Element element, String newType);
	}

	private static ElementPanelUiBinder uiBinder = GWT.create(ElementPanelUiBinder.class);

	@UiField TextBox m_name;
	@UiField ListBox m_type;
	@UiField TextBox m_description;
	@UiField TextBox m_minOccurs;
	@UiField TextBox m_maxOccurs;
	
	ChangeTypeHandler m_changeTypeHandler = null;

	interface ElementPanelUiBinder extends UiBinder<Widget, CommonElementPanel> { }

	public CommonElementPanel()
	{
		Widget w = uiBinder.createAndBindUi(this);
		initWidget(w);
		
		setIsForAttributes(false);
	}
	
	// If attributes is true, this panel is used to display
	// properties of attributes, so don't allow selection of
	// certain metadata element types.
	public void setIsForAttributes(boolean attributes)
	{
		m_type.clear();
		for (Metadata.ElementTypes e : Metadata.ElementTypes.values())
		{
			if (e.isVisible() && (!attributes || e.isUseInAttributes()))
			{
				m_type.addItem(e.toString());
			}
		}
	}
	
	public void setChangeTypeHandler(ChangeTypeHandler handler)
	{
		m_changeTypeHandler = handler;
	}
	
	public void update(Metadata.Element element)
	{
		element.setSetting("name", m_name.getText());
		element.setDescription(m_description.getText());
		
		String minOccurs = m_minOccurs.getValue();
		element.setSetting("min-occurs", minOccurs);
		String maxOccurs = m_maxOccurs.getValue();
		element.setSetting("max-occurs", maxOccurs);
	}

	public void set(Metadata.Element element)
	{
		super.set(element);

		// Set name and type for the element from the values we are editing.
		m_name.setText(element.getSetting("name", ""));
		m_description.setText(element.getDescription());

		String type = element.getType().toString();
		
		for (int i = 0; i < m_type.getItemCount(); i++)
		{
			if (m_type.getItemText(i).equals(type))
			{
				m_type.setSelectedIndex(i);
				break;
			}
		}
		
		String minOccurs = element.getSetting("min-occurs", "1");
		String maxOccurs = element.getSetting("max-occurs", "");
		m_minOccurs.setValue(minOccurs);
		m_maxOccurs.setValue(maxOccurs);
	}
	
	public void setReadOnly(boolean readOnly)
	{
		super.setReadOnly(readOnly);
		m_name.setEnabled(!readOnly);
		m_description.setEnabled(!readOnly);
		m_minOccurs.setEnabled(!readOnly);
		m_maxOccurs.setEnabled(!readOnly);
		m_type.setEnabled(!readOnly);
	}
	
	@UiHandler("m_type")
	void onTypeSelected(ChangeEvent event)
	{
		if (m_changeTypeHandler != null)
		{
			int index = m_type.getSelectedIndex();
			String newType = m_type.getItemText(index);
			m_changeTypeHandler.onChangeType(m_element, newType);
		}
	}
	
	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.All;
	}
}

package monbulk.MetadataEditor;

import monbulk.shared.Services.Metadata;
import monbulk.shared.Services.Metadata.Element;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.event.dom.client.ChangeEvent;

public class AttributesPanel extends ElementPanel
{
	private static AttributesPanelUiBinder uiBinder = GWT.create(AttributesPanelUiBinder.class);
	interface AttributesPanelUiBinder extends UiBinder<Widget, AttributesPanel> { }

	@UiField ListBox m_attributes;
	@UiField Button m_add;
	@UiField Button m_remove;
	@UiField Button m_edit;

	public AttributesPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void set(Metadata.Element element)
	{
		super.set(element);
		m_attributes.clear();
		
		for (Metadata.Element e : element.getAttributes())
		{
			String name = e.getSetting("name", "");
			if (name.length() > 0)
			{
				m_attributes.addItem(name);
			}
		}
		
		setButtonState(false);
	}

	public void update(Metadata.Element element)
	{
	}

	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.Attribute;
	}
	
	@UiHandler("m_attributes")
	void onAttributeSelected(ChangeEvent event)
	{
		setButtonState(true);
	}
	
	private void setButtonState(boolean enabled)
	{
		m_edit.setEnabled(enabled);
		m_remove.setEnabled(enabled);
	}
}

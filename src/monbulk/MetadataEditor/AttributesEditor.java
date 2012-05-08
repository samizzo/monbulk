package monbulk.MetadataEditor;

import monbulk.shared.widgets.Window.*;
import monbulk.shared.Services.Metadata;

public class AttributesEditor extends OkCancelWindow
{
	private ElementProperties m_elementProperties;

	public AttributesEditor()
	{
		super("AttributesEditor", "Attributes");
		m_windowSettings.width = -1;
		m_windowSettings.height = -1;

		m_elementProperties = new ElementProperties();
		m_elementProperties.setWidth("400px");
		m_elementProperties.setIsForAttributes(true);
		m_contentPanel.add(m_elementProperties);
	}
	
	public void setChangeTypeHandler(CommonElementPanel.ChangeTypeHandler handler)
	{
		m_elementProperties.setChangeTypeHandler(handler);
	}
	
	public void onHide()
	{
		m_elementProperties.setElement(null, false);
	}

	public void setElement(Metadata.Element element)
	{
		m_elementProperties.setElement(element, false);
	}

	protected void onOk()
	{
		m_elementProperties.updateCurrentElement();
	}
}

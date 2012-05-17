package monbulk.MetadataEditor;

import monbulk.shared.widgets.Window.*;
import monbulk.shared.Services.Metadata;

public class ElementEditor extends OkCancelWindow
{
	private ElementProperties m_elementProperties;

	public ElementEditor(boolean canHaveAttributes) throws Exception
	{
		super("ElementEditor", "Element Editor");
		m_windowSettings.width = -1;
		m_windowSettings.height = -1;

		m_elementProperties = new ElementProperties(canHaveAttributes);
		m_elementProperties.setWidth("500px");
		m_contentPanel.add(m_elementProperties);
	}
	
	public void updateCurrentElement()
	{
		m_elementProperties.updateCurrentElement();
	}
	
	public void setReadOnly(boolean readOnly)
	{
		m_elementProperties.setReadOnly(readOnly);
	}

	public void setChangeTypeHandler(CommonElementPanel.ChangeTypeHandler handler)
	{
		m_elementProperties.setChangeTypeHandler(handler);
	}

	public void onHide()
	{
		m_elementProperties.setMetadataElement(null, false);
	}

	public void setMetadataElement(Metadata.Element element)
	{
		m_elementProperties.setMetadataElement(element, false);
	}
	
	public Metadata.Element getMetadataElement()
	{
		return m_elementProperties.getMetadataElement();
	}

	protected void onOk()
	{
		m_elementProperties.updateCurrentElement();
	}
}

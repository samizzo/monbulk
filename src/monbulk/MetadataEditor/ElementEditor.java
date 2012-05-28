package monbulk.MetadataEditor;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

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
		m_elementProperties.addNameKeyUpHandler(new KeyUpHandler()
		{
			public void onKeyUp(KeyUpEvent event)
			{
				setButtonState();
			}
		});
		m_contentPanel.add(m_elementProperties);
		
		setButtonState();
	}
	
	private void setButtonState()
	{
		String name = m_elementProperties.getName();
		m_ok.setEnabled(name != null && name.length() > 0);
	}
	
	public void updateCurrentElement()
	{
		m_elementProperties.updateCurrentElement();
	}
	
	public String getName()
	{
		return m_elementProperties.getName();
	}
	
	public void setReadOnly(boolean readOnly)
	{
		m_elementProperties.setReadOnly(readOnly);
	}
	
	public void setNameFocus()
	{
		m_elementProperties.setNameFocus();
	}
	
	public void setMinOccursFocus()
	{
		m_elementProperties.setMinOccursFocus();
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
		setButtonState();
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

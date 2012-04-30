package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import monbulk.shared.Services.*;

public class StringElementPanel extends ElementPanel
{
	private static StringElementPanelUiBinder uiBinder = GWT.create(StringElementPanelUiBinder.class);
	interface StringElementPanelUiBinder extends UiBinder<Widget, StringElementPanel> {	}

	@UiField TextBox m_maxLength;

	public StringElementPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void update(Metadata.Element element)
	{
		String maxLength = m_maxLength.getText();
		element.setRestriction("max-length", maxLength);
	}
	
	public void set(Metadata.Element element)
	{
		String maxLength = element.getRestriction("max-length", "");
		m_maxLength.setText(maxLength);
	}
	
	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.String;
	}
	
	public void setReadOnly(boolean readOnly)
	{
		super.setReadOnly(readOnly);
		m_maxLength.setEnabled(!readOnly);
	}
}

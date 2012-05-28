package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.CheckBox;

import monbulk.shared.Services.Element;

public class DateElementPanel extends ElementPanel
{
	private static DateElementPanelUiBinder uiBinder = GWT.create(DateElementPanelUiBinder.class);
	interface DateElementPanelUiBinder extends UiBinder<Widget, DateElementPanel> { }

	@UiField CheckBox m_includeTime;

	public DateElementPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void update(Element element)
	{
		element.setRestriction("time", m_includeTime.getValue().toString());
	}
	
	public void set(Element element)
	{
		String time = element.getRestriction("time", "true");
		m_includeTime.setValue(Boolean.valueOf(time));
	}
	
	public Element.ElementTypes getType()
	{
		return Element.ElementTypes.Date;
	}
	
	public void setReadOnly(boolean readOnly)
	{
		super.setReadOnly(readOnly);
		m_includeTime.setEnabled(!readOnly);
	}
}

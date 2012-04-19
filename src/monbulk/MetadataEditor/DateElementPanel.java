package daris.Monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.CheckBox;

import daris.Monbulk.shared.Services.Metadata;

public class DateElementPanel extends ElementPanel
{
	private static DateElementPanelUiBinder uiBinder = GWT.create(DateElementPanelUiBinder.class);
	interface DateElementPanelUiBinder extends UiBinder<Widget, DateElementPanel> { }

	@UiField CheckBox m_includeTime;

	public DateElementPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void update(Metadata.Element element)
	{
		element.setRestriction("time", m_includeTime.getValue().toString());
	}
	
	public void set(Metadata.Element element)
	{
		String time = element.getRestriction("time", "true");
		m_includeTime.setValue(Boolean.valueOf(time));
	}
	
	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.Date;
	}
}

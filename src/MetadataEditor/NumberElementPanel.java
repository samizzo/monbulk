package daris.Monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import daris.Monbulk.shared.Services.*;
import daris.Monbulk.shared.widgets.TextBoxEx;

public class NumberElementPanel extends ElementPanel
{
	private static FloatDoubleElementPanelUiBinder uiBinder = GWT.create(FloatDoubleElementPanelUiBinder.class);
	interface FloatDoubleElementPanelUiBinder extends UiBinder<Widget, NumberElementPanel> { }

	@UiField TextBoxEx m_minimum;
	@UiField TextBoxEx m_maximum;

	public NumberElementPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void update(Metadata.Element element)
	{
		String min = m_minimum.getText();
		element.setRestriction("minimum", min);
		String max = m_maximum.getText();
		element.setRestriction("maximum", max);
	}
	
	public void set(Metadata.Element element)
	{
		Metadata.ElementTypes t = element.getType();
		boolean floatType = (t == Metadata.ElementTypes.Double || t == Metadata.ElementTypes.Float);
		m_minimum.setAllowDecimalPoint(floatType);
		m_maximum.setAllowDecimalPoint(floatType);

		String min = element.getRestriction("minimum", "");
		m_minimum.setText(min);
		String max = element.getRestriction("maximum", "");
		m_maximum.setText(max);
	}
	
	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.Number;
	}
}

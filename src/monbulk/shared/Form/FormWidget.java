package monbulk.shared.Form;

import java.util.HashMap;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FormWidget extends HorizontalPanel{

	//FieldName
	private String WidgetName;
	
	
	private HashMap<String, Widget> WidgetList;
	
	public final String VALID_WIDGET = "validWidget";
	public final String FORM_WIDGET = "formWidget";
	public final String LABEL_WIDGET = "labelWidget";
	public FormWidget(String WidgetName)
	{
		super();
		WidgetList = new HashMap<String,Widget>();
		this.WidgetName = WidgetName;
	}
	public void setFormWidget(Widget w)
	{
		WidgetList.put(FORM_WIDGET, w);
		
		w.setStyleName(FORM_WIDGET);
		
		this.add(WidgetList.get(FORM_WIDGET));
		
	}
	public void setValidWidget(Widget w)
	{
		WidgetList.put(VALID_WIDGET, w);
		w.setStyleName(VALID_WIDGET);
		this.add(WidgetList.get(VALID_WIDGET));
	}
	public void setLabelWidget(Widget w)
	{
		WidgetList.put(LABEL_WIDGET, w);
		w.setStyleName(LABEL_WIDGET);
		this.add(WidgetList.get(LABEL_WIDGET));
	}
	public void enable()
	{
		this.setVisible(true);
	}
	public Object getValue()
	{
		HasValue<Object> item = (HasValue<Object>)WidgetList.get(FORM_WIDGET);
		return item.getValue();
	}
	public void clear()
	{
		HasValue<Object> item = (HasValue<Object>)WidgetList.get(FORM_WIDGET);
		item.setValue(null);
		
	}
	public void disable()
	{
		this.setVisible(false);
	}
	public String getWidgetName()
	{
		return this.WidgetName;
	}
	public Widget getFormWidget()
	{
		
		return this.WidgetList.get(FORM_WIDGET);
	}	
	public void setFormValue(Object Value)
	{
		HasValue<Object> item = (HasValue<Object>)WidgetList.get(FORM_WIDGET);
		item.setValue(Value);
	}
}

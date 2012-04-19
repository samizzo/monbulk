package daris.Monbulk.shared.Form;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Widget;

public interface iFormField {

	public String GetFieldName();
	public Object GetFieldValue();
	public String getFieldTypeName();
	public String GetWidgetName();
	public void SetFieldValue(String FieldValue);
	public iFormFieldValidation GetFieldValidation();
	public Boolean hasValue();
	public void setAsSummaryField();
	public void setAsTitleField();
	public Boolean isStatic();
	public Boolean isSummary();
	public Boolean isTitle();
	public void setFieldName(String newName);
	
	public FormWidget getWidgetReference();
	public String Validate(String FieldValue);
	//public <T> void registerHandler(EventHandler handler, Type<T> type);
	public <H extends EventHandler> HandlerRegistration addHandler(final H handler, GwtEvent.Type<H> type);
	public interface iFormListField extends iFormField
	{
		public void addListItem(String newItem);
		public void removeListItem(String newItem);
	}
	public interface iFormFieldValidation
	{
		public boolean isValueValid(String value);
		public String getInvalidReason();
	}
}

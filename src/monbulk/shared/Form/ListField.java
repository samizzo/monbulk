package monbulk.shared.Form;

import java.util.ArrayList;
import java.util.Iterator;

import monbulk.shared.Form.iFormField.iFormFieldValidation;
import monbulk.shared.Form.iFormField.iFormListField;

public class ListField extends FormField implements iFormListField  {

	private ArrayList<String> Values;
	private String FieldName;
	private String FieldType;
	private Object FieldValue;
	private String FieldWidget;
	private iFormFieldValidation fieldValidator;
	private Boolean isSummaryField;
	private Boolean isTitleField;
	private Boolean isStatic;
	private Boolean hasValue;
	
	public ListField(String FormName, Object Value)
	{
		super(FormName, "List");
		this.FieldName = FormName;
		this.FieldType = "List";
		this.FieldValue = Value;
		this.FieldWidget="Label";
		this.fieldValidator = null;
		isSummaryField = false;
		isTitleField = false;
		isStatic = true;
		hasValue = true;
	}
	
	@Override
	public String GetFieldName() {
		
		return FieldName;
	}

	@Override
	public Object GetFieldValue() {
		
		return FieldValue;
	}

	@Override
	public String getFieldTypeName() {
		
		return FieldType;
	}

	@Override
	public String GetWidgetName() {
		
		return FieldWidget;
	}

	@Override
	public void SetFieldValue(String FieldValue) {
		
		
		if(Values.contains(FieldValue))
		{
			this.FieldValue = FieldValue;
		}
		
	}

	@Override
	public iFormFieldValidation GetFieldValidation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListItem(String newItem) {
		this.Values.add(newItem);
		
	}

	@Override
	public void removeListItem(String newItem) {
		this.Values.remove(newItem);
		
	}

}

package monbulk.shared.Form;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.Form.iFormField.iFormFieldValidation;
import monbulk.shared.Form.iFormField.iFormListField;

public class ListField extends FormField implements iFormListField  {

	protected ArrayList<String> Values;
	/*
	private String FieldName;
	private String FieldType;
	private Object FieldValue;
	private String FieldWidget;
	private Widget _FieldVaLueWidget;
	private Widget _ValidationWidget;
	private iFormFieldValidation fieldValidator;
	private Boolean isSummaryField;
	private Boolean isTitleField;
	private Boolean isStatic;
	private Boolean hasValue;
	private FormWidget returnWidget;
*/
	
	public ListField(String FormName)
	{
		super(FormName, "List",false);
		this.FieldName = FormName;
		this.FieldType = "List";
		this.fieldValidator = new StringValidation(this.FieldName);
		isStatic = true;
		hasValue = true;
		
		
		BuildWidget();
		
	}
	public ListField(String FormName, String DefaultValue)
	{
		super(FormName, "List",false);
		
		this.fieldValidator = new StringValidation(this.FieldName);
		this.FieldName = FormName;
		this.FieldType = "List";
		this.FieldWidget= "ListBox";

		isStatic = false;
		hasValue = false;
		
		BuildWidget();
	}
	private void BuildWidget()
	{
		
		
		isSummaryField = false;
		isTitleField = false;
		
		Label FieldNameLabel = new Label();
		FieldNameLabel.setText(this.FieldName);
		this._ValidationWidget = new Label();
		this.returnWidget = new FormWidget(this.FieldName);
		this._FieldVaLueWidget = new ListBox();
		this.returnWidget.setLabelWidget(FieldNameLabel);
		this.returnWidget.setFormWidget(_FieldVaLueWidget);
		this.returnWidget.setValidWidget(_ValidationWidget);
	}
	public void loadList(ArrayList<String> inList, String DefaultValue)
	{
		this.Values = inList;
		ListBox fields = new ListBox();
		Iterator<String> i = inList.iterator();
		int SelectedIndex = 0;
		int index = 0;
		while(i.hasNext())
		{
			String item = i.next();
			if(item == DefaultValue)
			{
				SelectedIndex = index;
			}
			fields.addItem(item);
			index++;
		}
		
		this._FieldVaLueWidget = fields;
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
	public void setList(ArrayList<String> Values)
	{
		this.Values= Values;
	}

}

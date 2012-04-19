package daris.Monbulk.shared.Form;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;


public class FormBuilder {

	private ArrayList<iFormField> FieldList;
	private String FormName;
	
	

	public FormBuilder()
	{
		this.FieldList = new ArrayList<iFormField>();
		this.FormName = "DefaultForm";
	}
	public void MergeForm(FormBuilder otherForm)
	{
		Iterator<iFormField> i = otherForm.getFormDetails().iterator();
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			tmpItem.setFieldName(otherForm.getFormName() + "." + tmpItem.GetFieldName());
			addField(tmpItem);
			//this.
		}
	}
	public void addField(iFormField item)
	{
		this.FieldList.add(item);
	}
	public void SetFormName(String fName)
	{
		this.FormName = fName;
	}
	public ArrayList<iFormField> getFormDetails()
	{
		return this.FieldList;
	}
	public ArrayList<iFormField> getStaticFormDetails()
	{
		return this.FieldList;
	}
	public void AddItem(String FieldName,String FieldType)
	{
		FormField newField = new FormField(FieldName,FieldType);
		FieldList.add(newField);
				
	}
	public void AddTitleItem(String FieldName,String FieldType)
	{
		FormField newField = new FormField(FieldName,FieldType);
		newField.setAsTitleField();
		FieldList.add(newField);
				
	}
	public void AddTitleItem(String FieldName,String FieldType,Object Value)
	{
		FormField newField = new FormField(FieldName,FieldType,Value);
		newField.setAsTitleField();
		FieldList.add(newField);
				
	}
	public void AddSummaryItem(String FieldName,String FieldType)
	{
		FormField newField = new FormField(FieldName,FieldType);
		newField.setAsSummaryField();
		FieldList.add(newField);
				
	}
	public void AddSummaryItem(String FieldName,String FieldType,Object Value)
	{
		FormField newField = new FormField(FieldName,FieldType,Value);
		newField.setAsSummaryField();
		FieldList.add(newField);
				
	}
	public void AddItem(String FieldName,String FieldType, Object FieldValue)
	{
		FormField newField = new FormField(FieldName,FieldType,FieldValue);
		this.FieldList.add(newField);
					
	}
	public Widget CreateForm()
	{
		FlexTable formGrid = new FlexTable();
		//formGrid.setWidget(0, 0, widget)
		int row = 0;
		while(this.FieldList.iterator().hasNext())
		{
			iFormField tmpField = this.FieldList.iterator().next();
			Label tmpLabel = new Label();
			tmpLabel.setText((String)tmpField.GetFieldName());
			tmpLabel.setStyleName("FormLabelName");
			formGrid.setWidget(row,0, tmpLabel);
			formGrid.setWidget(row,1, this.GetFieldType(tmpField));
			Label tmpLabel2 = new Label();
			tmpLabel.setText("");
			tmpLabel.setStyleName("FormError");
			formGrid.setWidget(row,2, tmpLabel2);
			row++;
			
			//Maybe add Separator??
		}
		
		return formGrid;
	}
	private Widget GetFieldType(iFormField tmpField)
	{
		//This would be a factory otherwise
		
		//TODO: Need to check for Static fields and comply with design
		if(tmpField.GetWidgetName().equals("TextBox"))
		{
			TextBox tmpBox = new TextBox();
			tmpBox.setStyleName("FormTextBox");
			tmpBox.setText((String) tmpField.GetFieldValue());
			//tmpBox.addValueChangeHandler(new ValueChangeHandler<String>()
			return tmpBox.asWidget();
		}
		else if(tmpField.GetWidgetName().equals("TextArea"))
		{
			TextArea tmpArea = new TextArea();
			tmpArea.setStyleName("FormTextArea");
			tmpArea.setText((String) tmpField.GetFieldValue());
			return tmpArea;
		}
		else if(tmpField.GetWidgetName().equals("Calendar"))
		{
		//	Calendar tmpControl = new Calendar();
			DatePicker tmpDate = new DatePicker();
			tmpDate.setStyleName("FormDatePicker");
			tmpDate.setCurrentMonth((Date)tmpField.GetFieldValue());
			return tmpDate;
		}
		else
		{
			Label tmpLabel = new Label();
			tmpLabel.setText((String)tmpField.GetFieldValue());
			tmpLabel.setStyleName("FormLabelValue");
			return tmpLabel;
		}
	}
	public iFormField getFieldItemForName(String FieldName)
	{
		Iterator<iFormField> i = this.FieldList.iterator();
		int j=0;
		
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			j++;
			if(tmpItem.GetFieldName().equals(FieldName))
			{
				return tmpItem;
			}
			if(j==1000)
			{
				return null;
			}
		}
		return null;
	}
	public String getFormName()
	{
		return this.FormName;
	}
	public Boolean RemoveFormItem(String FieldName, Boolean isFieldName)
	{
		Iterator<iFormField> i = this.FieldList.iterator();
		int j=0;
		
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			
			if(isFieldName)
			{
				if(tmpItem.GetFieldName().equals(FieldName))
				{
					this.FieldList.remove(j);
					return true;
				}
			}
			else if(tmpItem.hasValue()) 
			{
				if(tmpItem.GetFieldValue().equals(FieldName))
				{
					this.FieldList.remove(j);
					return true;
				}
			}
			j++;
			
		}
		return false;
	}
}

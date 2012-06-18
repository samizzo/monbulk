package monbulk.MethodBuilder.client.model;

import java.util.Iterator;

import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.iModel.iFormModel;
import monbulk.shared.Form.DictionaryFormField;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoStepDetails;

/*
 * Class baseModel
 * Author: Andrew Glenn
 * Date: 28/3/12
 * 
 * Purpose: All models effectively perform a round tripe of the following
 * 
 * 1. Getting any related data For a form (from a POJO)
 * 2. Saving details from a form to pojo
 * 3. Calling a Service to write and read data
 * 3. Write output in various formats
 */

public abstract class baseModel implements iFormModel{

	protected IPojo dataObject;
	protected FormBuilder formData;
	protected FormPresenter Presenter;
	public void convertPojoToForm(IPojo dataObject)
	{
		this.formData = dataObject.getFormStructure();
	}
	public void savePojo()
	{
		//this.dataObject. Should take formData and Save
	}
	public String getStringRpresentation(String Format) {
		
		return this.dataObject.writeOutput(Format);
	}
	@Override
	public String ValidateForm() {
		
		Iterator<iFormField> i = this.formData.getFormDetails().iterator();
		//Assumedly we should validate as well
		String Validation ="";
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			if(tmpItem.getFieldTypeName()=="String" || tmpItem.getFieldTypeName()=="Description")
			{
				if(!tmpItem.hasValue())
				{
					Validation = Validation + "<p>Please provide a value for the " + tmpItem.GetFieldName() + " field</p>";
				}
			}
			else if(tmpItem.getFieldTypeName()=="List" || tmpItem.getFieldTypeName()=="Dictionary")
			{
				if(this.formData.getFormName().contains(pojoStepDetails.FormName))
				{
					iFormField tmpField = this.formData.getFieldItemForName(pojoStepDetails.HasStudyField);
					if(tmpField!=null)
					{
						if(tmpField.hasValue())
						{
							if(tmpField.GetFieldValue()=="true")
							{
								Validation = Validation + ValidateDictionary(tmpItem);
							}
						}
					}
				}
				else
				{
					Validation  = Validation  + ValidateDictionary(tmpItem);
				}
			}
		}
		return Validation ;
	}
	private String ValidateDictionary(iFormField tmpItem)
	{
		if(tmpItem!=null)
		{
			if(!tmpItem.hasValue())
			{
				return "<p>Please provide a value for the " + tmpItem.GetFieldName() + " field</p>";
					
			}
			else if(tmpItem.GetFieldValue().toString().contains("null"))
			{
				return "<p>Please provide a value for the " + tmpItem.GetFieldName() + " field</p>";
			}
			else if((tmpItem.GetFieldValue().toString().contains(DictionaryFormField.defaultField)))
			{
				return "<p>Please provide a value for the " + tmpItem.GetFieldName() + " field</p>";
			}
			else if(tmpItem.GetFieldValue().toString().length()==0)
			{
				return "<p>Please provide a value for the " + tmpItem.GetFieldName() + " field</p>";
			}
			else
			{
				return "";
			}
		}
		else
		{
			return "<p>Please provide a value for the " + tmpItem.GetFieldName() + " field</p>";
		}
		
	}
	@Override
	public String ValidateForm(String FormName) {
		return this.ValidateForm();
	}
	@Override
	public FormBuilder getFormData() {	
		return this.formData;
	}
	public iFormField getFormItem(String FieldName)
	{
		if(this.formData != null)
		{
			iFormField thisField = this.formData.getFieldItemForName(FieldName);
			if(thisField!=null)
			{
				return thisField;
			}
		}
		return null;
	}
	
	@Override
	public void setPresenter(FormPresenter presenter) {
		// TODO Auto-generated method stub
		this.Presenter = presenter;
	}
}

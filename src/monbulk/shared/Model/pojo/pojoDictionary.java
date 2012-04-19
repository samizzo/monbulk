package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.Iterator;

import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.ListField;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;

public class pojoDictionary implements IPojo {

	private String DictionaryName;
	private ArrayList<String> DictionaryTerms;
	private String DictionaryNamespace;
	
	private String SelectedValue;
	public static final String DictinaryField = "Dictionary";
	public pojoDictionary(String DictionaryName)
	{
		this.DictionaryName = DictionaryName;
	}
	@Override
	public void saveForm(FormBuilder input) {
		Iterator<iFormField> i = input.getFormDetails().iterator();		
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			if(tmpItem.hasValue())
			{
				setFieldVale(tmpItem.GetFieldName(),tmpItem.GetFieldValue());
			}
		}
		
	}
	@Override
	public FormBuilder getFormStructure() {
		
		FormBuilder dictionaryForm = new FormBuilder(); 
		dictionaryForm.SetFormName(DictionaryName);
		ListField tmpField = new ListField(DictinaryField,"");
		
		dictionaryForm.addField(tmpField);
		return dictionaryForm;
	}
	@Override
	public void deserialise() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deserialiseFromList(String XML) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String writeOutput(String Format) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void readInput(String Format) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setFieldVale(String FieldName, Object FieldValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getFieldVale(String FieldName) {
		// TODO Auto-generated method stub
		return null;
	}


}

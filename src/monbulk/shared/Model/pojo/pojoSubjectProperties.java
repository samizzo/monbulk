package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.Iterator;

import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;

public class pojoSubjectProperties implements IPojo {

	private String SubjectName;
	private String SubjectType;
	
	private ArrayList<pojoMetaData> attachedMetaData;
	
	public  static final String FormName = "SubjectProperties";
	public static final String SubjectNameField = "SubjectName";
	public static final String SubjectTypeField = "SubjectType";
	@Override
	public void saveForm(FormBuilder input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FormBuilder getFormStructure() {
		// TODO Auto-generated method stub
		FormBuilder SubjectropertiesForm = new FormBuilder();
		//String FormName = FormName + FormIndex;
		SubjectropertiesForm.SetFormName(FormName);

		
		if(this.SubjectName.equals(null))
		{
			SubjectropertiesForm.AddTitleItem(SubjectNameField,"String","");
		}
		else
		{
			SubjectropertiesForm.AddTitleItem(SubjectNameField,"String",this.SubjectName);
		}
		if(this.SubjectType.equals(null))
		{	
			SubjectropertiesForm.AddSummaryItem(SubjectTypeField, "String");	
		}
		else
		{
			SubjectropertiesForm.AddSummaryItem(SubjectTypeField, "String",SubjectType);
		}
		Iterator<pojoMetaData> i = this.attachedMetaData.iterator();
		while(i.hasNext())
		{
			pojoMetaData tmpItem = i.next();
			SubjectropertiesForm.MergeForm(tmpItem.getFormStructure());
		}
		return SubjectropertiesForm;

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
	public void setFieldVale(String FieldName, Object FieldValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFieldVale(String FieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readInput(String Format, String Input) {
		// TODO Auto-generated method stub
		
	}

	

}

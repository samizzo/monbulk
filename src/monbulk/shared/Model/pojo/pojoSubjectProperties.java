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
	private FormBuilder SubjectPropertiesForm;
	@Override
	public void saveForm(FormBuilder input) {
		// TODO Auto-generated method stub
		
	}
	public pojoSubjectProperties()
	{
		this.attachedMetaData = new ArrayList<pojoMetaData>();
		SubjectPropertiesForm = new FormBuilder();
		BuildForm();
	}
	private void BuildForm()
	{
		SubjectPropertiesForm = new FormBuilder();
		//String FormName = FormName + FormIndex;
		SubjectPropertiesForm.SetFormName(FormName);

		
		if(this.SubjectName ==null)
		{
			SubjectPropertiesForm.AddTitleItem(SubjectNameField,"String");
		}
		else
		{
			SubjectPropertiesForm.AddTitleItem(SubjectNameField,"String",SubjectName);
		}
		if(this.SubjectType == null)
		{	
			SubjectPropertiesForm.AddSummaryItem(SubjectTypeField, "String");	
		}
		else
		{
			SubjectPropertiesForm.AddSummaryItem(SubjectTypeField, "String",SubjectType);
		}
		Iterator<pojoMetaData> i = this.attachedMetaData.iterator();
		while(i.hasNext())
		{
			pojoMetaData tmpItem = i.next();
			SubjectPropertiesForm.MergeForm(tmpItem.getFormStructure());
		}

	}
	@Override
	public FormBuilder getFormStructure() {
		// TODO Auto-generated method stub
				return SubjectPropertiesForm;

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
	public void setSubjectName(String SubjectName)
	{
		this.SubjectName = SubjectName;
		this.SubjectPropertiesForm.getFieldItemForName(SubjectNameField).SetFieldValue(SubjectName);
	}
	public void setSubjectType(String SubjectType)
	{
		this.SubjectType = SubjectType;
		this.SubjectPropertiesForm.getFieldItemForName(SubjectTypeField).SetFieldValue(SubjectType);
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
	public void readInput(String Format, Object Input) {
		// TODO Auto-generated method stub
		
	}

	

}

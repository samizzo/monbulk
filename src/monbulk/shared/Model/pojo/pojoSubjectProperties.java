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
		
		if(Format=="XML")
		{
			name.pehl.totoe.xml.client.XmlParser tmpParser = new name.pehl.totoe.xml.client.XmlParser(); 
			name.pehl.totoe.xml.client.Document document = new name.pehl.totoe.xml.client.XmlParser().parse(Input.toString());
			
			this.setSubjectName("Deprecated");
			this.setSubjectType("Deprecated");
		//	document.selectNodes("/response/method/subject/");
			
			java.util.List<name.pehl.totoe.xml.client.Node> subjectList = document.selectNodes("/response/method/subject/project/public/metadata");
			java.util.List<name.pehl.totoe.xml.client.Node> privateList = document.selectNodes("/response/method/subject/project/private/metadata");
			
			if(subjectList!=null)
			{
				if(subjectList.size() > 0)
				{
				//	String firstNodeName = subjectList.get(0).getName();
					Iterator<name.pehl.totoe.xml.client.Node> NodeIndex = subjectList.iterator();
					while(NodeIndex.hasNext())
					{
						name.pehl.totoe.xml.client.Node tmpItem = NodeIndex.next();
						String isMandatory = tmpItem.selectNode("definition/@requirement").toString();
						String mdName = tmpItem.selectValue("definition").toString();
						pojoMetaData tmpMD = new pojoMetaData(mdName);
						tmpMD.setFieldVale(pojoMetaData.IsPublicField, true);
						if(isMandatory =="mandatory")
						{
							tmpMD.setFieldVale(pojoMetaData.IsMandatoryField, true);
						}
						else
						{
							tmpMD.setFieldVale(pojoMetaData.IsMandatoryField, false);
						}
						this.attachedMetaData.add(tmpMD);
						//
					}
				}
			}
			if(privateList!=null)
			{
				if(privateList.size() > 0)
				{
				//	String firstNodeName = subjectList.get(0).getName();
					Iterator<name.pehl.totoe.xml.client.Node> NodeIndex = privateList.iterator();
					while(NodeIndex.hasNext())
					{
						name.pehl.totoe.xml.client.Node tmpItem = NodeIndex.next();
						String isMandatory = tmpItem.selectNode("definition/@requirement").toString();
						String mdName = tmpItem.selectValue("definition").toString();
						pojoMetaData tmpMD = new pojoMetaData(mdName);
						tmpMD.setFieldVale(pojoMetaData.IsPublicField, true);
						if(isMandatory =="mandatory")
						{
							tmpMD.setFieldVale(pojoMetaData.IsMandatoryField, true);
						}
						else
						{
							tmpMD.setFieldVale(pojoMetaData.IsMandatoryField, false);
						}
						this.attachedMetaData.add(tmpMD);
						//
					}
				}
			}
		}
	}

	

}

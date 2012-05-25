package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;


import monbulk.shared.util.HtmlFormatter;

import monbulk.shared.Form.ButtonField;
import monbulk.shared.Form.DraggableFormField;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;

public class pojoSubjectProperties implements IPojo {

	private String SubjectName;
	private String SubjectType;
	
	//private ArrayList<pojoMetaData> attachedMetaData;
	
	private HashMap<String,pojoMetaData> attachedMetaData;
	public  static final String FormName = "SUBJECT_PROPERTIES";
	public static final String SubjectNameField = "SubjectName";
	public static final String SubjectTypeField = "SubjectType";
	public static final String SubjectMetaDataField = "Add MetaData";
	private FormBuilder SubjectPropertiesForm;
	@Override
	public void saveForm(FormBuilder input) {
		
		Iterator<iFormField> i = input.getFormDetails().iterator();
		//BUG HERE
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			
			//FIXED
			if(tmpItem.hasValue())
			{
				String FieldName = tmpItem.GetFieldName(); 
				if(tmpItem.GetFieldName().contains(input.getFormName()))
				{
					FieldName = FieldName.replace(input.getFormName() + ".", "");
					
				}
				setFieldVale(FieldName,tmpItem.GetFieldValue() + "");
				
			}
		}
	}
	public void AppendXML(Element xml,Document doc)
	{
		Element node1 = doc.createElement("project");
		Element node2 = doc.createElement("public");
		Element node3 = doc.createElement("private");
		
		
		Iterator<Entry<String, pojoMetaData>> i = this.attachedMetaData.entrySet().iterator();
		int index = 0;
		while(i.hasNext())
		{
			Entry<String, pojoMetaData> tmpItem = i.next();
			//SubjectPropertiesForm.MergeForm(tmpItem.getFormStructure());
			pojoMetaData tmpPojo = tmpItem.getValue();
			if(tmpPojo.getFieldVale(pojoMetaData.IsPublicField)=="true")
			{
				tmpPojo.AppendXML(node2, doc);
			}
			else
			{
				tmpPojo.AppendXML(node3, doc);
			}
		}
		if(node2.hasChildNodes())
		{
			node1.appendChild(node2);
		}
		if(node3.hasChildNodes())
		{
			node1.appendChild(node3);
		}
		xml.appendChild(node1);
		
		//xml.appendChild(newChild)
	}
	public pojoSubjectProperties()
	{
		this.attachedMetaData = new HashMap<String,pojoMetaData>();
		SubjectPropertiesForm = new FormBuilder();
		BuildForm();
	}
	public HashMap<String,pojoMetaData> getMetaData(String ListName)
	{
			return this.attachedMetaData;
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
		SubjectPropertiesForm.AddItem(new ButtonField(this.SubjectMetaDataField,"Add MetaData"));
		Iterator<Entry<String, pojoMetaData>> i = this.attachedMetaData.entrySet().iterator();
		int index = 0;
		while(i.hasNext())
		{
			Entry<String, pojoMetaData> tmpItem = i.next();
			//SubjectPropertiesForm.MergeForm(tmpItem.getFormStructure());
			SubjectPropertiesForm.AddItem(new DraggableFormField(SubjectMetaDataField + index, index, false, tmpItem.getValue()));
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
		StringBuilder sb = new StringBuilder();
		
		String Output ="";
		
		//String SubjectType= ":metadata < :definition -requirement mandatory hfi.pssd.subject :value < :type constant(" + this.SubjectType + " > >\\";
		if(Format=="TCL")
		{
			if(this.attachedMetaData.size()>0)
			{
				String tclSubjectType="";
				if(this.SubjectType!=null)
				{
					//To be handled as a metaData item with SubjectName and Type
					tclSubjectType=HtmlFormatter.GetHTMLTabs(5) +  ":metadata < :definition -requirement mandatory hfi.pssd.subject :value < :type constant(" + this.SubjectType + ") > >\\" + HtmlFormatter.GetHTMLNewline(); 
				}
				
				sb.append(HtmlFormatter.GetHTMLTabs(2) + ":subject < \\" + HtmlFormatter.GetHTMLNewline());
				sb.append(HtmlFormatter.GetHTMLTabs(3)+ ":project < \\" + HtmlFormatter.GetHTMLNewline());
				
				//sb.append(tclSubjectType);
				sb.append(HtmlFormatter.GetHTMLMetaDataList("tcl", this.attachedMetaData,5,true));
				
				sb.append(HtmlFormatter.GetHTMLTabs(4)+ ">\\" + HtmlFormatter.GetHTMLNewline());
				sb.append(HtmlFormatter.GetHTMLTabs(3)+ ">\\" + HtmlFormatter.GetHTMLNewline());
			}
			
		}
		return sb.toString();
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
	public void UpdateMetaData(pojoMetaData selectedPOJO,Boolean isAdd)
	{
		if(isAdd)
		{
			if(this.attachedMetaData.get(selectedPOJO.getFieldVale(pojoMetaData.MetaDataNameField))==null)
			{
				this.attachedMetaData.put(selectedPOJO.getFieldVale(pojoMetaData.MetaDataNameField), selectedPOJO);
			}
		}
		else
		{
			if(this.attachedMetaData.get(selectedPOJO.getFieldVale(pojoMetaData.MetaDataNameField))==null)
			{
				this.attachedMetaData.remove(selectedPOJO.getFieldVale(pojoMetaData.MetaDataNameField));
			}
		}
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
						this.attachedMetaData.put(tmpMD.getFieldVale(tmpMD.MetaDataNameField), tmpMD);
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
						this.attachedMetaData.put(tmpMD.getFieldVale(tmpMD.MetaDataNameField), tmpMD);
						//
					}
				}
			}
		}
	}

	

}

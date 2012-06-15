package monbulk.shared.Model.pojo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;


import monbulk.client.Monbulk;
import monbulk.shared.util.GWTLogger;
import monbulk.shared.util.HtmlFormatter;

import monbulk.shared.Form.ButtonField;
import monbulk.shared.Form.DictionaryFormField;
import monbulk.shared.Form.DraggableFormField;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;

public class pojoSubjectProperties implements IPojo {

	private String SubjectName;
	private String SubjectType;
	private String SubjectPropertiesDocType;
	private String SubjectPropertiesDictionary;
	//private ArrayList<pojoMetaData> attachedMetaData;
	
	private HashMap<String,pojoMetaData> attachedMetaData;
	public  static final String FormName = "SUBJECT_PROPERTIES";
	public static final String SubjectNameField = "Subject Name";
	public static final String SubjectTypeField = "Subject Type";
	public static final String SubjectMetaDataField = "Add MetaData";
	
	public static final String STUDYTYPE_DICTIONARY = "pssd.study.types";
	
	
	private FormBuilder SubjectPropertiesForm;
	public pojoSubjectProperties()
	{
		this.SubjectPropertiesDocType = Monbulk.getSettings().get("subject_doctype", "mbi.subject.properties");
		this.SubjectPropertiesDictionary = Monbulk.getSettings().get("subject_dictionary", "mbi.subjectTypes");
		//"subject_dictionary" "mbi.subjectTypes"
		this.attachedMetaData = new HashMap<String,pojoMetaData>();
		SubjectPropertiesForm = new FormBuilder();
		BuildForm();
	}
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
		pojoMetaData subjectMD = new pojoMetaData(this.SubjectPropertiesDocType);
		subjectMD.addMetaDataField("subjectName", this.SubjectName);
		subjectMD.addMetaDataField("subjectType", this.SubjectType);
		subjectMD.AppendXML(node2, doc);
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
			SubjectPropertiesForm.AddSummaryItem(SubjectNameField,"String");
		}
		else
		{
			SubjectPropertiesForm.AddSummaryItem(SubjectNameField,"String",SubjectName);
		}
		DictionaryFormField subjectTypeField = new DictionaryFormField(SubjectTypeField,this.SubjectPropertiesDictionary);
		subjectTypeField.setAsSummaryField();
		if(this.SubjectType == null)
		{	
			subjectTypeField.SetFieldValue(SubjectType);
		}
		SubjectPropertiesForm.AddItem(subjectTypeField);
		
		
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
					tclSubjectType=HtmlFormatter.GetHTMLTabs(5) +  ":metadata < :definition -requirement mandatory " + this.SubjectPropertiesDocType + " :value < :subjectType constant(" + this.SubjectType + ") > >\\" + HtmlFormatter.GetHTMLNewline(); 
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
		String FieldName = HtmlFormatter.stripFormFieldName(FormName, SubjectNameField);
		this.SubjectPropertiesForm.getFieldItemForName(FieldName).SetFieldValue(SubjectName);
	}
	public void setSubjectType(String SubjectType)
	{
		this.SubjectType = SubjectType;
		String FieldName = HtmlFormatter.stripFormFieldName(FormName, SubjectTypeField);
		this.SubjectPropertiesForm.getFieldItemForName(FieldName).SetFieldValue(SubjectType);
	}
	@Override
	public void setFieldVale(String FieldName, Object FieldValue) {
		FieldName = HtmlFormatter.stripFormFieldName(FormName, FieldName);
		if(HtmlFormatter.compareStrings(FieldName,SubjectNameField))
		{
			this.setSubjectName(FieldValue.toString());
		}
		else if(HtmlFormatter.compareStrings(FieldName,SubjectTypeField))
		{
			this.setSubjectType(FieldValue.toString());
		}
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
			else
			{
				this.attachedMetaData.remove(selectedPOJO.getFieldVale(pojoMetaData.MetaDataNameField));
				this.attachedMetaData.put(selectedPOJO.getFieldVale(pojoMetaData.MetaDataNameField), selectedPOJO);
			}
		}
		else
		{
			if(this.attachedMetaData.get(selectedPOJO.getFieldVale(pojoMetaData.MetaDataNameField))!=null)
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
			
			this.setSubjectName("Not Set");
			this.setSubjectType("Not Set");
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
						//"subject_doctype" "mbi.subjectTypes"/
						
						if(mdName.contains(this.SubjectPropertiesDocType) && this.SubjectPropertiesDocType.length()==mdName.length())
						{
							if(tmpItem.selectNode("value")!=null)
							{
								try
								{
									String value = tmpItem.selectValue("value/subjectType");
									value = value.replace("constant(", "");
									value = value.replace(")", "");
									this.setSubjectType(value);		
									String value2 = tmpItem.selectValue("value/subjectName");
									value2 = value2.replace("constant(", "");
									value2 = value2.replace(")", "");
									this.setSubjectName(value2);
								}
								catch(Exception ex)
								{
									//do nothing - non-fatal
									GWTLogger.Log("Couldn't find constants", "pojoSubjectProperties", "ReadInput", "271");
								}
								
							}
							
							
							
						}
						else
						{
							pojoMetaData tmpMD = new pojoMetaData(mdName);
							tmpMD.setFieldVale(pojoMetaData.IsPublicField, true);
							if(isMandatory.contains("mandatory"))
							{
								tmpMD.setFieldVale(pojoMetaData.IsMandatoryField, true);
							}
							else
							{
								tmpMD.setFieldVale(pojoMetaData.IsMandatoryField, false);
							}
							this.attachedMetaData.put(tmpMD.getFieldVale(tmpMD.MetaDataNameField), tmpMD);
						}
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

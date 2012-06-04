package monbulk.shared.Model.pojo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;

import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.ListField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.util.HtmlFormatter;

public class pojoStudy implements IPojo {
	private HashMap<String,pojoMetaData> attachedMetaData;
	//private ArrayList<pojoMetaData> attachedMetaData;
	private String DicomModality;
	private String StudyType;
	
	
	public static final String StudyTypeField = "Study Type";
	public static final String DicomModalityField = "Dicom Modality";
	public static final String FormName = "Study Details";
	public static final String DICOM_DICTIONARY = "pssd.dicom.modality";
	public static final String STUDYTYPE_DICTIONARY = "pssd.study.types";
	public static final String STUDY_METADATA= "Study MetaData";
	
	public pojoStudy()
	{
		
		attachedMetaData = new HashMap<String,pojoMetaData>(); 
	}
	public pojoStudy(pojoDictionary Dicom, pojoDictionary StudyTypes)
	{
		
		attachedMetaData = new HashMap<String,pojoMetaData>();
	}
	@Override
	public void saveForm(FormBuilder input) {
		// TODO Auto-generated method stub
		
	}
	public HashMap<String,pojoMetaData> getMetaDataList()
	{
		return attachedMetaData;
	}
	@Override
	public FormBuilder getFormStructure() {
		
		FormBuilder studyForm = new FormBuilder(); 
		studyForm.SetFormName(FormName);
		ListField tmpField = new ListField(StudyTypeField,"");
		ListField tmpField2 = new ListField(DicomModalityField,"");
		
		/*Iterator<pojoMetaData> i = this.attachedMetaData.iterator();
		while(i.hasNext())
		{
			pojoMetaData tmpItem = i.next();
			studyForm.MergeForm(tmpItem.getFormStructure());
		}*/
		return studyForm;
	}

	@Override
	public void deserialise() {
		// TODO Auto-generated method stub
		
	}
	public void setStudyType(String StudyType){this.StudyType = StudyType;	}
	public void setDICOM(String DICOM){ this.DicomModality = DICOM;	}
	public String getStudyType(){return this.StudyType;}
	public String getDICOM(){return this.DicomModality;}
	@Override
	public void deserialiseFromList(String XML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String writeOutput(String Format) {
		StringBuilder sb = new StringBuilder();
		if(Format=="TCL")
		{
			sb.append(HtmlFormatter.GetHTMLTabs(3)+ ":study < " + HtmlFormatter.GetHTMLNewline());
			sb.append(HtmlFormatter.GetHTMLTabs(4)+ ":type " + this.StudyType + " \\" +  HtmlFormatter.GetHTMLNewline());
			if(this.DicomModality!=null)
			{
				sb.append(HtmlFormatter.GetHTMLTabs(4) +" :dicom < :modality " + this.DicomModality + "> \\" + HtmlFormatter.GetHTMLNewline());
			}
			sb.append(HtmlFormatter.GetHTMLMetaDataList("TCL", this.attachedMetaData, 4));
			sb.append(HtmlFormatter.GetHTMLTabs(3)+ "> \\" +  HtmlFormatter.GetHTMLNewline());
		}
		return sb.toString();
	}
	public void AppendXML(Element xml,Document doc)
	{
		Element sType = doc.createElement("type");
		sType.appendChild(doc.createTextNode(this.StudyType));
		xml.appendChild(sType);
		if(this.DicomModality!=null)
		{
			Element dicom = doc.createElement("dicom");
			Element modality = doc.createElement("modality");
			modality.appendChild(doc.createTextNode(this.DicomModality));
			dicom.appendChild(modality);
			xml.appendChild(dicom);
		}
		if(this.attachedMetaData.size()>0)
		{
			Iterator<Entry<String, pojoMetaData>> i = this.attachedMetaData.entrySet().iterator();
			int index = 0;
			while(i.hasNext())
			{
				Entry<String, pojoMetaData> tmpItem = i.next();
				//SubjectPropertiesForm.MergeForm(tmpItem.getFormStructure());
				pojoMetaData tmpPojo = tmpItem.getValue();
				tmpPojo.AppendXML(xml, doc);
			}
		}
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

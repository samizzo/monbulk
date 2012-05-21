package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.ListField;
import monbulk.shared.Model.IPojo;

public class pojoStudy implements IPojo {
	private HashMap<String,pojoMetaData> attachedMetaData;
	//private ArrayList<pojoMetaData> attachedMetaData;
	private pojoDictionary DicomModality;
	private pojoDictionary StudyType;
	
	public static final String StudyTypeField = "Study Type";
	public static final String DicomModalityField = "Dicom Modality";
	public static final String FormName = "Study Details";
	public static final String DICOM_DICTIONARY = "pssd.dicom.modality";
	public static final String STUDYTYPE_DICTIONARY = "pssd.study.types";
	public static final String STUDY_METADATA= "Study MetaData";
	
	public pojoStudy()
	{
		DicomModality = new pojoDictionary("");
		StudyType = new pojoDictionary("");
		attachedMetaData = new HashMap<String,pojoMetaData>(); 
	}
	public pojoStudy(pojoDictionary Dicom, pojoDictionary StudyTypes)
	{
		DicomModality = Dicom;
		StudyType = StudyTypes;
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
	public void readInput(String Format, Object Input) {
		// TODO Auto-generated method stub
		
	}

}

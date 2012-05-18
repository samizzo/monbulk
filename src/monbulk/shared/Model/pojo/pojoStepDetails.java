package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import monbulk.shared.Form.ButtonField;
import monbulk.shared.Form.DictionaryFormField;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormField;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Services.Dictionary;
import monbulk.shared.Services.Dictionary.Entry;
import monbulk.shared.Services.DictionaryService;
import monbulk.shared.Services.DictionaryService.GetDictionaryHandler;
import monbulk.shared.util.GWTLogger;
import monbulk.shared.util.HtmlFormatter;

public class pojoStepDetails implements IPojo{

	private String StepName;
	private String StepDescription;
	private String StepID;
	private ArrayList<pojoMetaData> attachedMetaData;
	
	private pojoStudy relatedStudy;
	private Boolean hasStudy;
	private int FormIndex;
	
	public  static final String FormName = "STEP_DETAILS";
	public static final String StepNameField = "Title";
	public static final String StepDescriptionField = "Description";
	public static final String HasStudyField = "Has Study?";
	public static final String FormIndexField = "StepIndex";
	public static final String StepMetaDataField = "Step MetaData";
	public static final String SubjectMetaDataField = "Subject MetaData";
	
	private FormBuilder StepDetailsForm;
	public pojoStepDetails(int Index)
	{
		
		this.FormIndex = Index;
		this.relatedStudy = new pojoStudy();
		StepDetailsForm = new FormBuilder();
		
		this.attachedMetaData = new ArrayList<pojoMetaData>();
	}
	
	public String writeTCL() {
		// TODO Auto-generated method stub
		String Output = "";
		
		String StepName = "";
		String StepDescription = "";
		String StepStudy = "";
		String Modality = "";
		
	
		//String SubjectType= ":metadata < :definition -requirement mandatory hfi.pssd.subject :value < :type constant(" + this.SubjectType + " > >\\";
		
			Output = Output + HtmlFormatter.GetHTMLTabs(2) + ":step < \\" + HtmlFormatter.GetHTMLNewline();
			Output = Output + HtmlFormatter.GetHTMLTabs(3)+ ":name \\\"" + StepName + "\\\"\\" + HtmlFormatter.GetHTMLNewline();
			Output = Output + HtmlFormatter.GetHTMLTabs(3)+ ":description \\\"" + StepDescription + "\\\" \\" + HtmlFormatter.GetHTMLNewline();;
			
			
			if(this.attachedMetaData.size() > 0)
			{
				Iterator<pojoMetaData> i = attachedMetaData.iterator();
				Output = Output +  HtmlFormatter.GetHTMLTabs(3) + ":subject -part p < \\" + HtmlFormatter.GetHTMLNewline();
				
				while(i.hasNext())
				{
					pojoMetaData tmpItem = i.next();
					if(tmpItem.getFieldVale(pojoMetaData.MetaDataNameSpaceField) == "Subject")
					{
						Output = Output + tmpItem.writeOutput("TCL");
					}
					//else add somewhere else
				}
				
				//Output = Output + HtmlFormatter.GetHTMLMetaDataList("tcl", this.MetaDataAddedList, 4) + "> \\" + HtmlFormatter.GetHTMLNewline();
			}
			
			if(hasStudy)
			{
				//Output = Output + HtmlFormatter.GetHTMLTabs(3) + ":study < :type \\\"" + StepStudy + "\\\" :dicom < :modality " + Modality + " > > \\" + HtmlFormatter.GetHTMLNewline();
				Output = Output + relatedStudy.writeOutput("TCL");
			}
			Output = Output + HtmlFormatter.GetHTMLTabs(2)+ "> \\" + HtmlFormatter.GetHTMLNewline();
		
		return Output;
	}

	@Override
	public void saveForm(FormBuilder input) {
		
		
	}

	@Override
	public FormBuilder getFormStructure() {
		// TODO Auto-generated method stub
//		StepDetailsForm = new FormBuilder();
		//String FormName = FormName + FormIndex;
		try
		{
			GWTLogger.Log("Running @ pojoStepDetails.getFormStructure:", "pojoStepDetails", "getFormStructure", "111");
		//StepDetailsForm.SetFormName(FormName + FormIndex);

		StepDetailsForm.SetFormName(FormName);
		if(this.StepName==null)
		{
			StepDetailsForm.AddTitleItem(StepNameField,"String","");
		}
		else
		{
			StepDetailsForm.AddTitleItem(StepNameField,"String",this.StepName);
		}
		if(this.StepDescription==null)
		{	
			StepDetailsForm.AddSummaryItem(StepDescriptionField, "Description");	
		}
		else
		{
			StepDetailsForm.AddSummaryItem(StepDescriptionField, "Description",StepDescription);
		}
		if(this.hasStudy==null)
		{
			StepDetailsForm.AddItem(HasStudyField, "Boolean");
		}
		else
		{
			StepDetailsForm.AddItem(HasStudyField, "Boolean",this.hasStudy); 
		}
				//StepDetailsForm.AddListItem(pojoStudy.DicomModalityField, new ArrayList<String>(), "Loading");
		 
		
		
		StepDetailsForm.AddItem(new DictionaryFormField(pojoStudy.DicomModalityField,pojoStudy.DICOM_DICTIONARY));
		
		
		StepDetailsForm.AddItem(new DictionaryFormField(pojoStudy.StudyTypeField,pojoStudy.STUDYTYPE_DICTIONARY));
		
		
		
		StepDetailsForm.AddItem(new ButtonField(this.SubjectMetaDataField,"Add MetaData"));
		GWTLogger.Log("Running @ pojoStepDetails.getFormStructure:", "pojoStepDetails", "getFormStructure", "150");
		if(this.attachedMetaData.size() > 0)
		{
			
		}
		
		StepDetailsForm.AddItem(new ButtonField(pojoStudy.STUDY_METADATA,"Add MetaData"));
		GWTLogger.Log("Running @ pojoStepDetails.getFormStructure:", "pojoStepDetails", "getFormStructure", "157");
		if(this.relatedStudy.getMetaDataList().size()>0)
		{
			
		}
		
		return StepDetailsForm;
		}
		catch(Exception ex)
		{
			GWTLogger.Log("Error @ pojoStepDetails.getFormStructure:" + ex.getMessage(), "pojoStepDetails", "getFormStructure", "167");
		}
		return StepDetailsForm;
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
		if(Format=="TCL")
		{
			return writeTCL();
		}
		return "";
	}
	@Override
	public void setFieldVale(String FieldName, Object FieldValue) {
		Window.alert("FieldName:" + FieldName + "Field Value:" + FieldValue);
		if(!FieldValue.equals(null))
		{
			if(FieldName == this.StepDescriptionField)
			{
				this.StepDescription = FieldValue.toString();
			}
			else if(FieldName == this.StepNameField)
			{
				this.StepName = FieldValue.toString();
			}
			else if(FieldName == this.HasStudyField)
			{
				this.hasStudy = (Boolean) FieldValue;
			}
			
		}
		
	}

	@Override
	public String getFieldVale(String FieldName) {
		if(FieldName == this.StepDescriptionField)
		{
			return this.StepDescription;
		}
		else if(FieldName == this.StepNameField)
		{
			return this.StepName;
		}
		else if(FieldName == this.HasStudyField)
		{
			return this.hasStudy.toString();
		}
		else
		{
			return "";
		}
	}

	@Override
	public void readInput(String Format, Object Input) {
	if(Format=="XML")
		{
			try
			{
				name.pehl.totoe.xml.client.XmlParser tmpParser = new name.pehl.totoe.xml.client.XmlParser(); 
				name.pehl.totoe.xml.client.Document document = new name.pehl.totoe.xml.client.XmlParser().parse(Input.toString());
				
				//Window.alert(document.selectValue("/step/@id"));
				StepID = document.selectValue("/step/@id");
				this.StepName = document.selectValue("/step/name");
				this.StepDescription = document.selectValue("/step/description");
				//document.selectValue("/step") Gives Description?
				//Window.alert(document.selectNode("/step/study").toString());
			}
			catch(Exception ex)
			{
				GWT.log("Exception caught @ pojoStepDetails.readInput(XML): " + ex.getMessage());
			}
			//java.util.List<name.pehl.totoe.xml.client.Node> subjectList = document.selectNodes("/study");
		}
	}

}

package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.Iterator;

import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.util.HtmlFormatter;

public class pojoStepDetails implements IPojo{

	private String StepName;
	private String StepDescription;
	
	private ArrayList<pojoMetaData> attachedMetaData;
	
	private pojoStudy relatedStudy;
	private Boolean hasStudy;
	private int FormIndex;
	
	public  static final String FormName = "StepDetails";
	public static final String StepNameField = "Title";
	public static final String StepDescriptionField = "Description";
	public static final String HasStudyField = "HasStudy";
	public static final String FormIndexField = "StepIndex";
	public pojoStepDetails(int Index)
	{
		this.FormIndex = Index;
		this.relatedStudy = new pojoStudy();
		
		
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
		FormBuilder StepDetailsForm = new FormBuilder();
		//String FormName = FormName + FormIndex;
		StepDetailsForm.SetFormName(FormName + FormIndex);

		
		if(this.StepName.equals(null))
		{
			StepDetailsForm.AddTitleItem(StepNameField,"String","");
		}
		else
		{
			StepDetailsForm.AddTitleItem(StepNameField,"String",this.StepName);
		}
		if(this.StepDescription.equals(null))
		{	
			StepDetailsForm.AddSummaryItem(StepDescriptionField, "String");	
		}
		else
		{
			StepDetailsForm.AddSummaryItem(StepDescriptionField, "String",StepDescription);
		}
		if(this.hasStudy.equals(null))
		{
			StepDetailsForm.AddItem(HasStudyField, "String");
		}
		else
		{
			StepDetailsForm.AddItem(HasStudyField, "String",this.hasStudy); 
		}
		
		StepDetailsForm.MergeForm(this.relatedStudy.getFormStructure());
		
		Iterator<pojoMetaData> i = this.attachedMetaData.iterator();
		while(i.hasNext())
		{
			pojoMetaData tmpItem = i.next();
			StepDetailsForm.MergeForm(tmpItem.getFormStructure());
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

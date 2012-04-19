package daris.Monbulk.MethodBuilder.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import daris.Monbulk.shared.util.HtmlFormatter;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Model.StackedCategories;
import daris.Monbulk.shared.Model.StackedCategories.ParentCategory;
import daris.Monbulk.MethodBuilder.shared.iMBModel;
import daris.Monbulk.MethodBuilder.shared.iModelAllowMetaData.iModelHasHelpExtendsMetaData;
import daris.Monbulk.shared.Architecture.IPresenter.FormPresenter;
import daris.Monbulk.shared.Architecture.IView.IDraggable;
import daris.Monbulk.shared.Form.FormBuilder;
import daris.Monbulk.shared.Form.FormField;
import daris.Monbulk.shared.Form.iFormField;
import daris.Monbulk.shared.view.IResult;
import daris.Monbulk.shared.view.ISearchFilter;

public class StepDetailsModel implements iMBModel, IDraggable{

	//private String StepName;
	//private String StudyType;
	//private String DicomModality;
	//private String StepDescription;
	private FormBuilder StepDetailsForm;
	
	//TODO The following should be implemented in POJO for serialisation - SHould implement isPublic, isMandatory,DefaultValues
	private int FormIndex;
	//Obviously we could extend StepDetails model to create StepTypes
	private FormPresenter Presenter; 
	
	public StepDetailsModel(int formIndex)
	{
		this.FormIndex = formIndex;
		
			
		
		StepDetailsForm = new FormBuilder();
		String FormName = "StepDetails" + FormIndex;
		StepDetailsForm.SetFormName(FormName);
		
	}

	public int getFormIndex()
	{
		return FormIndex;
	}


	@Override
	public String ValidateForm() {
		// TODO Auto-generated method stub
		Iterator<iFormField> i = this.StepDetailsForm.getFormDetails().iterator();
		//Assumedly we should validate as well
		
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			if(!tmpItem.hasValue())
			{
				return "Validation Fails: No Value specified for Field:" + tmpItem.GetFieldName();
			}
		}
		return "";
	}
	
	
	@Override
	public String getStringRpresentation(String Format) {
		String Output ="";
		
	
		Output = Output + HtmlFormatter.GetHTMLTabs(2)+ "> \\" + HtmlFormatter.GetHTMLNewline();
		
		return Output;
	}
	@Override
	public void setPresenter(FormPresenter presenter) {
		// TODO Auto-generated method stub
		this.Presenter = presenter;
	}
	@Override
	public ArrayList<IResult> Search(ArrayList<ISearchFilter> searchFilters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadData(String ID) {
		// TODO Auto-generated method stub
		
	}
	public FormBuilder getFormData()
	{
		return this.StepDetailsForm;
	}

	@Override
	public void loadData(IPojo someDataObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean DragItem(IPojo someItem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean DroptItem(IPojo someItem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Update(FormBuilder formData) {
		// TODO Auto-generated method stub
		
	}

}

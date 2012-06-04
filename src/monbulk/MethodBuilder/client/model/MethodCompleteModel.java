package monbulk.MethodBuilder.client.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import monbulk.MethodBuilder.shared.iMBModel;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMetaData;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoMethodComplete;
import monbulk.shared.Model.pojo.pojoStepDetails;
import monbulk.shared.Model.pojo.pojoStudy;
import monbulk.shared.Model.pojo.pojoSubjectProperties;
import monbulk.shared.Services.MethodService;
import monbulk.shared.Services.ServiceRegistry;
import monbulk.shared.util.MonbulkEnums.ServiceNames;
import monbulk.shared.view.IResult;
import monbulk.shared.view.ISearchFilter;

public class MethodCompleteModel extends baseModel implements iMBModel, MethodService.MethodServiceHandler{

	private pojoMethodComplete CompleteModel;
	//private FormBuilder formCompleteModel;
	private FormPresenter Presenter; 
	
	private Boolean isLoaded;
	private int StepCount;
	private String CurrentStep;
	private String FirstStep;
	public MethodCompleteModel()
	{
		isLoaded = false;
		
		//this.allSteps.RemoveStep(0);
		this.CompleteModel = new pojoMethodComplete();
		super.formData = this.CompleteModel.getFormStructure();
		isLoaded = false;
		CurrentStep=pojoStepDetails.FormName + "0";
		FirstStep = pojoStepDetails.FormName + "0";
		StepCount=0;
	
		
		
	}
	public String getMethodName(){return this.CompleteModel.getMethodDetails().getMethodName();}
	public int getStepCount()
	{
		return this.CompleteModel.getStepCount();
	}
	public MethodCompleteModel(String ID, FormPresenter presenter)
	{
		isLoaded = true;
		this.Presenter = presenter;
		this.loadData(ID);
		FirstStep = pojoStepDetails.FormName + "0";
	
	}
	public void removeStep(String StepFormName)
	{
		
		this.CompleteModel.removeStep(StepFormName);
		this.CurrentStep="";
		this.StepCount = this.CompleteModel.getStepCount();
		
	}
	public void addStep()
	{
		this.CompleteModel.addStep();
		
		this.StepCount++;
	}
	public pojoStepDetails getFirstStep()
	{
		
		if(this.CompleteModel.getStepCount() > 0)
		{
			this.CurrentStep = FirstStep;
			return this.CompleteModel.getSteps().get(FirstStep);
		}
		else
		{
			this.addStep();
			this.CurrentStep = FirstStep;
			return this.CompleteModel.getSteps().get(FirstStep);
		}
	}
	public pojoStepDetails getStep(String StepFormName)
	{
		if(this.CompleteModel.getSteps().get(StepFormName)!=null)
		{
			this.CurrentStep=StepFormName;
			return this.CompleteModel.getSteps().get(StepFormName); 
		}
		else
		{
			return null;
		}
	}
	
	public pojoStepDetails getNextStep(String StepFormName)
	{
		
			if(this.CompleteModel.getSteps().get(StepFormName)!=null)
			{
				int index = this.CompleteModel.getSteps().get(StepFormName).getFormIndex();
				index++;
				String newFormName = pojoStepDetails.FormName + index;
				if(this.CompleteModel.getSteps().get(newFormName)==null)
				{
					this.addStep();
					this.CurrentStep=newFormName;
					return this.CompleteModel.getSteps().get(newFormName);
				}
				else
				{
					return this.CompleteModel.getSteps().get(newFormName);
				}
			}
			else
			{
				return null;
			}
		
	}
	public void addStep(pojoStepDetails stepIn)
	{
		this.CompleteModel.addStep(stepIn,stepIn.FormName);
		this.StepCount++;
	}
	
	public Boolean isLoaded()
	{
		return this.isLoaded;
	}
	@Override
	public ArrayList<IResult> Search(ArrayList<ISearchFilter> searchFilters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveData() {
		//this.CompleteModel.saveForm(input)
		
	}
	
	@Override
	public void loadData(String ID) {
		if(ID!="")
		{
			try
			{
				MethodService tmpSvc = MethodService.get();
				if(tmpSvc != null)
				{
					tmpSvc.getMethod(ID, this);
						
				}
				else
				{
					throw new ServiceRegistry.ServiceNotFoundException(ServiceNames.Methods);
				}
			}
			catch (ServiceRegistry.ServiceNotFoundException e)
			{
				GWT.log("Couldn't find Method service");
			}
		}
		else
		{
			this.CompleteModel = new pojoMethodComplete(); 
		}
		
	}

	@Override
	public FormBuilder getFormData() {
		// TODO Auto-generated method stub
		return this.formData;
	}
	public FormBuilder getFormData(String FormName)
	{
		try
		{
		//Window.alert("Loading form" + FormName);
		if(FormName==pojoMethod.FormName)
		{
			return this.CompleteModel.getMethodDetails().getFormStructure();
		}
		else if(FormName==pojoSubjectProperties.FormName)
		{
			return this.CompleteModel.getSubjectProperties().getFormStructure();
			
		}
		else if(FormName.contains(pojoStepDetails.FormName))
		{
			//return this.CompleteModel.`.getFormStructure();
			pojoStepDetails returnStep= this.CompleteModel.getSteps().get(FormName);
			if(returnStep!=null)
			{
				return returnStep.getFormStructure();
			}
			else
			{
				Window.alert("We could not find that Step");
				return new FormBuilder();
			}
		}
		else
		{
			return this.formData;
		}
		}
		catch(Exception ex)
		{
			GWT.log("Error @ MethodCompleteModel.getFormData(String)" + ex.getMessage());
		}
		return formData;
	}

	@Override
	public void setPresenter(FormPresenter presenter) {
		this.Presenter = presenter;
		
	}

	@Override
	public void onReadMethodList(ArrayList<pojoMethod> arrMethods) {
		return;
		
	}
	@Override
	public String ValidateForm() {
		//super.formData = this.formCompleteModel;
		return super.ValidateForm();
	}
	@Override
	public String ValidateForm(String FormName) {
		super.formData = this.getFormData(FormName);
		return super.ValidateForm();
	}
	@Override
	public void onReadMethod(pojoMethodComplete method) {
		
		GWT.log("onReadMethod starts");
		try
		{
			
		this.CompleteModel = method;
		this.StepCount = this.CompleteModel.getStepCount(); 
		//this.formCompleteModel = this.CompleteModel.getFormStructure();
		//this.Presenter.ModelUpdate("GetMethod");
		GWT.log("thus far A");
		isLoaded = true;
		
		this.Presenter.ModelUpdate("GetMethod");
		GWT.log("thus far");
		}
		catch(Exception ex)
		{
			GWT.log("Error occurs @ MethodCompleteModel.onReadMethod" + ex.getMessage());
		}
		
		
	}
	@Override
	public void loadData(IPojo someDataObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void Update(FormBuilder formData) {
		String FieldName = formData.getFormName();
		if(FieldName.contains(pojoSubjectProperties.FormName))
		{
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
			this.CompleteModel.getSubjectProperties().saveForm(formData);
		}
		else if(FieldName.contains(pojoStepDetails.FormName))
		{
			this.CompleteModel.getSteps().get(this.CurrentStep).saveForm(formData);
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
		}
		else if(FieldName.contains(pojoMethod.FormName))
		{
			this.CompleteModel.getMethodDetails().saveForm(formData);
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
		}
		
	}
	public void setAsClone()
	{
		this.CompleteModel.setMethodID("");
	}
	@Override
	public String getStringRpresentation(String Format) {
		
		return this.CompleteModel.writeOutput(Format);
	}
	public void loadMetaData(String FieldName, pojoMetaData metaDataItem)
	{
		if(FieldName.contains(pojoSubjectProperties.SubjectMetaDataField))
		{
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
			this.CompleteModel.getSubjectProperties().UpdateMetaData(metaDataItem, true);
		}
		if(FieldName.contains(pojoStepDetails.SubjectMetaDataField))
		{
			this.CompleteModel.getSteps().get(this.CurrentStep).UpdateMetaData(metaDataItem, true, FieldName);
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
		}
		if(FieldName.contains(pojoStudy.STUDY_METADATA))
		{
			this.CompleteModel.getSteps().get(this.CurrentStep).UpdateMetaData(metaDataItem, true, FieldName);
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
		}
	}
	public void removeMetaData(String FieldName, pojoMetaData metaDataItem)
	{
		if(FieldName.contains(pojoSubjectProperties.SubjectMetaDataField))
		{
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
			this.CompleteModel.getSubjectProperties().UpdateMetaData(metaDataItem, false);
			
		}
		else if(FieldName.contains(pojoStepDetails.SubjectMetaDataField))
		{
			this.CompleteModel.getSteps().get(this.CurrentStep).UpdateMetaData(metaDataItem, false, FieldName);
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
		}
		else if(FieldName.contains(pojoStudy.STUDY_METADATA))
		{
			this.CompleteModel.getSteps().get(this.CurrentStep).UpdateMetaData(metaDataItem, false, FieldName);
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
		}
	}
	public HashMap<String,pojoMetaData> getMetaDataList(String FieldName)
	{
		if(FieldName.contains(pojoStudy.STUDY_METADATA))
		{
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
			return this.CompleteModel.getSteps().get(this.CurrentStep).getMetaData(FieldName);
			
			
		}
		else if(FieldName.contains(pojoStepDetails.SubjectMetaDataField))
		{
			return this.CompleteModel.getSteps().get(this.CurrentStep).getMetaData(FieldName);
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
		}
		else if(FieldName.contains(pojoSubjectProperties.SubjectMetaDataField))
		{
			return this.CompleteModel.getSubjectProperties().getMetaData(FieldName);
			//this.CompleteModel.getSubjectProperties().setFieldVale(FieldName, metaDataItem);
		}
		return null;
	}
	

}
